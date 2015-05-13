package org.das.das_grupo;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.das.das_grupo.packGestores.GestorConexiones;
import org.das.das_grupo.packGestores.GestorUsuarios;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PreferenciasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PreferenciasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreferenciasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button cambiar;
    private EditText contraV, contraN1, contraN2;
    private CheckBox avisos;

    private String contv,contn1,contn2;
    private boolean avis = false;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreferenciasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PreferenciasFragment newInstance(String param1, String param2) {
        PreferenciasFragment fragment = new PreferenciasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PreferenciasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cambiar = (Button) getActivity().findViewById(R.id.cambiarPref);
        contraV = (EditText) getActivity().findViewById(R.id.contraAntPref);
        contraN1 = (EditText) getActivity().findViewById(R.id.contraNuev1Pref);
        contraN2 = (EditText) getActivity().findViewById(R.id.contraNueva2Pref);
        avisos = (CheckBox) getActivity().findViewById(R.id.avisosPref);

        cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contv = String.valueOf(contraV.getText());
                contn1 = String.valueOf(contraN1.getText());
                contn2 = String.valueOf(contraN2.getText());
                avis = avisos.isChecked();

                if (contv.matches("") || contn1.matches("") || contn2.matches("")) {
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.camposincorrectos), Toast.LENGTH_SHORT).show();
                } else {
                    if (contn1.equals(contn2) && contv.equals(GestorUsuarios.getGestorUsuarios().getContrasenaUsuario(getActivity().getApplicationContext()))) {
                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Void... params) {
                                return GestorConexiones.getGestorConexiones().updateUser(GestorUsuarios.getGestorUsuarios().getIdUsuario(getActivity().getApplication()),
                                        GestorUsuarios.getGestorUsuarios().getGcmIdUsuario(getActivity().getApplication()),contn1 ,contv, avis);
                            }

                            @Override
                            protected void onPostExecute(Boolean id) {
                                super.onPostExecute(id);

                                if (id == false) {
                                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.errorReg), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.execute(null, null, null);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.contrasenasNoCoinciden), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preferencias, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
