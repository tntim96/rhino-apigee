/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.arrays;

/**
 * An implementation of the external array using an array of "short"s. From a JavaScript perspective,
 * only "number" types may be set in the array. Valid values are between -32768 and 32767, inclusive.
 */

public class ExternalShortArray
    extends ExternalArray
{
    private static final long serialVersionUID = -2766378114727057429L;

    private final short[] array;

    public ExternalShortArray(short[] array) {
        this.array = array;
    }

    public short[] getArray() {
        return array;
    }

    protected Object getElement(int index) {
        return array[index];
    }

    protected void putElement(int index, Object value) {
        short val = ((Number)value).shortValue();
        array[index] = val;
    }

    public int getLength() {
        return array.length;
    }
}
