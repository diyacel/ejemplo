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
import ec.edu.monster.entidades.PeempEmpleado;
import ec.edu.monster.entidades.PeescEstciv;
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
public class PeescEstcivJpaController implements Serializable {

    public PeescEstcivJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PeescEstciv peescEstciv) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (peescEstciv.getPeempEmpleadoCollection() == null) {
            peescEstciv.setPeempEmpleadoCollection(new ArrayList<PeempEmpleado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<PeempEmpleado> attachedPeempEmpleadoCollection = new ArrayList<PeempEmpleado>();
            for (PeempEmpleado peempEmpleadoCollectionPeempEmpleadoToAttach : peescEstciv.getPeempEmpleadoCollection()) {
                peempEmpleadoCollectionPeempEmpleadoToAttach = em.getReference(peempEmpleadoCollectionPeempEmpleadoToAttach.getClass(), peempEmpleadoCollectionPeempEmpleadoToAttach.getPeempCodig());
                attachedPeempEmpleadoCollection.add(peempEmpleadoCollectionPeempEmpleadoToAttach);
            }
            peescEstciv.setPeempEmpleadoCollection(attachedPeempEmpleadoCollection);
            em.persist(peescEstciv);
            for (PeempEmpleado peempEmpleadoCollectionPeempEmpleado : peescEstciv.getPeempEmpleadoCollection()) {
                PeescEstciv oldPeescCodigoOfPeempEmpleadoCollectionPeempEmpleado = peempEmpleadoCollectionPeempEmpleado.getPeescCodigo();
                peempEmpleadoCollectionPeempEmpleado.setPeescCodigo(peescEstciv);
                peempEmpleadoCollectionPeempEmpleado = em.merge(peempEmpleadoCollectionPeempEmpleado);
                if (oldPeescCodigoOfPeempEmpleadoCollectionPeempEmpleado != null) {
                    oldPeescCodigoOfPeempEmpleadoCollectionPeempEmpleado.getPeempEmpleadoCollection().remove(peempEmpleadoCollectionPeempEmpleado);
                    oldPeescCodigoOfPeempEmpleadoCollectionPeempEmpleado = em.merge(oldPeescCodigoOfPeempEmpleadoCollectionPeempEmpleado);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPeescEstciv(peescEstciv.getPeescCodigo()) != null) {
                throw new PreexistingEntityException("PeescEstciv " + peescEstciv + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PeescEstciv peescEstciv) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PeescEstciv persistentPeescEstciv = em.find(PeescEstciv.class, peescEstciv.getPeescCodigo());
            Collection<PeempEmpleado> peempEmpleadoCollectionOld = persistentPeescEstciv.getPeempEmpleadoCollection();
            Collection<PeempEmpleado> peempEmpleadoCollectionNew = peescEstciv.getPeempEmpleadoCollection();
            List<String> illegalOrphanMessages = null;
            for (PeempEmpleado peempEmpleadoCollectionOldPeempEmpleado : peempEmpleadoCollectionOld) {
                if (!peempEmpleadoCollectionNew.contains(peempEmpleadoCollectionOldPeempEmpleado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PeempEmpleado " + peempEmpleadoCollectionOldPeempEmpleado + " since its peescCodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<PeempEmpleado> attachedPeempEmpleadoCollectionNew = new ArrayList<PeempEmpleado>();
            for (PeempEmpleado peempEmpleadoCollectionNewPeempEmpleadoToAttach : peempEmpleadoCollectionNew) {
                peempEmpleadoCollectionNewPeempEmpleadoToAttach = em.getReference(peempEmpleadoCollectionNewPeempEmpleadoToAttach.getClass(), peempEmpleadoCollectionNewPeempEmpleadoToAttach.getPeempCodig());
                attachedPeempEmpleadoCollectionNew.add(peempEmpleadoCollectionNewPeempEmpleadoToAttach);
            }
            peempEmpleadoCollectionNew = attachedPeempEmpleadoCollectionNew;
            peescEstciv.setPeempEmpleadoCollection(peempEmpleadoCollectionNew);
            peescEstciv = em.merge(peescEstciv);
            for (PeempEmpleado peempEmpleadoCollectionNewPeempEmpleado : peempEmpleadoCollectionNew) {
                if (!peempEmpleadoCollectionOld.contains(peempEmpleadoCollectionNewPeempEmpleado)) {
                    PeescEstciv oldPeescCodigoOfPeempEmpleadoCollectionNewPeempEmpleado = peempEmpleadoCollectionNewPeempEmpleado.getPeescCodigo();
                    peempEmpleadoCollectionNewPeempEmpleado.setPeescCodigo(peescEstciv);
                    peempEmpleadoCollectionNewPeempEmpleado = em.merge(peempEmpleadoCollectionNewPeempEmpleado);
                    if (oldPeescCodigoOfPeempEmpleadoCollectionNewPeempEmpleado != null && !oldPeescCodigoOfPeempEmpleadoCollectionNewPeempEmpleado.equals(peescEstciv)) {
                        oldPeescCodigoOfPeempEmpleadoCollectionNewPeempEmpleado.getPeempEmpleadoCollection().remove(peempEmpleadoCollectionNewPeempEmpleado);
                        oldPeescCodigoOfPeempEmpleadoCollectionNewPeempEmpleado = em.merge(oldPeescCodigoOfPeempEmpleadoCollectionNewPeempEmpleado);
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
                String id = peescEstciv.getPeescCodigo();
                if (findPeescEstciv(id) == null) {
                    throw new NonexistentEntityException("The peescEstciv with id " + id + " no longer exists.");
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
            PeescEstciv peescEstciv;
            try {
                peescEstciv = em.getReference(PeescEstciv.class, id);
                peescEstciv.getPeescCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The peescEstciv with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<PeempEmpleado> peempEmpleadoCollectionOrphanCheck = peescEstciv.getPeempEmpleadoCollection();
            for (PeempEmpleado peempEmpleadoCollectionOrphanCheckPeempEmpleado : peempEmpleadoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PeescEstciv (" + peescEstciv + ") cannot be destroyed since the PeempEmpleado " + peempEmpleadoCollectionOrphanCheckPeempEmpleado + " in its peempEmpleadoCollection field has a non-nullable peescCodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(peescEstciv);
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

    public List<PeescEstciv> findPeescEstcivEntities() {
        return findPeescEstcivEntities(true, -1, -1);
    }

    public List<PeescEstciv> findPeescEstcivEntities(int maxResults, int firstResult) {
        return findPeescEstcivEntities(false, maxResults, firstResult);
    }

    private List<PeescEstciv> findPeescEstcivEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PeescEstciv.class));
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

    public PeescEstciv findPeescEstciv(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PeescEstciv.class, id);
        } finally {
            em.close();
        }
    }

    public int getPeescEstcivCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PeescEstciv> rt = cq.from(PeescEstciv.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
