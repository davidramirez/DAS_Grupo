package org.das.das_grupo.packGestores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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
     * Proporciona las imagenes identificadas dados sus path en el servidor.
     */
    public String[] getImagen(String[] serverPaths)
    {
        String[] localPaths = null;
        //TODO implementar la descarga de la imagen del servidor, opcional el cacheo
        return localPaths;
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
                newWidth = width * (newHeight/height);
            }
            else {
                newWidth = 500;
                newHeight = height * (newWidth / width);
            }
            Log.i("FOTO","alto: "+newHeight+"ancho: "+newWidth);
            redimensionado = Bitmap.createScaledBitmap(imagen, newWidth, newHeight, true);
        }
        else
            redimensionado = imagen;

        return redimensionado;
    }
}
