/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author isaac
 */
public final class Entidad {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("Inventario_PolishPU");

    public static EntityManagerFactory getInstancia() {
        return emf;       
    }
}
