/*
 * Copyright 2004-2005 the original author or authors.
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
package net.sf.morph;

import net.sf.morph.lang.languages.SimpleLanguage;
import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.reflect.ContainerReflector;
import net.sf.morph.reflect.DecoratedReflector;
import net.sf.morph.reflect.GrowableContainerReflector;
import net.sf.morph.reflect.IndexedContainerReflector;
import net.sf.morph.reflect.InstantiatingReflector;
import net.sf.morph.reflect.MutableIndexedContainerReflector;
import net.sf.morph.reflect.SizableReflector;
import net.sf.morph.reflect.reflectors.SimpleDelegatingReflector;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.DecoratedCopier;
import net.sf.morph.transform.DecoratedTransformer;
import net.sf.morph.transform.converters.DefaultToTextConverter;
import net.sf.morph.transform.converters.NumberConverter;
import net.sf.morph.transform.converters.ObjectToPrettyTextConverter;
import net.sf.morph.transform.converters.TextConverter;
import net.sf.morph.transform.converters.TimeConverter;
import net.sf.morph.transform.copiers.ContainerCopier;
import net.sf.morph.transform.transformers.SimpleDelegatingTransformer;

/**
 * Default instances of the main objects that are used repeatedly in the
 * framework.
 * 
 * @author Matt Sgarlata
 * @since Jan 9, 2005
 */
public abstract class Defaults {

//	private static final BeanReflector BEAN_REFLECTOR = (BeanReflector) CompositeUtils.specialize(REFLECTOR, BeanReflector.class);
//	private static final ContainerReflector CONTAINER_REFLECTOR = (ContainerReflector) CompositeUtils.specialize(REFLECTOR, ContainerReflector.class);
//	private static final GrowableContainerReflector GROWABLE_CONTAINER_REFLECTOR = (GrowableContainerReflector) CompositeUtils.specialize(REFLECTOR, GrowableContainerReflector.class);
//	private static final IndexedContainerReflector INDEXED_CONTAINER_REFLECTOR = (IndexedContainerReflector) CompositeUtils.specialize(REFLECTOR, IndexedContainerReflector.class);
//	private static final InstantiatingReflector INSTANTIATING_REFLECTOR = (InstantiatingReflector) CompositeUtils.specialize(REFLECTOR, InstantiatingReflector.class);
//	private static final MutableIndexedContainerReflector MUTABLE_INDEXED_CONTAINER_REFLECTOR = (MutableIndexedContainerReflector) CompositeUtils.specialize(REFLECTOR, MutableIndexedContainerReflector.class);
//	private static final SizableReflector SIZABLE_REFLECTOR = (SizableReflector) CompositeUtils.specialize(REFLECTOR, SizableReflector.class);

//	private static final TextConverter TEXT_CONVERTER = new TextConverter();
//	private static final DefaultToTextConvert TO_TEXT_CONVERTER = new DefaultToTextConverter();
//	private static final TimeConverter TIME_CONVERTER = new TimeConverter();
//	private static final NumberConverter NUMBER_CONVERTER = new NumberConverter();
//	private static final ContainerCopier CONTAINER_COPIER = new ContainerCopier();
//	private static final DecoratedConverter PRETTY_TEXT_CONVERTER = new ObjectToPrettyTextConverter(); 
//	private static final DecoratedTransformer TRANSFORMER = new SimpleDelegatingTransformer();
//	private static final DecoratedConverter CONVERTER = (DecoratedConverter) CompositeUtils.specialize(TRANSFORMER, DecoratedConverter.class);	
//	private static final DecoratedCopier COPIER = (DecoratedCopier) CompositeUtils.specialize(TRANSFORMER, DecoratedCopier.class);

//	private static final SimpleLanguage LANGUAGE = new SimpleLanguage();	
	
	public static final DecoratedReflector createReflector() {
		return new SimpleDelegatingReflector();
	}
	public static final BeanReflector createBeanReflector() {
		return new SimpleDelegatingReflector();
	}
	public static final ContainerReflector createContainerReflector() {
		return new SimpleDelegatingReflector();
	}
	public static final GrowableContainerReflector createGrowableContainerReflector() {
		return new SimpleDelegatingReflector();
	}
	public static final IndexedContainerReflector createIndexedContainerReflector() {
		return new SimpleDelegatingReflector();
	}
	public static final InstantiatingReflector createInstantiatingReflector() {
		return new SimpleDelegatingReflector();
	}
	public static final MutableIndexedContainerReflector createMutableIndexedContainerReflector() {
		return new SimpleDelegatingReflector();
	}
	public static final SizableReflector createSizableReflector() {
		return new SimpleDelegatingReflector();
	}

	public static final DecoratedTransformer createTransformer() {
		return new SimpleDelegatingTransformer();
	}
	public static final DecoratedConverter createConverter() {
		return new SimpleDelegatingTransformer();
	}
	public static final DecoratedCopier createCopier() {
		return new SimpleDelegatingTransformer();
	}
	public static final TextConverter createTextConverter() {
		return new TextConverter();
	}
	public static final DefaultToTextConverter createToTextConverter() {
		return new DefaultToTextConverter();
	}
	public static final TimeConverter createTimeConverter() {
		return new TimeConverter();
	}
	public static final NumberConverter createNumberConverter() {
		return new NumberConverter();
	}
	public static final DecoratedConverter createPrettyTextConverter() {
		return new ObjectToPrettyTextConverter();
	}
	public static final ContainerCopier createContainerCopier() {
		return new ContainerCopier();
	}

	public static final SimpleLanguage createLanguage() {
		return new SimpleLanguage();
	}

}