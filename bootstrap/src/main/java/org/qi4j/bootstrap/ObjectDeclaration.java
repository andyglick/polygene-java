/*
 * Copyright (c) 2007, Rickard Öberg. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.qi4j.bootstrap;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.qi4j.runtime.composite.ObjectModelFactory;
import org.qi4j.spi.structure.ObjectDescriptor;
import org.qi4j.spi.structure.Visibility;

/**
 * TODO
 */
public final class ObjectDeclaration
{
    private Iterable<Class> objectTypes;
    private Map<Class, Serializable> objectInfos = new HashMap<Class, Serializable>();
    private Visibility visibility = Visibility.module;

    public ObjectDeclaration( Iterable<Class> classes )
    {
        for( Class clazz : classes )
        {
            // best try to find out if the class is a concrete class
            if( clazz.isEnum() || Modifier.isAbstract( clazz.getModifiers() ) )
            {
                throw new IllegalArgumentException( "Declared objects must be concrete classes." );
            }
        }
        this.objectTypes = classes;
    }

    public <T extends Serializable> ObjectDeclaration setObjectInfo( Class<T> infoType, T info )
    {
        objectInfos.put( infoType, info );
        return this;
    }

    public ObjectDeclaration visibleIn( Visibility visibility )
        throws IllegalStateException
    {
        this.visibility = visibility;
        return this;
    }

    List<ObjectDescriptor> getObjectDescriptors( ObjectModelFactory objectModelFactory )
    {
        List<ObjectDescriptor> objectDescriptors = new ArrayList<ObjectDescriptor>();
        for( Class objectType : objectTypes )
        {
            ObjectDescriptor objectDescriptor = new ObjectDescriptor( objectModelFactory.newObjectModel( objectType ), objectInfos, visibility );
            objectDescriptors.add( objectDescriptor );
        }
        return objectDescriptors;
    }
}
