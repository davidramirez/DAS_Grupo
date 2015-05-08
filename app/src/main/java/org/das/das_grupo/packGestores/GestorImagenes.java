package org.das.das_grupo.packGestores;

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
     * Proporciona la imagen identificada por su path en el servidor.
     */
  /*  public ?? getImagen(String serverPath)
    {
        //TODO implementar la descarga de la imagen del servidor, opcional el cacheo
    }*/
}
