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
package net.sf.morph2.transform;

import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;
import net.sf.morph2.Morph;

/**
 * Verifies that when the same object is transformed to different types that
 * the transformation is correct each time.  Formerly, there was a bug that
 * Eduard Letifov found where the first time the transformation was performed
 * the transformation went through correctly, but the second time the results
 * of the first transformation were returned instead of the results of the
 * second transformation.
 * 
 * @author Eduard Letifov
 * @author Matt Sgarlata
 * @since Nov 7, 2005
 */
public class SourceToDifferentDestinationsTestCase extends TestCase {

    public static class A {
    }

    public static class B {
    }


    public static class Destination {
        A[] field1;
        B[] field2;

        public A[] getField1() {
            return field1;
        }

        public void setField1(A[] field1) {
            this.field1 = field1;
        }

        public B[] getField2() {
            return field2;
        }

        public void setField2(B[] field2) {
            this.field2 = field2;
        }
    }

    public static class Source {
        Set field1 = new TreeSet();
        Set field2 = new TreeSet();

        public Set getField1() {
            return field1;
        }

        public void setField1(Set field1) {
            this.field1 = field1;
        }

        public Set getField2() {
            return field2;
        }

        public void setField2(Set field2) {
            this.field2 = field2;
        }
    }

    public void testBug() throws Exception {

        Morph.convert(Destination.class, new Source());

    }
	
}
