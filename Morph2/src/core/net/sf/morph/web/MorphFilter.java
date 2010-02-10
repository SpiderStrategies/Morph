/*
 * Copyright 2004-2005, 2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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

	/**
	 * Default request attribute where the {@link HttpServletContext} will be stored.
	 */
	public static final String DEFAULT_REQUEST_ATTRIBUTE = "morph";

	private String requestAttribute;

	/**
	 * Create a new MorphFilter instance.
	 */
	public MorphFilter() {
		setRequestAttribute(DEFAULT_REQUEST_ATTRIBUTE);
	}

	/**
	 * {@inheritDoc}
	 */
	public void init(FilterConfig config) throws ServletException {
		// do nothing
	}

	/**
	 * {@inheritDoc}
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (request.getAttribute(getRequestAttribute()) == null) {
			request.setAttribute(getRequestAttribute(), new HttpServletContext(
					(HttpServletRequest) request));
		}

		chain.doFilter(request, response);
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroy() {
		// do nothing
	}

	/**
	 * Get the requestAttribute of this MorphFilter.
	 * @return the requestAttribute
	 */
	public String getRequestAttribute() {
		return requestAttribute;
	}

	/**
	 * Set the requestAttribute of this MorphFilter.
	 * @param requestAttribute the String to set
	 */
	public void setRequestAttribute(String requestAttribute) {
		this.requestAttribute = requestAttribute;
	}

}
