/*
 * Copyright 2007 the original author or authors.
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

import net.sf.morph.transform.Copier;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.DecoratedCopier;
import net.sf.morph.transform.transformers.BaseCompositeTransformer;

/**
 * CompositeTransformer whose children must be Copiers and which invokes
 * each child Copier in turn in the course of performing a copy operation.
 *
 * @author Matt Benson
 * @since Morph 1.0.2
 */
public class CompositeCopier extends BaseCompositeTransformer implements
		DecoratedCopier, DecoratedConverter {

	protected void initializeImpl() throws Exception {
		super.initializeImpl();
		Object[] components = getComponents();
		if (!(components instanceof Copier[])) {
			Copier[] copiers = new Copier[components.length];
			System.arraycopy(components, 0, copiers, 0, components.length);
			setComponents(copiers);
		}
	}

	/**
	 * Returns the destination classes supported by <em>all</em> components.
	 * 
	 * @return Class[]
	 */
	protected synchronized Class[] getDestinationClassesImpl() throws Exception {
		Copier[] copiers = (Copier[]) getComponents();
		HashSet s = new HashSet(Arrays.asList(copiers[0].getDestinationClasses()));
		for (int i = 1; i < copiers.length; i++) {
			s.retainAll(Arrays.asList(copiers[i].getDestinationClasses()));
		}
		return (Class[]) s.toArray(new Class[s.size()]);
	}

	/**
	 * Returns the source classes supported by <em>all</em> components.
	 * 
	 * @return Class[]
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		Copier[] copiers = (Copier[]) getComponents();
		HashSet s = new HashSet(Arrays.asList(copiers[0].getSourceClasses()));
		for (int i = 1; i < copiers.length; i++) {
			s.retainAll(Arrays.asList(copiers[i].getSourceClasses()));
		}
		return (Class[]) s.toArray(new Class[s.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseCompositeTransformer#getComponentType()
	 */
	public Class getComponentType() {
		return Copier.class;
	}

	protected void copyImpl(Object destination, Object source, Locale locale,
			Integer preferredTransformationType) throws Exception {
		Copier[] copiers = (Copier[]) getComponents();
		for (int i = 0; i < copiers.length; i++) {
			copiers[i].copy(destination, source, locale);
		}
	}

}
