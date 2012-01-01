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
package org.objectweb.fractal.fscript.model.fractal;

import static org.objectweb.fractal.fscript.types.PrimitiveType.*;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.fscript.AttributesHelper;
import org.objectweb.fractal.fscript.model.BasicModel;
import org.objectweb.fractal.fscript.model.ComposedAxis;
import org.objectweb.fractal.fscript.model.Model;
import org.objectweb.fractal.fscript.model.Property;
import org.objectweb.fractal.fscript.model.ReflectiveAxis;
import org.objectweb.fractal.fscript.model.TransitiveAxis;
import org.objectweb.fractal.fscript.procedures.NativeLibrary;
import org.objectweb.fractal.fscript.procedures.NativeProcedure;
import org.objectweb.fractal.util.Fractal;

/**
 * This class represents the standard Fractal component model in terms of the
 * {@link Model} APIs. It describes which kinds of nodes are present in a Fractal
 * architecture, what properties these nodes have, and what axes can connect these nodes
 * together to form a complete Fractal architecture. It also provides all the
 * introspection and reconfiguration procedures implied by this description (and a few
 * others) a {@link NativeProcedure}s (through its {@link NativeLibrary} interface).
 * 
 * @author Pierre-Charles David
 */
public class FractalModel extends BasicModel implements NodeFactory, BindingController,
        FractalModelAttributes {
    /**
     * The Fractal ADL Factory used by actions which create new components.
     */
    private Factory adlFactory;

    /**
     * The context argument passed to {@link Factory#newComponent(String, Map)} by
     * <code>adl-new()</code>.
     */
    private Map<String, Object> instanciationContext = new HashMap<String, Object>();

    /**
     * Contributes the different kinds of nodes used to represent Fractal architectures.
     */
    @Override
    protected void createNodeKinds() {
        addKind("component", new Property("name", STRING, true),
                new Property("state", STRING, true));
        addKind("interface", new Property("name", STRING, false), new Property("internal", BOOLEAN,
                false), new Property("signature", STRING, false), new Property("collection",
                BOOLEAN, false), new Property("optional", BOOLEAN, false), new Property("client",
                BOOLEAN, false));
        addKind("attribute", new Property("name", STRING, false), new Property("type", STRING,
                false), new Property("value", OBJECT, true), new Property("readable", BOOLEAN,
                false), new Property("writable", BOOLEAN, false));
    }

    /**
     * Contributes the axes which can be used to connect Fractal nodes in order to
     * represent the structure of a Fractal architecture.
     */
    @Override
    protected void createAxes() {
        // Base axes
        addAxis(new ChildAxis(this));
        addAxis(new ParentAxis(this));
        addAxis(new ComponentAxis(this));
        addAxis(new InterfaceAxis(this, false)); // External interfaces
        addAxis(new InterfaceAxis(this, true)); // Internal interfaces
        addAxis(new AttributeAxis(this));
        addAxis(new BindingAxis(this));
        // Transitive axes
        addAxis(new TransitiveAxis(getAxis("child"), "descendant"));
        addAxis(new TransitiveAxis(getAxis("parent"), "ancestor"));
        // Composed axes
        addAxis(new ComposedAxis("sibling", getAxis("parent"), getAxis("child")));
        // Reflective axes
        addAxis(new ReflectiveAxis(getAxis("child")));
        addAxis(new ReflectiveAxis(getAxis("parent")));
        addAxis(new ReflectiveAxis(getAxis("descendant")));
        addAxis(new ReflectiveAxis(getAxis("ancestor")));
        addAxis(new ReflectiveAxis(getAxis("sibling")));
    }

    /**
     * Contributes a few custom procedures to manipulate Fractal architecture which can
     * not be described and generated in the framework of the {@link Model} APIs.
     */
    @Override
    protected void createAdditionalProcedures() {
        ADLNewAction adlNew = new ADLNewAction();
        try {
            adlNew.bindFc("fractal-model", this);
        } catch (NoSuchInterfaceException e) {
            throw new AssertionError("Internal inconsistency with ADLNewAction.");
        }
        addProcedure(adlNew);

        NewCompositeAction nca = new NewCompositeAction();
        try {
            nca.bindFc("fractal-model", this);
            nca.bindFc("bootstrap", Fractal.getBootstrapComponent());
        } catch (NoSuchInterfaceException e) {
            throw new AssertionError("Internal inconsistency with NewCompositeAction.");
        } catch (InstantiationException e) {
            throw new AssertionError("Unable to get Fractal's bootstrap component.");
        }
        addProcedure(nca);

        CollectionBindAction cba = new CollectionBindAction();
        try {
            cba.bindFc("fractal-model", this);
        } catch (NoSuchInterfaceException e) {
            throw new AssertionError("Internal inconsistency with CollectionBindAction.");
        }
        addProcedure(cba);

        createRMIProcedures();
    }

    /**
     * Contributes Fractal RMI specific custom procedures.
     */
    private void createRMIProcedures() {
        AbstractFractalProcedure[] procs = new AbstractFractalProcedure[] {
                new RmiLookupFunction(), new RmiBindingsFunction(), new RmiBindAction(),
                new RmiUnbindAction() };
        for (AbstractFractalProcedure afp : procs) {
            try {
                afp.bindFc("fractal-model", this);
            } catch (NoSuchInterfaceException e) {
                throw new AssertionError("Internal inconsistency with " + afp.getName() + ".");
            }
            addProcedure(afp);
        }
    }
    
    public Map<String, Object> getInstanciationContext() {
        return instanciationContext;
    }

    public ComponentNode createComponentNode(Component comp) {
        return new ComponentNode(this, comp);
    }

    public InterfaceNode createInterfaceNode(Interface itf) {
        return new InterfaceNode(this, itf);
    }

    public AttributeNode createAttributeNode(Component comp, String attr) {
        return new AttributeNode(this, new AttributesHelper(comp), attr);
    }

    public String[] listFc() {
        return new String[] { "adl-factory" };
    }

    public Object lookupFc(String clItfName) throws NoSuchInterfaceException {
        if ("adl-factory".equals(clItfName)) {
            return this.adlFactory;
        } else {
            throw new NoSuchInterfaceException(clItfName);
        }
    }

    public void bindFc(String clItfName, Object srvItf) throws NoSuchInterfaceException {
        if ("adl-factory".equals(clItfName)) {
            this.adlFactory = (Factory) srvItf;
        } else {
            throw new NoSuchInterfaceException(clItfName);
        }
    }

    public void unbindFc(String clItfName) throws NoSuchInterfaceException {
        if ("adl-factory".equals(clItfName)) {
            this.adlFactory = null;
        } else {
            throw new NoSuchInterfaceException(clItfName);
        }
    }

    @Override
    public String toString() {
        return "Fractal model";
    }
}
