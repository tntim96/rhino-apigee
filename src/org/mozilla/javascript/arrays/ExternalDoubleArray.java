/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.arrays;

/**
 * An implementation of the external array using an array of "double"s. Only "number" types may be set in
 * the array.
 */

public class ExternalDoubleArray
    extends ExternalArray
{
    private static final long serialVersionUID = -773914084068347275L;

    private final double[] array;

    public ExternalDoubleArray(double[] array) {
        this.array = array;
    }

    public double[] getArray() {
        return array;
    }

    protected Object getElement(int index) {
        return array[index];
    }

    protected void putElement(int index, Object value) {
        double val = ((Number)value).doubleValue();
        array[index] = val;
    }

    public int getLength() {
        return array.length;
    }
}
