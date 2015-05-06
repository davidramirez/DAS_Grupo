package org.das.das_grupo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.das.das_grupo.packGestores.GestorConexiones;
import org.das.das_grupo.packGestores.GestorUsuarios;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;


public class LoginActivity extends ActionBarActivity {

    private Button btLogin,btRegistrase;
    private EditText usuario,contrasena;

    private String nombre,contra;
    Boolean ok = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btLogin = (Button) findViewById(R.id.btLogInLog);
        btRegistrase = (Button) findViewById(R.id.btRegistrarseLog);

        usuario = (EditText) findViewById(R.id.usuarioLog);
        contrasena = (EditText) findViewById(R.id.contrasenaLog);


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = String.valueOf(usuario.getText());
                contra = String.valueOf(contrasena.getText());

                new AsyncTask<Void, Void, Integer>() {
                    @Override
                    protected Integer doInBackground(Void... params) {
                        return GestorConexiones.getGestorConexiones().logInUser(nombre,contra);
                    }

                    @Override
                    protected void onPostExecute(Integer id) {
                        super.onPostExecute(id);

                        if(id != 0)
                        {
                            GestorUsuarios.getGestorUsuarios().guardarContrasenaUsuario(LoginActivity.this,contra);
                            GestorUsuarios.getGestorUsuarios().guardarNombreUsuario(LoginActivity.this, nombre);
                            GestorUsuarios.getGestorUsuarios().guardarIdUsuario(LoginActivity.this,id.toString());
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,getString(R.string.errorLogin),Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute(null,null,null);

            }
        });

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
}
