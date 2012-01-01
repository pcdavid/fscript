/*
 * Copyright (c) 2004-2005 Universite de Nantes (LINA)
 * Copyright (c) 2005-2006 France Telecom
 * Copyright (c) 2006-2007 ARMINES
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
package org.objectweb.fractal.fscript;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.OperationNotSupportedException;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.AttributeController;
import org.objectweb.fractal.util.Fractal;

/**
 * This class provides methods to discover and manipulate Fractal components attributes in
 * a generic way. It uses reflection to discover attributes, get and set attributes values
 * by name.
 * 
 * @author Pierre-Charles David <pcdavid@gmail.com>
 */
public class AttributesHelper {
    private final Component component;

    private final AttributeController target;

    private final Map<String, Attribute> attributes;

    /**
     * Creates a new <code>AttributesHelper</code> to access the specified component's
     * attributes.
     * 
     * @param c
     *            the component owner of the attributes to access.
     */
    public AttributesHelper(final Component c) {
        component = c;
        target = getAttributeController(c);
        attributes = new HashMap<String, Attribute>();
        if (target != null) {
            discoverAttributes();
        }
    }

    /**
     * Returns the component owning the attributes this helper gives access to.
     * 
     * @return the component owning the attributes.
     */
    public Component getComponent() {
        return component;
    }

    private AttributeController getAttributeController(Component c) {
        try {
            return Fractal.getAttributeController(c);
        } catch (NoSuchInterfaceException e) {
            return null;
        }
    }

    /**
     * Matches a valid attribute name.
     */
    private static final String NAME_REGEX = "\\p{Alpha}\\p{Alnum}*";

    /**
     * Matches an attribute reader method name.
     */
    private static final Pattern GETTER_REGEX = Pattern.compile("^(get|is)(" + NAME_REGEX
            + ")$");

    /**
     * Matches an attribute writer method name.
     */
    private static final Pattern SETTER_REGEX = Pattern.compile("^set(" + NAME_REGEX
            + ")$");

    /**
     * Introspect the target to discover all its configuration attributes and create the
     * appropriate proxy <code>Attribute</code> objects.
     */
    private void discoverAttributes() {
        Map<String, Method> readers = new HashMap<String, Method>();
        Map<String, Method> writers = new HashMap<String, Method>();
        for (Method meth : findAllAttributeMethods()) {
            // TODO How to handle multiple readers or writers for a given name?
            // Multiple readers are possible if both 'get*' and 'is*' style readers are
            // provided. Multiple writers are possible with overloading.
            if (isReader(meth)) {
                readers.put(getAttributeNameReadBy(meth), meth);
            } else if (isWriter(meth)) {
                writers.put(getAttributeNameWrittenBy(meth), meth);
            }
        }
        Set<String> names = new HashSet<String>();
        names.addAll(readers.keySet());
        names.addAll(writers.keySet());
        for (String name : names) {
            Attribute attr = new Attribute(name, readers.get(name), writers.get(name));
            attributes.put(name, attr);
        }
    }

    /**
     * Checks whether a method is a valid attribute reader (based on its signature). A
     * reader must:
     * <ul>
     * <li>take zero arguments</li>
     * <li>return something (i.e. return type is not <code>void</code>)</li>
     * <li>have a name of the form <code>is*</code> or <code>get*</code></li>
     * <li>if the name if of the form <code>is*</code>, its return type must be a
     * boolean (primitive or wrapper type).</li>
     * </ul>
     * 
     * @param meth
     *            the method to check.
     * @return <code>true</code> iff <code>meth</code> is a valid reader method.
     */
    private boolean isReader(Method meth) {
        if (meth.getParameterTypes().length != 0) {
            return false;
        }
        Matcher matcher = GETTER_REGEX.matcher(meth.getName());
        matcher.find();
        if (!matcher.matches()) {
            return false;
        }
        Class<?> returnType = meth.getReturnType();
        if (matcher.group(1).equals("is")) {
            return returnType.equals(Boolean.TYPE) || returnType.equals(Boolean.class);
        } else {
            return !returnType.equals(Void.TYPE);
        }
    }

