/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelos.Categoria;
import modelos.Tienda;
import modelos.ArticuloTalla;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelos.Articulo;

/**
 *
 * @author isaac
 */
public class ArticuloJpaController implements Serializable {

    public ArticuloJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Articulo articulo) throws PreexistingEntityException, Exception {
        if (articulo.getArticuloTallaList() == null) {
            articulo.setArticuloTallaList(new ArrayList<ArticuloTalla>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria idCategoria = articulo.getIdCategoria();
            if (idCategoria != null) {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getIdcategoria());
                articulo.setIdCategoria(idCategoria);
            }
            Tienda idTienda = articulo.getIdTienda();
            if (idTienda != null) {
                idTienda = em.getReference(idTienda.getClass(), idTienda.getCodigo());
                articulo.setIdTienda(idTienda);
            }
            List<ArticuloTalla> attachedArticuloTallaList = new ArrayList<ArticuloTalla>();
            for (ArticuloTalla articuloTallaListArticuloTallaToAttach : articulo.getArticuloTallaList()) {
                articuloTallaListArticuloTallaToAttach = em.getReference(articuloTallaListArticuloTallaToAttach.getClass(), articuloTallaListArticuloTallaToAttach.getIdarticuloTalla());
                attachedArticuloTallaList.add(articuloTallaListArticuloTallaToAttach);
            }
            articulo.setArticuloTallaList(attachedArticuloTallaList);
            em.persist(articulo);
            if (idCategoria != null) {
                idCategoria.getArticuloList().add(articulo);
                idCategoria = em.merge(idCategoria);
            }
            if (idTienda != null) {
                idTienda.getArticuloList().add(articulo);
                idTienda = em.merge(idTienda);
            }
            for (ArticuloTalla articuloTallaListArticuloTalla : articulo.getArticuloTallaList()) {
                Articulo oldIdArticuloOfArticuloTallaListArticuloTalla = articuloTallaListArticuloTalla.getIdArticulo();
                articuloTallaListArticuloTalla.setIdArticulo(articulo);
                articuloTallaListArticuloTalla = em.merge(articuloTallaListArticuloTalla);
                if (oldIdArticuloOfArticuloTallaListArticuloTalla != null) {
                    oldIdArticuloOfArticuloTallaListArticuloTalla.getArticuloTallaList().remove(articuloTallaListArticuloTalla);
                    oldIdArticuloOfArticuloTallaListArticuloTalla = em.merge(oldIdArticuloOfArticuloTallaListArticuloTalla);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findArticulo(articulo.getCodigo()) != null) {
                throw new PreexistingEntityException("Articulo " + articulo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Articulo articulo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articulo persistentArticulo = em.find(Articulo.class, articulo.getCodigo());
            Categoria idCategoriaOld = persistentArticulo.getIdCategoria();
            Categoria idCategoriaNew = articulo.getIdCategoria();
            Tienda idTiendaOld = persistentArticulo.getIdTienda();
            Tienda idTiendaNew = articulo.getIdTienda();
            List<ArticuloTalla> articuloTallaListOld = persistentArticulo.getArticuloTallaList();
            List<ArticuloTalla> articuloTallaListNew = articulo.getArticuloTallaList();
            List<String> illegalOrphanMessages = null;
            for (ArticuloTalla articuloTallaListOldArticuloTalla : articuloTallaListOld) {
                if (!articuloTallaListNew.contains(articuloTallaListOldArticuloTalla)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ArticuloTalla " + articuloTallaListOldArticuloTalla + " since its idArticulo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getIdcategoria());
                articulo.setIdCategoria(idCategoriaNew);
            }
            if (idTiendaNew != null) {
                idTiendaNew = em.getReference(idTiendaNew.getClass(), idTiendaNew.getCodigo());
                articulo.setIdTienda(idTiendaNew);
            }
            List<ArticuloTalla> attachedArticuloTallaListNew = new ArrayList<ArticuloTalla>();
            for (ArticuloTalla articuloTallaListNewArticuloTallaToAttach : articuloTallaListNew) {
                articuloTallaListNewArticuloTallaToAttach = em.getReference(articuloTallaListNewArticuloTallaToAttach.getClass(), articuloTallaListNewArticuloTallaToAttach.getIdarticuloTalla());
                attachedArticuloTallaListNew.add(articuloTallaListNewArticuloTallaToAttach);
            }
            articuloTallaListNew = attachedArticuloTallaListNew;
            articulo.setArticuloTallaList(articuloTallaListNew);
            articulo = em.merge(articulo);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getArticuloList().remove(articulo);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getArticuloList().add(articulo);
                idCategoriaNew = em.merge(idCategoriaNew);
            }
            if (idTiendaOld != null && !idTiendaOld.equals(idTiendaNew)) {
                idTiendaOld.getArticuloList().remove(articulo);
                idTiendaOld = em.merge(idTiendaOld);
            }
            if (idTiendaNew != null && !idTiendaNew.equals(idTiendaOld)) {
                idTiendaNew.getArticuloList().add(articulo);
                idTiendaNew = em.merge(idTiendaNew);
            }
            for (ArticuloTalla articuloTallaListNewArticuloTalla : articuloTallaListNew) {
                if (!articuloTallaListOld.contains(articuloTallaListNewArticuloTalla)) {
                    Articulo oldIdArticuloOfArticuloTallaListNewArticuloTalla = articuloTallaListNewArticuloTalla.getIdArticulo();
                    articuloTallaListNewArticuloTalla.setIdArticulo(articulo);
                    articuloTallaListNewArticuloTalla = em.merge(articuloTallaListNewArticuloTalla);
                    if (oldIdArticuloOfArticuloTallaListNewArticuloTalla != null && !oldIdArticuloOfArticuloTallaListNewArticuloTalla.equals(articulo)) {
                        oldIdArticuloOfArticuloTallaListNewArticuloTalla.getArticuloTallaList().remove(articuloTallaListNewArticuloTalla);
                        oldIdArticuloOfArticuloTallaListNewArticuloTalla = em.merge(oldIdArticuloOfArticuloTallaListNewArticuloTalla);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = articulo.getCodigo();
                if (findArticulo(id) == null) {
                    throw new NonexistentEntityException("The articulo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articulo articulo;
            try {
                articulo = em.getReference(Articulo.class, id);
                articulo.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The articulo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ArticuloTalla> articuloTallaListOrphanCheck = articulo.getArticuloTallaList();
            for (ArticuloTalla articuloTallaListOrphanCheckArticuloTalla : articuloTallaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Articulo (" + articulo + ") cannot be destroyed since the ArticuloTalla " + articuloTallaListOrphanCheckArticuloTalla + " in its articuloTallaList field has a non-nullable idArticulo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categoria idCategoria = articulo.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getArticuloList().remove(articulo);
                idCategoria = em.merge(idCategoria);
            }
            Tienda idTienda = articulo.getIdTienda();
            if (idTienda != null) {
                idTienda.getArticuloList().remove(articulo);
                idTienda = em.merge(idTienda);
            }
            em.remove(articulo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Articulo> findArticuloEntities() {
        return findArticuloEntities(true, -1, -1);
    }

    public List<Articulo> findArticuloEntities(int maxResults, int firstResult) {
        return findArticuloEntities(false, maxResults, firstResult);
    }

    private List<Articulo> findArticuloEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Articulo.class));
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

    public Articulo findArticulo(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Articulo.class, id);
        } finally {
            em.close();
        }
    }

    public int getArticuloCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Articulo> rt = cq.from(Articulo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
