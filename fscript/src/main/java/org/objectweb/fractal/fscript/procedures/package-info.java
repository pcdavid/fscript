/**
 * This packages defines the core notion or an FScript {@link Procedure}, {@link
 * NativeProcedure} and {@link NativeLibrary}. It also contains the implementation of all
 * the model-independent pre-defined procedures. These procedures form the core
 * operations available to all FScript programs. Because of the way FScript is
 * implemented, this includes arithmetic, comparison and boolean operators.
 * <p>
 * This package also defines the <code>CoreLibrary</code> class, which is intended to
 * be used as a primitive Fractal component (with its corresponding Fractal ADL
 * definition file) to make all these core procedures available as a single
 * <code>NativeLibrary</code> component.
 * 
 *  @since 2.0
 */
package org.objectweb.fractal.fscript.procedures;