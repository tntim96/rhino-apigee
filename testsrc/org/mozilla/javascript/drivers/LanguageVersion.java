/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.javascript.drivers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Intended to be used as an optional annotation for subclasses
 * of {@link org.mozilla.javascript.drivers.ScriptTestsBase}.
 * Sets the language version of test's script execution context.
 */
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LanguageVersion {
    int value();
}
