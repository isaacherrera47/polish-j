/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelos.Articulo;
import modelos.ArticuloTalla;
import modelos.Pedido;
import modelos.Talla;

/**
 *
 * @author isaac
 */
public class ArticuloTallaJpaController implements Serializable {

    public ArticuloTallaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ArticuloTalla articuloTalla) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articulo idArticulo = articuloTalla.getIdArticulo();
            if (idArticulo != null) {
                idArticulo = em.getReference(idArticulo.getClass(), idArticulo.getCodigo());
                articuloTalla.setIdArticulo(idArticulo);
            }
            Pedido idPedido = articuloTalla.getIdPedido();
            if (idPedido != null) {
                idPedido = em.getReference(idPedido.getClass(), idPedido.getIdpedido());
                articuloTalla.setIdPedido(idPedido);
            }
            Talla idTalla = articuloTalla.getIdTalla();
            if (idTalla != null) {
                idTalla = em.getReference(idTalla.getClass(), idTalla.getIdtalla());
                articuloTalla.setIdTalla(idTalla);
            }
            em.persist(articuloTalla);
            if (idArticulo != null) {
                idArticulo.getArticuloTallaList().add(articuloTalla);
                idArticulo = em.merge(idArticulo);
            }
            if (idPedido != null) {
                idPedido.getArticuloTallaList().add(articuloTalla);
                idPedido = em.merge(idPedido);
            }
            if (idTalla != null) {
                idTalla.getArticuloTallaList().add(articuloTalla);
                idTalla = em.merge(idTalla);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ArticuloTalla articuloTalla) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ArticuloTalla persistentArticuloTalla = em.find(ArticuloTalla.class, articuloTalla.getIdarticuloTalla());
            Articulo idArticuloOld = persistentArticuloTalla.getIdArticulo();
            Articulo idArticuloNew = articuloTalla.getIdArticulo();
            Pedido idPedidoOld = persistentArticuloTalla.getIdPedido();
            Pedido idPedidoNew = articuloTalla.getIdPedido();
            Talla idTallaOld = persistentArticuloTalla.getIdTalla();
            Talla idTallaNew = articuloTalla.getIdTalla();
            if (idArticuloNew != null) {
                idArticuloNew = em.getReference(idArticuloNew.getClass(), idArticuloNew.getCodigo());
                articuloTalla.setIdArticulo(idArticuloNew);
            }
            if (idPedidoNew != null) {
                idPedidoNew = em.getReference(idPedidoNew.getClass(), idPedidoNew.getIdpedido());
                articuloTalla.setIdPedido(idPedidoNew);
            }
            if (idTallaNew != null) {
                idTallaNew = em.getReference(idTallaNew.getClass(), idTallaNew.getIdtalla());
                articuloTalla.setIdTalla(idTallaNew);
            }
            articuloTalla = em.merge(articuloTalla);
            if (idArticuloOld != null && !idArticuloOld.equals(idArticuloNew)) {
                idArticuloOld.getArticuloTallaList().remove(articuloTalla);
                idArticuloOld = em.merge(idArticuloOld);
            }
            if (idArticuloNew != null && !idArticuloNew.equals(idArticuloOld)) {
                idArticuloNew.getArticuloTallaList().add(articuloTalla);
                idArticuloNew = em.merge(idArticuloNew);
            }
            if (idPedidoOld != null && !idPedidoOld.equals(idPedidoNew)) {
                idPedidoOld.getArticuloTallaList().remove(articuloTalla);
                idPedidoOld = em.merge(idPedidoOld);
            }
            if (idPedidoNew != null && !idPedidoNew.equals(idPedidoOld)) {
                idPedidoNew.getArticuloTallaList().add(articuloTalla);
                idPedidoNew = em.merge(idPedidoNew);
            }
            if (idTallaOld != null && !idTallaOld.equals(idTallaNew)) {
                idTallaOld.getArticuloTallaList().remove(articuloTalla);
                idTallaOld = em.merge(idTallaOld);
            }
            if (idTallaNew != null && !idTallaNew.equals(idTallaOld)) {
                idTallaNew.getArticuloTallaList().add(articuloTalla);
                idTallaNew = em.merge(idTallaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = articuloTalla.getIdarticuloTalla();
                if (findArticuloTalla(id) == null) {
                    throw new NonexistentEntityException("The articuloTalla with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ArticuloTalla articuloTalla;
            try {
                articuloTalla = em.getReference(ArticuloTalla.class, id);
                articuloTalla.getIdarticuloTalla();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The articuloTalla with id " + id + " no longer exists.", enfe);
            }
            Articulo idArticulo = articuloTalla.getIdArticulo();
            if (idArticulo != null) {
                idArticulo.getArticuloTallaList().remove(articuloTalla);
                idArticulo = em.merge(idArticulo);
            }
            Pedido idPedido = articuloTalla.getIdPedido();
            if (idPedido != null) {
                idPedido.getArticuloTallaList().remove(articuloTalla);
                idPedido = em.merge(idPedido);
            }
            Talla idTalla = articuloTalla.getIdTalla();
            if (idTalla != null) {
                idTalla.getArticuloTallaList().remove(articuloTalla);
                idTalla = em.merge(idTalla);
            }
            em.remove(articuloTalla);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ArticuloTalla> findArticuloTallaEntities() {
        return findArticuloTallaEntities(true, -1, -1);
    }

    public List<ArticuloTalla> findArticuloTallaEntities(int maxResults, int firstResult) {
        return findArticuloTallaEntities(false, maxResults, firstResult);
    }

    private List<ArticuloTalla> findArticuloTallaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ArticuloTalla.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ArticuloTalla findArticuloTalla(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ArticuloTalla.class, id);
        } finally {
            em.close();
        }
    }

    public int getArticuloTallaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ArticuloTalla> rt = cq.from(ArticuloTalla.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
