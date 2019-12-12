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
import ec.edu.monster.entidades.XeEstEstado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ec.edu.monster.entidades.XeusuUsuar;
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
public class XeEstEstadoJpaController implements Serializable {

    public XeEstEstadoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XeEstEstado xeEstEstado) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (xeEstEstado.getXeusuUsuarCollection() == null) {
            xeEstEstado.setXeusuUsuarCollection(new ArrayList<XeusuUsuar>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<XeusuUsuar> attachedXeusuUsuarCollection = new ArrayList<XeusuUsuar>();
            for (XeusuUsuar xeusuUsuarCollectionXeusuUsuarToAttach : xeEstEstado.getXeusuUsuarCollection()) {
                xeusuUsuarCollectionXeusuUsuarToAttach = em.getReference(xeusuUsuarCollectionXeusuUsuarToAttach.getClass(), xeusuUsuarCollectionXeusuUsuarToAttach.getXeusuUsuarPK());
                attachedXeusuUsuarCollection.add(xeusuUsuarCollectionXeusuUsuarToAttach);
            }
            xeEstEstado.setXeusuUsuarCollection(attachedXeusuUsuarCollection);
            em.persist(xeEstEstado);
            for (XeusuUsuar xeusuUsuarCollectionXeusuUsuar : xeEstEstado.getXeusuUsuarCollection()) {
                XeEstEstado oldXeuestCodigoOfXeusuUsuarCollectionXeusuUsuar = xeusuUsuarCollectionXeusuUsuar.getXeuestCodigo();
                xeusuUsuarCollectionXeusuUsuar.setXeuestCodigo(xeEstEstado);
                xeusuUsuarCollectionXeusuUsuar = em.merge(xeusuUsuarCollectionXeusuUsuar);
                if (oldXeuestCodigoOfXeusuUsuarCollectionXeusuUsuar != null) {
                    oldXeuestCodigoOfXeusuUsuarCollectionXeusuUsuar.getXeusuUsuarCollection().remove(xeusuUsuarCollectionXeusuUsuar);
                    oldXeuestCodigoOfXeusuUsuarCollectionXeusuUsuar = em.merge(oldXeuestCodigoOfXeusuUsuarCollectionXeusuUsuar);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findXeEstEstado(xeEstEstado.getXeuestCodigo()) != null) {
                throw new PreexistingEntityException("XeEstEstado " + xeEstEstado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XeEstEstado xeEstEstado) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            XeEstEstado persistentXeEstEstado = em.find(XeEstEstado.class, xeEstEstado.getXeuestCodigo());
            Collection<XeusuUsuar> xeusuUsuarCollectionOld = persistentXeEstEstado.getXeusuUsuarCollection();
            Collection<XeusuUsuar> xeusuUsuarCollectionNew = xeEstEstado.getXeusuUsuarCollection();
            List<String> illegalOrphanMessages = null;
            for (XeusuUsuar xeusuUsuarCollectionOldXeusuUsuar : xeusuUsuarCollectionOld) {
                if (!xeusuUsuarCollectionNew.contains(xeusuUsuarCollectionOldXeusuUsuar)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XeusuUsuar " + xeusuUsuarCollectionOldXeusuUsuar + " since its xeuestCodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<XeusuUsuar> attachedXeusuUsuarCollectionNew = new ArrayList<XeusuUsuar>();
            for (XeusuUsuar xeusuUsuarCollectionNewXeusuUsuarToAttach : xeusuUsuarCollectionNew) {
                xeusuUsuarCollectionNewXeusuUsuarToAttach = em.getReference(xeusuUsuarCollectionNewXeusuUsuarToAttach.getClass(), xeusuUsuarCollectionNewXeusuUsuarToAttach.getXeusuUsuarPK());
                attachedXeusuUsuarCollectionNew.add(xeusuUsuarCollectionNewXeusuUsuarToAttach);
            }
            xeusuUsuarCollectionNew = attachedXeusuUsuarCollectionNew;
            xeEstEstado.setXeusuUsuarCollection(xeusuUsuarCollectionNew);
            xeEstEstado = em.merge(xeEstEstado);
            for (XeusuUsuar xeusuUsuarCollectionNewXeusuUsuar : xeusuUsuarCollectionNew) {
                if (!xeusuUsuarCollectionOld.contains(xeusuUsuarCollectionNewXeusuUsuar)) {
                    XeEstEstado oldXeuestCodigoOfXeusuUsuarCollectionNewXeusuUsuar = xeusuUsuarCollectionNewXeusuUsuar.getXeuestCodigo();
                    xeusuUsuarCollectionNewXeusuUsuar.setXeuestCodigo(xeEstEstado);
                    xeusuUsuarCollectionNewXeusuUsuar = em.merge(xeusuUsuarCollectionNewXeusuUsuar);
                    if (oldXeuestCodigoOfXeusuUsuarCollectionNewXeusuUsuar != null && !oldXeuestCodigoOfXeusuUsuarCollectionNewXeusuUsuar.equals(xeEstEstado)) {
                        oldXeuestCodigoOfXeusuUsuarCollectionNewXeusuUsuar.getXeusuUsuarCollection().remove(xeusuUsuarCollectionNewXeusuUsuar);
                        oldXeuestCodigoOfXeusuUsuarCollectionNewXeusuUsuar = em.merge(oldXeuestCodigoOfXeusuUsuarCollectionNewXeusuUsuar);
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
                String id = xeEstEstado.getXeuestCodigo();
                if (findXeEstEstado(id) == null) {
                    throw new NonexistentEntityException("The xeEstEstado with id " + id + " no longer exists.");
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
            XeEstEstado xeEstEstado;
            try {
                xeEstEstado = em.getReference(XeEstEstado.class, id);
                xeEstEstado.getXeuestCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xeEstEstado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<XeusuUsuar> xeusuUsuarCollectionOrphanCheck = xeEstEstado.getXeusuUsuarCollection();
            for (XeusuUsuar xeusuUsuarCollectionOrphanCheckXeusuUsuar : xeusuUsuarCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XeEstEstado (" + xeEstEstado + ") cannot be destroyed since the XeusuUsuar " + xeusuUsuarCollectionOrphanCheckXeusuUsuar + " in its xeusuUsuarCollection field has a non-nullable xeuestCodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(xeEstEstado);
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

    public List<XeEstEstado> findXeEstEstadoEntities() {
        return findXeEstEstadoEntities(true, -1, -1);
    }

    public List<XeEstEstado> findXeEstEstadoEntities(int maxResults, int firstResult) {
        return findXeEstEstadoEntities(false, maxResults, firstResult);
    }

    private List<XeEstEstado> findXeEstEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XeEstEstado.class));
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

    public XeEstEstado findXeEstEstado(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XeEstEstado.class, id);
        } finally {
            em.close();
        }
    }

    public int getXeEstEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XeEstEstado> rt = cq.from(XeEstEstado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
