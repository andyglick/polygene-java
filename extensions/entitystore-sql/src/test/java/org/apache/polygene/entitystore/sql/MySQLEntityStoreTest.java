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
package org.apache.polygene.entitystore.sql;

import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;
import org.junit.BeforeClass;
import org.apache.polygene.api.common.Visibility;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.usecase.UsecaseBuilder;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.entitystore.sql.assembly.MySQLEntityStoreAssembler;
import org.apache.polygene.entitystore.sql.internal.SQLs;
import org.apache.polygene.library.sql.assembly.DataSourceAssembler;
import org.apache.polygene.library.sql.common.SQLConfiguration;
import org.apache.polygene.library.sql.dbcp.DBCPDataSourceServiceAssembler;
import org.apache.polygene.test.EntityTestAssembler;
import org.apache.polygene.test.entity.AbstractEntityStoreTest;
import org.apache.polygene.valueserialization.orgjson.OrgJsonValueSerializationAssembler;

import static org.apache.polygene.test.util.Assume.assumeConnectivity;

public class MySQLEntityStoreTest
    extends AbstractEntityStoreTest
{
    @BeforeClass
    public static void beforeMySQLEntityStoreTests()
    {
        assumeConnectivity( "localhost", 3306 );
    }

    @Override
    // START SNIPPET: assembly
    public void assemble( ModuleAssembly module )
        throws AssemblyException
    {
        // END SNIPPET: assembly
        super.assemble( module );
        ModuleAssembly config = module.layer().module( "config" );
        new EntityTestAssembler().assemble( config );
        new OrgJsonValueSerializationAssembler().assemble( module );

        // START SNIPPET: assembly
        // DataSourceService
        new DBCPDataSourceServiceAssembler().
            identifiedBy( "mysql-datasource-service" ).
            visibleIn( Visibility.module ).
            withConfig( config, Visibility.layer ).
            assemble( module );

        // DataSource
        new DataSourceAssembler().
            withDataSourceServiceIdentity( "mysql-datasource-service" ).
            identifiedBy( "mysql-datasource" ).
            visibleIn( Visibility.module ).
            withCircuitBreaker().
            assemble( module );

        // SQL EntityStore
        new MySQLEntityStoreAssembler().
            visibleIn( Visibility.application ).
            withConfig( config, Visibility.layer ).
            assemble( module );
    }
    // END SNIPPET: assembly

    @Override
    public void tearDown()
        throws Exception
    {
        if( true )
        {
            return;
        }
        UnitOfWork uow = this.unitOfWorkFactory.newUnitOfWork(
            UsecaseBuilder.newUsecase( "Delete " + getClass().getSimpleName() + " test data" )
        );
        try
        {
            SQLConfiguration config = uow.get( SQLConfiguration.class, MySQLEntityStoreAssembler.DEFAULT_ENTITYSTORE_IDENTITY );
            Connection connection = serviceFinder.findService( DataSource.class ).get().getConnection();
            connection.setAutoCommit( false );
            String schemaName = config.schemaName().get();
            if( schemaName == null )
            {
                schemaName = SQLs.DEFAULT_SCHEMA_NAME;
            }
            try( Statement stmt = connection.createStatement() )
            {
                stmt.execute( String.format( "DELETE FROM %s." + SQLs.TABLE_NAME, schemaName ) );
                connection.commit();
            }
        }
        finally
        {
            uow.discard();
            super.tearDown();
        }
    }

}