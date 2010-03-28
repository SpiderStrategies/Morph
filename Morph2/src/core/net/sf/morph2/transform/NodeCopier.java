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
package net.sf.morph2.transform;

/**
 * A copier used by a {@link net.sf.morph2.transform.GraphTransformer} to 
 * assist in the transformation of one object graph into another object graph.
 * A node copier is responsible for transforming a single node in the graph.
 * The GraphTransformer chooses the appropriate node transformer for each node
 * in the object graph. 
 * 
 * @author Matt Sgarlata
 * @since Feb 22, 2005
 */
public interface NodeCopier extends Copier, NestingAwareTransformer {

	/**
	 * Returns a version of the given source object that can be used multiple
	 * times by this copier. In most cases, a source object is reusable without
	 * changes and is simply returned. However, in some cases a source object
	 * may be a "throw-away" object that can only be used once. A good example
	 * of this is traversal objects like Iterators and Enumerations. For objects
	 * like these, the information must be copied to a reuseable object before
	 * being used
	 * 
	 * @param destinationClass
	 *            the destination class to which this source object will be
	 *            transformed
	 * @param source
	 *            a source object that will undergo a transformation by this
	 *            copier
	 * @return a reusable version of fthe source object
	 */
	public Object createReusableSource(Class destinationClass, Object source);

	/**
	 * Creates a new instance of the destination class that is capable of
	 * holding the information contained in the given source.
	 * 
	 * @param destinationClass
	 *            the type of the new instance to be returned
	 * @param source
	 *            the source to be transformed by this transformer
	 * @return the new instance
	 */
	public Object createNewInstance(Class destinationClass, Object source);

}
