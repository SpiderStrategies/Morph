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
package net.sf.morph.reflect.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Holds a reference to the getter and setter methods of a JavaBeans property.
 *  
 * @author Matt Sgarlata
 * @author Alexander Volanis
 * @since Feb 3, 2005
 */
public class MethodHolder {
	
	private static final Log log = LogFactory.getLog(MethodHolder.class);
	
    private Method mutator;
    private Method indexedMutator;
    private Method accessor;
    private Method indexedAccessor;

	public Method getAccessor() {
		return accessor;
	}
	public void setAccessor(Method accessor) {
		this.accessor = accessor;
	}
	public Method getMutator() {
		return mutator;
	}
	public void setMutator(Method mutator) {
		this.mutator = mutator;
	}
    public Method getIndexedAccessor() {
        return indexedAccessor;
    }
    public void setIndexedAccessor(Method indexedAccessor) {
        this.indexedAccessor = indexedAccessor;
    }
    public Method getIndexedMutator() {
        return indexedMutator;
    }
    public void setIndexedMutator(Method indexedMutator) {
        this.indexedMutator = indexedMutator;
    }

    protected String methodToString(Method method) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<");
		buffer.append(method.getReturnType() == null ? "void" : method.getReturnType().getName());
		buffer.append("> ");
		buffer.append(method.getName());
		buffer.append("(");
		Class[] parameterTypes = method.getParameterTypes();
		if (parameterTypes != null) {
			for (int i=0; i<parameterTypes.length; i++) {
				buffer.append(parameterTypes[i].getName());
				if (i != parameterTypes.length - 1) {
					buffer.append(",");
				}
			}
		}
		buffer.append(")");
		return buffer.toString();
	}
	
    public void invokeMutator(Object bean, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (log.isTraceEnabled()) {
            log.trace("Invoking mutator " + methodToString(mutator) + " on bean of class " + bean.getClass().getName());
        }
        mutator.invoke(bean, new Object[] { value });
    }
    public Object invokeAccessor(Object bean) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (log.isTraceEnabled()) {
            log.trace("Invoking accessor " + methodToString(accessor) + " on bean of class " + bean.getClass().getName());
        }
        return accessor.invoke(bean, (Object[]) null);
    }
    public void invokeIndexedMutator(Object bean, Object index, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (log.isTraceEnabled()) {
            log.trace("Invoking mutator " + methodToString(indexedMutator) + " on bean of class " + bean.getClass().getName());
        }
        indexedMutator.invoke(bean, new Object[] { index, value });
    }
    public Object invokeIndexedAccessor(Object bean, Object index) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (log.isTraceEnabled()) {
            log.trace("Invoking accessor " + methodToString(indexedAccessor) + " on bean of class " + bean.getClass().getName());
        }
        return indexedAccessor.invoke(bean, new Object[] { index });
    }
}