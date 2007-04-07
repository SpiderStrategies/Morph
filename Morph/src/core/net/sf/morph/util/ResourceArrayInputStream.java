/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package net.sf.morph.util;

import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

import org.springframework.core.io.Resource;

/**
 * Special <code>InputStream</code> that will concatenate the contents of a Spring Resource[].
 * Adapted from Apache Ant ConcatResourceInputStream.
 */
public class ResourceArrayInputStream extends InputStream {

    private static final int EOF = -1;
    private boolean eof = false;
    private InputStream currentStream;
    private Resource[] resources;
    private int index = -1;

    /**
     * Construct a new ResourceArrayInputStream.
     * @param resources
     */
    public ResourceArrayInputStream(Resource[] resources) {
    	this.resources = resources;
    }

    /**
     * Close the stream.
     * @throws IOException if there is an error.
     */
     public void close() throws IOException {
        closeCurrent();
        eof = true;
    }

    /**
     * Read a byte.
     * @return the byte (0 - 255) or -1 if this is the end of the stream.
     * @throws IOException if there is an error.
     */
    public int read() throws IOException {
        if (eof) {
            return EOF;
        }
        int result = readCurrent();
        if (result == EOF) {
            nextResource();
            result = readCurrent();
        }
        return result;
    }

    private int readCurrent() throws IOException {
        return eof || currentStream == null ? EOF : currentStream.read();
    }

    private void nextResource() throws IOException {
        closeCurrent();
        if (resources == null || ++index == resources.length) {
        	eof = true;
        } else {
        	currentStream = new BufferedInputStream(resources[index].getInputStream());
        }
    }

    private void closeCurrent() {
		if (currentStream != null) {
			try {
				currentStream.close();
			} catch (IOException e) {
				//ignore
			}
	        currentStream = null;
		}
    }
}
