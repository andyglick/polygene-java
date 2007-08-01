/*
 * Copyright (c) 2007, Rickard Öberg. All Rights Reserved.
 * Copyright (c) 2007, Niclas Hedhman. All Rights Reserved.
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
package org.qi4j.api.persistence;

import java.util.List;
import org.qi4j.api.persistence.composite.EntityComposite;

/**
 * Query of objects from underlying stores.
 *
 * Example;
 * <code><pre>
 * Query q = em.createQuery(PersonComposite.class);
 * q.where(Name.class).setName("foo");
 * q.where(Age.class, Is.LESS_THAN).setAge(30);
 * q.orderBy(Name.class).getName();
 * List<PersonComposite> result = q.find();
 * </pre></code>
 */
public interface Query<T extends EntityComposite>
{
    List<T> find();

    T findSingle();

    void setMaxResults( int maxResult );

    void setFirstResult( int startPosition );

    void setParameter( String name, Object value );

}
