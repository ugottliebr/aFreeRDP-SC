/*
   Copyright 2018 Aktiv Co.

   This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
   If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package com.freerdp.freerdpcore.services.pcsc;

import android.support.annotation.NonNull;

import com.freerdp.freerdpcore.rutoken.RtPcsc;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

import java.util.List;

public class PcscStatus {
    private PcscStatus(List<String> readerNames, int state, int protocol, byte[] atr) {
        this.readerNames = readerNames;
        this.state = state;
        this.protocol = protocol;
        this.atr = atr;
    }

    private static class StatusHelper {
        List<String> readerNames;
        final IntByReference readerLen = new IntByReference();
        final IntByReference state = new IntByReference();
        final IntByReference protocol = new IntByReference();
        byte[] atr;
        final IntByReference atrLen = new IntByReference();
    }

    public static PcscStatus getStatus(@NonNull PcscCard connection) throws PcscException {
        StatusHelper status = new StatusHelper();
        int result;
        @NonNull
        byte[] readerNamesBuffer;
        do {
            int r = RtPcsc.getLibrary().SCardStatusA(connection.getHandle(), null,
                    status.readerLen, status.state, status.protocol, null, status.atrLen);
            PcscException.throwIfNoSuccess(r, "Can't get status buffers sizes");
            //length can change between two SCardStatusA calls
            readerNamesBuffer = new byte[status.readerLen.getValue()];
            status.atr = new byte[status.atrLen.getValue()];
            result = RtPcsc.getLibrary().SCardStatusA(connection.getHandle(), readerNamesBuffer,
                    status.readerLen, status.state, status.protocol, status.atr, status.atrLen);
        } while (PcscReturnCode.SCARD_E_INSUFFICIENT_BUFFER.getValue() == result);
        PcscException.throwIfNoSuccess(result, "Can't get status");

        status.readerNames = Utility.stringListFromMultiString(readerNamesBuffer, status.readerLen.getValue());

        return new PcscStatus(status.readerNames, status.state.getValue(),
                status.protocol.getValue(), status.atr);
    }

    public final List<String> readerNames;
    public final int state;
    public final int protocol;
    public final byte[] atr;
}
