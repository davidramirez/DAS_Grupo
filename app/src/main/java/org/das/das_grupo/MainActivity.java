package org.das.das_grupo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.das.das_grupo.packGestores.GestorImagenes;
import org.das.das_grupo.packGestores.GestorUsuarios;


public class MainActivity extends ActionBarActivity {

    private ListView lalista;
    private DrawerLayout ellayout;
    private String[] opciones;
    private Fragment elfragmento = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (GestorUsuarios.getGestorUsuarios().getNombreUsuario(MainActivity.this) == null)
        {
            cerrarSesion();
        }

        opciones = getResources ().getStringArray(R.array.nav_options);

        this.lalista = (ListView) findViewById(R.id.elmenu);
        // Load an array of options names
        final
// Set previous array as adapter of the list
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, opciones);
        lalista.setAdapter(adapter);
        ellayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        lalista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                elfragmento = null;
                Bundle args = new Bundle();
                switch (position) {
                    case 0://Ultima Historia
                        elfragmento = new ??();
                        break;
                    case 1://Mis Historias
                        elfragmento = new ??();

                        break;
                    case 2://Por etiquetas
                        elfragmento = new ??();

                        break;
                    case 3://Mejores
                        elfragmento = new ??();

                        break;

                    case 4://Preferencias
                        elfragmento = new ??();

                        break;
                    case 5://Cerrar Sesion
                       cerrarSesion();

                }
                elfragmento.setArguments(args);
                FragmentManager elgestorfragmentos = getSupportFragmentManager();

                elgestorfragmentos.beginTransaction().replace(R.id.contenido, elfragmento).commit();
                lalista.setItemChecked(position, true);
                String tituloseccion = opciones[position];
                getSupportActionBar().setTitle(tituloseccion);
                ellayout.closeDrawer(lalista);
            }




        });
    }

    private void cerrarSesion() {
        GestorUsuarios.getGestorUsuarios().borrarContrasenaUsuario(MainActivity.this);
        GestorUsuarios.getGestorUsuarios().borrarGcmIdUsuario(MainActivity.this);
        GestorUsuarios.getGestorUsuarios().borrarNombreUsuario(MainActivity.this);
        GestorUsuarios.getGestorUsuarios().borrarIdUsuario(MainActivity.this);

        Intent i = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //Si hemos lanzado algun fragmento guardamos
        //el tag del fragmento que esta en pantallla
        if (elfragmento != null)
        {   String fragName = elfragmento.getTag();
            savedInstanceState.putString("TAG", fragName);
            if (getActionBar() != null)
                savedInstanceState.putCharSequence("titulo", getActionBar().getTitle());
            else
                savedInstanceState.putCharSequence("titulo", "");
        }
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Recuperamos el tag del fragmentos que
        //estaba en pantalla
        Fragment fragmentoActual = this.getSupportFragmentManager().findFragmentByTag(savedInstanceState.getString("TAG"));
        //Si habia algun fragmento lanzado lo
        //relanzamos
        if(fragmentoActual!=null){
            FragmentManager elgestorfragmentos = getSupportFragmentManager();

            elgestorfragmentos.beginTransaction().replace(R.id.contenido, elfragmento).commit();

            getSupportActionBar().setTitle(savedInstanceState.getCharSequence("titulo"));
        }
    }
}
