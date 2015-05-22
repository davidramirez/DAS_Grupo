package org.das.das_grupo;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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
import org.das.das_grupo.packGestores.GestorImagenes;
import org.das.das_grupo.packGestores.GestorUsuarios;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.net.Uri;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;


public class VerHistoriaActivity extends ActionBarActivity {

    private int id,id_us;
    private TextView autor,descipcion,etiquetas;
    private ListView list;
    private Button left,right, comentar;
    private ImageSwitcher switcher;
    private EditText comentario;

    private ArrayList<String> comentarios;
    private ArrayAdapter<String> arrayAdapter;
    private String textoCom;
    private ArrayList<String> fotos;

    private ArrayList<Uri> resources;
    private int index = 0;
    private File carpeta = new File(Environment.getExternalStorageDirectory(), ".Capturas");

    private ProgressDialog progreso;

    private TextToSpeech myTTS;
    private static final int MY_DATA_CHECK_CODE = 55;
    private static final int VR_REQUEST = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_historia);

        autor = (TextView) findViewById(R.id.autorVerH);
        descipcion = (TextView) findViewById(R.id.descripcionVerH);
        etiquetas = (TextView) findViewById(R.id.etiquetasVerH);

        list = (ListView) findViewById(R.id.listaCoVerH);

        left = (Button) findViewById(R.id.leftVerH);
        right = (Button) findViewById(R.id.rightVerH);
        comentar = (Button) findViewById(R.id.comentarBVerH);

        Bundle args = getIntent().getExtras();

        if (args != null){
            id = args.getInt("id");
        }

        progreso = new ProgressDialog(this);
        progreso.setCancelable(false);

        id_us = Integer.valueOf(GestorUsuarios.getGestorUsuarios().getIdUsuario(VerHistoriaActivity.this));


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

        comentario = (EditText) findViewById(R.id.comentarTVerH);

        if (savedInstanceState != null) {
            fotos = savedInstanceState.getStringArrayList("fotos");
            comentarios = savedInstanceState.getStringArrayList("comentarios");
            Log.i("LOAD", comentarios.toString());
            setTitle(savedInstanceState.getString("titulo"));
            autor.setText(savedInstanceState.getString("autor"));
            descipcion.setText(savedInstanceState.getString("descipcion"));
            etiquetas.setText(savedInstanceState.getString("etiquetas"));
            comentario.setText(savedInstanceState.getString("comentario"));
            loadImages();
        }
        else {
            fotos = new ArrayList<String>();
            comentarios = new ArrayList<String>();
            fillData();
        }
        resources = new ArrayList<Uri>();

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

        descipcion.setOnLongClickListener(new View.OnLongClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View view) {
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
// API 21 edo altuago
                if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    myTTS.speak(String.valueOf(descipcion.getText()), TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    myTTS.speak(String.valueOf(descipcion.getText()), TextToSpeech.QUEUE_FLUSH, null);
                }
                return false;
            }
        });

        comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textoCom = String.valueOf(comentario.getText());

                if (!textoCom.matches("")) {
                    comentarios.add(textoCom);
                    arrayAdapter.notifyDataSetChanged();
                    new AsyncTask<Void, Void, Boolean>() {
                        @Override
                        protected Boolean doInBackground(Void... voids) {
                            return GestorConexiones.getGestorConexiones().registrarComentario(id, id_us, textoCom);
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {
                            super.onPostExecute(aBoolean);
                            comentario.setText("");
                        }
                    }.execute(null, null, null);
                }
            }
        });

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

    }

    private void left() {
        if (index - 1 >= 0)
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

                        setTitle(jsonObject.getString("titulo"));
                        autor.setText(jsonObject.getString("autor") + " | " + jsonObject.getString("fecha"));
                        descipcion.setText(jsonObject.getString("descripcion"));

                        JSONArray etiq = jsonArray.getJSONArray("etiquetas");

                        etiquetas.setText("Tags: ");
                        for (int i = 0; i < etiq.length(); i++) {
                            if (etiq.length() - 1 == i)
                                etiquetas.append(etiq.getJSONObject(i).getString("nombre"));
                            else
                                etiquetas.append(etiq.getJSONObject(i).getString("nombre") + "; ");
                        }

                        JSONArray com = jsonArray.getJSONArray("comentarios");

                        for (int i = 0; i < com.length(); i++)
                            comentarios.add(com.getJSONObject(i).getString("texto"));

                        arrayAdapter.notifyDataSetChanged();

                        com = jsonArray.getJSONArray("fotos");
                        //Log.i("FOTO", com.toString());

                        for (int i = 0; i < com.length(); i++){
                            fotos.add(com.getJSONObject(i).getString("path"));
                        }

                        //Tras tener los paths de las fotos, cargarlas
                        loadImages();

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

    public void loadImages(){

        Log.i("FOTO", "Descargando imagenes");

        new AsyncTask<Void, Void, String[]>() {
            @Override
            protected String[] doInBackground(Void... voids) {
                String[] fot = {""} ;
                Log.i("FOTO",fotos.toString());
                return  GestorImagenes.getGestorImagenes().getImagen(fotos.toArray(fot));
            }

            @Override
            protected void onPostExecute(String[] strings) {
                super.onPostExecute(strings);
                Uri ur;
                for (int i = 0; i < strings.length; i++){
                    ur = Uri.fromFile(new File(strings[i]));
                    resources.add(ur);
                }
                if (resources.size() != 0)
                    switcher.setImageURI(resources.get(index));

                progreso.dismiss();


            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progreso.setMessage(getString(R.string.descargandoImagenes));
                progreso.setIndeterminate(true);
                progreso.show();
            }
        }.execute(null,null,null);
    }

    @Override
    public void onSaveInstanceState(Bundle elBundle) {
        elBundle.putStringArrayList("fotos", fotos);
        elBundle.putStringArrayList("comentarios", comentarios);
        elBundle.putString("titulo", getTitle().toString());
        elBundle.putString("autor", autor.getText().toString());
        elBundle.putString("descipcion", descipcion.getText().toString());
        elBundle.putString("etiquetas", etiquetas.getText().toString());
        elBundle.putString("comentario", comentario.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
// … OK
/*            myTTS = new TextToSpeech(this, (TextToSpeech.OnInitListener) this);
            // Como tercer parámetro, se puede pasar el nombre del
            // paquete del sintetizador que queramos usar.*/

            myTTS = new TextToSpeech(getApplicationContext(),
                    new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int initStatus) {
                            if (initStatus == TextToSpeech.SUCCESS) {
// ...
                                Locale espLocale = new Locale("es", "ES");
                                if (myTTS.isLanguageAvailable(espLocale) == TextToSpeech.LANG_AVAILABLE)
                                    myTTS.setLanguage(espLocale);
                            } else if (initStatus == TextToSpeech.ERROR) {
                                Toast.makeText(VerHistoriaActivity.this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
// Hay que instalar un motor de TTS
            Intent installTTSIntent = new Intent();
            installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(installTTSIntent);
        }

    }
}
