package org.das.das_grupo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.das.das_grupo.packGestores.GestorConexiones;
import org.das.das_grupo.packGestores.GestorUsuarios;
import org.xml.sax.helpers.LocatorImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class LoginActivity extends ActionBarActivity {

    private String SENDER_ID = "1042125637037";
    private GoogleCloudMessaging gcm = null;

    private Button btLogin,btRegistrase;
    private EditText usuario,contrasena;

    private String nombre,contra,regid;
    private int id;
    Boolean ok = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(GestorUsuarios.getGestorUsuarios().getNombreUsuario(LoginActivity.this) != null &&
                GestorUsuarios.getGestorUsuarios().getContrasenaUsuario(LoginActivity.this) != null)
        {
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }

        btLogin = (Button) findViewById(R.id.btLogInLog);
        btRegistrase = (Button) findViewById(R.id.btRegistrarseLog);

        usuario = (EditText) findViewById(R.id.usuarioLog);
        contrasena = (EditText) findViewById(R.id.contrasenaLog);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = String.valueOf(usuario.getText());
                contra = String.valueOf(contrasena.getText());

                if (nombre.matches("")) {
                    Toast.makeText(LoginActivity.this,getString(R.string.nombre_vacio),Toast.LENGTH_SHORT).show();
                } else if (contra.matches("")) {
                    Toast.makeText(LoginActivity.this,getString(R.string.pass_vacia),Toast.LENGTH_SHORT).show();
                } else {
                    new AsyncTask<Void, Void, Integer>() {
                        @Override
                        protected Integer doInBackground(Void... params) {
                            getGCM();

                            id = GestorConexiones.getGestorConexiones().logInUser(nombre, contra);
                            GestorConexiones.getGestorConexiones().singInGCM(id, contra, regid);
                            return id;
                        }

                        @Override
                        protected void onPostExecute(Integer id) {
                            super.onPostExecute(id);

                            if (id != 0) {
                                GestorUsuarios.getGestorUsuarios().guardarContrasenaUsuario(LoginActivity.this, contra);
                                GestorUsuarios.getGestorUsuarios().guardarNombreUsuario(LoginActivity.this, nombre);
                                GestorUsuarios.getGestorUsuarios().guardarIdUsuario(LoginActivity.this, id.toString());
                                GestorUsuarios.getGestorUsuarios().guardarGcmIdUsuario(LoginActivity.this, regid);
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();

                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.errorLogin), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute(null, null, null);
                }
            }
        });

        btRegistrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignInActivity.class);
                startActivity(i);
            }
        });

    }

    private void getGCM() {

        if (gcm == null) {
            gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
        }
        try {
            Log.i("GCM", "obteniendo nuevo ID");
            regid = gcm.register(SENDER_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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



    private String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line;
        String result = "";
        try {
            while((line = bufferedReader.readLine())!= null)
                result += line;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Close Stream
        if(null!=inputStream){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("nombre",usuario.getText().toString());
        savedInstanceState.putString("contrasena",contrasena.getText().toString());
        savedInstanceState.putString("regid",regid);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getString("nombre")!=null)
        {
            nombre = savedInstanceState.getString("nombre");
            usuario.setText(nombre);
        }

        if (savedInstanceState.getString("contrasena")!=null)
        {
            contra = savedInstanceState.getString("contrasena");
            contrasena.setText(contra);
        }

        if (savedInstanceState.getString("regid")!=null)
        {
            regid = savedInstanceState.getString("regid");

        }
    }
}
