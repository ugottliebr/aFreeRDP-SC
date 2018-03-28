/*
   Copyright 2018 Aktiv Co.

   This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
   If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package com.freerdp.freerdpcore.services.pcsc.atr;

import android.support.annotation.NonNull;


public class PcscRawAtrParser implements PcscAtrParser {
    public PcscRawAtrParser(@NonNull byte[] atr) {
        mAtr = atr;
    }

    @NonNull
    @Override
    public PcscAtr parse() throws PcscAtrException {
        if (mAtr.length < TS_T0_SIZE) {
            throw new PcscAtrException("No TS, T0");
        }
        final byte ts = mAtr[0];
        if (ts != DIRECT_CONVENTION && ts != INVERSE_CONVENTION) {
            throw new PcscAtrException("Unknown TS encoding");
        }
        final byte t0 = mAtr[1];
        final int historicalBytesLen = t0 & 0x0f;

        int historicalBytesOffset = TS_T0_SIZE;
        int interfaceBytesFlags = (t0 & 0xf0) >> 4;//TA TB TC TD presence
        while (interfaceBytesFlags != 0) {
            if ((interfaceBytesFlags & 1) != 0) {//has TA
                ++historicalBytesOffset;
            }
            if ((interfaceBytesFlags & 2) != 0) {//has TB
                ++historicalBytesOffset;
            }
            if ((interfaceBytesFlags & 4) != 0) {//has TC
                ++historicalBytesOffset;
            }
            if ((interfaceBytesFlags & 8) != 0) {//has TD
                if (historicalBytesOffset >= mAtr.length) {
                    throw new PcscAtrException("Historical bytes offset out of ATR range");
                }
                final byte TD = mAtr[historicalBytesOffset];
                interfaceBytesFlags = (TD & 0xf0) >> 4;
                ++historicalBytesOffset;
            } else {
                break;
            }
        }
        int atrLen = historicalBytesOffset + historicalBytesLen;
        if (atrLen != mAtr.length && atrLen != mAtr.length - TCK_SIZE) {// TCK is optional
            throw new PcscAtrException("Parsing error: atrLen out of range");
        }

        byte[] historicalBytes = new byte[historicalBytesLen];
        System.arraycopy(mAtr, historicalBytesOffset, historicalBytes, 0, historicalBytesLen);
        return new PcscAtr(mAtr, historicalBytes);
    }

    private static final int TS_T0_SIZE = 2;
    private static final int TCK_SIZE = 1;
    private static final byte DIRECT_CONVENTION = 0x3b;
    private static final byte INVERSE_CONVENTION = 0x3f;

    @NonNull
    private final byte[] mAtr;
}
