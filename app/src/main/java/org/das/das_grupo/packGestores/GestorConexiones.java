package org.das.das_grupo.packGestores;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.das.das_grupo.MainActivity;
import org.das.das_grupo.R;

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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by David on 05/05/2015.
 */
public class GestorConexiones {

    private static GestorConexiones mGestor = new GestorConexiones();

    //URL base para las conexiones al servidor web
    public final static String WEB_SERVER_URL ="http://david-net.softether.net:8080";//"http://galan.ehu.es/dramirez003/DAS/grupo";

    private GestorConexiones()
    {

    }

    public static GestorConexiones getGestorConexiones()
    {
        return mGestor;
    }

    public Integer logInUser(String nombre, String contrasena){
        Integer id = 0;

        URL url = null;
        try {
            url = new URL(WEB_SERVER_URL + "/login.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("Accept", "application/json");
        try {
            urlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        urlConnection.setDoOutput(true); // Send data
        urlConnection.setDoInput(true); // Receive data
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);

        String parametros ="nombre=" + nombre + "&contrasena=" + toSha512(contrasena);



        PrintWriter out = null;
        OutputStream stream = null;

        try {
            urlConnection.connect();
            stream = urlConnection.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }



        out = new PrintWriter(stream);


        out.print(parametros.toString());
        out.close();

        int statusCode = 0;
        try {
            statusCode = urlConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String response = "";
        InputStream inputStream = null;
                /* 200 represents HTTP OK */
        if (statusCode == 200) {
            try {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            response = convertInputStreamToString(inputStream);
            if(response.equals("false"))
                id = 0;
            else
                id = Integer.valueOf(response);

        } else{
            // actuar frente al error
            response = String.valueOf(statusCode);
        }

        urlConnection.disconnect();

        return id;
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

    /**
     * Metodo que devuelve al clave tras aplicar
     * SHA-512
     * @param contrasena la contraseña
     * @return
     */
    private String toSha512(String contrasena){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String clave = contrasena;
        md.update(clave.getBytes());
        String output = "";
        byte[] mb = md.digest();
        for (int i = 0; i < mb.length; i++) {
            byte temp = mb[i];
            String s = Integer.toHexString(new Byte(temp));
            while (s.length() < 2) {
                s = "0" + s;
            }
            s = s.substring(s.length() - 2);
            output += s;
        }
        return output;
    }

    public Boolean SingInUser(String nombre, String contra, boolean avisos) {
        URL url = null;
        Boolean id =false;
        try {
            url = new URL(WEB_SERVER_URL+"/registrar.php");
            // url = new URL(WEB_SERVER_URL+"/grupo/r.php");
            //    url = new URL("http://192.168.1.120/registrar.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("Accept", "application/json");
        try {
            urlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        urlConnection.setDoOutput(true); // Send data
        urlConnection.setDoInput(true); // Receive data
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);

        /*ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
        parametros.add(new BasicNameValuePair("nombre", nombre));
        parametros.add(new BasicNameValuePair("contrasena", toSha512(contra)));
*/
        String parametros ="nombre=" + nombre + "&contrasena=" + toSha512(contra) +
                "&aviso=" + String.valueOf(avisos);

        // parametros = "";

        PrintWriter out = null;
        OutputStream stream = null;

        try {
            urlConnection.connect();
            stream = urlConnection.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }



        out = new PrintWriter(stream);


        out.print(parametros);
        out.close();

        int statusCode = 0;
        try {
            statusCode = urlConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String response = "";
        InputStream inputStream = null;
                /* 200 represents HTTP OK */
        if (statusCode == 200) {
            try {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            response = convertInputStreamToString(inputStream);
            id = Boolean.valueOf(response);

        } else{
            // actuar frente al error
            response = String.valueOf(statusCode);
        }

        urlConnection.disconnect();



        return id;
    }


    public boolean singInGCM(int id, String contra, String regid) {
        Log.i("GCM", "subiendo id al servidor");
        URL url = null;
        Boolean ok =false;
        try {
            url = new URL(WEB_SERVER_URL+"/registro_gcm.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("Accept", "application/json");
        try {
            urlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        urlConnection.setDoOutput(true); // Send data
        urlConnection.setDoInput(true); // Receive data
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);



        String parametros ="id=" + id +
                "&contrasena=" + toSha512(contra) +
                "&gcm="+ regid;

        // parametros = "";

        PrintWriter out = null;
        OutputStream stream = null;

        try {
            urlConnection.connect();
            stream = urlConnection.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }



        out = new PrintWriter(stream);


        out.print(parametros);
        out.close();

        int statusCode = 0;
        try {
            statusCode = urlConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String response = "";
        InputStream inputStream = null;
                /* 200 represents HTTP OK */
        if (statusCode == 200) {
            try {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            response = convertInputStreamToString(inputStream);
            Log.i("GCM","Respuesta al registro: "+response);
            ok = Boolean.valueOf(response);

        } else{
            // actuar frente al error
            response = String.valueOf(statusCode);
        }

        urlConnection.disconnect();

        return ok;
    }

    public String listarHistorias(String accion, int ident) {
        URL url = null;
        Boolean id =false;

        try {
            url = new URL(WEB_SERVER_URL+"/listar_historias.php");
           // url = new URL("http://192.168.1.67/listar_historias.php");


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("Accept", "application/json");
        try {
            urlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        urlConnection.setDoOutput(true); // Send data
        urlConnection.setDoInput(true); // Receive data
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);

        /*ArrayList<NameValuePair> parametros = new ArrayList<NameValuePair>();
        parametros.add(new BasicNameValuePair("nombre", nombre));
        parametros.add(new BasicNameValuePair("contrasena", toSha512(contra)));
*/
        String parametros ="accion=" + accion +
                "&id=" + ident;

        // parametros = "";

        PrintWriter out = null;
        OutputStream stream = null;

        try {
            urlConnection.connect();
            stream = urlConnection.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }



        out = new PrintWriter(stream);


        out.print(parametros);
        out.close();

        int statusCode = 0;
        try {
            statusCode = urlConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String response = "";
        InputStream inputStream = null;
                /* 200 represents HTTP OK */
        if (statusCode == 200) {
            try {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            response = convertInputStreamToString(inputStream);

        } else{
            // actuar frente al error
            response = String.valueOf(statusCode);
        }

        urlConnection.disconnect();



        return response;
    }

    public Boolean updateUser(String idUsuario, String gcmIdUsuario, String contn1, String contv, boolean avis) {
        URL url = null;
        Boolean ok =false;
        try {
            url = new URL(WEB_SERVER_URL+"/update_user.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("Accept", "application/json");
        try {
            urlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        urlConnection.setDoOutput(true); // Send data
        urlConnection.setDoInput(true); // Receive data
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);



        String parametros ="id=" +idUsuario+
                "&contrasena=" + toSha512(contn1) +
                "&gcm="+ gcmIdUsuario+"&aviso=" + String.valueOf(avis)+
                "&oldpass=" + contv ;

        // parametros = "";

        PrintWriter out = null;
        OutputStream stream = null;

        try {
            urlConnection.connect();
            stream = urlConnection.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }



        out = new PrintWriter(stream);


        out.print(parametros);
        out.close();

        int statusCode = 0;
        try {
            statusCode = urlConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String response = "";
        InputStream inputStream = null;
                /* 200 represents HTTP OK */
        if (statusCode == 200) {
            try {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            response = convertInputStreamToString(inputStream);
            ok = Boolean.valueOf(response);

        } else{
            // actuar frente al error
            response = String.valueOf(statusCode);
        }

        urlConnection.disconnect();

        return ok;
    }

    public String listarEtiquetas() {
        URL url = null;
        Boolean ok =false;
        try {
            url = new URL(WEB_SERVER_URL+"/listar_etiquetas.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("Accept", "application/json");
        try {
            urlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        urlConnection.setDoOutput(true); // Send data
        urlConnection.setDoInput(true); // Receive data
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);



        String parametros ="";

        // parametros = "";

        PrintWriter out = null;
        OutputStream stream = null;

        try {
            urlConnection.connect();
            stream = urlConnection.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }



        out = new PrintWriter(stream);


        out.print(parametros);
        out.close();

        int statusCode = 0;
        try {
            statusCode = urlConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String response = "";
        InputStream inputStream = null;
                /* 200 represents HTTP OK */
        if (statusCode == 200) {
            try {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            response = convertInputStreamToString(inputStream);

        } else{
            // actuar frente al error
            response = String.valueOf(statusCode);
        }

        urlConnection.disconnect();

        return response;
    }

    public String getHistoria(int id) {
        URL url = null;
        Boolean ok =false;
        try {
            url = new URL(WEB_SERVER_URL+"/listar_historia.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("Accept", "application/json");
        try {
            urlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        urlConnection.setDoOutput(true); // Send data
        urlConnection.setDoInput(true); // Receive data
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);



        String parametros ="id=" + id;

        // parametros = "";

        PrintWriter out = null;
        OutputStream stream = null;

        try {
            urlConnection.connect();
            stream = urlConnection.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }



        out = new PrintWriter(stream);


        out.print(parametros);
        out.close();

        int statusCode = 0;
        try {
            statusCode = urlConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String response = "";
        InputStream inputStream = null;
                /* 200 represents HTTP OK */
        if (statusCode == 200) {
            try {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            response = convertInputStreamToString(inputStream);

        } else{
            // actuar frente al error
            response = String.valueOf(statusCode);
        }

        urlConnection.disconnect();

        return response;
    }

    public Boolean registrarComentario(int id, int id_us, String textoCom) {
        URL url = null;
        Boolean ok =false;
        try {
            url = new URL(WEB_SERVER_URL+"/registrar_comentario.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("Accept", "application/json");
        try {
            urlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        urlConnection.setDoOutput(true); // Send data
        urlConnection.setDoInput(true); // Receive data
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);



        String parametros ="id_usuario=" + id_us+
                "&id_historia="+ id +
                "&comentario=" + textoCom;

        // parametros = "";

        PrintWriter out = null;
        OutputStream stream = null;

        try {
            urlConnection.connect();
            stream = urlConnection.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }



        out = new PrintWriter(stream);


        out.print(parametros);
        out.close();

        int statusCode = 0;
        try {
            statusCode = urlConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String response = "";
        InputStream inputStream = null;
                /* 200 represents HTTP OK */
        if (statusCode == 200) {
            try {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            response = convertInputStreamToString(inputStream);
            ok = Boolean.valueOf(response);

        } else{
            // actuar frente al error
            response = String.valueOf(statusCode);
        }

        urlConnection.disconnect();

        return ok;
    }
}
