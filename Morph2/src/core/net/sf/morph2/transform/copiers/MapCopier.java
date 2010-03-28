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
package net.sf.morph2.transform.copiers;

import java.util.Locale;
import java.util.Map;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.reflect.reflectors.MapReflector;
import net.sf.morph2.transform.TransformationType;
import net.sf.morph2.util.ClassUtils;
import net.sf.morph2.util.TransformerUtils;

/**
 * Copies one Map to another.  A PropertyNameMatchingCopier can do this as well,
 * but only for maps whose keys are Strings!
 *
 * @author mbenson
 * @since Morph 1.1
 */
public class MapCopier extends ContainerCopier {
	/**
	 * Basic Map.Entry implementation.
	 */
	public static class BasicEntry implements Map.Entry {
		private Object key;
		private Object value;

		/**
		 * {@inheritDoc}
		 */
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj instanceof Map.Entry == false) {
				return false;
			}
			Map.Entry e = (Map.Entry) obj;
			return ObjectUtils.equals(getKey(), e.getKey())
					&& ObjectUtils.equals(getValue(), e.getValue());
		}

		/**
		 * {@inheritDoc}
		 */
		public Object getKey() {
			return key;
		}

		/**
		 * {@inheritDoc}
		 */
		public Object getValue() {
			return value;
		}

		/**
		 * {@inheritDoc}
		 */
		public int hashCode() {
			return super.hashCode();
		}

		/**
		 * {@inheritDoc}
		 */
		public Object setValue(Object value) {
			try {
				return this.value;
			} finally {
				this.value = value;
			}
		}
	}

	private static final Class[] SOURCE_AND_DESTINATION_TYPES = { Map.class };

	/**
	 * {@inheritDoc}
	 */
	protected void initializeImpl() throws Exception {
		super.initializeImpl();
		setReflector(new MapReflector(MapReflector.EXTRACT_ENTRIES));
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_AND_DESTINATION_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return SOURCE_AND_DESTINATION_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object nestedTransform(Class destinationContainedType, Object destinationValue,
			Object sourceValue, Locale locale, TransformationType preferredTransformationType) {
		if (Map.Entry.class.isAssignableFrom(destinationContainedType)
				&& Map.Entry.class.isInstance(sourceValue)) {
			// as long as we return an instance of Map.Entry, the MapReflector
			// will take care of us;
			// we just handle any deep copying we can:
			Map.Entry e = (Map.Entry) sourceValue;
			Class keyClass = ClassUtils.getClass(e.getKey());
			Class valueClass = ClassUtils.getClass(e.getValue());
			boolean k = TransformerUtils
					.isTransformable(getNestedTransformer(), keyClass, keyClass);
			boolean v = TransformerUtils.isTransformable(getNestedTransformer(), valueClass,
					valueClass);
			if (k || v) {
				BasicEntry result = new BasicEntry();
				result.key = k ? TransformerUtils.transform(getNestedTransformer(), keyClass, null,
						e.getKey(), locale, TransformationType.CONVERT) : e.getKey();
				result.value = v ? TransformerUtils.transform(getNestedTransformer(), valueClass,
						null, e.getValue(), locale, TransformationType.CONVERT) : e.getValue();
				return result;
			}
			return sourceValue;
		}
		return super.nestedTransform(destinationContainedType, destinationValue, sourceValue,
				locale, preferredTransformationType);
	}
}
