package org.das.das_grupo.packGestores;

/**
 * Created by David on 05/05/2015.
 */
public class GestorConexiones {

    private static GestorConexiones mGestor = new GestorConexiones();

    //URL base para las conexiones al servidor web
    public final static String WEB_SERVER_URL ="http://galan.ehu.es/dramirez003/das/";

    private GestorConexiones()
    {

    }

    public static GestorConexiones getGestorConexiones()
    {
        return mGestor;
    }
}
