package org.das.das_grupo.packDialogos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import org.das.das_grupo.R;

/**
 * Created by David on 09/05/2015.
 */
public class CerrarSesionDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(getString(R.string.cerrarSesion))
                .setTitle(getString(R.string.aviso))
                .setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(ellistener != null)
                            ellistener.cerrarSesion();
                    }
                })
                .setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        return builder.create();
    }


    public interface ListenerCierreSesion{
        public void cerrarSesion();
    }

    private ListenerCierreSesion ellistener;

    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try{
            ellistener = (ListenerCierreSesion) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "debe implementar ListenerCierreSesion");
        }

    }
}
