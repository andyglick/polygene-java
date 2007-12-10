/*
 * Copyright (c) 2007, Rickard �berg. All Rights Reserved.
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

package org.qi4j.spi.composite;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * TODO
 */
public class PropertyModel
    implements Serializable
{
    private String name;
    private Type type;
    private Method writeMethod;
    private Method readMethod;

    public PropertyModel( String name, Type type, Method writeMethod, Method readMethod )
    {
        this.name = name;
        this.type = type;
        this.writeMethod = writeMethod;
        this.readMethod = readMethod;
    }

    public String getName()
    {
        return name;
    }

    public Type getType()
    {
        return type;
    }

    public Method getWriteMethod()
    {
        return writeMethod;
    }

    public Method getReadMethod()
    {
        return readMethod;
    }

    public boolean isWriteable()
    {
        return writeMethod != null;
    }

    public boolean isReadable()
    {
        return readMethod != null;
    }
}
