package org.das.das_grupo.packGestores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by David on 05/05/2015.
 */
public class GestorImagenes {

    private static GestorImagenes mGestor = new GestorImagenes();

    private GestorImagenes(){

    }

    public static GestorImagenes getGestorImagenes(){
        return mGestor;
    }

    /**
     * Proporciona las imagenes dados sus path en el servidor, comprobando que no estan ya descargadas.
     *
     */
    public String[] getImagen(String[] serverPaths)
    {
        String[] paths = new String[serverPaths.length];
       /* String pat ="";
        for(int j=0;j<serverPaths.length;j++)
            pat += serverPaths[j]+" ";
        Log.i("FOTO", pat);*/

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        for(int i = 0; i<serverPaths.length;i++)
        {
            File local = new File(storageDir.getAbsolutePath()+"/"+serverPaths[i]);
            Log.i("FOTO","archivo a comprobar: " + storageDir.getAbsolutePath()+"/"+serverPaths[i]);

            //Si ya tenemos en la carpeta una foto con ese nombre, ese es el archivo buscado, no descargamos
            if (local.exists() && !local.isDirectory()) {
                Log.i("FOTO","imagen no descargada");
                paths[i] = local.getAbsolutePath();
            }
            else {
                try {
                    Log.i("FOTO","descargando foto del servidor");
                    String url = GestorConexiones.WEB_SERVER_URL+"/imagenes/"+serverPaths[i];
                    Log.i("FOTO", url);
                    Bitmap bm = downloadImage(url);

                    //Guardar el bitmap
                    File imagen = null;

                    Log.i("FOTO","nombre: "+serverPaths[i].substring(0, serverPaths[i].length()-4));
                    imagen = new File(storageDir.getAbsolutePath()+"/"+serverPaths[i]);
                    FileOutputStream out = new FileOutputStream(imagen);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, out);

                    paths[i] = imagen.getAbsolutePath();
                    Log.i("FOTO","path imagen descargada: "+imagen.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return paths;
    }


    /**
     * Escalar un bitmap
     */
    public Bitmap escalarImagen(Bitmap imagen) {
        // Raw height and width of image
        int height = imagen.getHeight();
        int width = imagen.getWidth();

        int maxSideLength = 500;

        Bitmap redimensionado;

        if (height > maxSideLength || width > maxSideLength) {

            int newHeight;
            int newWidth;
            //Ver que lado es mayor y calcular nuevas dimensiones
            if(height >= width) {
                newHeight = maxSideLength;
                newWidth = (int) (width * newHeight / (double) height);
            }
            else {
                newWidth = 500;
                newHeight = (int) (height * newWidth / (double) width);
            }
            Log.i("FOTO","alto: "+newHeight+"ancho: "+newWidth);
            redimensionado = Bitmap.createScaledBitmap(imagen, newWidth, newHeight, true);
        }
        else
            redimensionado = imagen;

        return redimensionado;
    }

    public Bitmap downloadImage(String url) {

        HttpParams hparams = new BasicHttpParams();
        /**
         * You can also add timeouts to the settings menu in a real project
         */
        HttpConnectionParams.setConnectionTimeout(hparams, 10000);
        HttpConnectionParams.setSoTimeout(hparams, 10000);
        HttpGet get = new HttpGet(url);
        DefaultHttpClient client;
        try {
            HttpResponse response = null;
            client = new DefaultHttpClient(hparams);
            response = client.execute(get);
            HttpEntity responseEntity = response.getEntity();
            BufferedHttpEntity httpEntity = new BufferedHttpEntity(
                    responseEntity);
            InputStream imageStream = httpEntity.getContent();

            return BitmapFactory.decodeStream(imageStream);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
