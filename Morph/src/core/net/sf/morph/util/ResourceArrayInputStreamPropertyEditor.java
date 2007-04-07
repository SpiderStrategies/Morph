package net.sf.morph.util;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceArrayPropertyEditor;
import org.springframework.util.StringUtils;

/**
 * Drop-in replacement for Spring's InputStreamPropertyEditor, to
 * concatenate potentially multiple Resources into a single InputStream.
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

	/* (non-Javadoc)
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
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
