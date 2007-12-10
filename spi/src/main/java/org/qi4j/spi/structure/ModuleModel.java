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

package org.qi4j.spi.structure;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.qi4j.spi.composite.CompositeModel;
import org.qi4j.spi.composite.ObjectModel;
import org.qi4j.spi.service.ServiceProvider;

/**
 * TODO
 */
public class ModuleModel
    implements Serializable
{
    private Iterable<CompositeModel> publicComposites;
    private Iterable<CompositeModel> privateComposites;
    private Map<Class, ServiceProvider> serviceProviders;
    private List<ObjectModel> objectModels;

    private String name;

    public ModuleModel( Map<Class, ServiceProvider> serviceProviders, String name, Iterable<CompositeModel> publicComposites, Iterable<CompositeModel> privateComposites, List<ObjectModel> objectModels )
    {
        this.privateComposites = privateComposites;
        this.publicComposites = publicComposites;
        this.name = name;
        this.serviceProviders = serviceProviders;
        this.objectModels = objectModels;
    }

    public Iterable<CompositeModel> getPublicComposites()
    {
        return publicComposites;
    }

    public Iterable<CompositeModel> getPrivateComposites()
    {
        return privateComposites;
    }

    public Map<Class, ServiceProvider> getServiceProviders()
    {
        return serviceProviders;
    }

    public List<ObjectModel> getObjectModels()
    {
        return objectModels;
    }

    public String getName()
    {
        return name;
    }
}
