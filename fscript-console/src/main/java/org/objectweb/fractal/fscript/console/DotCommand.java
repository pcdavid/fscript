/*
 * Copyright (c) 2004-2005 Universite de Nantes (LINA)
 * Copyright (c) 2005-2006 France Telecom
 * Copyright (c) 2006-2008 ARMINES
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
package org.objectweb.fractal.fscript.console;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.FScript;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.model.Node;
import org.objectweb.fractal.fscript.model.fractal.AttributeNode;
import org.objectweb.fractal.fscript.model.fractal.ComponentNode;
import org.objectweb.fractal.fscript.model.fractal.InterfaceNode;
import org.objectweb.fractal.fscript.model.fractal.NodeFactory;

/**
 * This command takes a set of Fractal components as arguments and generates a file
 * describing the corresponding FPath graph. The generated file can be processed by the
 * 'dot' program, part of the <a href='http://www.graphviz.org/>GraphViz</a> suite to
 * generated a graphical representation in various formats.
 * 
 * @author Pierre-Charles David
 */
public class DotCommand extends AbstractCommand {
    private NodeFactory fact;

    private IdentifierFactory<Object> identifiers;

    @Override
    public void setFScriptEngine(Component newFscript) {
        super.setFScriptEngine(newFscript);
        fact = getEngineInterface("node-factory", NodeFactory.class);
        identifiers = new IdentifierFactory<Object>("node");
    }

    public void execute(String expr) throws Exception {
        Object result = engine.execute(expr);
        Component root = ((ComponentNode) FScript.getSingleNode(result)).getComponent();
        File tempFile = File.createTempFile("fractal", ".dot");
        PrintStream out = new PrintStream(new FileOutputStream(tempFile));
        try {
            out.println("digraph {");
            generateGraphDescription(fact.createComponentNode(root), out);
            out.println("}");
        } finally {
            identifiers.clear();
            out.close();
        }
        showMessage("Output written to " + tempFile + ".");
    }

    private void generateGraphDescription(ComponentNode comp, PrintStream out) {
        declareComponent(out, comp);

        for (InterfaceNode itfNode : getInterfaceNodes(comp)) {
            declareInterface(out, itfNode);
            declareEdge(out, comp, itfNode, "interface");
        }

        for (AttributeNode attrNode : getAttributeNodes(comp)) {
            declareAttribute(out, attrNode);
            declareEdge(out, comp, attrNode, "attribute");
        }

        for (ComponentNode childNode : getChildrenNodes(comp)) {
            generateGraphDescription(childNode, out);
            declareEdge(out, comp, childNode, "child");
        }
    }

    private void declareComponent(PrintStream out, ComponentNode node) {
        out.print(identifiers.getIdentifierFor(node) + " [");
        out.print("color=green,");
        out.print("style=filled,");
        out.print("peripheries=2,");
        out.print("label=\"" + node.getName() + "\"");
        out.print("];");
    }

    private void declareInterface(PrintStream out, InterfaceNode node) {
        out.print(identifiers.getIdentifierFor(node) + " [");
        out.print("color=orange,");
        out.print("style=filled,");
        out.print("label=\"" + node.getName() + "\"");
        out.print("];");
    }

    private void declareAttribute(PrintStream out, AttributeNode node) {
        out.print(identifiers.getIdentifierFor(node) + " [");
        out.print("color=red,");
        out.print("style=filled,");
        out.print("label=\"" + node.getName() + "\"");
        out.print("];");
    }

    private void declareEdge(PrintStream out, Node source, Node dest, String arc) {
        String sourceId = identifiers.getIdentifierFor(source);
        String destId = identifiers.getIdentifierFor(dest);
        out.println("  " + sourceId + " -> " + destId + " [ label = \"" + arc
                + "\",labelfontsize=11 ];");
    }

    private InterfaceNode[] getInterfaceNodes(ComponentNode node) {
        return selectAll(node, "interface").toArray(new InterfaceNode[0]);
    }

    private AttributeNode[] getAttributeNodes(ComponentNode comp) {
        return selectAll(comp, "attribute").toArray(new AttributeNode[0]);
    }

    private ComponentNode[] getChildrenNodes(ComponentNode comp) {
        return selectAll(comp, "child").toArray(new ComponentNode[0]);
    }

    @SuppressWarnings("unchecked")
    private Collection<Node> selectAll(Node source, String axis) {
        try {
            String query = "$root/" + axis + "::*";
            engine.setGlobalVariable("root", source);
            return (Collection<Node>) engine.execute(query);
        } catch (FScriptException fse) {
            fse.printStackTrace();
            return null;
        }
    }

    class IdentifierFactory<T> {
        // @GuardedBy("this")
        private long nextIndex = 0;

        // @GuardedBy("this")
        private final Map<T, String> identifiers = new HashMap<T, String>();

        private final String prefix;

        public IdentifierFactory(String prefix) {
            this.prefix = prefix;
        }

        public String getIdentifierFor(T obj) {
            synchronized (this) {
                if (!identifiers.containsKey(obj)) {
                    String id = prefix + nextIndex++;
                    identifiers.put(obj, id);
                }
                return identifiers.get(obj);
            }
        }

        public void clear() {
            synchronized (this) {
                identifiers.clear();
            }
        }
    }
}
