package org.das.das_grupo;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.das.das_grupo.packGestores.GestorConexiones;
import org.das.das_grupo.packListasMod.historia;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListarEtiquetasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ListarEtiquetasFragment extends Fragment {

    private ListView listview;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<Etiqueta> etiquetas;
    private ArrayList<String> display;

    private OnFragmentInteractionListener mListener;

    public ListarEtiquetasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listview = (ListView) getActivity().findViewById(R.id.listView);
        display = new ArrayList<String>();

        fillData();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listar_etiquetas, container, false);
    }

    private void fillDisplay() {

    if (etiquetas.size() != 0) {
        for (Etiqueta aux : etiquetas) {
            display.add(aux.getNombre() + "\t(" + aux.getCantidad() + ")");
        }
    }

        arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,display){
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=super.getView(position, convertView, parent);
            TextView text= (TextView) view.findViewById(android.R.id.text1);
            text.setTextColor(Color.BLACK);
            return view;
        }
    };
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mListener.onEtiquetaSelected(etiquetas.get(i).getId());
            }
        });
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
        public void onEtiquetaSelected(int id);
    }

    private void fillData() {
        etiquetas = new ArrayList<Etiqueta>();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return GestorConexiones.getGestorConexiones().listarEtiquetas();
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

                    JSONObject aux = null;
                    for (int i = 0; i < json.length(); i++) {
                        try {
                            aux = json.getJSONObject(i);
                            etiquetas.add(new Etiqueta(aux.getInt("id"), aux.getString("nombre"),
                                    aux.getInt("cantidad")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        fillDisplay();
                        arrayAdapter.notifyDataSetChanged();
                    }

                }
            }
        }.execute(null,null,null);
    }


}
