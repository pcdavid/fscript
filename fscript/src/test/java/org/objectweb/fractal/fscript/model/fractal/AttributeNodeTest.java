/*
 * Copyright (c) 2008 ARMINES
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
 * Contact: Pierre-Charles David <pcdavid@gmail.com>
 */
package org.objectweb.fractal.fscript.model.fractal;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.objectweb.fractal.fscript.AttributesHelper;
import org.objectweb.fractal.fscript.HelloWorldHelper;

public class AttributeNodeTest {
    private AttributeNode headerAttr;
    private AttributeNode countAttr;
    private static FractalModel model;
    private HelloWorldHelper helper;
    
    @BeforeClass
    public static void staticSetUp() {
        model = new FractalModel();
        model.startFc();
    }

    @Before
    public void setUp() throws Exception {
        helper = new HelloWorldHelper();
        AttributesHelper attrHelper = new AttributesHelper(helper.server);
        headerAttr = new AttributeNode(model, attrHelper, "header");
        countAttr = new AttributeNode(model, attrHelper, "count");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void createInvalidAttribute() {
        new AttributeNode(model, new AttributesHelper(helper.server), "invalid");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void readInvalidProperty() {
        headerAttr.getProperty("invalid");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void writeInvalidProperty() {
        headerAttr.setProperty("invalid", "none");
    }
    
    @Test
    public void readAttributeName() {
        assertEquals("header", headerAttr.getName());
        assertEquals("count", countAttr.getName());
    }
    
    @Test
    public void genericReadAttributeName() {
        assertEquals("header", headerAttr.getProperty("name"));
        assertEquals("count", countAttr.getProperty("name"));
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void writeReadOnlyProperty() {
        headerAttr.setProperty("name", "prefix");
    }
    
    @Test
    public void readAttributeValue() {
        assertEquals("-> ", headerAttr.getValue());
        assertEquals(1, countAttr.getValue());
    }
    
    @Test
    public void genericReadAttributeValue() {
        assertEquals("-> ", headerAttr.getProperty("value"));
        assertEquals(1, countAttr.getProperty("value"));
    }
    
    @Test
    public void writeAttributeValue() {
        headerAttr.setValue("=> ");
        assertEquals("=> ", headerAttr.getValue());
        assertEquals("=> ", new AttributesHelper(helper.server).getAttribute("header"));
        //
        countAttr.setValue(42);
        assertEquals(42, countAttr.getValue());
        assertEquals(42, new AttributesHelper(helper.server).getAttribute("count"));
    }
    
    @Test
    public void genericWriteAttributeValue() {
        headerAttr.setProperty("value", "=> ");
        assertEquals("=> ", headerAttr.getProperty("value"));
        assertEquals("=> ", new AttributesHelper(helper.server).getAttribute("header"));
        //
        countAttr.setProperty("value", 42);
        assertEquals(42, countAttr.getProperty("value"));
        assertEquals(42, new AttributesHelper(helper.server).getAttribute("count"));
    }

    @Test
    public void readAttributeIsWritable() {
        assertEquals(true, headerAttr.isWritable());
        assertEquals(true, countAttr.isWritable());
    }
    
    @Test
    public void genericReadAttributeIsWritable() {
        assertEquals(true, headerAttr.getProperty("writable"));
        assertEquals(true, countAttr.getProperty("writable"));
    }
}
