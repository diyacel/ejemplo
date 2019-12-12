/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.dao;

import ec.edu.monster.dao.exceptions.IllegalOrphanException;
import ec.edu.monster.dao.exceptions.NonexistentEntityException;
import ec.edu.monster.dao.exceptions.PreexistingEntityException;
import ec.edu.monster.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ec.edu.monster.entidades.PerfilOpcion;
import ec.edu.monster.entidades.XeOpcOpcion;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Diego
 */
public class XeOpcOpcionJpaController implements Serializable {

    public XeOpcOpcionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XeOpcOpcion xeOpcOpcion) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (xeOpcOpcion.getPerfilOpcionCollection() == null) {
            xeOpcOpcion.setPerfilOpcionCollection(new ArrayList<PerfilOpcion>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<PerfilOpcion> attachedPerfilOpcionCollection = new ArrayList<PerfilOpcion>();
            for (PerfilOpcion perfilOpcionCollectionPerfilOpcionToAttach : xeOpcOpcion.getPerfilOpcionCollection()) {
                perfilOpcionCollectionPerfilOpcionToAttach = em.getReference(perfilOpcionCollectionPerfilOpcionToAttach.getClass(), perfilOpcionCollectionPerfilOpcionToAttach.getPopFechaAsignacion());
                attachedPerfilOpcionCollection.add(perfilOpcionCollectionPerfilOpcionToAttach);
            }
            xeOpcOpcion.setPerfilOpcionCollection(attachedPerfilOpcionCollection);
            em.persist(xeOpcOpcion);
            for (PerfilOpcion perfilOpcionCollectionPerfilOpcion : xeOpcOpcion.getPerfilOpcionCollection()) {
                XeOpcOpcion oldXeopcCodigoOfPerfilOpcionCollectionPerfilOpcion = perfilOpcionCollectionPerfilOpcion.getXeopcCodigo();
                perfilOpcionCollectionPerfilOpcion.setXeopcCodigo(xeOpcOpcion);
                perfilOpcionCollectionPerfilOpcion = em.merge(perfilOpcionCollectionPerfilOpcion);
                if (oldXeopcCodigoOfPerfilOpcionCollectionPerfilOpcion != null) {
                    oldXeopcCodigoOfPerfilOpcionCollectionPerfilOpcion.getPerfilOpcionCollection().remove(perfilOpcionCollectionPerfilOpcion);
                    oldXeopcCodigoOfPerfilOpcionCollectionPerfilOpcion = em.merge(oldXeopcCodigoOfPerfilOpcionCollectionPerfilOpcion);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findXeOpcOpcion(xeOpcOpcion.getXeopcCodigo()) != null) {
                throw new PreexistingEntityException("XeOpcOpcion " + xeOpcOpcion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XeOpcOpcion xeOpcOpcion) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            XeOpcOpcion persistentXeOpcOpcion = em.find(XeOpcOpcion.class, xeOpcOpcion.getXeopcCodigo());
            Collection<PerfilOpcion> perfilOpcionCollectionOld = persistentXeOpcOpcion.getPerfilOpcionCollection();
            Collection<PerfilOpcion> perfilOpcionCollectionNew = xeOpcOpcion.getPerfilOpcionCollection();
            List<String> illegalOrphanMessages = null;
            for (PerfilOpcion perfilOpcionCollectionOldPerfilOpcion : perfilOpcionCollectionOld) {
                if (!perfilOpcionCollectionNew.contains(perfilOpcionCollectionOldPerfilOpcion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PerfilOpcion " + perfilOpcionCollectionOldPerfilOpcion + " since its xeopcCodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<PerfilOpcion> attachedPerfilOpcionCollectionNew = new ArrayList<PerfilOpcion>();
            for (PerfilOpcion perfilOpcionCollectionNewPerfilOpcionToAttach : perfilOpcionCollectionNew) {
                perfilOpcionCollectionNewPerfilOpcionToAttach = em.getReference(perfilOpcionCollectionNewPerfilOpcionToAttach.getClass(), perfilOpcionCollectionNewPerfilOpcionToAttach.getPopFechaAsignacion());
                attachedPerfilOpcionCollectionNew.add(perfilOpcionCollectionNewPerfilOpcionToAttach);
            }
            perfilOpcionCollectionNew = attachedPerfilOpcionCollectionNew;
            xeOpcOpcion.setPerfilOpcionCollection(perfilOpcionCollectionNew);
            xeOpcOpcion = em.merge(xeOpcOpcion);
            for (PerfilOpcion perfilOpcionCollectionNewPerfilOpcion : perfilOpcionCollectionNew) {
                if (!perfilOpcionCollectionOld.contains(perfilOpcionCollectionNewPerfilOpcion)) {
                    XeOpcOpcion oldXeopcCodigoOfPerfilOpcionCollectionNewPerfilOpcion = perfilOpcionCollectionNewPerfilOpcion.getXeopcCodigo();
                    perfilOpcionCollectionNewPerfilOpcion.setXeopcCodigo(xeOpcOpcion);
                    perfilOpcionCollectionNewPerfilOpcion = em.merge(perfilOpcionCollectionNewPerfilOpcion);
                    if (oldXeopcCodigoOfPerfilOpcionCollectionNewPerfilOpcion != null && !oldXeopcCodigoOfPerfilOpcionCollectionNewPerfilOpcion.equals(xeOpcOpcion)) {
                        oldXeopcCodigoOfPerfilOpcionCollectionNewPerfilOpcion.getPerfilOpcionCollection().remove(perfilOpcionCollectionNewPerfilOpcion);
                        oldXeopcCodigoOfPerfilOpcionCollectionNewPerfilOpcion = em.merge(oldXeopcCodigoOfPerfilOpcionCollectionNewPerfilOpcion);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = xeOpcOpcion.getXeopcCodigo();
                if (findXeOpcOpcion(id) == null) {
                    throw new NonexistentEntityException("The xeOpcOpcion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            XeOpcOpcion xeOpcOpcion;
            try {
                xeOpcOpcion = em.getReference(XeOpcOpcion.class, id);
                xeOpcOpcion.getXeopcCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xeOpcOpcion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<PerfilOpcion> perfilOpcionCollectionOrphanCheck = xeOpcOpcion.getPerfilOpcionCollection();
            for (PerfilOpcion perfilOpcionCollectionOrphanCheckPerfilOpcion : perfilOpcionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XeOpcOpcion (" + xeOpcOpcion + ") cannot be destroyed since the PerfilOpcion " + perfilOpcionCollectionOrphanCheckPerfilOpcion + " in its perfilOpcionCollection field has a non-nullable xeopcCodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(xeOpcOpcion);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<XeOpcOpcion> findXeOpcOpcionEntities() {
        return findXeOpcOpcionEntities(true, -1, -1);
    }

    public List<XeOpcOpcion> findXeOpcOpcionEntities(int maxResults, int firstResult) {
        return findXeOpcOpcionEntities(false, maxResults, firstResult);
    }

    private List<XeOpcOpcion> findXeOpcOpcionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XeOpcOpcion.class));
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

    public XeOpcOpcion findXeOpcOpcion(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XeOpcOpcion.class, id);
        } finally {
            em.close();
        }
    }

    public int getXeOpcOpcionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XeOpcOpcion> rt = cq.from(XeOpcOpcion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
