/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.arrays;

/**
 * An implementation of the external array using an array of "short"s. From a JavaScript perspective,
 * only "number" types may be set in the array. Valid values are between 0 and 65536, inclusive. Negative values
 * will be converted to unsigned "number" objects when accessed from JavaScript, whereas in Java, values
 * must be treated as "short" instances.
 */

public class ExternalUnsignedShortArray
    extends ExternalArray
{
    private static final long serialVersionUID = -5341065722456287177L;

    private final short[] array;

    public ExternalUnsignedShortArray(short[] array) {
        this.array = array;
    }

    public short[] getArray() {
        return array;
    }

    protected Object getElement(int index) {
        short val = array[index];
        return val & 0xffff;
    }

    protected void putElement(int index, Object value) {
        int val = ((Number)value).intValue();
        array[index] = (short)(val & 0xffff);
    }

    public int getLength() {
        return array.length;
    }
}
