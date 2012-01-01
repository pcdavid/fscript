/**
 * This package provides a meta-level description of the Fractal component model. It
 * specializes the abstract notion of a {@link Model} to represent Fractal in a way
 * suitable for the rest of the FScript implementation (now completely model-agnostic)
 * to work on Fractal architectures.
 * <p>
 * The main element of this package is the {@link FractalModel} class and its
 * corresponding component definition, which aggregate all the other types in the
 * package in a consistent whole.
 * <p>
 * A few Fractal-related operations can not currently be modeled in the framework defined
 * by the {@link Model} abstraction. They are implemented as ad hoc primitive procedures.
 * These include: all the operations which create new components, <code>cbind()</code>,
 * which deals with collection interfaces, and all the operations which manipulate Fractal
 * RMI registries.
 * 
 *  @since 2.0
 */
package org.objectweb.fractal.fscript.model.fractal;

import org.objectweb.fractal.fscript.model.Model;

