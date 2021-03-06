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

package org.apache.polygene.spi.service.importer;

import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.test.AbstractPolygeneTest;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.service.ImportedServiceDescriptor;
import org.apache.polygene.api.service.ServiceComposite;
import org.apache.polygene.api.service.ServiceImporter;
import org.apache.polygene.api.service.ServiceImporterException;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.ImportedServiceDeclaration;
import org.apache.polygene.bootstrap.ModuleAssembly;

/**
 * JAVADOC
 */
public class ServiceInstanceImporterTest
    extends AbstractPolygeneTest
{
    public void assemble( ModuleAssembly module )
        throws AssemblyException
    {
        module.importedServices( TestService.class ).
            identifiedBy( "test" ).
            setMetaInfo( new StringIdentity( "testimporter" ) ).
            importedBy( ImportedServiceDeclaration.SERVICE_IMPORTER );
        module.services( TestImporterService.class ).identifiedBy( "testimporter" );

        module.objects( ServiceInstanceImporterTest.class );
    }

    @Service
    TestService service;

    @Test
    public void testImportServiceFromService()
    {
        Assert.assertThat( service.helloWorld(), CoreMatchers.equalTo( "Hello World" ) );
    }

    public static class TestService
    {
        String helloWorld()
        {
            return "Hello World";
        }
    }

    @Mixins( TestImporterService.Mixin.class )
    interface TestImporterService
        extends ServiceComposite, ServiceImporter<TestService>
    {
        class Mixin
            implements ServiceImporter<TestService>
        {
            public TestService importService( ImportedServiceDescriptor serviceDescriptor )
                throws ServiceImporterException
            {
                return new TestService();
            }

            public boolean isAvailable( TestService instance )
            {
                return true;
            }
        }
    }
}
