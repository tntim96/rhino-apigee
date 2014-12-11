package org.mozilla.javascript.tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.drivers.TestUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertFalse;

/**
 * This class provides a way to run a directory full of "jsunit" tests as they are found in the V8 test suite.
 */
@RunWith(Parameterized.class)
public class MJsUnitTest
{
    private static final String baseDirectory = "testsrc" + File.separator + "mjsunit";
    private static final String jsExtension = ".js";
    private static final String baseScriptName = "mjsunit.js";
    private static final int[] optLevels = new int[] { -1, 0, 9 };

    private static String baseScript;

    @Parameterized.Parameters(name = "{index}: {0} ({1}, {2})")
    public static Collection<Object[]> enumerateTests()
    {
        File[] testFiles = TestUtils.recursiveListFiles(new File(baseDirectory),
                             new FileFilter() {
                                 public boolean accept(File f) {
                                    return f.getName().endsWith(jsExtension);
                                 }
                             });

        ArrayList<Object[]> tests = new ArrayList<Object[]>();

        for (File testFile : testFiles) {
            for (int optLevel : optLevels) {
                String n = testFile.getName() + '-' + optLevel;
                tests.add(new Object[] { n, testFile, optLevel });
            }
        }

        return tests;
    }

    private static String loadBaseScript()
        throws IOException
    {
        if (baseScript != null) {
            return baseScript;
        }

        File baseFile = new File(baseDirectory, baseScriptName);

        FileReader rdr = new FileReader(baseFile);
        StringBuilder src = new StringBuilder();
        char[] tmp = new char[4096];
        int count;

        do {
            count = rdr.read(tmp);
            if (count > 0) {
                src.append(tmp, 0, count);
            }
        } while (count > 0);

        baseScript = src.toString();
        return baseScript;
    }

    private final File fileName;
    private final int optLevel;

    public MJsUnitTest(String name, File fileName, int optLevel)
    {
        this.fileName = fileName;
        this.optLevel = optLevel;
    }

    @Test
    public void testMJsUnit()
        throws IOException
    {
        ContextFactory factory = ContextFactory.getGlobal();
        Context cx  = factory.enterContext();
        try {
            cx.setGeneratingDebug(true);
            cx.setOptimizationLevel(optLevel);
            Scriptable global = cx.initStandardObjects();
            cx.evaluateString(global, loadBaseScript(), "mjsunit.js", 1, null);

            Reader rdr = new FileReader(fileName);
            try {
                cx.evaluateReader(global, rdr, fileName.getName() + '-' + optLevel, 1, null);
            } finally {
                rdr.close();
            }

        } catch (RhinoException re) {
            System.err.println("Error in " + fileName.getName() + " opt level " + optLevel +
                               ": " + re + ": " + re.getScriptStackTrace());
            assertFalse("Test " + fileName.getName() + " failed at opt level " + optLevel, true);
        } finally {
            Context.exit();
        }
    }
}
