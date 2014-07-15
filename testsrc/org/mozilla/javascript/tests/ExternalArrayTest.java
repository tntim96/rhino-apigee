package org.mozilla.javascript.tests;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.arrays.ExternalArray;
import org.mozilla.javascript.arrays.ExternalByteArray;
import org.mozilla.javascript.arrays.ExternalClampedByteArray;
import org.mozilla.javascript.arrays.ExternalDoubleArray;
import org.mozilla.javascript.arrays.ExternalFloatArray;
import org.mozilla.javascript.arrays.ExternalIntArray;
import org.mozilla.javascript.arrays.ExternalShortArray;
import org.mozilla.javascript.arrays.ExternalUnsignedByteArray;
import org.mozilla.javascript.arrays.ExternalUnsignedIntArray;
import org.mozilla.javascript.arrays.ExternalUnsignedShortArray;

import static org.junit.Assert.*;

public class ExternalArrayTest
{
    final static String LS = System.getProperty("line.separator");

    @Test
    public void testPutGet()
    {
        byte[] ba = new byte[2];
        ba[0] = 1;
        ba[1] = 2;
        ExternalArray array = new ExternalByteArray(ba);
        runWithArray(array,
          "assertEquals(theArray[0], 1);" + LS +
          "assertEquals(theArray[1], 2);");
    }

    @Test
    public void testZeroLength()
    {
        ExternalArray array = new ExternalByteArray(new byte[0]);
        runWithArray(array,
          "if (theArray[0]) { throw 'Unexpected value out of range'; }");
    }

    @Test
    public void testGetRange()
    {
        // Should get undefined when going out of range
        ExternalArray array = new ExternalByteArray(new byte[2]);
        runWithArray(array,
          "if (theArray[2]) { throw 'Unexpected value out of range'; }");
    }

    @Test
    public void testPutRange()
    {
        // Should be able to put outside the range, using "non-external" array storage.
        ExternalArray array = new ExternalByteArray(new byte[2]);
        runWithArray(array,
          "theArray[2] = 3; assertEquals(theArray[2], 3);");
    }

    @Test
    public void testCountProps()
    {
        ExternalArray array = new ExternalByteArray(new byte[2]);
        runWithArray(array,
          "var i; var c = 0; for (i in theArray) { c++; }" + LS +
          "assertEquals(c, 2);");
    }

    @Test
    public void testCheckMixedProps()
    {
        // Should be able to mix properties and external array data
        byte[] ba = new byte[2];
        ba[0] = 11;
        ba[1] = 13;
        ExternalArray array = new ExternalByteArray(ba);

        runWithArray(array,
          "theArray.foo = 'foo';" + LS +
          "theArray.bar = 123;" + LS +
          "assertEquals(theArray[0], 11);" + LS +
          "assertEquals(theArray[1], 13);" + LS +
          "assertEquals(theArray['foo'], 'foo');" + LS +
          "assertEquals(theArray['bar'], 123);"
        );
    }

    @Test
    public void testCheckMixedPropIds()
    {
        byte[] ba = new byte[2];
        ba[0] = 11;
        ba[1] = 13;
        ExternalArray array = new ExternalByteArray(ba);

        runWithArray(array,
          "theArray.foo = 'foo';" + LS +
          "theArray.bar = 123;" + LS +
          "var one, two, foo, bar = false;" + LS +
          "var i; for (i in theArray) { " + LS +
          "if (i == 0) { one = true; }" + LS +
          "else if (i == 1) { two = true; }" + LS +
          "else if (i == 'foo') { foo = true; }" + LS +
          "else if (i == 'bar') { bar = true; }" + LS +
          "else { throw 'Unexpected property ' + i; }}" + LS +
          "assertEquals(one, true);" + LS +
          "assertEquals(two, true);" + LS +
          "assertEquals(foo, true);" + LS +
          "assertEquals(bar, true);"
        );
    }

    @Test
    public void testByte()
    {
        byte[] ba = new byte[4];
        ba[0] = (byte)1;
        ba[1] = (byte)-1;
        ba[2] = (byte)127;
        ba[3] = (byte)-128;
        ExternalArray array = new ExternalByteArray(ba);

        runWithArray(array,
          "assertEquals(theArray[2], 127);" + LS +
          "assertEquals(theArray[3], -128);" + LS +
          "theArray[0] = 11; assertEquals(theArray[0], 11);" + LS +
          "theArray[1] = -11; assertEquals(theArray[1], -11);"
        );

        assertEquals(11, ba[0]);
        assertEquals(-11, ba[1]);
    }

    @Test
    public void testUnsignedByte()
    {
        byte[] ba = new byte[4];
        ExternalArray array = new ExternalUnsignedByteArray(ba);

        runWithArray(array,
          "theArray[0] = 11; assertEquals(theArray[0], 11);" + LS +
          "theArray[1] = 150; assertEquals(theArray[1], 150);" + LS +
          "theArray[2] = 255; assertEquals(theArray[2], 255);"
        );
    }

