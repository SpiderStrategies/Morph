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
package net.sf.morph2.transform.copiers.dsl;

/**
 * Represents a mapping direction.
 */
class Direction {
	private String name;

	private Direction(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public static final Direction BIDI = new Direction("bidi");
	public static final Direction LEFT = new Direction("left");
	public static final Direction RIGHT = new Direction("right");
}
