/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.csv;

import org.junit.Test;

/**
 * @version $Id: AssertionsTest.java 1518808 2013-08-29 20:25:19Z britter $
 */
public class AssertionsTest {

    @Test
    public void testNotNull() throws Exception {
        Assertions.notNull(new Object(), "object");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotNullNull() throws Exception {
        Assertions.notNull(null, "object");
    }
}
