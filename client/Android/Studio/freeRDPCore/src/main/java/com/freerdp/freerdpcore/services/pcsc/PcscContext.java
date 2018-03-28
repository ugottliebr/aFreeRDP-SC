/*
   Copyright 2018 Aktiv Co.

   This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
   If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package com.freerdp.freerdpcore.services.pcsc;

import android.support.annotation.NonNull;

import com.freerdp.freerdpcore.rutoken.RtPcsc;
import com.sun.jna.ptr.IntByReference;

import java.util.ArrayList;
import java.util.List;

public class PcscContext implements AutoCloseable {
    public PcscContext(int scope) throws PcscException {
        int r = RtPcsc.getLibrary().SCardEstablishContext(scope, null, null, mHandle);
        PcscException.throwIfNoSuccess(r, "Can't establish context");
    }

    public int getHandle() {
        return mHandle.getValue();
    }

    @NonNull
    public List<PcscReader> listReaders() throws PcscException {
        IntByReference readersBufferSize = new IntByReference();
        int result;
        @NonNull
        byte[] readersBuffer;
        do {
            int r = RtPcsc.getLibrary().SCardListReadersA(getHandle(), null, null, readersBufferSize);
            PcscException.throwIfNoSuccess(r, "Can't get readers buffer size");

            //readers amount can change between two SCardListReadersA calls
            readersBuffer = new byte[readersBufferSize.getValue()];
            result = RtPcsc.getLibrary().SCardListReadersA(getHandle(), null, readersBuffer, readersBufferSize);
        } while (PcscReturnCode.SCARD_E_INSUFFICIENT_BUFFER.getValue() == result);
        PcscException.throwIfNoSuccess(result, "Can't list readers");

        List<PcscReader> readers = new ArrayList<>();
        for (String name : Utility.stringListFromMultiString(readersBuffer, readersBufferSize.getValue())) {
            readers.add(new PcscReader(name, this));
        }
        return readers;
    }

    @Override
    public void close() throws PcscException {
        release();
    }

    private void release() throws PcscException {
        int r = RtPcsc.getLibrary().SCardReleaseContext(getHandle());
        PcscException.throwIfNoSuccess(r, "Can't release context");
    }

    private final IntByReference mHandle = new IntByReference();
}