    /**
     * Checks whether a method is a valid attribute reader (based on its signature). A
     * writer must:
     * <ul>
     * <li>take exactly one argument</li>
     * <li>have a name of the form <code>set*</code></li>
     * <li>not return anything (i.e. have a <code>void</code> return type)</li>
     * </ul>
     * 
     * @param meth
     *            the method to check.
     * @return <code>true</code> iff <code>meth</code> is a valid writer method.
     */
    private boolean isWriter(Method meth) {
        return SETTER_REGEX.matcher(meth.getName()).matches()
                && (meth.getParameterTypes().length == 1)
                && meth.getReturnType().equals(Void.TYPE);
    }

    private String getAttributeNameReadBy(Method meth) {
        Matcher matcher = GETTER_REGEX.matcher(meth.getName());
        matcher.find();
        return toNameCase(matcher.group(2));
    }

    private String getAttributeNameWrittenBy(Method meth) {
        Matcher matcher = SETTER_REGEX.matcher(meth.getName());
        matcher.find();
        return toNameCase(matcher.group(1));
    }

    private String toNameCase(String str) {
        if (str.length() == 0) {
            return str;
        } else {
            char chars[] = str.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            return new String(chars);
        }
    }

    // TODO What are the exact rules?
    // Currently, returns all the methods of all the sub-interfaces of AttributeController
    // which are implemented by the target.
    private Collection<Method> findAllAttributeMethods() {
        Set<Method> methods = new HashSet<Method>();
        for (Class<?> itf : getAttributeInterfaces()) {
            methods.addAll(Arrays.asList(itf.getMethods()));
        }
        return methods;
    }

    /**
     * Returns the types of all the interfaces implemented by the target component's
     * <code>attribute-controller</code> interface which extend
     * {@link AttributeController}.
     * 
     * @return the types of all the attribute controller interfaces of the target
     *         component.
     */
    public Collection<Class<?>> getAttributeInterfaces() {
        List<Class<?>> interfaces = new ArrayList<Class<?>>();
        for (Class<?> itf : target.getClass().getInterfaces()) {
            if (AttributeController.class.isAssignableFrom(itf)) {
                interfaces.add(itf);
            }
        }
        return interfaces;
    }

    /**
     * Finds all the attributes of the target component.
     * 
     * @return the names of all the attributes (readable, writable or both) declared by
     *         the target component in its <code>attribute-controller</code> interface.
     */
    public Set<String> getAttributesNames() {
        return Collections.unmodifiableSet(attributes.keySet());
    }

    /**
     * Returns the names and values of all the <em>readable</em> attributes of the
     * target component.
     * 
     * @return a map from readable attribute name to its current value.
     */
    public Map<String, Object> getAttributes() {
        Map<String, Object> result = new HashMap<String, Object>();
        for (Attribute attr : attributes.values()) {
            if (attr.isReadable()) {
                result.put(attr.getName(), attr.get());
            }
        }
        return result;
    }

    /**
     * Reads the current value of an attribute.
     * 
     * @param name
     *            the name of the attribute to read.
     * @return the current value of the attribute.
     * @throws NoSuchElementException
     *             if the target component does not have an attribute named
     *             <code>name</code>.
     * @throws OperationNotSupportedException
     *             if the target component has an attribute named <code>name</code> but
     *             it is not readable.
     */
    public Object getAttribute(String name) throws NoSuchElementException,
            OperationNotSupportedException {
        Attribute attr = attributes.get(name);
        if (attr == null) {
            throw new NoSuchElementException("No attribute named " + name + ".");
        } else if (attr.isReadable()) {
            return attr.get();
        } else {
            throw new OperationNotSupportedException("Attribute " + name
                    + " is not readable.");
        }
    }

