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
package org.apache.polygene.runtime.instantiation;

import org.apache.polygene.api.configuration.Configuration;
import org.apache.polygene.api.entity.Lifecycle;
import org.apache.polygene.api.injection.scope.This;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.property.Property;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.entitystore.memory.MemoryEntityStoreService;
import org.apache.polygene.test.AbstractPolygeneTest;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ConfigurationInstantiationTest extends AbstractPolygeneTest
{

    @Override
    public void assemble( ModuleAssembly module )
        throws AssemblyException
    {
        module.defaultServices();
        module.services( MemoryEntityStoreService.class );
        module.services( MyService.class ).instantiateOnStartup();
        module.configurations( MyConfig.class );
    }

    @Test
    public void givenSpecialInitializableWhenStartingExpectOsNameToBeSet()
    {
        MyService myService = serviceFinder.findService( MyService.class ).get();
        assertThat( myService.osName(), equalTo(System.getProperty( "os.name" )));
    }


    @Mixins( MyMixin.class)
    public interface MyService
    {
        String osName();
    }

    public class MyMixin
        implements MyService, Lifecycle
    {
        @This
        private Configuration<MyConfig> config;

        @Override
        public String osName()
        {
            return config.get().osName().get();
        }

        @Override
        public void create()
            throws Exception
        {
            config.get().osName().set( System.getProperty( "os.name" ) );
        }

        @Override
        public void remove()
            throws Exception
        {

        }
    }

    public interface MyConfig
    {
        Property<String> osName();
    }
}
