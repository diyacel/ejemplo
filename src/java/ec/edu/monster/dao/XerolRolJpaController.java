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
import ec.edu.monster.entidades.RolUsuario;
import ec.edu.monster.entidades.XerolRol;
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
public class XerolRolJpaController implements Serializable {

    public XerolRolJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XerolRol xerolRol) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (xerolRol.getRolUsuarioCollection() == null) {
            xerolRol.setRolUsuarioCollection(new ArrayList<RolUsuario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<RolUsuario> attachedRolUsuarioCollection = new ArrayList<RolUsuario>();
            for (RolUsuario rolUsuarioCollectionRolUsuarioToAttach : xerolRol.getRolUsuarioCollection()) {
                rolUsuarioCollectionRolUsuarioToAttach = em.getReference(rolUsuarioCollectionRolUsuarioToAttach.getClass(), rolUsuarioCollectionRolUsuarioToAttach.getRusFechaAsignacion());
                attachedRolUsuarioCollection.add(rolUsuarioCollectionRolUsuarioToAttach);
            }
            xerolRol.setRolUsuarioCollection(attachedRolUsuarioCollection);
            em.persist(xerolRol);
            for (RolUsuario rolUsuarioCollectionRolUsuario : xerolRol.getRolUsuarioCollection()) {
                XerolRol oldXerolCodigoOfRolUsuarioCollectionRolUsuario = rolUsuarioCollectionRolUsuario.getXerolCodigo();
                rolUsuarioCollectionRolUsuario.setXerolCodigo(xerolRol);
                rolUsuarioCollectionRolUsuario = em.merge(rolUsuarioCollectionRolUsuario);
                if (oldXerolCodigoOfRolUsuarioCollectionRolUsuario != null) {
                    oldXerolCodigoOfRolUsuarioCollectionRolUsuario.getRolUsuarioCollection().remove(rolUsuarioCollectionRolUsuario);
                    oldXerolCodigoOfRolUsuarioCollectionRolUsuario = em.merge(oldXerolCodigoOfRolUsuarioCollectionRolUsuario);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findXerolRol(xerolRol.getXerolCodigo()) != null) {
                throw new PreexistingEntityException("XerolRol " + xerolRol + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XerolRol xerolRol) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            XerolRol persistentXerolRol = em.find(XerolRol.class, xerolRol.getXerolCodigo());
            Collection<RolUsuario> rolUsuarioCollectionOld = persistentXerolRol.getRolUsuarioCollection();
            Collection<RolUsuario> rolUsuarioCollectionNew = xerolRol.getRolUsuarioCollection();
            List<String> illegalOrphanMessages = null;
            for (RolUsuario rolUsuarioCollectionOldRolUsuario : rolUsuarioCollectionOld) {
                if (!rolUsuarioCollectionNew.contains(rolUsuarioCollectionOldRolUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RolUsuario " + rolUsuarioCollectionOldRolUsuario + " since its xerolCodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<RolUsuario> attachedRolUsuarioCollectionNew = new ArrayList<RolUsuario>();
            for (RolUsuario rolUsuarioCollectionNewRolUsuarioToAttach : rolUsuarioCollectionNew) {
                rolUsuarioCollectionNewRolUsuarioToAttach = em.getReference(rolUsuarioCollectionNewRolUsuarioToAttach.getClass(), rolUsuarioCollectionNewRolUsuarioToAttach.getRusFechaAsignacion());
                attachedRolUsuarioCollectionNew.add(rolUsuarioCollectionNewRolUsuarioToAttach);
            }
            rolUsuarioCollectionNew = attachedRolUsuarioCollectionNew;
            xerolRol.setRolUsuarioCollection(rolUsuarioCollectionNew);
            xerolRol = em.merge(xerolRol);
            for (RolUsuario rolUsuarioCollectionNewRolUsuario : rolUsuarioCollectionNew) {
                if (!rolUsuarioCollectionOld.contains(rolUsuarioCollectionNewRolUsuario)) {
                    XerolRol oldXerolCodigoOfRolUsuarioCollectionNewRolUsuario = rolUsuarioCollectionNewRolUsuario.getXerolCodigo();
                    rolUsuarioCollectionNewRolUsuario.setXerolCodigo(xerolRol);
                    rolUsuarioCollectionNewRolUsuario = em.merge(rolUsuarioCollectionNewRolUsuario);
                    if (oldXerolCodigoOfRolUsuarioCollectionNewRolUsuario != null && !oldXerolCodigoOfRolUsuarioCollectionNewRolUsuario.equals(xerolRol)) {
                        oldXerolCodigoOfRolUsuarioCollectionNewRolUsuario.getRolUsuarioCollection().remove(rolUsuarioCollectionNewRolUsuario);
                        oldXerolCodigoOfRolUsuarioCollectionNewRolUsuario = em.merge(oldXerolCodigoOfRolUsuarioCollectionNewRolUsuario);
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
                String id = xerolRol.getXerolCodigo();
                if (findXerolRol(id) == null) {
                    throw new NonexistentEntityException("The xerolRol with id " + id + " no longer exists.");
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
            XerolRol xerolRol;
            try {
                xerolRol = em.getReference(XerolRol.class, id);
                xerolRol.getXerolCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xerolRol with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<RolUsuario> rolUsuarioCollectionOrphanCheck = xerolRol.getRolUsuarioCollection();
            for (RolUsuario rolUsuarioCollectionOrphanCheckRolUsuario : rolUsuarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XerolRol (" + xerolRol + ") cannot be destroyed since the RolUsuario " + rolUsuarioCollectionOrphanCheckRolUsuario + " in its rolUsuarioCollection field has a non-nullable xerolCodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(xerolRol);
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

    public List<XerolRol> findXerolRolEntities() {
        return findXerolRolEntities(true, -1, -1);
    }

    public List<XerolRol> findXerolRolEntities(int maxResults, int firstResult) {
        return findXerolRolEntities(false, maxResults, firstResult);
    }

    private List<XerolRol> findXerolRolEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XerolRol.class));
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

    public XerolRol findXerolRol(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XerolRol.class, id);
        } finally {
            em.close();
        }
    }

    public int getXerolRolCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XerolRol> rt = cq.from(XerolRol.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
