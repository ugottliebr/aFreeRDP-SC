/*
   Copyright 2018 Aktiv Co.

   This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
   If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
*/

package com.freerdp.freerdpcore.presentation.rutoken;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.freerdp.freerdpcore.R;
import com.freerdp.freerdpcore.rutoken.RtPcsc;

import java.util.List;

public class RtListDialog extends DialogFragment {

    public static RtListDialog newInstance() {
        return new RtListDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflatedView = inflater.inflate(R.layout.rutoken_list, null);
        ListView list = inflatedView.findViewById(R.id.rutokensListView);

        List<String> names = RtPcsc.getRutokenReaderNames();
        if (!names.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.rutoken_list_item, R.id.rutokenNameTextView);
            adapter.addAll(names);
            list.setAdapter(adapter);
        } else {
            TextView messageView = inflatedView.findViewById(R.id.rutokenListMessage);
            messageView.setText(R.string.no_available_devices);
        }

        ImageView image = new ImageView(getContext());
        image.setImageResource(R.drawable.icon_menu_rutoken);
        image.setPadding(30, 30, 30, 30);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton(R.string.close, null)
                .setCustomTitle(image)
                .setView(inflatedView);

        return builder.create();
    }
}
