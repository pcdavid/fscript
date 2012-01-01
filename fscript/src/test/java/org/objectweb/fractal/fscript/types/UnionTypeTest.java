/*
 * Copyright (c) 2007-2008 ARMINES
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: fractal@objectweb.org
 */
package org.objectweb.fractal.fscript.types;

import static org.junit.Assert.*;

import org.junit.Test;

public class UnionTypeTest {
    @Test
    public void stringOrBoolean() {
        Type t = new UnionType("string-or-bool", PrimitiveType.STRING, PrimitiveType.BOOLEAN);
        assertTrue(t.checkValue("foo"));
        assertTrue(t.checkValue(true));
        assertTrue(t.checkValue(new Boolean(false)));
        //
        assertFalse(t.checkValue(new Object()));
        assertFalse(t.checkValue(42.0));
    }
}
