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
package net.sf.morph.transform.transformers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.morph.reflect.Reflector;
import net.sf.morph.reflect.reflectors.SimpleDelegatingReflector;
import net.sf.morph.reflect.reflectors.SimpleInstantiatingReflector;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.converters.IdentityConverter;
import net.sf.morph.transform.copiers.ContainerCopier;
import net.sf.morph.transform.copiers.PropertyNameMatchingCopier;
import net.sf.morph.util.ClassUtils;
import net.sf.morph.util.TransformerUtils;
import net.sf.morph.util.TypeMap;

/**
 * <p>
 * A transformer that performs a deep copy of the data in an object graph while
 * also allowing the types in the destination graph to be different than the
 * types in the source graph. A typical application of this transformer is to
 * transform a domain model into a graph of transfer objects suitable for
 * transferring to a remote client or another tier of an n-tier architecture.
 * </p>
 * 
 * <p>
 * Note: if you are using this transformer to transformer an object graph
 * composed of CGLIB proxies (such as an object graph created by Hibernate), you
 * may need to specify your source types as interfaces, because CGLIB proxies
 * may not necessarily be instances of the expected class.
 * </p>
 * 
 * @author Matt Sgarlata
 * @since December 1, 2005
 */
public class TypeChangingGraphTransformer extends SimpleDelegatingTransformer {
	
	private Map sourceToDestinationTypeMapping;

	/**
	 * Construct a new TypeChangingGraphTransformer.
	 */
	public TypeChangingGraphTransformer() {
		setAppendDefaultComponents(true);
	}

	protected Transformer getTransformer(Class transformerType) {
		Object[] components = getComponents();
		for (int i=0; i<components.length; i++) {
			if (transformerType.isAssignableFrom(components[i].getClass())) {
				return (Transformer) components[i];
			}
		}
		
		throw new TransformationException(
				"Could not find a component of type '"
						+ transformerType.getName() + "' in transformer "
						+ this);
	}
	
	protected ContainerCopier getContainerCopier() {
		return (ContainerCopier) getTransformer(ContainerCopier.class);
	}
	
	protected PropertyNameMatchingCopier getPropertyNameMatchingCopier() {
		return (PropertyNameMatchingCopier) getTransformer(PropertyNameMatchingCopier.class);
	}
	
	protected IdentityConverter getIdentityConverter() {
		return (IdentityConverter) getTransformer(IdentityConverter.class);
	}
	
	protected Reflector createReflector() {
		// create a reflector that will instantiate classes of the destination
		// types designated by sourceToDestinationTypeMapping when it encounters
		// objects that are instances of the source types designated in
		// sourceToDestinationTypeMapping 
		SimpleInstantiatingReflector instantiatingReflector = new SimpleInstantiatingReflector();
		instantiatingReflector.setRequestedToInstantiatedTypeMap(sourceToDestinationTypeMapping);
		// create a delegating reflector that will first do the instantiations
		// as designated above before using other reflectors to perform standard
		// reflection operations like reading property names
		SimpleDelegatingReflector reflector = new SimpleDelegatingReflector();
		List components = new ArrayList(Arrays.asList(reflector.getComponents()));
		components.add(0, instantiatingReflector);		
		reflector.setComponents(components.toArray(new Reflector[components.size()]));
		return reflector;
	}
	
	protected void initializeImpl() throws Exception {
		super.initializeImpl();
		
		Reflector reflector = createReflector();
		
		ContainerCopier cc = getContainerCopier();
		cc.setContainedSourceToDestinationTypeMap(getSourceToDestinationTypeMapping());
		cc.setReflector(reflector);
		
		PropertyNameMatchingCopier pnmc = getPropertyNameMatchingCopier();
		pnmc.setReflector(reflector);
		
		IdentityConverter identityConverter = getIdentityConverter();
		identityConverter.setSourceClasses(ClassUtils.getImmutableTypes());
		identityConverter.setDestinationClasses(ClassUtils.getImmutableTypes());

		this.setReflector(reflector);
	}
	
	protected Object convertImpl(Class destinationType, Object source, Locale locale) throws Exception {
		Class transformedDestinationType =
			TransformerUtils.getMappedDestinationType(
				getSourceToDestinationTypeMapping(), destinationType);
		if (transformedDestinationType == null) {
			transformedDestinationType = destinationType;
		}
		return super.convertImpl(transformedDestinationType, source, locale);
	}	

	public Map getSourceToDestinationTypeMapping() {
		return sourceToDestinationTypeMapping;
	}

	public void setSourceToDestinationTypeMapping(Map sourceToDestinationTypeMapping) {
		this.sourceToDestinationTypeMapping = new TypeMap(sourceToDestinationTypeMapping);
	}

}
