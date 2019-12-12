/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.dao;

import ec.edu.monster.dao.exceptions.NonexistentEntityException;
import ec.edu.monster.dao.exceptions.PreexistingEntityException;
import ec.edu.monster.dao.exceptions.RollbackFailureException;
import ec.edu.monster.entidades.PecarCargo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ec.edu.monster.entidades.PeempEmpleado;
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
public class PecarCargoJpaController implements Serializable {

    public PecarCargoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PecarCargo pecarCargo) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (pecarCargo.getPeempEmpleadoCollection() == null) {
            pecarCargo.setPeempEmpleadoCollection(new ArrayList<PeempEmpleado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<PeempEmpleado> attachedPeempEmpleadoCollection = new ArrayList<PeempEmpleado>();
            for (PeempEmpleado peempEmpleadoCollectionPeempEmpleadoToAttach : pecarCargo.getPeempEmpleadoCollection()) {
                peempEmpleadoCollectionPeempEmpleadoToAttach = em.getReference(peempEmpleadoCollectionPeempEmpleadoToAttach.getClass(), peempEmpleadoCollectionPeempEmpleadoToAttach.getPeempCodig());
                attachedPeempEmpleadoCollection.add(peempEmpleadoCollectionPeempEmpleadoToAttach);
            }
            pecarCargo.setPeempEmpleadoCollection(attachedPeempEmpleadoCollection);
            em.persist(pecarCargo);
            for (PeempEmpleado peempEmpleadoCollectionPeempEmpleado : pecarCargo.getPeempEmpleadoCollection()) {
                PecarCargo oldPecarCodigoOfPeempEmpleadoCollectionPeempEmpleado = peempEmpleadoCollectionPeempEmpleado.getPecarCodigo();
                peempEmpleadoCollectionPeempEmpleado.setPecarCodigo(pecarCargo);
                peempEmpleadoCollectionPeempEmpleado = em.merge(peempEmpleadoCollectionPeempEmpleado);
                if (oldPecarCodigoOfPeempEmpleadoCollectionPeempEmpleado != null) {
                    oldPecarCodigoOfPeempEmpleadoCollectionPeempEmpleado.getPeempEmpleadoCollection().remove(peempEmpleadoCollectionPeempEmpleado);
                    oldPecarCodigoOfPeempEmpleadoCollectionPeempEmpleado = em.merge(oldPecarCodigoOfPeempEmpleadoCollectionPeempEmpleado);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPecarCargo(pecarCargo.getPecarCodigo()) != null) {
                throw new PreexistingEntityException("PecarCargo " + pecarCargo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PecarCargo pecarCargo) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PecarCargo persistentPecarCargo = em.find(PecarCargo.class, pecarCargo.getPecarCodigo());
            Collection<PeempEmpleado> peempEmpleadoCollectionOld = persistentPecarCargo.getPeempEmpleadoCollection();
            Collection<PeempEmpleado> peempEmpleadoCollectionNew = pecarCargo.getPeempEmpleadoCollection();
            Collection<PeempEmpleado> attachedPeempEmpleadoCollectionNew = new ArrayList<PeempEmpleado>();
            for (PeempEmpleado peempEmpleadoCollectionNewPeempEmpleadoToAttach : peempEmpleadoCollectionNew) {
                peempEmpleadoCollectionNewPeempEmpleadoToAttach = em.getReference(peempEmpleadoCollectionNewPeempEmpleadoToAttach.getClass(), peempEmpleadoCollectionNewPeempEmpleadoToAttach.getPeempCodig());
                attachedPeempEmpleadoCollectionNew.add(peempEmpleadoCollectionNewPeempEmpleadoToAttach);
            }
            peempEmpleadoCollectionNew = attachedPeempEmpleadoCollectionNew;
            pecarCargo.setPeempEmpleadoCollection(peempEmpleadoCollectionNew);
            pecarCargo = em.merge(pecarCargo);
            for (PeempEmpleado peempEmpleadoCollectionOldPeempEmpleado : peempEmpleadoCollectionOld) {
                if (!peempEmpleadoCollectionNew.contains(peempEmpleadoCollectionOldPeempEmpleado)) {
                    peempEmpleadoCollectionOldPeempEmpleado.setPecarCodigo(null);
                    peempEmpleadoCollectionOldPeempEmpleado = em.merge(peempEmpleadoCollectionOldPeempEmpleado);
                }
            }
            for (PeempEmpleado peempEmpleadoCollectionNewPeempEmpleado : peempEmpleadoCollectionNew) {
                if (!peempEmpleadoCollectionOld.contains(peempEmpleadoCollectionNewPeempEmpleado)) {
                    PecarCargo oldPecarCodigoOfPeempEmpleadoCollectionNewPeempEmpleado = peempEmpleadoCollectionNewPeempEmpleado.getPecarCodigo();
                    peempEmpleadoCollectionNewPeempEmpleado.setPecarCodigo(pecarCargo);
                    peempEmpleadoCollectionNewPeempEmpleado = em.merge(peempEmpleadoCollectionNewPeempEmpleado);
                    if (oldPecarCodigoOfPeempEmpleadoCollectionNewPeempEmpleado != null && !oldPecarCodigoOfPeempEmpleadoCollectionNewPeempEmpleado.equals(pecarCargo)) {
                        oldPecarCodigoOfPeempEmpleadoCollectionNewPeempEmpleado.getPeempEmpleadoCollection().remove(peempEmpleadoCollectionNewPeempEmpleado);
                        oldPecarCodigoOfPeempEmpleadoCollectionNewPeempEmpleado = em.merge(oldPecarCodigoOfPeempEmpleadoCollectionNewPeempEmpleado);
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
                String id = pecarCargo.getPecarCodigo();
                if (findPecarCargo(id) == null) {
                    throw new NonexistentEntityException("The pecarCargo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PecarCargo pecarCargo;
            try {
                pecarCargo = em.getReference(PecarCargo.class, id);
                pecarCargo.getPecarCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pecarCargo with id " + id + " no longer exists.", enfe);
            }
            Collection<PeempEmpleado> peempEmpleadoCollection = pecarCargo.getPeempEmpleadoCollection();
            for (PeempEmpleado peempEmpleadoCollectionPeempEmpleado : peempEmpleadoCollection) {
                peempEmpleadoCollectionPeempEmpleado.setPecarCodigo(null);
                peempEmpleadoCollectionPeempEmpleado = em.merge(peempEmpleadoCollectionPeempEmpleado);
            }
            em.remove(pecarCargo);
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

    public List<PecarCargo> findPecarCargoEntities() {
        return findPecarCargoEntities(true, -1, -1);
    }

    public List<PecarCargo> findPecarCargoEntities(int maxResults, int firstResult) {
        return findPecarCargoEntities(false, maxResults, firstResult);
    }

    private List<PecarCargo> findPecarCargoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PecarCargo.class));
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

    public PecarCargo findPecarCargo(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PecarCargo.class, id);
        } finally {
            em.close();
        }
    }

    public int getPecarCargoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PecarCargo> rt = cq.from(PecarCargo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
