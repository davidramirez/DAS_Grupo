package org.das.das_grupo.packListasMod;

import java.util.Date;

/**
 * Created by Alberto on 12/05/2015.
 */
public class historia {
    public int id;
    public String titulo,autor;
    public Date fecha;

    public historia(int pId, String pTitulo, String pAutor, Date pFecha){
        id = pId;
        titulo = pTitulo;
        autor = pAutor;
        fecha = pFecha;
    }
}
