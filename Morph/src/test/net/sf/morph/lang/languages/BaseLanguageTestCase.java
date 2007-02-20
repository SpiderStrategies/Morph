package net.sf.morph.lang.languages;

import java.util.Locale;

import junit.framework.TestCase;
import net.sf.morph.lang.DecoratedLanguage;
import net.sf.morph.lang.Language;
import net.sf.morph.lang.LanguageException;
import net.sf.morph.util.TestClass;

/**
 * @author Matt Sgarlata
 * @since Dec 4, 2004
 */
public abstract class BaseLanguageTestCase extends TestCase {

	protected Language language;
	
	public BaseLanguageTestCase() {
		super();
	}
	public BaseLanguageTestCase(String arg0) {
		super(arg0);
	}
	
	public void testGetTypeArguments() {
		try {
			language.getType(null, "anything");
			fail("language.getType(null, <anything>) should throw a LanguageException");
		}
		catch (LanguageException e) { }
	}
	
	public void testGetArguments() {
		try {
			language.get(null, "anything");
			fail("language.get(null, <anything>) should throw a LanguageException");
		}
		catch (LanguageException e) { }
		if (language instanceof DecoratedLanguage) {
			try {
				getDecoratedLanguage().get(null, "anything", TestClass.class);
				fail("language.get(null, <anything>, <anthing>) should throw a LanguageException");
			}
			catch (LanguageException e) { }
			try {
				getDecoratedLanguage().get(null, "anything", TestClass.class, Locale.getDefault());
				fail("language.get(null, <anything>, <anthing>) should throw a LanguageException");
			}
			catch (LanguageException e) { }
			try {
				getDecoratedLanguage().get(null, "anything", TestClass.class, Locale.getDefault());
				fail("language.get(null, <anything>, <anthing>) should throw a LanguageException");
			}
			catch (LanguageException e) { }
		}
	}
	
	/**
	 * TODO need to finish up for decorated languages
	 */
	public void testsetArguments() {
		try {
			language.set(null, "anything", "anything");
			fail("language.set(null, <anything>, <anything>) should throw a LanguageException");
		}
		catch (LanguageException e) { }
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		language = createLanguage();
	}
	
	protected abstract Language createLanguage();
	
	public DecoratedLanguage getDecoratedLanguage() {
		return (DecoratedLanguage) language;
	}
}
