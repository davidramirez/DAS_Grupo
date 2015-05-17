package org.das.das_grupo;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.das.das_grupo.packGestores.GestorConexiones;
import org.das.das_grupo.packGestores.GestorUsuarios;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.net.Uri;
import android.widget.ViewSwitcher;

import java.io.File;
import java.util.ArrayList;


public class VerHistoriaActivity extends ActionBarActivity {

    private int id,id_us;
    private TextView titulo,autor,descipcion,etiquetas;
    private ListView list;
    private Button left,right, comentar;
    private ImageSwitcher switcher;
    private EditText comentario;

    private ArrayList<String> comentarios;
    private ArrayAdapter<String> arrayAdapter;
    private String textoCom;

    private ArrayList<Uri> resources;
    private int index = 0;
    private File carpeta = new File(Environment.getExternalStorageDirectory(), ".Capturas");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_historia);

        Bundle args = getIntent().getExtras();

        if (args != null){
            id = args.getInt("id");
        }

        id_us = Integer.valueOf(GestorUsuarios.getGestorUsuarios().getIdUsuario(VerHistoriaActivity.this));

        titulo = (TextView) findViewById(R.id.tituloVerH);
        autor = (TextView) findViewById(R.id.autorVerH);
        descipcion = (TextView) findViewById(R.id.descripcionVerH);
        etiquetas = (TextView) findViewById(R.id.etiquetasVerH);

        list = (ListView) findViewById(R.id.listaCoVerH);

        left = (Button) findViewById(R.id.leftVerH);
        right = (Button) findViewById(R.id.rightVerH);
        comentar = (Button) findViewById(R.id.comentarBVerH);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                left();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                right();
            }
        });
        switcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);

        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.
                        FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT));
                return myView;
            }
        });

        if (carpeta.exists()) {
            resources = new ArrayList<>();
            resources.add(Uri.fromFile(new File(carpeta, "001.png")));
            resources.add(Uri.fromFile(new File(carpeta, "002.png")));
            resources.add(Uri.fromFile(new File(carpeta, "003.png")));


            switcher.setImageURI(resources.get(0));
        }
        comentario = (EditText) findViewById(R.id.comentarTVerH);



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

        comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textoCom = String.valueOf(comentario.getText());

                if (!textoCom.matches(""))
                {
                    comentarios.add(textoCom);
                    arrayAdapter.notifyDataSetChanged();
                    new AsyncTask<Void, Void, Boolean>() {
                        @Override
                        protected Boolean doInBackground(Void... voids) {
                            return GestorConexiones.getGestorConexiones().registrarComentario(id,id_us,textoCom);
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
                            super.onPostExecute(aBoolean);
                            comentario.setText("");
                        }
                    }.execute(null,null,null);}
            }
        });

    }

    private void left() {
        if (index - 1 <= 0)
            switcher.setImageURI(resources.get(--index));
    }

    private void right() {
        String resource;

        if (index + 1 < resources.size() )
        switcher.setImageURI(resources.get(++index));
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
