package org.das.das_grupo.packDialogos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.app.Dialog;

import org.das.das_grupo.R;

/**
 * Created by David on 09/05/2015.
 */
public class SelectorFuenteFotoDialog extends DialogFragment{


    public static int FUENTE_CAMARA = 0;
    public static int FUENTE_GALERIA = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder eldialogo = new AlertDialog.Builder(getActivity());
        eldialogo.setTitle(getString(R.string.seleccionfuentefoto));
        final CharSequence[] opciones = {getString(R.string.camara), getString(R.string.galeria)};
        eldialogo.setItems(opciones, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                ellistener.procesarFuente(item);
            }
        });

        return eldialogo.create();
    }

    public interface ListenerFuenteFoto{
        public void procesarFuente(int fuente);
    }

    private ListenerFuenteFoto ellistener;

    public void onAttach(Activity activity)
    {
        try{
            ellistener = (ListenerFuenteFoto) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "debe implementar ListenerFuenteFoto");
        }

    }
}
