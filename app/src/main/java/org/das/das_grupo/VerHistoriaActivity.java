package org.das.das_grupo;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ListView;
import android.widget.TextView;

import org.das.das_grupo.packGestores.GestorConexiones;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class VerHistoriaActivity extends ActionBarActivity {

    private int id;
    private TextView titulo,autor,descipcion,etiquetas;
    private ListView list;
    private Button left,right, comentar;
    private ImageSwitcher switcher;
    private EditText comentario;

    private ArrayList<String> comentarios;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_historia);

        Bundle args = getIntent().getExtras();

        if (args != null){
            id = args.getInt("id");
        }

        titulo = (TextView) findViewById(R.id.tituloVerH);
        autor = (TextView) findViewById(R.id.autorVerH);
        descipcion = (TextView) findViewById(R.id.descripcionVerH);
        etiquetas = (TextView) findViewById(R.id.etiquetasVerH);

        list = (ListView) findViewById(R.id.listaCoVerH);

        left = (Button) findViewById(R.id.leftVerH);
        right = (Button) findViewById(R.id.rightVerH);
        comentar = (Button) findViewById(R.id.comentarBVerH);

        comentario = (EditText) findViewById(R.id.comentarTVerH);

        switcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);

        comentarios = new ArrayList<String>();

        arrayAdapter = new ArrayAdapter<String>(VerHistoriaActivity.this,android.R.layout.simple_list_item_1,comentarios){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view=super.getView(position, convertView, parent);
                TextView text= (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };

        list.setAdapter(arrayAdapter);

        fillData();

    }

    private void fillData() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                return GestorConexiones.getGestorConexiones().getHistoria(id);
            }

            @Override
            protected void onPostExecute(String stringJson) {
                super.onPostExecute(stringJson);
                JSONObject jsonArray = null;
                JSONObject jsonObject = null;
                if (!stringJson.equals("null")){

                    try {
                        jsonArray = new JSONObject(stringJson);

                        jsonObject = jsonArray.getJSONObject("0");

                        titulo.append(jsonObject.getString("titulo"));
                        autor.append(jsonObject.getString("autor"));
                        descipcion.setText(jsonObject.getString("descripcion"));

                        JSONArray etiq = jsonArray.getJSONArray("etiquetas");


                        for (int i = 0; i < etiq.length(); i++)
                            etiquetas.append(etiq.getJSONObject(i).getString("nombre") + "; ");

                        JSONArray com = jsonArray.getJSONArray("comentarios");

                        for (int i = 0; i < com.length(); i++)
                            comentarios.add(com.getJSONObject(i).getString("texto"));

                        arrayAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.execute(null,null,null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ver_historia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
