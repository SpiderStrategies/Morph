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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 */
public class HasA {
	private A a;

	public HasA() {
	}

	public HasA(A a) {
		setA(a);
	}

	/**
	 * Get the a of this HasA.
	 * @return the a
	 */
	public synchronized A getA() {
		return a;
	}

	/**
	 * Set the a of this HasA.
	 * @param a the a to set
	 */
	public synchronized void setA(A a) {
		this.a = a;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
