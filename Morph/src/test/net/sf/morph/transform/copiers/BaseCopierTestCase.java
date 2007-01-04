package net.sf.morph.transform.copiers;

import java.util.Locale;

import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.converters.BaseConverterTestCase;

/**
 * @author Matt Sgarlata
 * @since Dec 11, 2004
 */
public abstract class BaseCopierTestCase extends BaseConverterTestCase {

	public BaseCopierTestCase() {
		super();
	}
	public BaseCopierTestCase(String arg0) {
		super(arg0);
	}
	public void testCopyArguments() {
		try {
			getCopier().copy(null, new Object(), Locale.getDefault());
			fail("Cannot copy to a null destination");
		}
		catch (TransformationException e) { }
		
		try {
			getCopier().copy(new Object(), null, null);
			fail("Cannot copy from a null source");
		}
		catch (TransformationException e) { }
	}

}
