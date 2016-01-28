/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import controladores.exceptions.IllegalOrphanException;
import controladores.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelos.Tienda;
import modelos.Factura;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelos.ArticuloTalla;
import modelos.Pedido;

/**
 *
 * @author isaac
 */
public class PedidoJpaController implements Serializable {

    public PedidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pedido pedido) {
        if (pedido.getFacturaList() == null) {
            pedido.setFacturaList(new ArrayList<Factura>());
        }
        if (pedido.getArticuloTallaList() == null) {
            pedido.setArticuloTallaList(new ArrayList<ArticuloTalla>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tienda idtienda = pedido.getIdtienda();
            if (idtienda != null) {
                idtienda = em.getReference(idtienda.getClass(), idtienda.getCodigo());
                pedido.setIdtienda(idtienda);
            }
            List<Factura> attachedFacturaList = new ArrayList<Factura>();
            for (Factura facturaListFacturaToAttach : pedido.getFacturaList()) {
                facturaListFacturaToAttach = em.getReference(facturaListFacturaToAttach.getClass(), facturaListFacturaToAttach.getIdfactura());
                attachedFacturaList.add(facturaListFacturaToAttach);
            }
            pedido.setFacturaList(attachedFacturaList);
            List<ArticuloTalla> attachedArticuloTallaList = new ArrayList<ArticuloTalla>();
            for (ArticuloTalla articuloTallaListArticuloTallaToAttach : pedido.getArticuloTallaList()) {
                articuloTallaListArticuloTallaToAttach = em.getReference(articuloTallaListArticuloTallaToAttach.getClass(), articuloTallaListArticuloTallaToAttach.getIdarticuloTalla());
                attachedArticuloTallaList.add(articuloTallaListArticuloTallaToAttach);
            }
            pedido.setArticuloTallaList(attachedArticuloTallaList);
            em.persist(pedido);
            if (idtienda != null) {
                idtienda.getPedidoList().add(pedido);
                idtienda = em.merge(idtienda);
            }
            for (Factura facturaListFactura : pedido.getFacturaList()) {
                Pedido oldIdPedidoOfFacturaListFactura = facturaListFactura.getIdPedido();
                facturaListFactura.setIdPedido(pedido);
                facturaListFactura = em.merge(facturaListFactura);
                if (oldIdPedidoOfFacturaListFactura != null) {
                    oldIdPedidoOfFacturaListFactura.getFacturaList().remove(facturaListFactura);
                    oldIdPedidoOfFacturaListFactura = em.merge(oldIdPedidoOfFacturaListFactura);
                }
            }
            for (ArticuloTalla articuloTallaListArticuloTalla : pedido.getArticuloTallaList()) {
                Pedido oldIdPedidoOfArticuloTallaListArticuloTalla = articuloTallaListArticuloTalla.getIdPedido();
                articuloTallaListArticuloTalla.setIdPedido(pedido);
                articuloTallaListArticuloTalla = em.merge(articuloTallaListArticuloTalla);
                if (oldIdPedidoOfArticuloTallaListArticuloTalla != null) {
                    oldIdPedidoOfArticuloTallaListArticuloTalla.getArticuloTallaList().remove(articuloTallaListArticuloTalla);
                    oldIdPedidoOfArticuloTallaListArticuloTalla = em.merge(oldIdPedidoOfArticuloTallaListArticuloTalla);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pedido pedido) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido persistentPedido = em.find(Pedido.class, pedido.getIdpedido());
            Tienda idtiendaOld = persistentPedido.getIdtienda();
            Tienda idtiendaNew = pedido.getIdtienda();
            List<Factura> facturaListOld = persistentPedido.getFacturaList();
            List<Factura> facturaListNew = pedido.getFacturaList();
            List<ArticuloTalla> articuloTallaListOld = persistentPedido.getArticuloTallaList();
            List<ArticuloTalla> articuloTallaListNew = pedido.getArticuloTallaList();
            List<String> illegalOrphanMessages = null;
            for (Factura facturaListOldFactura : facturaListOld) {
                if (!facturaListNew.contains(facturaListOldFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Factura " + facturaListOldFactura + " since its idPedido field is not nullable.");
                }
            }
            for (ArticuloTalla articuloTallaListOldArticuloTalla : articuloTallaListOld) {
                if (!articuloTallaListNew.contains(articuloTallaListOldArticuloTalla)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ArticuloTalla " + articuloTallaListOldArticuloTalla + " since its idPedido field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idtiendaNew != null) {
                idtiendaNew = em.getReference(idtiendaNew.getClass(), idtiendaNew.getCodigo());
                pedido.setIdtienda(idtiendaNew);
            }
            List<Factura> attachedFacturaListNew = new ArrayList<Factura>();
            for (Factura facturaListNewFacturaToAttach : facturaListNew) {
                facturaListNewFacturaToAttach = em.getReference(facturaListNewFacturaToAttach.getClass(), facturaListNewFacturaToAttach.getIdfactura());
                attachedFacturaListNew.add(facturaListNewFacturaToAttach);
            }
            facturaListNew = attachedFacturaListNew;
            pedido.setFacturaList(facturaListNew);
            List<ArticuloTalla> attachedArticuloTallaListNew = new ArrayList<ArticuloTalla>();
            for (ArticuloTalla articuloTallaListNewArticuloTallaToAttach : articuloTallaListNew) {
                articuloTallaListNewArticuloTallaToAttach = em.getReference(articuloTallaListNewArticuloTallaToAttach.getClass(), articuloTallaListNewArticuloTallaToAttach.getIdarticuloTalla());
                attachedArticuloTallaListNew.add(articuloTallaListNewArticuloTallaToAttach);
            }
            articuloTallaListNew = attachedArticuloTallaListNew;
            pedido.setArticuloTallaList(articuloTallaListNew);
            pedido = em.merge(pedido);
            if (idtiendaOld != null && !idtiendaOld.equals(idtiendaNew)) {
                idtiendaOld.getPedidoList().remove(pedido);
                idtiendaOld = em.merge(idtiendaOld);
            }
            if (idtiendaNew != null && !idtiendaNew.equals(idtiendaOld)) {
                idtiendaNew.getPedidoList().add(pedido);
                idtiendaNew = em.merge(idtiendaNew);
            }
            for (Factura facturaListNewFactura : facturaListNew) {
                if (!facturaListOld.contains(facturaListNewFactura)) {
                    Pedido oldIdPedidoOfFacturaListNewFactura = facturaListNewFactura.getIdPedido();
                    facturaListNewFactura.setIdPedido(pedido);
                    facturaListNewFactura = em.merge(facturaListNewFactura);
                    if (oldIdPedidoOfFacturaListNewFactura != null && !oldIdPedidoOfFacturaListNewFactura.equals(pedido)) {
                        oldIdPedidoOfFacturaListNewFactura.getFacturaList().remove(facturaListNewFactura);
                        oldIdPedidoOfFacturaListNewFactura = em.merge(oldIdPedidoOfFacturaListNewFactura);
                    }
                }
            }
            for (ArticuloTalla articuloTallaListNewArticuloTalla : articuloTallaListNew) {
                if (!articuloTallaListOld.contains(articuloTallaListNewArticuloTalla)) {
                    Pedido oldIdPedidoOfArticuloTallaListNewArticuloTalla = articuloTallaListNewArticuloTalla.getIdPedido();
                    articuloTallaListNewArticuloTalla.setIdPedido(pedido);
                    articuloTallaListNewArticuloTalla = em.merge(articuloTallaListNewArticuloTalla);
                    if (oldIdPedidoOfArticuloTallaListNewArticuloTalla != null && !oldIdPedidoOfArticuloTallaListNewArticuloTalla.equals(pedido)) {
                        oldIdPedidoOfArticuloTallaListNewArticuloTalla.getArticuloTallaList().remove(articuloTallaListNewArticuloTalla);
                        oldIdPedidoOfArticuloTallaListNewArticuloTalla = em.merge(oldIdPedidoOfArticuloTallaListNewArticuloTalla);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pedido.getIdpedido();
                if (findPedido(id) == null) {
                    throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido pedido;
            try {
                pedido = em.getReference(Pedido.class, id);
                pedido.getIdpedido();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Factura> facturaListOrphanCheck = pedido.getFacturaList();
            for (Factura facturaListOrphanCheckFactura : facturaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pedido (" + pedido + ") cannot be destroyed since the Factura " + facturaListOrphanCheckFactura + " in its facturaList field has a non-nullable idPedido field.");
            }
            List<ArticuloTalla> articuloTallaListOrphanCheck = pedido.getArticuloTallaList();
            for (ArticuloTalla articuloTallaListOrphanCheckArticuloTalla : articuloTallaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pedido (" + pedido + ") cannot be destroyed since the ArticuloTalla " + articuloTallaListOrphanCheckArticuloTalla + " in its articuloTallaList field has a non-nullable idPedido field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tienda idtienda = pedido.getIdtienda();
            if (idtienda != null) {
                idtienda.getPedidoList().remove(pedido);
                idtienda = em.merge(idtienda);
            }
            em.remove(pedido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pedido> findPedidoEntities() {
        return findPedidoEntities(true, -1, -1);
    }

    public List<Pedido> findPedidoEntities(int maxResults, int firstResult) {
        return findPedidoEntities(false, maxResults, firstResult);
    }

    private List<Pedido> findPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pedido.class));
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

    public Pedido findPedido(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pedido> rt = cq.from(Pedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
