/*
 * Copyright 2004-2005 the original author or authors.
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
package net.sf.morph.context.contexts;

import javax.servlet.http.HttpServletRequest;

import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.reflect.reflectors.HttpSessionAttributeReflector;
import net.sf.morph.reflect.reflectors.ServletContextAttributeReflector;
import net.sf.morph.reflect.reflectors.ServletRequestAttributeReflector;
import net.sf.morph.reflect.reflectors.ServletRequestParameterReflector;

/**
 * <p>
 * Context for HTTP servlets that evaluates request parameters, request
 * attributes, session attributes, and application attributes, in that order.
 * Exposing request parameters in this manner is somewhat unusual, so
 * {@link HttpServletContext#setReflectingRequestParameters(boolean)}can be
 * called to turn this behavior off.
 * </p>
 * 
 * <p>
 * This class is <em>not</em> threadsafe, but since usually only a single
 * thread is accessing a request at any given time, this should be OK.
 * </p>
 * 
 * @author Matt Sgarlata
 * @since Nov 29, 2004
 */
public class HttpServletContext extends ReflectorHierarchicalContext {

	private static final BeanReflector REQUEST_PARAMETER_REFLECTOR = new ServletRequestParameterReflector();
	private static final BeanReflector REQUEST_ATTRIBUTE_REFLECTOR = new ServletRequestAttributeReflector();
	private static final BeanReflector SESSION_ATTRIBUTE_REFLECTOR = new HttpSessionAttributeReflector();
	private static final BeanReflector APPLICATION_ATTRIBUTE_REFLECTOR = new ServletContextAttributeReflector();
	
	private ReflectorHierarchicalContext applicationContext;
	private ReflectorHierarchicalContext sessionContext;
	private ReflectorHierarchicalContext requestContext;
	
	private boolean reflectingRequestParameters;
	
	public HttpServletContext() {
		super();
		reflectingRequestParameters = true;
		
		applicationContext = new ReflectorHierarchicalContext();
		applicationContext.setBeanReflector(APPLICATION_ATTRIBUTE_REFLECTOR);

		sessionContext = new ReflectorHierarchicalContext();
		sessionContext.setBeanReflector(SESSION_ATTRIBUTE_REFLECTOR);
		sessionContext.setParentContext(applicationContext);

		requestContext = new ReflectorHierarchicalContext();
		requestContext.setBeanReflector(REQUEST_ATTRIBUTE_REFLECTOR);
		requestContext.setParentContext(sessionContext);
		
		this.setBeanReflector(REQUEST_PARAMETER_REFLECTOR);
		this.setParentContext(requestContext);
	}
	
	public HttpServletContext(HttpServletRequest request) {
		this();
		setRequest(request);
	}
	
	/**
	 * This method is overridden so that set calls are always invoked on the
	 * requestContext, since request parameters cannot be set but logically a
	 * set call is being sent to the request, so it should succeed.
	 */
	protected void setHierarchicalImpl(String propertyName, Object propertyValue)
		throws Exception {
		requestContext.set(propertyName, propertyValue);
	}
	
	public HttpServletRequest getRequest() {
		return (HttpServletRequest) getDelegate();
	}
	public void setRequest(HttpServletRequest request) {
		applicationContext.setDelegate(request.getSession().getServletContext());
		sessionContext.setDelegate(request.getSession());
		requestContext.setDelegate(request);
		this.setDelegate(request);
	}
	/**
	 * @return Returns the reflectingRequestParameters.
	 */
	public boolean isReflectingRequestParameters() {
		return reflectingRequestParameters;
	}
	/**
	 * @param reflectingRequestParameters The reflectingRequestParameters to set.
	 */
	public void setReflectingRequestParameters(
		boolean reflectingRequestParameters) {
		this.reflectingRequestParameters = reflectingRequestParameters;
		if (reflectingRequestParameters) {
			this.setBeanReflector(REQUEST_PARAMETER_REFLECTOR);
			this.setParentContext(requestContext);
		}
		else {
			this.setBeanReflector(REQUEST_ATTRIBUTE_REFLECTOR);
			this.setParentContext(sessionContext);
		}
	}
}