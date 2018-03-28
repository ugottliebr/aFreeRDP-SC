/*
   Copyright 2018 Aktiv Co.

   This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
   If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package com.freerdp.freerdpcore.services.pcsc.atr;

import android.support.annotation.NonNull;

public class PcscAtr {
    public PcscAtr(@NonNull byte[] atr, @NonNull byte[] historicalBytes) {
        mAtr = atr;
        mHistoricalBytes = historicalBytes;
    }

    @NonNull
    public byte[] toBytes() {
        return mAtr;
    }
    @NonNull
    public byte[] getHistoricalBytes() {
        return mHistoricalBytes;
    }

    @NonNull
    private final byte[] mAtr;
    @NonNull
    private final byte[] mHistoricalBytes;
}
