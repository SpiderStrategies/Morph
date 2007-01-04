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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Iterator;

import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.reflect.ContainerReflector;
import net.sf.morph.reflect.support.ResultSetIterator;
import net.sf.morph.transform.TransformationException;

/**
 * Exposes the information in a ResultSet. This reflector can function both as a
 * container reflector and as a bean reflector. When functionining a container
 * reflector, this reflector exposes the rows in a ResultSet. As a bean
 * reflector, this reflector exposes the column names from a particular row of a
 * ResultSet as properties. The column names are always converted to all
 * lowercase.
 * 
 * @author Matt Sgarlata
 * @since Dec 18, 2004
 */
public class ResultSetReflector
	extends BaseBeanReflector
	implements BeanReflector, ContainerReflector {
	
	private static final Class[] REFLECTABLE_TYPES = new Class[] {
		ResultSet.class
	};
	
	private ResultSet getResultSet(Object bean) {
		return (ResultSet) bean;
	}
	
	private ResultSetMetaData getMetaData(Object bean) throws Exception {
		return getResultSet(bean).getMetaData();
	}

// container reflector methods	
	
	protected Class getContainedTypeImpl(Class clazz) throws Exception {
		return ResultSet.class;
	}
	protected Iterator getIteratorImpl(Object container) throws Exception {
		return new ResultSetIterator((ResultSet) container);
	}

// bean reflector methods	
	
	private int getIndexForColumn(Object bean, String propertyName) throws Exception {
		int numColumns = getMetaData(bean).getColumnCount();
		String lowerCasePropertyName = propertyName.toLowerCase();
		for (int i=1; i<numColumns; i++) {
			if (lowerCasePropertyName.equals(getMetaData(bean).getColumnLabel(i).toLowerCase())) {
				return i;
			}
		}
		throw new TransformationException("The propertyName you specified '" + propertyName + "' was not found to be a column in the given ResultSet");
	}

	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		ResultSetMetaData meta = getMetaData(bean);
		int numColumns = meta.getColumnCount();
		String[] propertyNames = new String[numColumns];
		for (int i=0; i<propertyNames.length; i++) {
			propertyNames[i] = meta.getColumnName(i+1).toLowerCase();
		}
		return propertyNames;
	}

	protected Class getTypeImpl(Object bean, String propertyName)
		throws Exception {
		return Class.forName(getMetaData(bean).getColumnClassName(
			getIndexForColumn(bean, propertyName)));
	}

	protected boolean isReadableImpl(Object bean, String propertyName)
		throws Exception {
		return super.isReadable(bean, propertyName.toLowerCase());
	}

	protected boolean isWriteableImpl(Object bean, String propertyName)
		throws Exception {
		return getMetaData(bean).isWritable(
			getIndexForColumn(bean, propertyName));
	}

	protected Object getImpl(Object bean, String propertyName) throws Exception {
		return getResultSet(bean).getObject(propertyName);
	}

	protected void setImpl(Object bean, String propertyName, Object value)
		throws Exception {
		getResultSet(bean).updateObject(propertyName, value);
	}

	protected Class[] getReflectableClassesImpl() throws Exception {
		return REFLECTABLE_TYPES;
	}

	public boolean isStrictlyTyped() {
		return true;
	}

}
