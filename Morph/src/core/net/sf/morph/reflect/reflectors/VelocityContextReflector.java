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
package net.sf.morph.reflect.reflectors;

import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;

/**
 * Exposes the information in a {@link org.apache.velocity.context.Context}.
 * 
 * @author Matt Sgarlata
 * @since Dec 29, 2004
 */
public class VelocityContextReflector extends BaseBeanReflector {
	
	private static final Class[] REFLECTABLE_TYPES = { VelocityContext.class };

	protected Class[] getReflectableClassesImpl() throws Exception {
		return REFLECTABLE_TYPES;
	}

	protected Context getVelocityContext(Object bean) {
		return (Context) bean;
	}
	
	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		Object[] keys = getVelocityContext(bean).getKeys();
		List propertyNames = new ArrayList();
		for (int i=0; i<keys.length; i++) {
			if (keys[i] instanceof String) {
				propertyNames.add(keys[i]);
			}
		}
		return (String[]) propertyNames.toArray(new String[propertyNames.size()]);
	}

	protected Class getTypeImpl(Object bean, String propertyName) throws Exception {
		return Object.class;
	}

	protected boolean isReadableImpl(Object bean, String propertyName) throws Exception {
		return true;
	}

	protected boolean isWriteableImpl(Object bean, String propertyName) throws Exception {
		return true;
	}

	protected Object getImpl(Object bean, String propertyName) throws Exception {
		return getVelocityContext(bean).get(propertyName);
	}

	protected void setImpl(Object bean, String propertyName, Object value) throws Exception {
		getVelocityContext(bean).put(propertyName, value);
	}
	
	

}