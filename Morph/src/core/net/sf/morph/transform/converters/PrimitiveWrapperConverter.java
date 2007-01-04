package net.sf.morph.transform.converters;

import java.util.Locale;

import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.ExplicitTransformer;
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
public class PrimitiveWrapperConverter extends BaseTransformer implements DecoratedConverter, ExplicitTransformer {

	protected Class[] getSourceClassesImpl() throws Exception {
		return new Class[] {
			boolean.class, byte.class, char.class, short.class,
			int.class, long.class, float.class, double.class,
			Boolean.class, Byte.class, char.class, Short.class,
			Integer.class, Long.class, Float.class, Double.class
		};
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return getSourceClassesImpl();
	}

	protected boolean isTransformableImpl(Class destinationType, Class sourceType) throws Exception {
		return 
			bothBoolean(destinationType, sourceType) ||
			bothByte(destinationType, sourceType) ||
			bothChar(destinationType, sourceType) ||
			bothShort(destinationType, sourceType) ||
			bothInt(destinationType, sourceType) ||
			bothLong(destinationType, sourceType) ||
			bothFloat(destinationType, sourceType) ||
			bothDouble(destinationType, sourceType);
	}

	protected Object convertImpl(Class destinationClass, Object source, Locale locale) throws Exception {
		return source;
	}

	private boolean bothBoolean(Class destinationType, Class sourceType) {
		return
			Boolean.class.equals(destinationType) && boolean.class.equals(sourceType) ||
			boolean.class.equals(destinationType) && Boolean.class.equals(sourceType);		
	}
	
	private boolean bothByte(Class destinationType, Class sourceType) {
		return
			Byte.class.equals(destinationType) && byte.class.equals(sourceType) ||
			byte.class.equals(destinationType) && Byte.class.equals(sourceType);		
	}
	
	private boolean bothChar(Class destinationType, Class sourceType) {
		return
			Character.class.equals(destinationType) && char.class.equals(sourceType) ||
			char.class.equals(destinationType) && Character.class.equals(sourceType);		
	}
	
	private boolean bothShort(Class destinationType, Class sourceType) {
		return
			Short.class.equals(destinationType) && short.class.equals(sourceType) ||
			short.class.equals(destinationType) && Short.class.equals(sourceType);		
	}
	
	private boolean bothInt(Class destinationType, Class sourceType) {
		return
			Integer.class.equals(destinationType) && int.class.equals(sourceType) ||
			int.class.equals(destinationType) && Integer.class.equals(sourceType);		
	}
	
	private boolean bothLong(Class destinationType, Class sourceType) {
		return
			Long.class.equals(destinationType) && long.class.equals(sourceType) ||
			long.class.equals(destinationType) && Long.class.equals(sourceType);		
	}
	
	private boolean bothFloat(Class destinationType, Class sourceType) {
		return
			Float.class.equals(destinationType) && float.class.equals(sourceType) ||
			float.class.equals(destinationType) && Float.class.equals(sourceType);		
	}
	
	private boolean bothDouble(Class destinationType, Class sourceType) {
		return
			Double.class.equals(destinationType) && double.class.equals(sourceType) ||
			double.class.equals(destinationType) && Double.class.equals(sourceType);		
	}

}
