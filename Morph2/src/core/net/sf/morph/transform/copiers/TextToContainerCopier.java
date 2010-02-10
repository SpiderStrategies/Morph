/*
 * Copyright 2007-2008 the original author or authors.
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
package net.sf.morph.transform.copiers;

import java.util.Enumeration;
import java.util.Locale;

import net.sf.morph.Defaults;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.Copier;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.DecoratedCopier;
import net.sf.morph.transform.transformers.BaseTransformer;
import net.sf.morph.util.MorphStringTokenizer;

/**
 * Parses text into multiple parts for storage in a container. For example, the
 * text <code>{ 1, 2, 3 }</code> code be converted into an
 * <code>Integer[]</code> array like this:
 * 
 * <pre>
 * String str = &quot;{ 1, 2, 3 }&quot;;
 * 
 * TextToContainerCopier copier = new TextToContainerCopier();
 * 
 * Integer[] array = (Integer[]) copier.convert(Integer[].class, str);
 * </pre>
 * 
 * <b>Configuration parameters</b>
 * <ul>
 * <li><code>ignoredCharacters</code> characters that are completely ignored
 * in the source. The default characters (specified by
 * {@link #DEFAULT_IGNORED_CHARACTERS}) are often used as grouping type
 * characters and probably aren't part of the information to be extracted to the
 * destination object.</li>
 * <li><code>delimiter</code> characters that are used to separate the
 * different elements to be copied to the container. The default characters are
 * specified by {@link #DEFAULT_DELIMITERS}.
 * </ul>
 * 
 * @author Matt Sgarlata
 * @since Apr 9, 2007
 */
public class TextToContainerCopier extends BaseTransformer implements DecoratedConverter, DecoratedCopier {
	/** Default delimiters */
	public static final String DEFAULT_DELIMITERS = " ,|";

	/** Default ignored characters */
	public static final String DEFAULT_IGNORED_CHARACTERS  = "()[]{}";

	private String delimiters = DEFAULT_DELIMITERS;
	private String ignoredCharacters = DEFAULT_IGNORED_CHARACTERS;

	private Converter textConverter = Defaults.createTextConverter();
	private Copier containerCopier = Defaults.createContainerCopier();

	/**
	 * {@inheritDoc}
	 */
	protected Object convertImpl(Class destinationClass, Object source, Locale locale) throws Exception {
		Enumeration tokenizer = getTokenizer(source, locale);
		// this call is the key reason we can't just fall back on the behavior
		// of the superclass.  a string like "1,2" is going to look to the
		// superclass like it has a size as 1
		Object destination = createNewInstance(destinationClass, tokenizer);
		getContainerCopier().copy(destination, tokenizer, locale);
		return destination;
    }

	/**
	 * {@inheritDoc}
	 */
	protected void copyImpl(Object destination, Object source, Locale locale, Integer preferredTransformationType) throws Exception {
		Enumeration tokenizer = getTokenizer(source, locale);	    
	    getContainerCopier().copy(destination, tokenizer, locale);
    }

	/**
	 * Constructs a StringTokenizer that can be passed directly to the
	 * <code>containerCopier</code> to complete the transformation.
	 * 
	 * @param source
	 *            the source, as passed in by the user
	 * @param locale
	 *            the locale in which the transformation is to take place
	 * @return a StringTokenizer that can be passed directly to the
	 *         <code>containerCopier</code> to complete the transformation
	 */
	protected Enumeration getTokenizer(Object source, Locale locale) {
	    // prepare the source
	    String sourceStr = (String) getTextConverter().convert(String.class, source, locale);
	    sourceStr = removeIgnoredCharacters(sourceStr, getIgnoredCharacters());
	    // parse the source into separate tokens
	    return new MorphStringTokenizer(sourceStr, getDelimiters());
    }
	
	/**
	 * Remove all characters contained in <code>ignoredCharacters</code> from
	 * <code>source</code>.
	 * 
	 * @param source
	 *            the String from which characters are to be removed
	 * @param ignoredCharacters
	 *            the characters to be removed
	 * @return a copy of <code>source</code>, but with all characters
	 *         contained in <code>ignoredCharacters</code> removed
	 */
	protected String removeIgnoredCharacters(String source, String ignoredCharacters) {
		if (source == null) {
			return null;
		}
		if (ignoredCharacters == null || "".equals(ignoredCharacters)) {
			return source;
		}

		StringBuffer buffer = new StringBuffer(source.length());
		for (int i=0; i<source.length(); i++) {
			for (int j=0; j<ignoredCharacters.length(); j++	) {
				// if we find an ignored character, break out of this loop
				if (source.charAt(i) == ignoredCharacters.charAt(j)) {
					break;
				}
				// if we get here, the character should not be ignored
				if (j == ignoredCharacters.length() - 1) {
					buffer.append(source.charAt(i));
				}
			}			
		}
		return buffer.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return getContainerCopier().getDestinationClasses();
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return getTextConverter().getSourceClasses();
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWrappingRuntimeExceptions() {
	    return false;
    }

	/**
	 * Returns the characters that are used to separate the different elements
	 * to be copied to the container.
	 * 
	 * @return the characters that are used to separate the different elements
	 *         to be copied to the container.
	 */
	public String getDelimiters() {
		return delimiters;
	}

	/**
	 * Sets the characters that are used to separate the different elements to
	 * be copied to the container.
	 * 
	 * @param delimiters
	 *            the characters that are used to separate the different
	 *            elements to be copied to the container
	 */
	public void setDelimiters(String delimiters) {
    	this.delimiters = delimiters;
    }

	/**
	 * Returns characters that are completely ignored in the source.
	 * 
	 * @return characters that are completely ignored in the source
	 */
	public String getIgnoredCharacters() {
		return ignoredCharacters;
	}

	/**
	 * Sets the characters that are completely ignored in the source.
	 * 
	 * @param ignoredCharacters
	 *            the characters that are completely ignored in the source
	 */
	public void setIgnoredCharacters(String ignoredCharacters) {
    	this.ignoredCharacters = ignoredCharacters;
    }

	/**
	 * Get the text converter used by this TextToContainerCopier.
	 * @return Converter
	 */
	public Converter getTextConverter() {
    	return textConverter;
    }

	/**
	 * Set the text converter used by this TextToContainerCopier.
	 * @param textConverter
	 */
	public void setTextConverter(Converter textConverter) {
    	this.textConverter = textConverter;
    }

	/**
	 * Get the container copier used by this TextToContainerCopier.
	 * @return Copier
	 */
	public Copier getContainerCopier() {
    	return containerCopier;
    }

	/**
	 * Set the container copier used by this TextToContainerCopier.
	 * @param containerCopier
	 */
	public void setContainerCopier(Copier containerCopier) {
    	this.containerCopier = containerCopier;
    }

}
