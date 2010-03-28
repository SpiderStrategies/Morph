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
package net.sf.morph2.transform.converters;

import java.util.Locale;

import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.ExplicitTransformer;
import net.sf.morph2.transform.TransformationException;
import net.sf.morph2.transform.transformers.BaseTransformer;
import net.sf.morph2.util.ClassUtils;

/**
 * Converter that attempts to call a public <code>clone()</code> method
 * on <code>Cloneable</code> source objects. Presumes that the called method
 * delegates to <code>Object.clone()</code>, returning a result such that
 * <code>source instanceof destinationType</code>.
 * @since Morph 1.1.2
 */
public class CloningConverter extends BaseTransformer implements DecoratedConverter,
		ExplicitTransformer {

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return new Class[] { Object.class };
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return new Class[] { Cloneable.class };
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isTransformableImpl(Class destinationType, Class sourceType) throws Exception {
		return super.isTransformableImpl(destinationType, sourceType)
				&& destinationType.isAssignableFrom(sourceType);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object convertImpl(Class destinationType, Object source, Locale locale)
			throws Exception {
		Class sourceType = ClassUtils.getClass(source);
		if (isTransformable(destinationType, sourceType)) {
			if (source instanceof Cloneable) {
				try {
					Object result = sourceType.getMethod("clone", null).invoke(source, null);
					if (!destinationType.isInstance(result)) {
						throw new TransformationException(destinationType, source,
								//manufacture a bogus CCE to make our point ;)
								new ClassCastException(destinationType.getName()));
					}
					return result;
				} catch (Exception e) {
					throw new TransformationException(destinationType, source, e);
				}
			}
		}
		throw new TransformationException(destinationType, source);
	}
}
