package org.das.das_grupo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.das.das_grupo.packDialogos.SelectorFuenteFotoDialog;
import org.das.das_grupo.packGestores.GestorConexiones;


public class NuevaHistoriaActivity extends ActionBarActivity implements SelectorFuenteFotoDialog.ListenerFuenteFoto {

    Button addfoto;
    Button addhistoria;

    private static final int CAMERA_REQUEST = 1888;
    private static final int RESULT_LOAD_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_historia);

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

            }
        });

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

    @Override
    public void procesarFuente(int fuente) {
        if (fuente == SelectorFuenteFotoDialog.FUENTE_CAMARA)
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if(cameraIntent.resolveActivity(getPackageManager()) != null)
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
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
    }


    public void subirHistoria(RequestParams parametros)
    {
        AsyncHttpClient cliente = new AsyncHttpClient();

        cliente.post(GestorConexiones.WEB_SERVER_URL + "/", parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Toast.makeText(getApplicationContext(),getString(R.string.exitoHistoria), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(getApplicationContext(),getString(R.string.errorHistoria), Toast.LENGTH_LONG).show();
            }
        });
    }
}
