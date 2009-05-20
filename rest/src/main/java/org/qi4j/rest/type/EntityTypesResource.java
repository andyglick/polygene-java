/*
 * Copyright (c) 2008, Niclas Hedhman. All Rights Reserved.
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
package org.qi4j.rest.type;

import org.qi4j.api.injection.scope.Service;
import org.qi4j.spi.entity.EntityType;
import org.qi4j.spi.entity.EntityTypeRegistry;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.ext.atom.Entry;
import org.restlet.ext.atom.Feed;
import org.restlet.ext.atom.Link;
import org.restlet.ext.atom.Text;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.List;

public final class EntityTypesResource extends ServerResource
{
    @Service
    EntityTypeRegistry registry;

    public EntityTypesResource()
    {
        getVariants().put(Method.ALL, MediaType.APPLICATION_ATOM);
        setNegotiated(true);
    }

    @Override
    public Representation get(Variant variant)
            throws ResourceException
    {
        if (MediaType.APPLICATION_ATOM.equals(variant.getMediaType()))
        {
            return representAtom();
        }

        throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
    }

    private Representation representAtom() throws ResourceException
    {
        Feed feed = new Feed();
        feed.setTitle(new Text(MediaType.TEXT_PLAIN, "Entity types"));
        List<Entry> entries = feed.getEntries();

        for (EntityType entityType : registry.getEntityTypes())
        {
            Entry entry = new Entry();
            entry.setTitle(new Text(MediaType.TEXT_PLAIN, entityType.type().name()));
            Link link = new Link();
            link.setHref(getRequest().getResourceRef().clone().addSegment(entityType.version()));
            entry.getLinks().add(link);
            entries.add(entry);
        }

        return feed;
    }
}
