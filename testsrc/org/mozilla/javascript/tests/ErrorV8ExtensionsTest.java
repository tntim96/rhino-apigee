/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.tests;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.StackStyle;


/**
 * Test for things that are specific to V8 like "captureStackTrace".
 */
public class ErrorV8ExtensionsTest {
    final static String LS = System.getProperty("line.separator");

    private void testScriptStackTrace(final String script, final String expectedStackTrace) {
        testScriptStackTrace(script, expectedStackTrace, -1);
        testScriptStackTrace(script, expectedStackTrace, 0);
        testScriptStackTrace(script, expectedStackTrace, 1);
    }

    private void testScriptStackTrace(final String script, final String expectedStackTrace,
                                      final int optimizationLevel) {
        try {
            Utils.executeScript(script, optimizationLevel);
        }
        catch (final RhinoException e) {
            Assert.assertEquals(expectedStackTrace, e.getScriptStackTrace());
        }
    }

    @AfterClass
    public static void resetStack()
    {
        RhinoException.setStackStyle(StackStyle.RHINO);
    }

    @Test
    public void defaultStack() {
        RhinoException.setStackStyle(StackStyle.RHINO);
        testIt("var o = {}; Error.captureStackTrace(o); o.stack;",
               "\tat myScript.js:1" + LS);
    }

    @Test
    public void mozillaStack() {
        RhinoException.setStackStyle(StackStyle.MOZILLA);
        testIt("var o = {}; Error.captureStackTrace(o); o.stack;",
               "@myScript.js:1" + LS);
    }

    @Test
    public void v8Stack() {
        RhinoException.setStackStyle(StackStyle.V8);
        testScriptStackTrace("null.method()", "    at myScript.js:1" + LS);
        final String script = "function f() \n{\n  null.method();\n}\nf();\n";
        testScriptStackTrace(script, "    at f (myScript.js:3)" + LS + "    at myScript.js:5" + LS);
        testIt("try { null.method() } catch (e) { e.stack }", "    at myScript.js:1" + LS);
        final String expectedStack = "    at f (myScript.js:2)" + LS + "    at myScript.js:4" + LS;
        testIt("function f() {\n null.method(); \n}\n try { f() } catch (e) { e.stack }", expectedStack);
    }

    @Test
    public void formatStack() {
        final String script =
            "Error.prepareStackTrace = function(err, st) { return st; };" + LS +
            "try { null.method(); } catch (e) { var st = e.stack[0];" + LS +
            "if (st.getFileName() !== 'myScript.js') { throw 'Wrong file name ' + st.getFileName(); }" + LS +
            "if (st.getLineNumber() !== 2) { throw 'Wrong line number ' + st.getLineNumber(); }}";
        runIt(script);
    }

    @Test
    public void captureAndFormatStack() {
        final String script =
            "Error.prepareStackTrace = function(err, st) { return st; };" + LS +
            "var obj = {}; Error.captureStackTrace(obj); var st = obj.stack[0];" + LS +
            "if (st.getFileName() !== 'myScript.js') { throw 'Wrong file name ' + st.getFileName(); }" + LS +
            "if (st.getLineNumber() !== 2) { throw 'Wrong line number ' + st.getLineNumber(); }";
        runIt(script);
    }

    private void testIt(final String script, final Object expected) {
        final ContextAction action = new ContextAction() {
            public Object run(final Context cx) {
                try {
                    final ScriptableObject scope = cx.initStandardObjects();
                    final Object o = cx.evaluateString(scope, script,
                            "myScript.js", 1, null);
                    Assert.assertEquals(expected, o);
                    return o;
                }
                catch (final RuntimeException e) {
                    throw e;
                }
                catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Utils.runWithAllOptimizationLevels(action);
    }

    private void runIt(final String script) {
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(Context cx)
            {
                final ScriptableObject scope = cx.initStandardObjects();
                cx.evaluateString(scope, script, "myScript.js", 1, null);
                return null;
            }
        };
        Utils.runWithAllOptimizationLevels(action);
    }
}
