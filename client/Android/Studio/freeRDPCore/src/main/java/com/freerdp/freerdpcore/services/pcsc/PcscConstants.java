/*
   Copyright 2018 Aktiv Co.

   This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
   If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package com.freerdp.freerdpcore.services.pcsc;

public class PcscConstants {
    public static final int SCARD_AUTOALLOCATE = -1;

    public static final int SCARD_SCOPE_USER = 0;
    public static final int SCARD_SCOPE_SYSTEM = 2;

    public static final int SCARD_PROTOCOL_UNDEFINED = 0x00000000;
    public static final int SCARD_PROTOCOL_T0 = 0x00000001;
    public static final int SCARD_PROTOCOL_T1 = 0x00000002;
    public static final int SCARD_PROTOCOL_ANY = SCARD_PROTOCOL_T0 | SCARD_PROTOCOL_T1;

    public static final int SCARD_SHARE_EXCLUSIVE = 1;
    public static final int SCARD_SHARE_SHARED = 2;
    public static final int SCARD_SHARE_DIRECT = 3;

    // dispositions
    public static final int SCARD_LEAVE_CARD = 0;
    public static final int SCARD_RESET_CARD = 1;
    public static final int SCARD_UNPOWER_CARD = 2;
    public static final int SCARD_EJECT_CARD = 3;

    public static final int SCARD_UNKNOWN = 1;
    public static final int SCARD_ABSENT = 2;
    public static final int SCARD_PRESENT = 3;
    public static final int SCARD_SWALLOWED = 4;
    public static final int SCARD_POWERED = 5;
    public static final int SCARD_NEGOTIABLE = 6;
    public static final int SCARD_SPECIFIC = 7;

    public static final int SCARD_STATE_UNAWARE = 0x00000000;
    public static final int SCARD_STATE_IGNORE = 0x00000001;
    public static final int SCARD_STATE_CHANGED = 0x00000002;
    public static final int SCARD_STATE_UNKNOWN = 0x00000004;
    public static final int SCARD_STATE_UNAVAILABLE = 0x00000008;
    public static final int SCARD_STATE_EMPTY = 0x00000010;
    public static final int SCARD_STATE_PRESENT = 0x00000020;
    public static final int SCARD_STATE_ATRMATCH = 0x00000040;
    public static final int SCARD_STATE_EXCLUSIVE = 0x00000080;
    public static final int SCARD_STATE_INUSE = 0x00000100;
    public static final int SCARD_STATE_MUTE = 0x00000200;
    public static final int SCARD_STATE_UNPOWERED = 0x00000400;

    public static final int SCARD_ATTR_ATR_STRING = 0x00090303;
}