    @Test
    public void testClampedByte()
    {
        byte[] ba = new byte[5];
        ExternalArray array = new ExternalClampedByteArray(ba);

        runWithArray(array,
          "theArray[0] = 11; assertEquals(theArray[0], 11);" + LS +
          "theArray[1] = 150; assertEquals(theArray[1], 150);" + LS +
          "theArray[2] = 255; assertEquals(theArray[2], 255);" + LS +
          "theArray[3] = -1; assertEquals(theArray[3], 0);" + LS +
          "theArray[4] = 300; assertEquals(theArray[4], 255);"
        );
    }

    @Test
    public void testShort()
    {
        short[] a = new short[4];
        a[0] = 1;
        a[1] = -1;
        a[2] = 32767;
        a[3] = -32768;
        ExternalArray array = new ExternalShortArray(a);

        runWithArray(array,
          "assertEquals(theArray[2], 32767);" + LS +
          "assertEquals(theArray[3], -32768);" + LS +
          "theArray[0] = 11; assertEquals(theArray[0], 11);" + LS +
          "theArray[1] = -11; assertEquals(theArray[1], -11);"
        );

        assertEquals(11, a[0]);
        assertEquals(-11, a[1]);
    }

    @Test
    public void testUnsignedShort()
    {
        short[] a = new short[4];
        ExternalArray array = new ExternalUnsignedShortArray(a);

        runWithArray(array,
          "theArray[0] = 11; assertEquals(theArray[0], 11);" + LS +
          "theArray[1] = 34000; assertEquals(theArray[1], 34000);" + LS +
          "theArray[2] = 65535; assertEquals(theArray[2], 65535);"
        );
    }

    @Test
    public void testInt()
    {
        int[] a = new int[4];
        a[0] = 1;
        a[1] = -1;
        a[2] = 2147483647;
        a[3] = -2147483648;
        ExternalArray array = new ExternalIntArray(a);

        runWithArray(array,
          "assertEquals(theArray[2], 2147483647);" + LS +
          "assertEquals(theArray[3], -2147483648);" + LS +
          "theArray[0] = 11; assertEquals(theArray[0], 11);" + LS +
          "theArray[1] = -11; assertEquals(theArray[1], -11);"
        );

        assertEquals(11, a[0]);
        assertEquals(-11, a[1]);
    }

    @Test
    public void testUnsignedInt()
    {
        int[] a = new int[4];
        ExternalArray array = new ExternalUnsignedIntArray(a);

        runWithArray(array,
          "theArray[0] = 11; assertEquals(theArray[0], 11);" + LS +
          "theArray[1] = 2147483647; assertEquals(theArray[1], 2147483647);" + LS +
          "theArray[1] = 2147483648; assertEquals(theArray[1], 2147483648);" + LS +
          "theArray[3] = 4294967295; assertEquals(theArray[3], 4294967295);"
        );
    }

    @Test
    public void testFloat()
    {
        float[] a = new float[6];
        a[0] = 1.0f;
        a[1] = 3.14f;
        a[2] = 3.4028234663852886E38f;
        a[3] = -1.401298464324817E-45f;
        ExternalArray array = new ExternalFloatArray(a);

        runWithArray(array,
          "assertEquals(theArray[0], 1.0);" + LS +
          "assertEquals(theArray[2], 3.4028234663852886E38);" + LS +
          "assertEquals(theArray[3], -1.401298464324817E-45);" + LS +
          "theArray[4] = 11.0; assertEquals(theArray[4], 11.0);" + LS +
          "theArray[5] = -11.0; assertEquals(theArray[5], -11.0);"
        );

        assertEquals(11.0, a[4], 0.0001);
        assertEquals(-11.0, a[5], 0.0001);
    }

    @Test
    public void testDouble()
    {
        double[] a = new double[6];
        a[0] = 1.0f;
        a[1] = 3.14f;
        a[2] = 3.4028234663852886E38f;
        a[3] = -1.401298464324817E-45f;
        ExternalArray array = new ExternalDoubleArray(a);

        runWithArray(array,
          "assertEquals(theArray[0], 1.0);" + LS +
          "assertEquals(theArray[2], 3.4028234663852886E38);" + LS +
          "assertEquals(theArray[3], -1.401298464324817E-45);" + LS +
          "theArray[4] = 11.0; assertEquals(theArray[4], 11.0);" + LS +
          "theArray[5] = -11.0; assertEquals(theArray[5], -11.0);"
        );

        assertEquals(11.0, a[4], 0.0001);
        assertEquals(-11.0, a[5], 0.0001);
    }

    private void runWithArray(final ExternalArray array, final String script)
    {
        ContextAction ca = new ContextAction() {
            @Override
            public Object run(Context cx)
            {
                Scriptable global = cx.initStandardObjects();
                ScriptableObject arr = (ScriptableObject)cx.newObject(global);
                arr.setExternalArray(array);
                assertTrue(arr.hasExternalArray());
                assertNotNull(arr.getExternalArray());
                global.put("theArray", global, arr);
                cx.evaluateString(global, HELPER + script, "myScript.js", 1, null);
                return null;
            }
        };
        Utils.runWithAllOptimizationLevels(ca);
    }

    private static final String HELPER =
      "function assertEquals(x, y) { if (x !== y) { throw 'Error: ' + x + ' !== ' + y; }} ";
}
