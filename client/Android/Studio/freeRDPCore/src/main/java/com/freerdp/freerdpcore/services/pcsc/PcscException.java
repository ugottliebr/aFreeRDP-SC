/*
   Copyright 2018 Aktiv Co.

   This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
   If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package com.freerdp.freerdpcore.services.pcsc;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class PcscException extends Exception {
    public PcscException(int code, String message) {
        super(generateMessage(code, message));
        mCode = code;
    }

    public static void throwIfNoSuccess(int code, String message) throws PcscException {
        if (PcscReturnCode.SCARD_S_SUCCESS.getValue() != code) {
            throw new PcscException(code, message);
        }
    }

    int getCode() {
        return mCode;
    }

    private static String generateMessage(int code, String message) {
        Map<String, String> info = new HashMap<>();
        info.put("code", String.format("0x%08X", code));
        try {
            info.put("name", PcscReturnCode.fromValue(code).name());
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }

        StringBuilder builder = new StringBuilder();
        builder.append(message);
        if (!info.isEmpty()) {
            builder.append(" [");
            for (Map.Entry pair : info.entrySet()) {
                builder.append(pair.getKey() + ": " + pair.getValue() + " ");
            }
            builder.append("]");
        }
        return builder.toString();
    }

    private final int mCode;
}
