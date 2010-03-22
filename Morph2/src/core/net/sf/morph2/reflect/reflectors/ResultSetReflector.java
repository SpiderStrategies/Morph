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
package net.sf.morph2.reflect.reflectors;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Iterator;

import org.springframework.jdbc.support.JdbcUtils;

import net.sf.morph2.reflect.BeanReflector;
import net.sf.morph2.reflect.ContainerReflector;
import net.sf.morph2.reflect.support.ResultSetIterator;
import net.sf.morph2.transform.TransformationException;

/**
 * <p>
 * Exposes the information in a ResultSet. This reflector can function both as a
 * container reflector and as a bean reflector. When functionining a container
 * reflector, this reflector exposes the rows in a ResultSet. As a bean
 * reflector, this reflector exposes the column names from a particular row of a
 * ResultSet as properties. The column names are always converted to all
 * lowercase.
 * </p>
 * 
 * <p>
 * Note: Code from the {@link #getImpl(Object, String)} method was taken from
 * Spring's {@link JdbcUtils} class.
 * </p>
 * 
 * @author Matt Sgarlata
 * @since Dec 18, 2004
 */
public class ResultSetReflector extends BaseBeanReflector implements BeanReflector,
		ContainerReflector {

	private static final Class[] REFLECTABLE_TYPES = new Class[] { ResultSet.class };

	private ResultSet getResultSet(Object bean) {
		return (ResultSet) bean;
	}

	private ResultSetMetaData getMetaData(Object bean) throws Exception {
		return getResultSet(bean).getMetaData();
	}

	// container reflector methods	

	/**
	 * {@inheritDoc}
	 */
	protected Class getContainedTypeImpl(Class clazz) throws Exception {
		return ResultSet.class;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Iterator getIteratorImpl(Object container) throws Exception {
		return new ResultSetIterator((ResultSet) container);
	}

	// bean reflector methods	

	private int getIndexForColumn(Object bean, String propertyName) throws Exception {
		int numColumns = getMetaData(bean).getColumnCount();
		String lowerCasePropertyName = propertyName.toLowerCase();
		for (int i = 1; i < numColumns; i++) {
			if (lowerCasePropertyName.equals(getMetaData(bean).getColumnLabel(i).toLowerCase())) {
				return i;
			}
		}
		throw new TransformationException("The propertyName you specified '" + propertyName
				+ "' was not found to be a column in the given ResultSet");
	}

	/**
	 * {@inheritDoc}
	 */
	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		ResultSetMetaData meta = getMetaData(bean);
		int numColumns = meta.getColumnCount();
		String[] propertyNames = new String[numColumns];
		for (int i = 0; i < propertyNames.length; i++) {
			propertyNames[i] = meta.getColumnName(i + 1).toLowerCase();
		}
		return propertyNames;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class getTypeImpl(Object bean, String propertyName) throws Exception {
		return Class.forName(getMetaData(bean).getColumnClassName(
				getIndexForColumn(bean, propertyName)));
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWriteableImpl(Object bean, String propertyName) throws Exception {
		return getMetaData(bean).isWritable(getIndexForColumn(bean, propertyName));
	}

	/**
	 * NOTE: Code copied here from Spring's
	 * {@link org.springframework.jdbc.support.JdbcUtils} class.
	 */
	protected Object getImpl(Object bean, String propertyName) throws Exception {
		ResultSet rs = getResultSet(bean);
		Object obj = rs.getObject(propertyName);
		if (obj instanceof Blob) {
			obj = rs.getBytes(propertyName);
		}
		else if (obj instanceof Clob) {
			obj = rs.getString(propertyName);
		}
		else if (obj != null && obj.getClass().getName().startsWith("oracle.sql.TIMESTAMP")) {
			obj = rs.getTimestamp(propertyName);
		}
		else if (obj != null && obj.getClass().getName().startsWith("oracle.sql.DATE")) {
			int index = getIndexForColumn(bean, propertyName);
			String metaDataClassName = rs.getMetaData().getColumnClassName(index);
			if ("java.sql.Timestamp".equals(metaDataClassName)
					|| "oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
				obj = rs.getTimestamp(propertyName);
			}
			else {
				obj = rs.getDate(propertyName);
			}
		}
		else if (obj != null && obj instanceof java.sql.Date) {
			int index = getIndexForColumn(bean, propertyName);
			if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
				obj = rs.getTimestamp(propertyName);
			}
		}
		return obj;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void setImpl(Object bean, String propertyName, Object value) throws Exception {
		getResultSet(bean).updateObject(propertyName, value);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getReflectableClassesImpl() throws Exception {
		return REFLECTABLE_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isStrictlyTyped() {
		return true;
	}

}
