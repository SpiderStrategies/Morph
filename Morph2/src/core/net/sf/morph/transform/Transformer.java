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
package net.sf.morph.transform;

import net.sf.composite.Component;

/**
 * <p>Transforms information taken from a source and makes it available at a
 * destination.  Each source class is implicitly considered transformable to each
 * destination class, unless this is an {@link net.sf.morph.transform.ExplicitTransformer}.</p>
 * 
 * <p>For performance reasons, it is recommended that transformers be
 * implemented in a threadsafe manner.  All transformers provided out-of-the-box
 * by the Morph framework are threadsafe.</p>
 * 
 * @author Matt Sgarlata
 * @since Nov 26, 2004
 */
public interface Transformer extends Component {
	
	public static final Integer TRANSFORMATION_TYPE_CONVERT = new Integer(1);
	public static final Integer TRANSFORMATION_TYPE_COPY = new Integer(2);
	
	/**
	 * Defines the types of objects that can be used as information sources by
	 * this transformer. This can be thought of as the valid "output" types for
	 * the transformer.
	 * 
	 * @return the types of objects that can be used as information sources by
	 *         this transformer
	 */
	public Class[] getSourceClasses();

	/**
	 * Defines the types of objects that can be used as information sources by
	 * this transformer. This method can be thought of as specifying the valid
	 * "output" types for this transformer.
	 * 
	 * @return the types of objects that can be used as information sources by
	 *         this transformer
	 */
	public Class[] getDestinationClasses();
	
}
