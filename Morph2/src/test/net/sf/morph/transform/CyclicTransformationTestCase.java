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

import junit.framework.TestCase;
import net.sf.morph.Defaults;
import net.sf.morph.Morph;

/**
 * Verifies that conversions of cyclic graphs are performed correctly.
 * 
 * @author Matt Sgarlata
 * @since Feb 25, 2005
 */
public class CyclicTransformationTestCase extends TestCase {

	private Node1 cyclicGraph;
	
	protected void setUp() throws Exception {
		cyclicGraph = new Node1();
		cyclicGraph.setAdjacentNode(cyclicGraph);
	}
	
	public static class Node1 {
		private Node1 adjacentNode;
		
		public Node1 getAdjacentNode() {
			return adjacentNode;
		}
		public void setAdjacentNode(Node1 adjacentNode) {
			this.adjacentNode = adjacentNode;
		}
	}
	
	public static class Node2 {
		private Node2 adjacentNode;
		
		public Node2 getAdjacentNode() {
			return adjacentNode;
		}
		public void setAdjacentNode(Node2 adjacentNode) {
			this.adjacentNode = adjacentNode;
		}
	}
	
	public void testConvert() {
		Node2 converted = (Node2) Morph.convert(Node2.class, cyclicGraph, null);
		
		// first of all, if we've reached here that means we didn't hit an
		// infinite loop, so we correctly stopped processing rather than
		// traversing the adjacentNode relationship forever
		
		// next, test that the adjacent node was populated
		assertNotNull(converted.getAdjacentNode());
	}
	
	public void testCopy() {
		
		Node2 newNode = new Node2();		
		Copier copier = Defaults.createCopier();
		copier.copy(newNode, cyclicGraph, null);		

		// first of all, if we've reached here that means we didn't hit an
		// infinite loop, so we correctly stopped processing rather than
		// traversing the adjacentNode relationship forever
		
		// next, test that the adjacent node was populated
		assertNotNull(newNode.getAdjacentNode());
	}
	
}
