package org.das.das_grupo;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.das.das_grupo.packDialogos.CerrarSesionDialog;
import org.das.das_grupo.packGestores.GestorImagenes;
import org.das.das_grupo.packGestores.GestorUsuarios;


public class MainActivity extends ActionBarActivity implements ListarEtiquetasFragment.OnFragmentInteractionListener, ListarHistoriasFragment.OnFragmentInteractionListener, PreferenciasFragment.OnFragmentInteractionListener, CerrarSesionDialog.ListenerCierreSesion {

    private ListView lalista;
    private DrawerLayout ellayout;
    private String[] opciones;
    private Fragment elfragmento = null;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;

    private int opcionPulsada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();

        if (GestorUsuarios.getGestorUsuarios().getNombreUsuario(MainActivity.this) == null)
        {
            cerrarSesion();
        }

        opciones = getResources ().getStringArray(R.array.nav_options);

        this.lalista = (ListView) findViewById(R.id.elmenu);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, opciones);
        lalista.setAdapter(adapter);
        ellayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        lalista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                opcionPulsada = position;
                //El fragmento que neceistemos para
                //la funcionalidad elegida ira aqui
                elfragmento = null;
                int seleccion = 0;
                int ident = 0;
                //Aquí pondremos los argumentos
                //para el fragmento elegido
                Bundle args = new Bundle();
                switch (position) {
                    case 0://Ultima Historia
                        elfragmento = new ListarHistoriasFragment();
                        seleccion = 0;

                        break;
                    case 1://Mis Historias
                        elfragmento = new ListarHistoriasFragment();
                        seleccion = 1;
                        break;
                    case 2://Por etiquetas
                        elfragmento = new ListarEtiquetasFragment();
                        break;
                    case 3://Mejores
                        elfragmento = new ListarHistoriasFragment();
                        seleccion = 2;
                        break;

                    case 4://Preferencias
                        elfragmento = new PreferenciasFragment();
                        break;
                    case 5://Cerrar Sesion
                        CerrarSesionDialog diag = new CerrarSesionDialog();
                        diag.onAttach(MainActivity.this);
                        diag.show(getSupportFragmentManager(), null);


                }
                args.putInt("opcion", seleccion);
                args.putInt("id", ident);
                //Para cerrar sesión no necesitamos fragmeto,
                //por eso controlamos que la opción elegeida
                //no sea cerrar sesión
                if (position != 5) {
                    elfragmento.setArguments(args);
                    android.support.v4.app.FragmentTransaction elgestorfragmentos = getSupportFragmentManager().beginTransaction();

                    elgestorfragmentos.replace(R.id.contenido, elfragmento);
                    elgestorfragmentos.addToBackStack(null);
                    elgestorfragmentos.commit();
                    getSupportFragmentManager().executePendingTransactions();

                    lalista.setItemChecked(position, true);
                    String tituloseccion = opciones[position];
                    getSupportActionBar().setTitle(tituloseccion);
                    ellayout.closeDrawer(lalista);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                ellayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close)
        {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                getSupportActionBar().setTitle(opciones[opcionPulsada]);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        ellayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

        //Comprobar si había un estado anterior
        if(savedInstanceState != null)
            opcionPulsada = savedInstanceState.getInt("opcionpulsada");
        else
            opcionPulsada = 0;

        //Cargar el fragment correspondiente simulando un click
        lalista.performItemClick(lalista.getAdapter().getView(opcionPulsada, null, null), opcionPulsada, lalista.getAdapter().getItemId(opcionPulsada));
    }


    public void cerrarSesion() {
        //Eliminamos el id del usuario de la BD,
        // su nombre, contraseña e Id del GCM y
        //lanzamos la actividad del login antes
        //de cerrar la actividad actual
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
        if (id == R.id.actionNuevaHistoria) {
            Intent i = new Intent(MainActivity.this,NuevaHistoriaActivity.class);
            startActivity(i);
            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
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

        //guardar el fragmento pulsado
        savedInstanceState.putInt("opcionpulsada", opcionPulsada);
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
            elgestorfragmentos.executePendingTransactions();

            getSupportActionBar().setTitle(savedInstanceState.getCharSequence("titulo"));
        }
    }

    @Override
    public void onBackPressed() { // opening the previous opened fragment if back space is pressed. Could not be any fragment to open, so the app closes
        getSupportFragmentManager().executePendingTransactions();
        if(getSupportFragmentManager().getBackStackEntryCount() == 0)
            super.onBackPressed();
        else {
            //String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
            getSupportFragmentManager().popBackStackImmediate();
            //getSupportActionBar().setTitle(tag);
        }
    }

    public void onFragmentInteraction(Uri uri) {

    }

    public void onEtiquetaSelected(int id) {
        Bundle args = new Bundle();
        elfragmento = new ListarHistoriasFragment();
        args.putInt("opcion", 3);
        args.putInt("id", id);
        elfragmento.setArguments(args);
        FragmentManager elgestorfragmentos = getSupportFragmentManager();
        elgestorfragmentos.beginTransaction().replace(R.id.contenido, elfragmento).commit();
        elgestorfragmentos.executePendingTransactions();
    }

    public void onHistoriaSelected(int id) {
        //Toast.makeText(MainActivity.this,String.valueOf(id),Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MainActivity.this, VerHistoriaActivity.class);
        i.putExtra("id", id);
        startActivity(i);

    }
}
