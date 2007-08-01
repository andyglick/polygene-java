/*  Copyright 2007 Niclas Hedhman.
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
package org.qi4j.api;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import org.qi4j.api.annotation.Dependency;

public class QiHelper
{

    public static Object getDependencyKey( AnnotatedElement dependentElement )
    {
        Dependency depAnnot = dependentElement.getAnnotation( Dependency.class );
        String value = depAnnot.value();
        if( value == null )
        {
            if( dependentElement instanceof Field )
            {
                return ( (Field) dependentElement ).getType();
            }
            else
            {
                return dependentElement;
            }
        }
        return value;

    }
}
