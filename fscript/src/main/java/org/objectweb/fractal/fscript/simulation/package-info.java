/**
 * This packages provides a generic simulator for FScript programs. It uses a standard
 * interpreter connected to a <em>fake</em> model, automatically derived from an actual
 * model definition (for example the Fractal model definition). The derived model
 * implements a copy-on-write strategy to simulate the execution of FPath and FScript
 * operations on a real target system without modifying it.
 * 
 * @since 2.0
 */
package org.objectweb.fractal.fscript.simulation;