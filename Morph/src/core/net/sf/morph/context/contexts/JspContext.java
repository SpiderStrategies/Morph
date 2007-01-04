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
import javax.servlet.jsp.PageContext;

import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.reflect.reflectors.PageContextAttributeReflector;

/**
 * <p>
 * Context for JSP pages that evaluates page context attributes, request
 * parameters, request attributes, session attributes, and application
 * attributes, in that order. Exposing request parameters in this manner is
 * somewhat unusual, so
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
 * @since Dec 4, 2004
 */
public class JspContext extends ReflectorHierarchicalContext {
	
	private static final BeanReflector PAGE_CONTEXT_ATTRIBUTE_REFLECTOR = new PageContextAttributeReflector();
	
	private HttpServletContext httpServletContext;
	
	private PageContext pageContext;
	
	public JspContext() {
		httpServletContext = new HttpServletContext();
		
		this.setBeanReflector(PAGE_CONTEXT_ATTRIBUTE_REFLECTOR);
		this.setParentContext(httpServletContext);
	}
	
	public JspContext(PageContext pageContext) {
		this();
		setPageContext(pageContext);
	}
	
	/**
	 * @return Returns the pageContext.
	 */
	public PageContext getPageContext() {
		return pageContext;
	}
	/**
	 * @param pageContext The pageContext to set.
	 */
	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
		this.httpServletContext.setRequest((HttpServletRequest) pageContext.getRequest());
		this.setDelegate(pageContext);
	}
	public boolean isReflectingRequestParameters() {
		return httpServletContext.isReflectingRequestParameters();
	}
	public void setReflectingRequestParameters(
		boolean reflectingRequestParameters) {
		httpServletContext.setReflectingRequestParameters(reflectingRequestParameters);
	}
}