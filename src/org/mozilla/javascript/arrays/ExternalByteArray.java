/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.arrays;

/**
 * An implementation of the external array using an array of bytes. From a JavaScript perspective,
 * only "number" types may be set in the array. Valid values are between -128 and 127, inclusive.
 */

public class ExternalByteArray
    extends ExternalArray
{
    private static final long serialVersionUID = 5377484970217959212L;

    private final byte[] array;

    public ExternalByteArray(byte[] array) {
        this.array = array;
    }

    public byte[] getArray() {
        return array;
    }

    protected Object getElement(int index) {
        return Integer.valueOf(array[index]);
    }

    protected void putElement(int index, Object value) {
        Number num = (Number)value;
        array[index] = num.byteValue();
    }

    public int getLength() {
        return array.length;
    }
}
