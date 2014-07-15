/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.arrays;

/**
 * An implementation of the external array using an array of "float"s. Only "number" types may be set in
 * the array.
 */

public class ExternalFloatArray
    extends ExternalArray
{
    private static final long serialVersionUID = 3786769656861013570L;

    private final float[] array;

    public ExternalFloatArray(float[] array) {
        this.array = array;
    }

    public float[] getArray() {
        return array;
    }

    protected Object getElement(int index) {
        return array[index];
    }

    protected void putElement(int index, Object value) {
        float val = ((Number)value).floatValue();
        array[index] = val;
    }

    public int getLength() {
        return array.length;
    }
}
