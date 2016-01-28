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
import modelos.ArticuloTalla;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelos.Talla;

/**
 *
 * @author isaac
 */
public class TallaJpaController implements Serializable {

    public TallaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Talla talla) throws PreexistingEntityException, Exception {
        if (talla.getArticuloTallaList() == null) {
            talla.setArticuloTallaList(new ArrayList<ArticuloTalla>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ArticuloTalla> attachedArticuloTallaList = new ArrayList<ArticuloTalla>();
            for (ArticuloTalla articuloTallaListArticuloTallaToAttach : talla.getArticuloTallaList()) {
                articuloTallaListArticuloTallaToAttach = em.getReference(articuloTallaListArticuloTallaToAttach.getClass(), articuloTallaListArticuloTallaToAttach.getIdarticuloTalla());
                attachedArticuloTallaList.add(articuloTallaListArticuloTallaToAttach);
            }
            talla.setArticuloTallaList(attachedArticuloTallaList);
            em.persist(talla);
            for (ArticuloTalla articuloTallaListArticuloTalla : talla.getArticuloTallaList()) {
                Talla oldIdTallaOfArticuloTallaListArticuloTalla = articuloTallaListArticuloTalla.getIdTalla();
                articuloTallaListArticuloTalla.setIdTalla(talla);
                articuloTallaListArticuloTalla = em.merge(articuloTallaListArticuloTalla);
                if (oldIdTallaOfArticuloTallaListArticuloTalla != null) {
                    oldIdTallaOfArticuloTallaListArticuloTalla.getArticuloTallaList().remove(articuloTallaListArticuloTalla);
                    oldIdTallaOfArticuloTallaListArticuloTalla = em.merge(oldIdTallaOfArticuloTallaListArticuloTalla);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTalla(talla.getIdtalla()) != null) {
                throw new PreexistingEntityException("Talla " + talla + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Talla talla) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Talla persistentTalla = em.find(Talla.class, talla.getIdtalla());
            List<ArticuloTalla> articuloTallaListOld = persistentTalla.getArticuloTallaList();
            List<ArticuloTalla> articuloTallaListNew = talla.getArticuloTallaList();
            List<String> illegalOrphanMessages = null;
            for (ArticuloTalla articuloTallaListOldArticuloTalla : articuloTallaListOld) {
                if (!articuloTallaListNew.contains(articuloTallaListOldArticuloTalla)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ArticuloTalla " + articuloTallaListOldArticuloTalla + " since its idTalla field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ArticuloTalla> attachedArticuloTallaListNew = new ArrayList<ArticuloTalla>();
            for (ArticuloTalla articuloTallaListNewArticuloTallaToAttach : articuloTallaListNew) {
                articuloTallaListNewArticuloTallaToAttach = em.getReference(articuloTallaListNewArticuloTallaToAttach.getClass(), articuloTallaListNewArticuloTallaToAttach.getIdarticuloTalla());
                attachedArticuloTallaListNew.add(articuloTallaListNewArticuloTallaToAttach);
            }
            articuloTallaListNew = attachedArticuloTallaListNew;
            talla.setArticuloTallaList(articuloTallaListNew);
            talla = em.merge(talla);
            for (ArticuloTalla articuloTallaListNewArticuloTalla : articuloTallaListNew) {
                if (!articuloTallaListOld.contains(articuloTallaListNewArticuloTalla)) {
                    Talla oldIdTallaOfArticuloTallaListNewArticuloTalla = articuloTallaListNewArticuloTalla.getIdTalla();
                    articuloTallaListNewArticuloTalla.setIdTalla(talla);
                    articuloTallaListNewArticuloTalla = em.merge(articuloTallaListNewArticuloTalla);
                    if (oldIdTallaOfArticuloTallaListNewArticuloTalla != null && !oldIdTallaOfArticuloTallaListNewArticuloTalla.equals(talla)) {
                        oldIdTallaOfArticuloTallaListNewArticuloTalla.getArticuloTallaList().remove(articuloTallaListNewArticuloTalla);
                        oldIdTallaOfArticuloTallaListNewArticuloTalla = em.merge(oldIdTallaOfArticuloTallaListNewArticuloTalla);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = talla.getIdtalla();
                if (findTalla(id) == null) {
                    throw new NonexistentEntityException("The talla with id " + id + " no longer exists.");
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
            Talla talla;
            try {
                talla = em.getReference(Talla.class, id);
                talla.getIdtalla();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The talla with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ArticuloTalla> articuloTallaListOrphanCheck = talla.getArticuloTallaList();
            for (ArticuloTalla articuloTallaListOrphanCheckArticuloTalla : articuloTallaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Talla (" + talla + ") cannot be destroyed since the ArticuloTalla " + articuloTallaListOrphanCheckArticuloTalla + " in its articuloTallaList field has a non-nullable idTalla field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(talla);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Talla> findTallaEntities() {
        return findTallaEntities(true, -1, -1);
    }

    public List<Talla> findTallaEntities(int maxResults, int firstResult) {
        return findTallaEntities(false, maxResults, firstResult);
    }

    private List<Talla> findTallaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Talla.class));
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

    public Talla findTalla(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Talla.class, id);
        } finally {
            em.close();
        }
    }

    public int getTallaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Talla> rt = cq.from(Talla.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
