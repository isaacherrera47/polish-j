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
import modelos.Pedido;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelos.Articulo;
import modelos.Tienda;

/**
 *
 * @author isaac
 */
public class TiendaJpaController implements Serializable {

    public TiendaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tienda tienda) throws PreexistingEntityException, Exception {
        if (tienda.getPedidoList() == null) {
            tienda.setPedidoList(new ArrayList<Pedido>());
        }
        if (tienda.getArticuloList() == null) {
            tienda.setArticuloList(new ArrayList<Articulo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Pedido> attachedPedidoList = new ArrayList<Pedido>();
            for (Pedido pedidoListPedidoToAttach : tienda.getPedidoList()) {
                pedidoListPedidoToAttach = em.getReference(pedidoListPedidoToAttach.getClass(), pedidoListPedidoToAttach.getIdpedido());
                attachedPedidoList.add(pedidoListPedidoToAttach);
            }
            tienda.setPedidoList(attachedPedidoList);
            List<Articulo> attachedArticuloList = new ArrayList<Articulo>();
            for (Articulo articuloListArticuloToAttach : tienda.getArticuloList()) {
                articuloListArticuloToAttach = em.getReference(articuloListArticuloToAttach.getClass(), articuloListArticuloToAttach.getCodigo());
                attachedArticuloList.add(articuloListArticuloToAttach);
            }
            tienda.setArticuloList(attachedArticuloList);
            em.persist(tienda);
            for (Pedido pedidoListPedido : tienda.getPedidoList()) {
                Tienda oldIdtiendaOfPedidoListPedido = pedidoListPedido.getIdtienda();
                pedidoListPedido.setIdtienda(tienda);
                pedidoListPedido = em.merge(pedidoListPedido);
                if (oldIdtiendaOfPedidoListPedido != null) {
                    oldIdtiendaOfPedidoListPedido.getPedidoList().remove(pedidoListPedido);
                    oldIdtiendaOfPedidoListPedido = em.merge(oldIdtiendaOfPedidoListPedido);
                }
            }
            for (Articulo articuloListArticulo : tienda.getArticuloList()) {
                Tienda oldIdTiendaOfArticuloListArticulo = articuloListArticulo.getIdTienda();
                articuloListArticulo.setIdTienda(tienda);
                articuloListArticulo = em.merge(articuloListArticulo);
                if (oldIdTiendaOfArticuloListArticulo != null) {
                    oldIdTiendaOfArticuloListArticulo.getArticuloList().remove(articuloListArticulo);
                    oldIdTiendaOfArticuloListArticulo = em.merge(oldIdTiendaOfArticuloListArticulo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTienda(tienda.getCodigo()) != null) {
                throw new PreexistingEntityException("Tienda " + tienda + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tienda tienda) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tienda persistentTienda = em.find(Tienda.class, tienda.getCodigo());
            List<Pedido> pedidoListOld = persistentTienda.getPedidoList();
            List<Pedido> pedidoListNew = tienda.getPedidoList();
            List<Articulo> articuloListOld = persistentTienda.getArticuloList();
            List<Articulo> articuloListNew = tienda.getArticuloList();
            List<String> illegalOrphanMessages = null;
            for (Pedido pedidoListOldPedido : pedidoListOld) {
                if (!pedidoListNew.contains(pedidoListOldPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pedido " + pedidoListOldPedido + " since its idtienda field is not nullable.");
                }
            }
            for (Articulo articuloListOldArticulo : articuloListOld) {
                if (!articuloListNew.contains(articuloListOldArticulo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Articulo " + articuloListOldArticulo + " since its idTienda field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Pedido> attachedPedidoListNew = new ArrayList<Pedido>();
            for (Pedido pedidoListNewPedidoToAttach : pedidoListNew) {
                pedidoListNewPedidoToAttach = em.getReference(pedidoListNewPedidoToAttach.getClass(), pedidoListNewPedidoToAttach.getIdpedido());
                attachedPedidoListNew.add(pedidoListNewPedidoToAttach);
            }
            pedidoListNew = attachedPedidoListNew;
            tienda.setPedidoList(pedidoListNew);
            List<Articulo> attachedArticuloListNew = new ArrayList<Articulo>();
            for (Articulo articuloListNewArticuloToAttach : articuloListNew) {
                articuloListNewArticuloToAttach = em.getReference(articuloListNewArticuloToAttach.getClass(), articuloListNewArticuloToAttach.getCodigo());
                attachedArticuloListNew.add(articuloListNewArticuloToAttach);
            }
            articuloListNew = attachedArticuloListNew;
            tienda.setArticuloList(articuloListNew);
            tienda = em.merge(tienda);
            for (Pedido pedidoListNewPedido : pedidoListNew) {
                if (!pedidoListOld.contains(pedidoListNewPedido)) {
                    Tienda oldIdtiendaOfPedidoListNewPedido = pedidoListNewPedido.getIdtienda();
                    pedidoListNewPedido.setIdtienda(tienda);
                    pedidoListNewPedido = em.merge(pedidoListNewPedido);
                    if (oldIdtiendaOfPedidoListNewPedido != null && !oldIdtiendaOfPedidoListNewPedido.equals(tienda)) {
                        oldIdtiendaOfPedidoListNewPedido.getPedidoList().remove(pedidoListNewPedido);
                        oldIdtiendaOfPedidoListNewPedido = em.merge(oldIdtiendaOfPedidoListNewPedido);
                    }
                }
            }
            for (Articulo articuloListNewArticulo : articuloListNew) {
                if (!articuloListOld.contains(articuloListNewArticulo)) {
                    Tienda oldIdTiendaOfArticuloListNewArticulo = articuloListNewArticulo.getIdTienda();
                    articuloListNewArticulo.setIdTienda(tienda);
                    articuloListNewArticulo = em.merge(articuloListNewArticulo);
                    if (oldIdTiendaOfArticuloListNewArticulo != null && !oldIdTiendaOfArticuloListNewArticulo.equals(tienda)) {
                        oldIdTiendaOfArticuloListNewArticulo.getArticuloList().remove(articuloListNewArticulo);
                        oldIdTiendaOfArticuloListNewArticulo = em.merge(oldIdTiendaOfArticuloListNewArticulo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tienda.getCodigo();
                if (findTienda(id) == null) {
                    throw new NonexistentEntityException("The tienda with id " + id + " no longer exists.");
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
            Tienda tienda;
            try {
                tienda = em.getReference(Tienda.class, id);
                tienda.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tienda with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Pedido> pedidoListOrphanCheck = tienda.getPedidoList();
            for (Pedido pedidoListOrphanCheckPedido : pedidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tienda (" + tienda + ") cannot be destroyed since the Pedido " + pedidoListOrphanCheckPedido + " in its pedidoList field has a non-nullable idtienda field.");
            }
            List<Articulo> articuloListOrphanCheck = tienda.getArticuloList();
            for (Articulo articuloListOrphanCheckArticulo : articuloListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tienda (" + tienda + ") cannot be destroyed since the Articulo " + articuloListOrphanCheckArticulo + " in its articuloList field has a non-nullable idTienda field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tienda);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tienda> findTiendaEntities() {
        return findTiendaEntities(true, -1, -1);
    }

    public List<Tienda> findTiendaEntities(int maxResults, int firstResult) {
        return findTiendaEntities(false, maxResults, firstResult);
    }

    private List<Tienda> findTiendaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tienda.class));
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

    public Tienda findTienda(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tienda.class, id);
        } finally {
            em.close();
        }
    }

    public int getTiendaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tienda> rt = cq.from(Tienda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
