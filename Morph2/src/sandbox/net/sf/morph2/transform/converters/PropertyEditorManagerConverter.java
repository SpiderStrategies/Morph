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

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.Locale;

import net.sf.morph2.transform.Converter;
import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.TransformationException;
import net.sf.morph2.transform.transformers.BaseTransformer;

/**
 * FIXME this implementation could be totally bogus... need to think about this
 * more
 * 
 * @author Matt Sgarlata
 */
public class PropertyEditorManagerConverter extends BaseTransformer implements Converter, DecoratedConverter {

	public static final Class[] ALL_OBJECTS = new Class[] { Object.class };
	
	public boolean isTransformableImpl(Class destinationType, Class sourceType) {
		PropertyEditor propertyEditor =
			PropertyEditorManager.findEditor(destinationType);
		return propertyEditor != null;
	}

	public Object convertImpl(Class destinationClass, Object source, Locale locale)
		throws TransformationException {

		PropertyEditor propertyEditor =
			PropertyEditorManager.findEditor(destinationClass);
		if (source == null) {
			propertyEditor.setAsText(null);
		}
		else {
			propertyEditor.setAsText(source.toString());
		}
		return propertyEditor.getValue();
	}

	protected boolean isWrappingRuntimeExceptions() {
	    return false;
    }

	public Class[] getSourceClassesImpl() {
		return ALL_OBJECTS;
	}

	public Class[] getDestinationClassesImpl() {
		return ALL_OBJECTS;
	}

}
