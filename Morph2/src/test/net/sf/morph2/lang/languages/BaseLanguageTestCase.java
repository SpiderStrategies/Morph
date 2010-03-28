/*
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
package net.sf.morph2.lang.languages;

import java.util.Locale;

import junit.framework.TestCase;
import net.sf.morph2.lang.DecoratedLanguage;
import net.sf.morph2.lang.Language;
import net.sf.morph2.lang.LanguageException;
import net.sf.morph2.util.TestClass;

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
