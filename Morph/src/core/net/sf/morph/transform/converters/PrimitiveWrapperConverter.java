package net.sf.morph.transform.converters;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.transformers.BaseTransformer;

/**
 * Converts primitive objects to their Object wrappers and vice-versa. Cannot
 * convert a primitive to a primitive or a wrapper to a wrapper (for those
 * conversions, use the
 * {@link net.sf.morph.transform.converters.IdentityConverter}.
 * 
 * @author Matt Sgarlata
 * @since Jun 14, 2006
 */
public class PrimitiveWrapperConverter extends BaseTransformer implements DecoratedConverter {

	private static final HashMap TYPE_MAP = new HashMap() {
		{
			put(boolean.class, Boolean.class);
			put(byte.class, Byte.class);
			put(short.class, Short.class);
			put(char.class, Character.class);
			put(int.class, Integer.class);
			put(long.class, Long.class);
			put(float.class, Float.class);
			put(double.class, Double.class);
		}
	};
	private static final Class[] SOURCE_DEST_CLASSES;
	static {
		HashSet s = new HashSet(TYPE_MAP.keySet());
		s.addAll(TYPE_MAP.values());
		SOURCE_DEST_CLASSES = (Class[]) s.toArray(new Class[s.size()]);
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_DEST_CLASSES;
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return SOURCE_DEST_CLASSES;
	}

	protected boolean isTransformableImpl(Class destinationType, Class sourceType) throws Exception {
		return destinationType != null && sourceType != null
				&& (sourceType == TYPE_MAP.get(destinationType) || destinationType == TYPE_MAP.get(sourceType));
	}

	protected Object convertImpl(Class destinationClass, Object source, Locale locale) throws Exception {
		return source;
	}

}
