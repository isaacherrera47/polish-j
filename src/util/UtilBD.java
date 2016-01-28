/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import controladores.Entidad;
import controladores.UsuarioJpaController;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import modelos.Usuario;

/**
 *
 * @author isaac
 */
public class UtilBD {
    private static Usuario SESSION;

    public static Usuario getSESSION() {
        return SESSION;
    }

    public static void setSESSION(Usuario SESSION) {
        UtilBD.SESSION = SESSION;
    }
    public static boolean crearUsuario(String nombre, String password, String nombreUsuario) {
        try {
            Usuario usuario = new Usuario(nombreUsuario, password, nombre);
            UsuarioJpaController u = new UsuarioJpaController(Entidad.getInstancia());
            if ((u.findUsuario(nombreUsuario) == null)) {
                u.create(usuario);
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(UtilBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean compruebaUsuario(String nombreUsuario, String password){        
        Usuario usuario;
        UsuarioJpaController u = new UsuarioJpaController(Entidad.getInstancia());
        EntityManager em = Entidad.getInstancia().createEntityManager();
        Query q;
        q = em.createNamedQuery("Usuario.findBySesion");        
        q.setParameter("usuario", nombreUsuario);
        q.setParameter("password", password);
        q.getSingleResult();
        return false;
    }

}
