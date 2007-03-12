/*  Copyright 2007 Niclas Hedhman.
 *  Copyright 2007 Edward Yakop
 *  Copyright 2007 Rickard Oberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.orthogon.internal;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import org.ops4j.lang.NullArgumentException;
import org.ops4j.orthogon.mixin.QiMixin;

public final class MixinFactory
{
    private final HashMap<Class, Class> m_mixinMapping;
    private final HashSet<Class> m_mixinImplementations;

    public MixinFactory()
    {
        m_mixinMapping = new HashMap<Class, Class>();
        m_mixinImplementations = new HashSet<Class>();
        registerMixin( IdentityMixin.class );
    }

    public final void registerMixin( Class... mixinImplementationClasses )
        throws IllegalArgumentException
    {
        NullArgumentException.validateNotNull( mixinImplementationClasses, "mixinImplementationClasses" );

        for( int i = 0; i < mixinImplementationClasses.length; i++ )
        {
            Class mixinImplementationClass = mixinImplementationClasses[ i ];

            if( mixinImplementationClass == null )
            {
                throw new IllegalArgumentException( "Mixin implementation index [" + i + "] is [null]." );
            }

            synchronized( this )
            {
                if( m_mixinImplementations.contains( mixinImplementationClasses ) )
                {
                    return;
                }

                Class[] classes = mixinImplementationClass.getInterfaces();
                for( Class cls : classes )
                {
                    Annotation[] annots = cls.getAnnotations();
                    for( Annotation annot : annots )
                    {
                        if( annot instanceof QiMixin )
                        {
                            m_mixinMapping.put( cls, mixinImplementationClass );
                        }
                    }
                }
                m_mixinImplementations.add( mixinImplementationClass );
            }
        }
    }

    public final boolean checkExistence( Class mixinInterface )
    {
        if( mixinInterface == null )
        {
            return false;
        }

        synchronized( this )
        {
            return m_mixinMapping.containsKey( mixinInterface );
        }
    }

    public final Object create( Class mixinInterface )
        throws IllegalArgumentException
    {
        NullArgumentException.validateNotNull( mixinInterface, "mixinInterface" );

        Class mixinImplementation;
        synchronized( this )
        {
            mixinImplementation = m_mixinMapping.get( mixinInterface );
        }

        if( mixinImplementation == null )
        {
            return null;
        }
        try
        {
            return mixinImplementation.newInstance();
        }
        catch( InstantiationException e )
        {
            // TODO: means the mixin implementation is an abstract or an interface. This should not happened.
            e.printStackTrace();
        }
        catch( IllegalAccessException e )
        {
            // TODO: means the default constructor is not public. This should not happened
            e.printStackTrace();
        }

        return null;
    }

    public void unregisterMixin( Class... mixinImplementationClasses )
        throws IllegalArgumentException
    {
        if( mixinImplementationClasses == null )
        {
            return;
        }

        for( Class mixinImplementationClass : mixinImplementationClasses )
        {
            if( mixinImplementationClass == null )
            {
                continue;
            }

            synchronized( this )
            {
                Class[] classes = mixinImplementationClass.getInterfaces();
                for( Class cls : classes )
                {
                    Annotation[] annots = cls.getAnnotations();
                    for( Annotation annot : annots )
                    {
                        if( annot instanceof QiMixin )
                        {
                            m_mixinMapping.remove( cls );
                        }
                    }
                }
                m_mixinImplementations.remove( mixinImplementationClasses );
            }
        }
    }
}
