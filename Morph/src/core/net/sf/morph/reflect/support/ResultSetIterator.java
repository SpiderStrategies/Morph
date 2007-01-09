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

	public ResultSetIterator(ResultSet resultSet) {
		this.resultSet = resultSet;
		// initialize to true, since when we start the result set begins before
		// the first row.  we don't actually need to return the row before the
		// first row, so we can consider it as having been already returned
		this.hasReturnedRow = true;
	}

	public boolean hasNext() {
		if (hasReturnedRow) {
			advanceToNextRow();
			hasReturnedRow = false;
		}
		return hasNext;
	}

	public Object next() {
		if (hasNext()) {
			hasReturnedRow = true;
			return resultSet;
		}
		throw new NoSuchElementException(NO_MORE);
	}

	protected void advanceToNextRow() throws MorphException {
		try {
			hasNext = resultSet.next();
		}
		catch (SQLException e) {
			handleResultSetNextException(e);
		}
	}

	protected void handleResultSetNextException(SQLException e)
		throws MorphException {
		if (log.isErrorEnabled()) {
			log.error("Error moving to next row in resultSet", e);
		}
		throw new MorphException("Error moving to next row in resultSet", e);
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}