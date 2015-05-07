package org.das.das_grupo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

    private ListView lalista;
    private DrawerLayout ellayout;
    private String[] opciones;
    private Fragment elfragmento = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
