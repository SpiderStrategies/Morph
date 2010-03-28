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
package net.sf.morph2.util;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceArrayPropertyEditor;
import org.springframework.util.StringUtils;

/**
 * Drop-in replacement for Spring's InputStreamEditor
 * to convert one or more Resources to an InputStream.
 * @author Matt Benson
 */
public class ResourceArrayInputStreamPropertyEditor extends PropertyEditorSupport {

	private ResourceArrayPropertyEditor resourceArrayPropertyEditor;

	/**
	 * Construct a new ResourceArrayInputStreamPropertyEditor.
	 */
	public ResourceArrayInputStreamPropertyEditor() {
		this(new ResourceArrayPropertyEditor());
	}

	/**
	 * Construct a new ResourceArrayInputStreamPropertyEditor.
	 * @param resourceArrayPropertyEditor
	 */
	public ResourceArrayInputStreamPropertyEditor(ResourceArrayPropertyEditor resourceArrayPropertyEditor) {
		this.resourceArrayPropertyEditor = resourceArrayPropertyEditor ;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAsText(String text) throws IllegalArgumentException {
		if (StringUtils.hasLength(text)) {
			String[] s = StringUtils.commaDelimitedListToStringArray(text);
			ArrayList l = new ArrayList(s.length);
			for (int i = 0; i < s.length; i++) {
				resourceArrayPropertyEditor.setAsText(s[i]);
				l.addAll(Arrays.asList((Resource[]) resourceArrayPropertyEditor.getValue()));
				setValue(new ResourceArrayInputStream((Resource[]) l.toArray(new Resource[l.size()])));
			}
		} else {
			setValue(null);
		}
	}

}
