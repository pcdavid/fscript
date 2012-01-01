/**
 * This package contains the definition of the very primitive type system used currently
 * in FScript. The only operation available on a type is to check whether or not a value
 * has this type. There is no explicit relation between the types themselves (no
 * sub-typing, no guarantee of disjointness...). Future versions may extend this type
 * system or replace it with something more sophisticated, but for now this simple
 * implementation is sufficient for dynamic type checking of procedure arguments and
 * return values.
 * 
 *  @since 2.0
 */
package org.objectweb.fractal.fscript.types;