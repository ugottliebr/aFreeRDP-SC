/*
   Copyright 2018 Aktiv Co.

   This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
   If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package com.freerdp.freerdpcore.rutoken;

import android.support.annotation.NonNull;

import com.freerdp.freerdpcore.services.pcsc.atr.PcscAtr;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class RtAtrHelper {
    public static boolean isRutokenAtr(@NonNull PcscAtr atr) {
        return mTokenTypes.containsKey(new String(atr.getHistoricalBytes()));
    }

    public static String getHumanReadableName(@NonNull PcscAtr atr) throws InvalidParameterException {
        RtTokenType tokenType = mTokenTypes.get(new String(atr.getHistoricalBytes()));
        if (null == tokenType)
            throw new InvalidParameterException();
        return tokenType.getHumanReadableName();
    }

    //use String as key because of java array comparison problem
    private static final Map<String/*historical byte*/, RtTokenType> mTokenTypes = new HashMap<>();

    static {
        for (RtTokenType tokenType : RtTokenType.values()) {
            mTokenTypes.put(new String(tokenType.getHistrocalAtr()), tokenType);
        }
    }
}
