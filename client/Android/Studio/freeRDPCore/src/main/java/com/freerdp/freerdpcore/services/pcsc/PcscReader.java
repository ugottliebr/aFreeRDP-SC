/*
   Copyright 2018 Aktiv Co.

   This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
   If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package com.freerdp.freerdpcore.services.pcsc;

import android.support.annotation.NonNull;

public class PcscReader {
    public PcscReader(String name, PcscContext context) {
        mName = name;
        mContext = context;
    }

    @NonNull
    public PcscCard connect() throws PcscException {
        return connect(PcscConstants.SCARD_SHARE_SHARED, PcscConstants.SCARD_PROTOCOL_ANY);
    }

    @NonNull
    public PcscCard connect(int shareMode, int prefferedProtocols) throws PcscException {
        return new PcscCard(mContext, mName, shareMode, prefferedProtocols);
    }

    private final String mName;
    private final PcscContext mContext;
}
