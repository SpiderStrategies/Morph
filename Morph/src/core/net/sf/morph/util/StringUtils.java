package net.sf.morph.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.morph.Morph;

/**
 * @author Matt Sgarlata
 * @since Mar 11, 2005
 */
public class StringUtils {
	
//	private static final DecoratedConverter LIST_CONVERTER = new SimpleDelegatingTransformer();

	public static int numOccurrences(String toSearch, String searchFor) {
		if (toSearch == null && searchFor == null) {
			return 0;
		}
		else if (searchFor == null || searchFor.length() == 0) {
			return 0;
		}
		else {
			int matchingIndex = 0;
			int numOccurrences = 0;
			for (int i=0; i<toSearch.length(); i++) {
				if (toSearch.charAt(i) == searchFor.charAt(matchingIndex)) {
					if (matchingIndex == searchFor.length() -1) {
						matchingIndex = 0;
						numOccurrences++;
					}
					else {
						matchingIndex++;
					}
				}
			}
			return numOccurrences;
		}
		
	}

	public static String repeat(String str, int repititions) {
		int size = repititions * (str == null ? 0 : str.length());
		StringBuffer buffer = new StringBuffer(size);
		for (int i=0; i<repititions; i++) {
			buffer.append(str);
		}
		return buffer.toString();
	}

	public static String englishJoin(Object values) {
		if (values == null) {
			return null;
		}
		else {
			StringBuffer buffer = new StringBuffer();
			int length = Array.getLength(values);
			for (int i = 0; i < length; i++) {
				buffer.append(Array.get(values, i));
				if (i < length - 2) {
					buffer.append(", ");
				}
				if (i == length - 2) {
					buffer.append(" and ");
				}
			}
			return buffer.toString();
		}
	}

	public static String englishJoin(Collection values) {
		return englishJoin(values.toArray(new Object[values.size()]));
	}

	public static String removeWhitespace(String expression) {
		if (expression == null) {
			return null;
		}
		StringBuffer buffer = new StringBuffer(expression.length());
		for (int i=0; i<expression.length(); i++) {
			if (!Character.isWhitespace(expression.charAt(i))) {
				buffer.append(expression.charAt(i));
			}
		}
		return buffer.toString();
	}

	public static String join(Object object, String separator) {
		if (object == null) {
			return null;
		}
		
		//build the metric id string
		StringBuffer idBuff = new StringBuffer();
		boolean firstIteration = true;
		Collection collection = (Collection)
			Morph.convert(List.class, object);
		for (Iterator iter = collection.iterator(); iter.hasNext();) {
			if (firstIteration) {
				firstIteration = false;
			}
			else {
				idBuff.append(separator);
			}
			idBuff.append(iter.next());
		}
		return idBuff.toString();
	}

	public static String join(Object object) {
		return join(object, ", ");
	}

}
