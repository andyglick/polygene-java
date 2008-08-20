/*  Copyright 2008 Edward Yakop.
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
package org.qi4j.runtime.query.grammar.impl;

import java.lang.reflect.Method;
import org.qi4j.query.grammar.AssociationReference;
import org.qi4j.query.grammar.ManyAssociationReference;

/**
 * @author edward.yakop@gmail.com
 */
public class ManyAssociationReferenceImpl extends AssociationReferenceImpl
    implements ManyAssociationReference
{
    public ManyAssociationReferenceImpl( final Method accessor )
    {
        super( accessor );
    }

    public ManyAssociationReferenceImpl( final Method accessor,
                                         final AssociationReference traversed )
    {
        super( accessor, traversed );
    }
}