/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */
package org.apache.polygene.library.osgi;


import java.lang.reflect.Method;
import org.osgi.framework.ServiceReference;

/**
 * The fallback strategy is invoked when the OSGi service is not available and a method call is invoked.
 * <p>
 * The FallbackStrategy is declared on the {@link OSGiServiceImporter} service declaration, like;
 * <pre><code>
 *     FallbackStrategy strategy = new MyStrategy();
 *     module.services( OSGiServiceImporter.class )
 *         .identifiedBy( "osgi" )
 *         .setMetaInfo( bundleContext )
 *         .setMetaInfo( strategy );
 * </code></pre>
 */
public interface FallbackStrategy {
    Object invoke(final ServiceReference reference, Method method, Object... args);
}