package net.sf.morph.transform.converters;

import java.util.Locale;

import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.transformers.BaseTransformer;
import net.sf.morph.util.ClassUtils;

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

	private static final Class[] SOURCE_DEST_CLASSES;
	static {
		Class[] primitiveTypes = ClassUtils.getPrimitiveTypes();
		Class[] wrapperTypes = ClassUtils.getWrapperTypes();
		SOURCE_DEST_CLASSES = new Class[primitiveTypes.length + wrapperTypes.length];
		System.arraycopy(primitiveTypes, 0, SOURCE_DEST_CLASSES, 0, primitiveTypes.length);
		System.arraycopy(wrapperTypes, 0, SOURCE_DEST_CLASSES, primitiveTypes.length, wrapperTypes.length);
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_DEST_CLASSES;
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return SOURCE_DEST_CLASSES;
	}

	protected boolean isTransformableImpl(Class destinationType, Class sourceType) throws Exception {
		return destinationType != null && sourceType != null
				&& (sourceType == ClassUtils.getPrimitiveWrapper(destinationType) ||
						destinationType == ClassUtils.getPrimitiveWrapper(sourceType));
	}

	protected Object convertImpl(Class destinationClass, Object source, Locale locale) throws Exception {
		return source;
	}

}
