/*
 * Copyright 2004-2005, 2008, 2010 the original author or authors.
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
package net.sf.morph2.context.contexts;

import java.util.Arrays;
import java.util.Set;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.context.Context;
import net.sf.morph2.context.ContextException;
import net.sf.morph2.context.HierarchicalContext;
import net.sf.morph2.util.ContainerUtils;

/**
 * <p>
 * Convenient base class for hierarchical Contexts. Validates arguments and
 * takes care of logging and exception handling. Also implements the
 * {@link java.util.Map} interface.
 * </p>
 *
 * @author Matt Sgarlata
 * @since Nov 29, 2004
 * @see net.sf.morph2.context.support.BaseContext
 * @see net.sf.morph2.context.HierarchicalContext
 */
public abstract class BaseHierarchicalContext extends BaseContext implements
	HierarchicalContext {

	private Context parentContext;

	/**
	 * Creates a new, empty context.
	 */
	public BaseHierarchicalContext() {
		super();
	}

	/**
	 * Creates a new, empty context with the specified parent.
	 */
	public BaseHierarchicalContext(Context parentContext) {
		super();
		this.parentContext = parentContext;
	}

	//TODO javadoc
	protected abstract Object getHierarchicalImpl(String propertyName) throws Exception;

	//TODO javadoc
	protected abstract String[] getPropertyNamesHierarchicalImpl() throws Exception;

	//TODO javadoc
	protected abstract void setHierarchicalImpl(String propertyName, Object propertyValue) throws Exception;

	/**
	 * {@inheritDoc}
	 */
	protected final Object getImpl(String propertyName) throws Exception {
		Object value = null;

		if (getLog().isTraceEnabled()) {
			getLog().trace("Getting property '" + propertyName + "' from context " + getClass().getName());
		}

		try {
			value = this.getHierarchicalImpl(propertyName);
		}
		catch (ContextException e) {
			if (getLog().isDebugEnabled()) {
				getLog().debug("Unable to retrieve property '" + propertyName + "' from context " + getClass().getName() + "; attempting to read property from parent context");
			}
		}
		if (value == null) {
			Context parent = getParentContext();
			if (parent != null) {
				value = parent.get(propertyName);
			}
		}
		return value;

//		// search the parent contexts
//		Context currentContext = parentContext;
//		int indent = 0;
//		while (value == null && currentContext != null &&
//			currentContext instanceof HierarchicalContext) {
//			value = currentContext.get(propertyName);
//			indent++;
//			if (getLog().isTraceEnabled()) {
//				getLog().trace(MorphUtils.repeat("  ", indent) + "Got value " + value + " for property '" + propertyName + "' from " + ObjectUtils.getObjectDescription(this));
//			}
//			currentContext =
//				((HierarchicalContext) currentContext).getParentContext();
//		}
//		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	protected final String[] getPropertyNamesImpl() throws Exception {
		String[] currentPropertyNames = this.getPropertyNamesHierarchicalImpl();
		Set propertyNames = ContainerUtils.createOrderedSet();
		if (!ObjectUtils.isEmpty(currentPropertyNames)) {
			propertyNames.addAll(Arrays.asList(currentPropertyNames));
		}
		Context currentContext = parentContext;
		while (currentContext != null &&
			currentContext instanceof HierarchicalContext) {
			currentPropertyNames = currentContext.getPropertyNames();
			if (!ObjectUtils.isEmpty(currentPropertyNames)) {
				propertyNames.addAll(
					Arrays.asList(currentPropertyNames));
			}
			currentContext =
				((HierarchicalContext) currentContext).getParentContext();
		}
		return (String[])
			propertyNames.toArray(new String[propertyNames.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	protected final void setImpl(String propertyName, Object propertyValue)
		throws Exception {

		if (getLog().isTraceEnabled()) {
			getLog().trace("Setting property '" + propertyName + "' to value " + ObjectUtils.getObjectDescription(propertyValue) + " for context " + ObjectUtils.getObjectDescription(this));
		}

		try {
			setHierarchicalImpl(propertyName, propertyValue);
		}
		catch (Exception e) {
			if (getLog().isDebugEnabled()) {
				getLog().debug("Unable to retrieve property '" + propertyName + "' from context " + ObjectUtils.getObjectDescription(this) + "; attempting to read property from parent context");
			}
			if (getParentContext() == null) {
				throw e;
			}
			getParentContext().set(propertyName, propertyValue);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setParentContext(Context parentContext) {
		this.parentContext = parentContext;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Context getParentContext() {
		return parentContext;
	}

}
