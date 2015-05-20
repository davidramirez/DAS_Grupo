package org.das.das_grupo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.das.das_grupo.packDialogos.AlertaCampoIncorrectoDialog;
import org.das.das_grupo.packDialogos.NoFotosDialog;
import org.das.das_grupo.packDialogos.SelectorFuenteFotoDialog;
import org.das.das_grupo.packGestores.GestorConexiones;
import org.das.das_grupo.packGestores.GestorUsuarios;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class NuevaHistoriaActivity extends ActionBarActivity implements SelectorFuenteFotoDialog.ListenerFuenteFoto {

    private Button addfoto;
    private Button addhistoria;

    private static final int CAMERA_REQUEST = 1888;
    private static final int RESULT_LOAD_IMG = 1;


    private TextView titulo;
    private TextView descripcion;
    private TextView etiquetas;

    //Paths de las fotos elegidas, guardadas todas en un path de la aplicacion
    private ArrayList<String> fotos;

    //fotos codificadas
    private String[] encodedImages;


    //Dialogo de progreso
    ProgressDialog progreso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_historia);

        titulo = (TextView) findViewById(R.id.titulo);
        descripcion = (TextView) findViewById(R.id.descripcion);
        etiquetas = (TextView) findViewById(R.id.etiquetas);

        //Cargar estado anterior si habia
        if(savedInstanceState != null){
            titulo.setText(savedInstanceState.getString("titulo"));
            descripcion.setText(savedInstanceState.getString("descripcion"));
            etiquetas.setText(savedInstanceState.getString("etiquetas"));

            fotos = savedInstanceState.getStringArrayList("fotos");
        }
        else{
            fotos = new ArrayList<String>();
        }
        progreso = new ProgressDialog(this);
        progreso.setCancelable(false);


        addfoto = (Button) findViewById(R.id.addimagen);
        addfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectorFuenteFotoDialog dialogo = new SelectorFuenteFotoDialog();
                dialogo.onAttach(NuevaHistoriaActivity.this);
                dialogo.show(getSupportFragmentManager(), null);
            }
        });

        addhistoria = (Button) findViewById(R.id.crear);
        addhistoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tit = titulo.getText().toString();
                String desc = descripcion.getText().toString();
                String[] etiq = descripcion.getText().toString().split(",");

                //si alguno de los campos obligatorios esta vacio
                if(tit.equals("") || desc.equals(""))
                {
                    AlertaCampoIncorrectoDialog dialogo = new AlertaCampoIncorrectoDialog();
                    dialogo.show(getSupportFragmentManager(), null);
                }
                else if (fotos.size() == 0)
                {
                    //Si no hay fotos seleccionadas
                    NoFotosDialog diag = new NoFotosDialog();
                    diag.show(getSupportFragmentManager(), null);
                }
                else
                {
                    //Preparar los parametros la peticion
                    RequestParams params = new RequestParams();
                    params.put("id", GestorUsuarios.getGestorUsuarios().getIdUsuario(getApplicationContext()));
                    params.put("titulo", tit);
                    params.put("descripcion", desc);
                    if(etiq.length != 0)
                        params.put("etiquetas", etiq);
                    setStringArrayEncodedImages();
                    params.put("fotos", encodedImages);

                    //Hacer la peticion
                    subirHistoria(params);
                }
            }
        });

    }

    private void setStringArrayEncodedImages() {

        progreso.setMessage(getString(R.string.preparandoImagenes));
        progreso.setMax(fotos.size());
        progreso.show();
        Log.i("FOTO", "Codificando imagenes");

        AsyncTask tarea = new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                encodedImages = new String[fotos.size()];

                for(int i = 0; i < fotos.size(); i++)
                {
                    progreso.setProgress(i+1);

                    Bitmap bitmap = BitmapFactory.decodeFile(fotos.get(i), options);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // Must compress the Image to reduce image size to make upload easy
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    byte[] byte_arr = stream.toByteArray();
                    // Encode Image to String
                    encodedImages[i] = Base64.encodeToString(byte_arr, 0);
                    Log.i("FOTO", encodedImages[i]);
                }

                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                progreso.setMessage(getString(R.string.subiendoHistoria));
                progreso.setIndeterminate(true);
            }
        }.execute(null, null, null);

        try {
            tarea.get();
            Log.i("FOTO", "tarea lista");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nueva_historia, menu);
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

    private File imagen;

    @Override
    public void procesarFuente(int fuente) {
        if (fuente == SelectorFuenteFotoDialog.FUENTE_CAMARA)
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if(cameraIntent.resolveActivity(getPackageManager()) != null) {
                //Guardar la imagen de la camara en un directorio propio
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                 imagen = null;
                try {
                    imagen = File.createTempFile(
                            imageFileName,  /* prefix */
                            ".jpg",         /* suffix */
                            storageDir      /* directory */
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (imagen != null)
                {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
                //TODO else error foto

            }
            else
                Toast.makeText(getApplicationContext(),getString(R.string.errorCamara), Toast.LENGTH_LONG).show();
        }
        else if (fuente == SelectorFuenteFotoDialog.FUENTE_GALERIA)
        {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if(galleryIntent.resolveActivity(getPackageManager()) != null)
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            else
                Toast.makeText(getApplicationContext(),getString(R.string.errorGaleria), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("FOTO", ""+requestCode);
        Log.i("FOTO", ""+resultCode);
        //Si la fuente era la camara
        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            //TODO comprimir imagen
            //Añadir la ruta del archivo a fotos
            fotos.add(imagen.getAbsolutePath());
            //Y volver a mostrar las imagenes
            showImages();
        }
        //Si la fuente era la galería
        else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {
            Uri imagenElegida = data.getData();
            InputStream is = null;
            try {
                is = getContentResolver().openInputStream(imagenElegida);
                Bitmap bitmap = BitmapFactory.decodeStream(is);

                //Guardar la imagen de la galeria en un directorio propio
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    imagen = File.createTempFile(
                            imageFileName,  /* prefix */
                            ".jpg",         /* suffix */
                            storageDir      /* directory */
                    );
                //TODO Comprimir imagen
                FileOutputStream out = new FileOutputStream(imagen);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);

                fotos.add(imagen.getAbsolutePath());
                showImages();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else
            Toast.makeText(getApplicationContext(), getString(R.string.imagenResError), Toast.LENGTH_LONG).show();
    }

    private void showImages() {
        Log.i("FOTO","Ahora se deberían enseñar las imagenes");
        Log.i("FOTO", fotos.toString());
    }


    public void subirHistoria(RequestParams parametros)
    {
        AsyncHttpClient cliente = new AsyncHttpClient();
        Log.i("FOTO", "Subiendo historia al servidor");
        cliente.post(GestorConexiones.WEB_SERVER_URL + "/subir_historia.php", parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] responseBody) {
                progreso.cancel();
                Log.i("FOTO", "exito subida, codigo " + i);
                try {
                    String respuesta = new String(responseBody, "UTF-8");
                    Log.i("FOTO", "respuesta: " + respuesta);

                    if (respuesta.equalsIgnoreCase("true")) {
                        Toast.makeText(getApplicationContext(), getString(R.string.exitoHistoria), Toast.LENGTH_LONG).show();
                        finalizarEdicion();
                    } else
                        Toast.makeText(getApplicationContext(), getString(R.string.errorHistoria), Toast.LENGTH_LONG).show();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] responseBody, Throwable throwable) {
                Toast.makeText(getApplicationContext(), getString(R.string.errorHistoria), Toast.LENGTH_LONG).show();
                progreso.cancel();
                Log.i("FOTO", "fallo subida, codigo " + i);
                try {
                    String respuesta = new String(responseBody, "UTF-8");
                    Log.i("FOTO", "respuesta del servidor: " + respuesta);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void finalizarEdicion() {
        this.finish();
    }

    @Override
    public void onSaveInstanceState(Bundle elBundle){

        elBundle.putString("titulo", titulo.getText().toString());
        elBundle.putString("descripcion", descripcion.getText().toString());
        elBundle.putString("etiquetas", etiquetas.getText().toString());

        elBundle.putStringArrayList("fotos", fotos);
    }
}
