package net.sf.morph.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.sf.morph.context.contexts.HttpServletContext;

/**
 * Stores an {@link net.sf.morph.context.support.HttpServletContext} in the
 * HTTP request.
 * 
 * @author Matt Sgarlata
 * @since Dec 20, 2004
 */
public class MorphFilter implements Filter {
	
	public static final String DEFAULT_REQUEST_ATTRIBUTE = "morph";
	
	private String requestAttribute;
	
	public MorphFilter() {
		requestAttribute = DEFAULT_REQUEST_ATTRIBUTE;
	}

	public void init(FilterConfig config) throws ServletException {
		// do nothing
	}

	public void doFilter(ServletRequest request, ServletResponse response,
		FilterChain chain) throws IOException, ServletException {
		if (request.getAttribute(getRequestAttribute()) == null) {
			request.setAttribute(getRequestAttribute(),
				new HttpServletContext((HttpServletRequest) request));
		}
		
		chain.doFilter(request, response);
	}

	public void destroy() {
		// do nothing
	}

	public String getRequestAttribute() {
		return requestAttribute;
	}
	public void setRequestAttribute(String requestAttributeName) {
		this.requestAttribute = requestAttributeName;
	}
}
