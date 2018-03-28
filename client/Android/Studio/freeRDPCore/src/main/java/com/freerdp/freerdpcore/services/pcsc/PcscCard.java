/*
   Copyright 2018 Aktiv Co.

   This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
   If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package com.freerdp.freerdpcore.services.pcsc;

import android.support.annotation.NonNull;

import com.freerdp.freerdpcore.rutoken.RtPcsc;
import com.sun.jna.ptr.IntByReference;

//TODO implement transmit transactions etc. in this class when needed
/**
 * Determines connection between application and smart card, not a physical card representation.
 * In other words there could be many PcscCard instances matching same card.
 */
public class PcscCard implements AutoCloseable {
    public PcscCard(@NonNull PcscContext context, String reader, int shareMode, int prefferedProtocols) throws PcscException {
        int r = RtPcsc.getLibrary().SCardConnectA(context.getHandle(), reader, shareMode, prefferedProtocols, mHandle, mActiveProtocol);
        PcscException.throwIfNoSuccess(r, "Can't connect");
    }

    public int getHandle() {
        return mHandle.getValue();
    }
    public int getActiveProtocol() {
        return mActiveProtocol.getValue();
    }

    public PcscStatus getStatus() throws PcscException {
        return PcscStatus.getStatus(this);
    }

    public void close(int disposition) throws PcscException {
        disconnect(disposition);
    }

    @Override
    public void close() throws PcscException {
        disconnect(PcscConstants.SCARD_LEAVE_CARD);
    }

    private void disconnect(int disposition) throws PcscException {
        int r = RtPcsc.getLibrary().SCardDisconnect(mHandle.getValue(), disposition);
        PcscException.throwIfNoSuccess(r, "Can't disconnect");
    }

    private final IntByReference mHandle = new IntByReference();
    private final IntByReference mActiveProtocol = new IntByReference();
}
