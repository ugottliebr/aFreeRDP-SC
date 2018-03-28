/*
   Copyright 2018 Aktiv Co.

   This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
   If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package com.freerdp.freerdpcore.services.pcsc;

import android.support.annotation.NonNull;

import com.sun.jna.Native;

import java.security.InvalidParameterException;
import java.util.List;

public class Utility {
    public static List<String> stringListFromMultiString(@NonNull byte[] multiString, int length) throws InvalidParameterException {
        if (length > multiString.length)
            throw new InvalidParameterException();

        char[] strings = new char[multiString.length];
        for (int i = 0; i < strings.length; ++i)
            strings[i] = (char) multiString[i];
        return Native.toStringList(strings);
    }
}
