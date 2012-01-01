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
package org.objectweb.fractal.fscript.console;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.FScriptInterpreter;
import org.objectweb.fractal.fscript.Values;
import org.objectweb.fractal.fscript.nodes.AttributeNode;
import org.objectweb.fractal.fscript.nodes.ComponentNode;
import org.objectweb.fractal.fscript.nodes.InterfaceNode;
import org.objectweb.fractal.fscript.nodes.Node;
import org.objectweb.fractal.fscript.nodes.NodeFactory;
import org.objectweb.fractal.fscript.nodes.NodeImpl;

public class DotCommand extends AbstractCommand {
	// Syntax: :dot <root> [<selected>]
	// See http://www.graphviz.org/doc/info/lang.html

	private final NodeFactory fact;

	private final IdentifierFactory<Object> identifiers;

	public DotCommand(Console console, FScriptInterpreter fscript) {
		super(console, fscript);
		fact = fscript;
		identifiers = new IdentifierFactory<Object>("node");
	}

	public String getName() {
		return "dot";
	}

	public String getDescription() {
		return "Generates a graphical representation of an FPath graph.";
	}

	public void execute(String expr) throws Exception {
		Object result = fscript.evaluate(expr, null);
		Component root = ((NodeImpl) Values.getSingleNode(result)).getComponent();
		File tempFile = File.createTempFile("fractal", ".dot");
		PrintStream out = new PrintStream(new FileOutputStream(tempFile));
		try {
			out.println("digraph {");
			generateGraphDescription((ComponentNode) fact
					.createComponentNode(root), out);
			out.println("}");
		} finally {
			identifiers.clear();
			out.close();
		}
		console.printMessage("Output written to " + tempFile + ".");
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

	private Collection<Node> selectAll(Node source, String axis) {
		return fscript.getAxis(axis).selectFrom(source);
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
