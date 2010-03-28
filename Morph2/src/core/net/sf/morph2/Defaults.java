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
package net.sf.morph2;

import net.sf.morph2.lang.languages.SimpleLanguage;
import net.sf.morph2.reflect.BeanReflector;
import net.sf.morph2.reflect.ContainerReflector;
import net.sf.morph2.reflect.DecoratedReflector;
import net.sf.morph2.reflect.GrowableContainerReflector;
import net.sf.morph2.reflect.IndexedContainerReflector;
import net.sf.morph2.reflect.InstantiatingReflector;
import net.sf.morph2.reflect.MutableIndexedContainerReflector;
import net.sf.morph2.reflect.SizableReflector;
import net.sf.morph2.reflect.reflectors.SimpleDelegatingReflector;
import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.DecoratedCopier;
import net.sf.morph2.transform.DecoratedTransformer;
import net.sf.morph2.transform.converters.DefaultToTextConverter;
import net.sf.morph2.transform.converters.NumberConverter;
import net.sf.morph2.transform.converters.ObjectToPrettyTextConverter;
import net.sf.morph2.transform.converters.TextConverter;
import net.sf.morph2.transform.converters.TimeConverter;
import net.sf.morph2.transform.copiers.ContainerCopier;
import net.sf.morph2.transform.transformers.SimpleDelegatingTransformer;

/**
 * Default instances of the main objects that are used repeatedly in the
 * framework.
 * 
 * @author Matt Sgarlata
 * @since Jan 9, 2005
 */
public abstract class Defaults {

	/**
	 * Create a basic DecoratedReflector.
	 * @return DecoratedReflector
	 */
	public static final DecoratedReflector createReflector() {
		return new SimpleDelegatingReflector();
	}

	/**
	 * Create a basic BeanReflector.
	 * @return BeanReflector
	 */
	public static final BeanReflector createBeanReflector() {
		return new SimpleDelegatingReflector();
	}

	/**
	 * Create a basic ContainerReflector.
	 * @return ContainerReflector
	 */
	public static final ContainerReflector createContainerReflector() {
		return new SimpleDelegatingReflector();
	}

	/**
	 * Create a basic GrowableContainerReflector.
	 * @return GrowableContainerReflector
	 */
	public static final GrowableContainerReflector createGrowableContainerReflector() {
		return new SimpleDelegatingReflector();
	}

	/**
	 * Create a basic IndexedContainerReflector.
	 * @return IndexedContainerReflector
	 */
	public static final IndexedContainerReflector createIndexedContainerReflector() {
		return new SimpleDelegatingReflector();
	}

	/**
	 * Create a basic InstantiatingReflector.
	 * @return InstantiatingReflector
	 */
	public static final InstantiatingReflector createInstantiatingReflector() {
		return new SimpleDelegatingReflector();
	}

	/**
	 * Create a basic MutableIndexedContainerReflector.
	 * @return MutableIndexedContainerReflector
	 */
	public static final MutableIndexedContainerReflector createMutableIndexedContainerReflector() {
		return new SimpleDelegatingReflector();
	}

	/**
	 * Create a basic SizableReflector.
	 * @return SizableReflector
	 */
	public static final SizableReflector createSizableReflector() {
		return new SimpleDelegatingReflector();
	}

	/**
	 * Create a basic DecoratedTransformer.
	 * @return DecoratedTransformer
	 */
	public static final DecoratedTransformer createTransformer() {
		return new SimpleDelegatingTransformer();
	}

	/**
	 * Create a basic DecoratedConverter.
	 * @return DecoratedConverter
	 */
	public static final DecoratedConverter createConverter() {
		return new SimpleDelegatingTransformer();
	}

	/**
	 * Create a basic DecoratedCopier.
	 * @return DecoratedCopier
	 */
	public static final DecoratedCopier createCopier() {
		return new SimpleDelegatingTransformer();
	}

	/**
	 * Create a basic TextConverter.
	 * @return TextConverter
	 */
	public static final TextConverter createTextConverter() {
		return new TextConverter();
	}

	/**
	 * Create a basic DefaultToTextConverter.
	 * @return DefaultToTextConverter
	 */
	public static final DefaultToTextConverter createToTextConverter() {
		return new DefaultToTextConverter();
	}

	/**
	 * Create a basic TimeConverter.
	 * @return TimeConverter
	 */
	public static final TimeConverter createTimeConverter() {
		return new TimeConverter();
	}

	/**
	 * Create a basic NumberConverter.
	 * @return NumberConverter
	 */
	public static final NumberConverter createNumberConverter() {
		return new NumberConverter();
	}

	/**
	 * Create a basic pretty text converter.
	 * @return DecoratedConverter
	 */
	public static final DecoratedConverter createPrettyTextConverter() {
		return new ObjectToPrettyTextConverter();
	}

	/**
	 * Create a basic ContainerCopier.
	 * @return ContainerCopier
	 */
	public static final ContainerCopier createContainerCopier() {
		return new ContainerCopier();
	}

	/**
	 * Create a basic Language.
	 * @return SimpleLanguage
	 */
	public static final SimpleLanguage createLanguage() {
		return new SimpleLanguage();
	}

}
