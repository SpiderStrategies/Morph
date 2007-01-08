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

/**
 * <p>
 * An extension of the Transformer interface that defines extra methods. All
 * methods specified in this interface can be easily implemented using just the
 * methods in the Transformer interface. Thus, if you are defining your own
 * transformer you should implement only the Transformer interface. If you
 * extend from {@link net.sf.morph.transform.transformers.BaseTransformer},
 * your transformer will implement this interface automatically.
 * </p>
 * 
 * <p>
 * If you don't want to extend from <code>BaseTransformer</code>, you can
 * still easily expose this interface by using the
 * {@link net.sf.morph.transform.transformer.TransformerDecorator}.
 * </p>
 * 
 * @author Matt Sgarlata
 * @since Dec 5, 2004
 */
public interface DecoratedTransformer extends Transformer, ExplicitTransformer {
		
}