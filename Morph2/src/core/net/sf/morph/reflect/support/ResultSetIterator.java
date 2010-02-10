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
package net.sf.morph.reflect.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sf.morph.MorphException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An iterator over a ResultSet.  SQLExceptions are wrapped in MorphExceptions.
 *
 * @author Matt Sgarlata
 * @since Dec 20, 2004
 */
public class ResultSetIterator implements Iterator {

	private static final Log log = LogFactory.getLog(ResultSetIterator.class);
	private static final String NO_MORE = "There are no more rows in the ResultSet";

	private ResultSet resultSet;
	private boolean hasNext;
	// set to true if the current row has been successfully sent to the user
	private boolean hasReturnedRow;

	/**
	 * Create a new ResultSetIterator instance.
	 * @param resultSet
	 */
	public ResultSetIterator(ResultSet resultSet) {
		this.resultSet = resultSet;
		// initialize to true, since when we start the result set begins before
		// the first row.  we don't actually need to return the row before the
		// first row, so we can consider it as having been already returned
		this.hasReturnedRow = true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasNext() {
		if (hasReturnedRow) {
			advanceToNextRow();
			hasReturnedRow = false;
		}
		return hasNext;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object next() {
		if (hasNext()) {
			hasReturnedRow = true;
			return resultSet;
		}
		throw new NoSuchElementException(NO_MORE);
	}

	/**
	 * Advance to next row.
	 * @throws MorphException
	 */
	protected void advanceToNextRow() throws MorphException {
		try {
			hasNext = resultSet.next();
		} catch (SQLException e) {
			handleResultSetNextException(e);
		}
	}

	/**
	 * Handle ResultSet next exception.
	 * @param e
	 * @throws MorphException
	 */
	protected void handleResultSetNextException(SQLException e) throws MorphException {
		if (log.isErrorEnabled()) {
			log.error("Error moving to next row in resultSet", e);
		}
		throw new MorphException("Error moving to next row in resultSet", e);
	}

	/**
	 * {@inheritDoc}
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}
}