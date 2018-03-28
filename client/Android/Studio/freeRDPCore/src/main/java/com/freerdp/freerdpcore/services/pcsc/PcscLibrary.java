/*
   Copyright 2018 Aktiv Co.

   This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
   If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package com.freerdp.freerdpcore.services.pcsc;

import com.sun.jna.Library;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;


public interface PcscLibrary extends Library {
    int SCardEstablishContext(int dwScope, Pointer pvReserved1, Pointer pvReserved2, IntByReference phContext);

    int SCardReleaseContext(int hContext);

    int SCardConnectA(int hContext, String szReader, int dwShareMode, int dwPreferredProtocols,
                      IntByReference phCard, IntByReference pdwActiveProtocol);

    int SCardDisconnect(int hCard, int dwDisposition);

    int SCardStatusA(int hCard, byte[] mszReaderName, IntByReference pcchReaderLen, IntByReference pdwState,
                     IntByReference pdwProtocol, byte[] pbAtr, IntByReference pcbAtrLen);

    int SCardListReadersA(int hContext, String[] mszGroups, byte[] mszReaders, IntByReference pcchReaders);
}
