package org.das.das_grupo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.das.das_grupo.packGestores.GestorConexiones;
import org.das.das_grupo.packGestores.GestorUsuarios;


public class SignInActivity extends ActionBarActivity {

    private Button cancelar,registrarse;
    private EditText nombre,contrasena;
    private CheckBox avisos;

    private String nomb,contra;
    private boolean avis = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        cancelar = (Button) findViewById(R.id.btCancelarSing);
        registrarse = (Button) findViewById(R.id.btSingInSing);

        nombre =(EditText) findViewById(R.id.usuarioSing);
        contrasena = (EditText) findViewById(R.id.contrasenaSing);

        avisos = (CheckBox) findViewById(R.id.checkAvisosSing);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avis = avisos.isChecked();
                nomb = String.valueOf(nombre.getText());
                contra = String.valueOf(contrasena.getText());

                if(nomb.matches("") || contra.matches(""))
                {
                    Toast.makeText(SignInActivity.this,getString(R.string.camposNoInformados), Toast.LENGTH_SHORT).show();
                }
                else {


                    new AsyncTask<Void, Void, Boolean>() {
                        @Override
                        protected Boolean doInBackground(Void... params) {
                            return GestorConexiones.getGestorConexiones().SingInUser(nomb, contra);
                        }

                        @Override
                        protected void onPostExecute(Boolean id) {
                            super.onPostExecute(id);

                            if (id == true) {
                                finish();
                            } else {
                                Toast.makeText(SignInActivity.this, getString(R.string.errorReg), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute(null, null, null);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_regist, menu);
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
