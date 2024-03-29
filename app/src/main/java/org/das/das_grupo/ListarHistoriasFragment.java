package org.das.das_grupo;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.das.das_grupo.packGestores.GestorConexiones;
import org.das.das_grupo.packGestores.GestorUsuarios;
import org.das.das_grupo.packListasMod.ListAdapter;
import org.das.das_grupo.packListasMod.historia;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListarHistoriasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListarHistoriasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListarHistoriasFragment extends  android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<historia> products;
    private ListAdapter boxAdapter;
    private int seleccion = 0;
    private int id = 0;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListarHistoriasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListarHistoriasFragment newInstance(String param1, String param2) {
        ListarHistoriasFragment fragment = new ListarHistoriasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ListarHistoriasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            seleccion = getArguments().getInt("opcion");
            id = getArguments().getInt("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_listar_historias, container, false);

    }

    private void fillData() {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String resultado = "";
                switch (seleccion){
                    case 0://Ultima historia
                        resultado = GestorConexiones.getGestorConexiones().listarHistorias("ultimas", id);
                    break;
                    case 1://Mis historias
                        resultado = GestorConexiones.getGestorConexiones().listarHistorias("usuario", Integer.parseInt(GestorUsuarios.getGestorUsuarios().getIdUsuario(getActivity().getApplicationContext())));
                    break;
                    case 2://Mejores
                        resultado = GestorConexiones.getGestorConexiones().listarHistorias("ultimas", id);
                    break;
                    case 3://etiquetas
                        resultado = GestorConexiones.getGestorConexiones().listarHistorias("etiqueta", id);
                        break;
                    default:
                        resultado = GestorConexiones.getGestorConexiones().listarHistorias("ultimas", 0);
                    break;
                }
                return resultado;

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (!s.equals("null")){
                    JSONArray json = null;
                    try {
                        json = new JSONArray(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    historia hist = null;
                    JSONObject aux = null;

                    ListView lvMain = (ListView) getView().findViewById(R.id.lvMain);// (ListView) getView().findViewById(R.id.lvMain);

                    if (json.length() != 0){
                    for (int i = 0; i < json.length(); i++) {
                        try {
                            aux = json.getJSONObject(i);
                            //        long time =java.util.Date.parse(aux.getString("fecha"));
                            //dat.

                            hist = new historia(aux.getInt("id"), aux.getString("titulo"),
                                    aux.getString("usuario"),new Date(System.currentTimeMillis()));
                            products.add(hist);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    boxAdapter = new ListAdapter(products,getActivity().getApplicationContext());


                    lvMain.setAdapter(boxAdapter);

                    lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mListener.onHistoriaSelected(products.get(position).id);
                        }
                    });}
                    else{
                        ArrayList<String> histo = new ArrayList<String>();
                        histo.add("No hay historias disponibles");
                        ArrayAdapter<String>arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,histo){
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view=super.getView(position, convertView, parent);
                                TextView text= (TextView) view.findViewById(android.R.id.text1);
                                text.setTextColor(Color.BLACK);
                                return view;
                            }
                        };
                        lvMain.setAdapter(arrayAdapter);
                    }
                }
            }
        }.execute(null,null,null);
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        products = new ArrayList<historia>();
        fillData();

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
        public void onHistoriaSelected(int id);
    }



}
