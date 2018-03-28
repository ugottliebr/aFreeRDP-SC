/*
   Copyright 2018 Aktiv Co.

   This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
   If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package com.freerdp.freerdpcore.rutoken;

import android.support.annotation.NonNull;

import com.freerdp.freerdpcore.services.pcsc.PcscCard;
import com.freerdp.freerdpcore.services.pcsc.PcscConstants;
import com.freerdp.freerdpcore.services.pcsc.PcscContext;
import com.freerdp.freerdpcore.services.pcsc.PcscLibrary;
import com.freerdp.freerdpcore.services.pcsc.PcscReader;
import com.freerdp.freerdpcore.services.pcsc.PcscStatus;
import com.freerdp.freerdpcore.services.pcsc.atr.PcscAtr;
import com.freerdp.freerdpcore.services.pcsc.atr.PcscRawAtrParser;
import com.sun.jna.Native;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RtPcsc {
    public static PcscLibrary getLibrary() {
        return sRtPcscLibrary;
    }

    /**
     * @return List of Rutoken reader names filtered by ATR
     */
    @NonNull
    public static List<String> getRutokenReaderNames() {
        List<String> rutokenReaders = new ArrayList<>();
        Map<String, Integer> readerIndexes = new HashMap<>();
        try (PcscContext context = new PcscContext(PcscConstants.SCARD_SCOPE_USER)) {
            for (PcscReader reader : context.listReaders()) {
                try (PcscCard card = reader.connect()) {
                    PcscStatus status = card.getStatus();
                    //check ATR
                    PcscRawAtrParser atrParser = new PcscRawAtrParser(status.atr);
                    PcscAtr atr = atrParser.parse();
                    if (RtAtrHelper.isRutokenAtr(atr)) {
                        String key = new String(atr.getHistoricalBytes());
                        Integer index = readerIndexes.get(key);
                        if (null == index) {
                            index = 0;
                            readerIndexes.put(key, index);
                        } else {
                            readerIndexes.put(key, ++index);
                        }
                        rutokenReaders.add(RtAtrHelper.getHumanReadableName(atr) + " #" + String.valueOf(index));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(rutokenReaders, Collator.getInstance());
        return rutokenReaders;
    }

    private static PcscLibrary sRtPcscLibrary = Native.loadLibrary("pcsclite", PcscLibrary.class);
}
