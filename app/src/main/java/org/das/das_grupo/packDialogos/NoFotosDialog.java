package org.das.das_grupo.packDialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import org.das.das_grupo.R;

/**
 * Created by David on 09/05/2015.
 */
public class NoFotosDialog extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(getString(R.string.nofotos))
                .setTitle(getString(R.string.ups))
                .setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}