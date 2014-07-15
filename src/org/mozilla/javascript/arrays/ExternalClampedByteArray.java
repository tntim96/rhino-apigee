/* -*- Mode: java; tab-width: 8; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.arrays;

/**
 * An implementation of the external array using an array of bytes. From a JavaScript perspective,
 * only "number" types may be set in the array. Valid values are between 0 and 255, inclusive. Any values out
 * of that range will be "clamped", meaning that values smaller than 0 will be set to 0 and larger than 255 will
 * be set to 255.
 */

public class ExternalClampedByteArray
    extends ExternalArray
{
    private static final long serialVersionUID = -1883561335812409618L;

    private final byte[] array;

    public ExternalClampedByteArray(byte[] array) {
        this.array = array;
    }

    public byte[] getArray() {
        return array;
    }

    protected Object getElement(int index) {
        return array[index] & 0xff;
    }

    protected void putElement(int index, Object value) {
        int val = ((Number)value).intValue();
        if (val < 0) {
            val = 0;
        } else if (val > 255) {
            val = 255;
        }
        array[index] = (byte)(val & 0xff);
    }

    public int getLength() {
        return array.length;
    }
}
