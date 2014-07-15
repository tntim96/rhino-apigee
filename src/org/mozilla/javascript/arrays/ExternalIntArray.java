/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.arrays;

/**
 * An implementation of the external array using an array of "int"s. From a JavaScript perspective,
 * only "number" types may be set in the array. Valid values are the same as an "int" type in Java,
 * namely -2^31 to (2^31)-1, inclusive.
 */

public class ExternalIntArray
    extends ExternalArray
{
    private static final long serialVersionUID = 2944694724181197664L;

    private final int[] array;

    public ExternalIntArray(int[] array) {
        this.array = array;
    }

    public int[] getArray() {
        return array;
    }

    protected Object getElement(int index) {
        return array[index];
    }

    protected void putElement(int index, Object value) {
        int val = ((Number)value).intValue();
        array[index] = val;
    }

    public int getLength() {
        return array.length;
    }
}
