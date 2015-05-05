package org.das.das_grupo.packGestores;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by david on 29/04/15.
 */
public class GestorUsuarios {

    private static GestorUsuarios mGestorUsuarios = new GestorUsuarios();

    public final static String PREFERENCIA_GCM_ID = "gcmid";
    public final static String PREFERENCIA_NOMBRE_USUARIO = "nomusuario";
    public final static String PREFERENCIA_PASS_USUARIO = "passusuario";
    public final static String PREFERENCIA_ID_USUARIO = "idusuario";

    private GestorUsuarios()
    {
    }

    public static GestorUsuarios getGestorUsuarios()
    {
        return mGestorUsuarios;
    }

    /**
     * Métodos para gestionar el nombre del usuario
     * @param contexto
     * @param nombreUsuario
     */
    public void guardarNombreUsuario(Context contexto, String nombreUsuario)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(contexto);
        SharedPreferences.Editor prefeditor = prefs.edit();
        prefeditor.putString(PREFERENCIA_NOMBRE_USUARIO, nombreUsuario);
        prefeditor.apply();
    }

    public String getNombreUsuario(Context contexto)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(contexto);
        return prefs.getString(PREFERENCIA_NOMBRE_USUARIO, null);
    }

    public void borrarNombreUsuario(Context contexto)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(contexto);
        SharedPreferences.Editor prefeditor = prefs.edit();
        prefeditor.remove(PREFERENCIA_NOMBRE_USUARIO);
        prefeditor.apply();
    }

    /**
     * Métodos para gestionar la contraseña del usaurio
     * @param contexto
     * @param contrasena
     */
    public void guardarContrasenaUsuario(Context contexto, String contrasena)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(contexto);
        SharedPreferences.Editor prefeditor = prefs.edit();
        prefeditor.putString(PREFERENCIA_PASS_USUARIO, contrasena);
        prefeditor.apply();
    }

    public String getContrasenaUsuario(Context contexto)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(contexto);
        return prefs.getString(PREFERENCIA_PASS_USUARIO, null);
    }

    public void borrarContrasenaUsuario(Context contexto)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(contexto);
        SharedPreferences.Editor prefeditor = prefs.edit();
        prefeditor.remove(PREFERENCIA_PASS_USUARIO);
        prefeditor.apply();
    }

    /**
     * Métodos para gestionar el id del GCM del usuario
     * @param contexto
     * @param gcmid
     */
    public void guardarGcmIdUsuario(Context contexto, String gcmid)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(contexto);
        SharedPreferences.Editor prefeditor = prefs.edit();
        prefeditor.putString(PREFERENCIA_GCM_ID, gcmid);
        prefeditor.apply();
    }

    public String getGcmIdUsuario(Context contexto)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(contexto);
        return prefs.getString(PREFERENCIA_GCM_ID, null);
    }

    public void borrarGcmIdUsuario(Context contexto)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(contexto);
        SharedPreferences.Editor prefeditor = prefs.edit();
        prefeditor.remove(PREFERENCIA_GCM_ID);
        prefeditor.apply();
    }

    /**
     * Métodos para gestionar el ID que el usuario tiene en la BBDD remota
     * @param contexto
     * @param id
     */
    public void guardarIdUsuario(Context contexto, String id)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(contexto);
        SharedPreferences.Editor prefeditor = prefs.edit();
        prefeditor.putString(PREFERENCIA_ID_USUARIO, id);
        prefeditor.apply();
    }

    public String getIdUsuario(Context contexto)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(contexto);
        return prefs.getString(PREFERENCIA_ID_USUARIO, null);
    }

    public void borrarIdUsuario(Context contexto)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(contexto);
        SharedPreferences.Editor prefeditor = prefs.edit();
        prefeditor.remove(PREFERENCIA_ID_USUARIO);
        prefeditor.apply();
    }

}
