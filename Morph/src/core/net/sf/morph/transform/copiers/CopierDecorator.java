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
package net.sf.morph.transform.copiers;

import java.util.Locale;

import net.sf.morph.transform.Copier;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.DecoratedCopier;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.transformers.BaseTransformer;

/**
 * Decorates any Copier so that it implements DecoratedCopier.
 * 
 * @author Matt Sgarlata
 * @since Dec 5, 2004
 */
public class CopierDecorator extends BaseTransformer implements DecoratedCopier, DecoratedConverter {
	
	private Copier nestedCopier;
	
	public CopierDecorator() {
		super();
	}
	
	public CopierDecorator(Copier copier) {
		setNestedCopier(copier);
	}

	protected void copyImpl(Object destination, Object source, Locale locale, Integer preferredTransformationType)
		throws TransformationException {
		getNestedCopier().copy(destination, source, locale);
	}
	
	/**
	 * @return Returns the copier.
	 */
	public Copier getNestedCopier() {
		return nestedCopier;
	}
	/**
	 * @param copier The copier to set.
	 */
	public void setNestedCopier(Copier copier) {
		this.nestedCopier = copier;
	}

	public Class[] getSourceClassesImpl() {
		return nestedCopier.getSourceClasses();
	}

	public Class[] getDestinationClassesImpl() {
		return nestedCopier.getDestinationClasses();
	}

	protected boolean isWrappingRuntimeExceptions() {
	    // the whole point of this copier is for decorating user defined
		// transformers, so we don't want to eat their exceptions ;)
	    return false;
    }
}
