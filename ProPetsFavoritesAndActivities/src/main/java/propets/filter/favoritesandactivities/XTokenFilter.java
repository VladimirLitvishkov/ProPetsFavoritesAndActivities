package propets.filter.favoritesandactivities;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import propets.configuration.favoritesandactivities.FavoritesAndActivitiesConfiguration;

@Service
@Order(10)
public class XTokenFilter implements Filter {
	
	@Autowired
	FavoritesAndActivitiesConfiguration configuration;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String path = request.getServletPath();
		String method = request.getMethod();
		String xToken = request.getHeader("X-token");
//		String url = "https://pro-pets-accounting.herokuapp.com/en/v1/check";
		String url = configuration.getUrlCheckToken();
		if (!checkPointCut(path, method)) {
			if (xToken == null) {
				response.sendError(401);
				return;
			}
			RestTemplate restTemplate = new RestTemplate();
			URI urlCheckTokenServ = null;
			try {
				urlCheckTokenServ = new URI(url);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			HttpHeaders headers = new HttpHeaders();
			headers.add("X-token", xToken);
			RequestEntity<String> requestCheckToken = new RequestEntity<>(headers, HttpMethod.GET, urlCheckTokenServ);
			ResponseEntity<String> responseCheckToken;
			try {
				responseCheckToken = restTemplate.exchange(requestCheckToken, String.class);
			} catch (RestClientException e) {
				response.sendError(409,"1");
				return;
			}
			if (responseCheckToken.getStatusCode().equals(HttpStatus.CONFLICT)) {
				response.sendError(409,"2");
				return;
			}
			String userId = findUserInPath(path);
			System.out.println(userId);
			System.out.println(responseCheckToken.getHeaders().getFirst("X-userId"));
			if (!userId.equalsIgnoreCase(responseCheckToken.getHeaders().getFirst("X-userId"))) {
				response.sendError(409,"3");
				return;
			}
			response.addHeader("X-token", responseCheckToken.getHeaders().getFirst("X-token"));
			response.addHeader("X-userId", responseCheckToken.getHeaders().getFirst("X-userId"));
			chain.doFilter(new WrapperRequest(request, responseCheckToken.getHeaders().getFirst("X-userId")), response);
			return;
		}
		chain.doFilter(request, response);
	}

	private String findUserInPath(String path) {
		String userId = path.substring(path.indexOf("v1/") + 3);
		int end = userId.indexOf("/");
		if (end > 0) {
			userId = userId.substring(0, end);
		}
		return userId;
	}

	private boolean checkPointCut(String path, String method) {
		boolean check = "Options".equalsIgnoreCase(method);
		return check;
	}

	private class WrapperRequest extends HttpServletRequestWrapper {
		String user;

		public WrapperRequest(HttpServletRequest request, String user) {
			super(request);
			this.user = user;
		}

		@Override
		public Principal getUserPrincipal() {
			return new Principal() { // () -> user;

				@Override
				public String getName() {
					return user;
				}
			};
		}
	}

}