    /**
     * Sets the value of an attribute.
     * 
     * @param name
     *            the name of attribute to change.
     * @param value
     *            the new value.
     * @throws NoSuchElementException
     *             if the target component does not have an attribute named
     *             <code>name</code>.
     * @throws OperationNotSupportedException
     *             if the target component has an attribute named <code>name</code> but
     *             it is not writable.
     * @throws IllegalArgumentException
     *             if <code>value</code> is not a possible value for the attribute (e.g.
     *             it is not type-compatible).
     */
    public void setAttribute(String name, Object value) throws NoSuchElementException,
            OperationNotSupportedException, IllegalArgumentException {
        Attribute attr = attributes.get(name);
        if (attr == null) {
            throw new NoSuchElementException("No attribute named " + name + ".");
        } else if (attr.isWritable()) {
            attr.set(value);
        } else {
            throw new OperationNotSupportedException("Attribute " + name
                    + " is not readable.");
        }
    }

    /**
     * Tests whether the target component has an attribute of a given name.
     * 
     * @param name
     *            the name of the attribute to search for.
     * @return <code>true</code> iff the target component has an attribute (readable,
     *         writable or both) named <code>name</code>.
     */
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    /**
     * Tests whether the target component has a readable attribute of a given name.
     * 
     * @param name
     *            the name of the attribute to search for.
     * @return <code>true</code> iff the target component has a readable attribute named
     *         <code>name</code>.
     */
    public boolean hasReadableAttribute(String name) {
        Attribute attr = attributes.get(name);
        return attr != null && attr.isReadable();
    }

    /**
     * Tests whether the target component has a writable attribute of a given name.
     * 
     * @param name
     *            the name of the attribute to search for.
     * @return <code>true</code> iff the target component has a writable attribute named
     *         <code>name</code>.
     */
    public boolean hasWritableAttribute(String name) {
        Attribute attr = attributes.get(name);
        return attr != null && attr.isWritable();
    }

    /**
     * Returns the type of an attribute.
     * 
     * @param name
     *            the name of the attribute.
     * @return the type of the attribute.
     * @throws NoSuchElementException
     *             if the target component has no attribute named <code>name</<code>.
     */
    public Class<?> getAttributeType(String name) throws NoSuchElementException {
        Attribute attr = attributes.get(name);
        if (attr != null) {
            return attr.getType();
        } else {
            throw new NoSuchElementException("No attribute named " + name + ".");
        }
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((component == null) ? 0 : component.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final AttributesHelper other = (AttributesHelper) obj;
        if (component == null) {
            if (other.component != null)
                return false;
        } else if (!component.equals(other.component))
            return false;
        return true;
    }

    /**
     * Represents a single attribute.
     */
    private class Attribute {
        private final String name;

        private final Method reader;

        private final Method writer;

        public Attribute(final String name, final Method reader, final Method writer) {
            this.name = name;
            this.reader = reader;
            this.writer = writer;
            if (reader != null && writer != null) {
                Class<?> readerType = reader.getReturnType();
                Class<?> writerType = writer.getParameterTypes()[0];
                if (!readerType.isAssignableFrom(writerType)) {
                    // TODO Inconsistent typing. How to handle this?
                }
            }
        }

        public String getName() {
            return name;
        }

        public Class<?> getType() {
            // FIXME Is this the correct priority?
            if (reader != null) {
                return reader.getReturnType();
            } else {
                return writer.getParameterTypes()[0];
            }
        }

        public boolean isReadable() {
            return reader != null;
        }

        public boolean isWritable() {
            return writer != null;
        }

        public Object get() {
            try {
                return reader.invoke(target, new Object[0]);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Access control errors not handled.", e);
            } catch (InvocationTargetException ite) {
                throw new RuntimeException("Error while reading attribute " + name + ".",
                        ite);
            }
        }

        public void set(Object value) throws IllegalArgumentException {
            try {
                writer.invoke(target, new Object[] { value });
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Access control errors not handled.", e);
            } catch (InvocationTargetException ite) {
                throw new RuntimeException("Error while writing attribute " + name + ".",
                        ite);
            }
        }
    }
}
