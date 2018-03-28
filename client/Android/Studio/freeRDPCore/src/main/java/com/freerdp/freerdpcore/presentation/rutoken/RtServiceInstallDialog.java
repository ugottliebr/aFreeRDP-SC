/*
   Copyright 2018 Aktiv Co.

   This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
   If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package com.freerdp.freerdpcore.presentation.rutoken;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.freerdp.freerdpcore.R;

import java.security.InvalidParameterException;

public class RtServiceInstallDialog extends DialogFragment {
    /**
     * Parent activity have to implement this interface
     */
    public interface ActionHandlerAccess {
        ActionHandler getRtServiceInstallDialogActionHandler(@NonNull RtServiceInstallDialog dialog);
    }
    /**
     * Callback interface, triggered by this dialog
     */
    public interface ActionHandler {
        void onPositiveButtonClick();
        void onNeutralButtonClick();
        void onNegativeButtonClick();
    }

    public static final String PARAM_SESSION_INTENT = "sessionIntent";
    public static final String PARAM_DIALOG_TYPE = "dialogType";

    public enum DialogType {
        INSTALL(0, R.string.msg_install_rutoken_service, R.string.install, R.string.ok_later, 0),
        INSTALL_ON_CONNECT(1, R.string.msg_install_rutoken_service_on_connect, R.string.install, R.string.ok_later, R.string.dont_redirect_smart_cards);

        DialogType(int value, @StringRes int msg,
                   @StringRes int positiveButton,
                   @StringRes int neutralButton,
                   @StringRes int negativeButton) {
            mValue = value;
            mMessage = msg;
            mPositiveButton = positiveButton;
            mNeutralButton = neutralButton;
            mNegativeButton = negativeButton;
        }

        @NonNull
        public static DialogType fromValue(int value) {
            for (DialogType type : DialogType.values()) {
                if (type.getValue() == value)
                    return type;
            }
            throw new InvalidParameterException();
        }

        public int getValue() {
            return mValue;
        }

        public int getMessage() {
            return mMessage;
        }

        public int getPositiveButton() {
            return mPositiveButton;
        }

        public int getNegativeButton() {
            return mNegativeButton;
        }

        public int getNeutralButton() {
            return mNeutralButton;
        }

        private final int mValue;
        @StringRes
        private final int mMessage;
        @StringRes
        private final int mPositiveButton;
        @StringRes
        private final int mNeutralButton;
        @StringRes
        private final int mNegativeButton;
    }

    @NonNull
    public static RtServiceInstallDialog newInstance(@NonNull DialogType type) {
        Bundle args = new Bundle();
        return newInstance(type, args);
    }

    public static RtServiceInstallDialog newInstance(@NonNull DialogType type, @NonNull Bundle bundle) {
        RtServiceInstallDialog dialog = new RtServiceInstallDialog();
        bundle.putInt(PARAM_DIALOG_TYPE, type.getValue());
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onDetach() {
        mActionHandler = null;// protect from resources leak
        super.onDetach();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setActionHandler(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        DialogType type = DialogType.fromValue(getArguments().getInt(PARAM_DIALOG_TYPE));

        builder.setMessage(type.getMessage());
        if (null != mActionHandler) {
            if (0 != type.getPositiveButton()) {
                builder.setPositiveButton(type.getPositiveButton(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActionHandler.onPositiveButtonClick();
                    }
                });
            }
            if (0 != type.getNeutralButton()) {
                builder.setNeutralButton(type.getNeutralButton(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActionHandler.onNeutralButtonClick();
                    }
                });
            }
            if (0 != type.getNegativeButton()) {
                builder.setNegativeButton(type.getNegativeButton(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActionHandler.onNegativeButtonClick();
                    }
                });
            }
            setCancelable(false);
        }
        return builder.create();
    }

    private void setActionHandler(Context context) {
        try {
            mActionHandler = ((ActionHandlerAccess) context).getRtServiceInstallDialogActionHandler(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement " + ActionHandlerAccess.class.getName());
        }
    }

    @Nullable
    ActionHandler mActionHandler;// may hold reference to activity, so we should control it's lifetime
}
