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
package org.objectweb.fractal.fscript;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.diagnostics.DiagnosticLog;
import org.objectweb.fractal.fscript.diagnostics.Severity;

public class LibraryTest extends FractalTestCase {

    private Component fscript;

    private ScriptLoader loader;
    
    private DiagnosticLog log;

    @Before
    public void setUp() throws Exception {
        fscript = FScript.newEngine();
        loader = FScript.getScriptLoader(fscript);
        log = (DiagnosticLog) fscript.getFcInterface("diagnostics");
        assertEquals(0, log.getDiagnostics().size());
    }
    
    @After
    public void tearDown() {
        log.clear();
    }

    @Test(expected = InvalidScriptException.class)
    public void tryToRedefinePrimitive() throws Exception {
        String definition = "action add-child(foo, bar) { echo('Ooops.'); }";
        loader.load(definition);
    }
    
    @Test(expected = InvalidScriptException.class)
    public void tryProcedureWithDuplicateArguments() throws InvalidScriptException {
        String definition = "function f(x, y, x) { echo('Ooops.'); }";
        loader.load(definition);
    }

    @Test
    public void tryToRedefineUserProc() throws Exception {
        String definition1 = "action foo(bar) { echo('Version 1.'); }";
        String definition2 = "action foo(bar) { echo('Version 2.'); }";
        loader.load(definition1);
        assertEquals(0, log.getDiagnostics().size());
        loader.load(definition2);
        assertEquals(2, log.getDiagnostics().size());
        Diagnostic diag = log.getDiagnostics().get(0);
        assertEquals(Severity.INFORMATION, diag.getSeverity());
        assertEquals("Procedure 'foo' redefined.", diag.getMessage());
        diag = log.getDiagnostics().get(1);
        assertEquals(Severity.INFORMATION, diag.getSeverity());
        assertEquals("New definition of method 'foo' registered.", diag.getMessage());
    }

    @Test
    public void tryDefineFunctionCallingAction() throws Exception {
        String definition = "function invalid(foo) { set-state($foo, 'STARTED'); }";
        loader.load(definition);
        assertEquals(1, log.getDiagnostics().size());
        Diagnostic diag = log.getDiagnostics().get(0);
        assertEquals(Severity.ERROR, diag.getSeverity());
        assertEquals(
                "Procedure declared as pure function but calls action set-state(component, string) -> void.",
                diag.getMessage());
    }

    @Test
    public void tryDefineProcedureCallinUnknownProcedure() throws Exception {
        loader = (ScriptLoader) fscript.getFcInterface("loader");
        String definition = "function incomplete(foo) { unknown($foo); }";
        loader.load(definition);
        assertEquals(1, log.getDiagnostics().size());
        Diagnostic diag = log.getDiagnostics().get(0);
        assertEquals(Severity.WARNING, diag.getSeverity());
        assertEquals("Definition references currently unknown procedure unknown().", diag
                .getMessage());
    }
}
