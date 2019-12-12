/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.dao;

import ec.edu.monster.dao.exceptions.NonexistentEntityException;
import ec.edu.monster.dao.exceptions.PreexistingEntityException;
import ec.edu.monster.dao.exceptions.RollbackFailureException;
import ec.edu.monster.entidades.PepaiPais;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ec.edu.monster.entidades.PeproProvin;
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
public class PepaiPaisJpaController implements Serializable {

    public PepaiPaisJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PepaiPais pepaiPais) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (pepaiPais.getPeproProvinCollection() == null) {
            pepaiPais.setPeproProvinCollection(new ArrayList<PeproProvin>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<PeproProvin> attachedPeproProvinCollection = new ArrayList<PeproProvin>();
            for (PeproProvin peproProvinCollectionPeproProvinToAttach : pepaiPais.getPeproProvinCollection()) {
                peproProvinCollectionPeproProvinToAttach = em.getReference(peproProvinCollectionPeproProvinToAttach.getClass(), peproProvinCollectionPeproProvinToAttach.getPeproCodigo());
                attachedPeproProvinCollection.add(peproProvinCollectionPeproProvinToAttach);
            }
            pepaiPais.setPeproProvinCollection(attachedPeproProvinCollection);
            em.persist(pepaiPais);
            for (PeproProvin peproProvinCollectionPeproProvin : pepaiPais.getPeproProvinCollection()) {
                PepaiPais oldPepaisCodigoOfPeproProvinCollectionPeproProvin = peproProvinCollectionPeproProvin.getPepaisCodigo();
                peproProvinCollectionPeproProvin.setPepaisCodigo(pepaiPais);
                peproProvinCollectionPeproProvin = em.merge(peproProvinCollectionPeproProvin);
                if (oldPepaisCodigoOfPeproProvinCollectionPeproProvin != null) {
                    oldPepaisCodigoOfPeproProvinCollectionPeproProvin.getPeproProvinCollection().remove(peproProvinCollectionPeproProvin);
                    oldPepaisCodigoOfPeproProvinCollectionPeproProvin = em.merge(oldPepaisCodigoOfPeproProvinCollectionPeproProvin);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPepaiPais(pepaiPais.getPepaisCodigo()) != null) {
                throw new PreexistingEntityException("PepaiPais " + pepaiPais + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PepaiPais pepaiPais) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PepaiPais persistentPepaiPais = em.find(PepaiPais.class, pepaiPais.getPepaisCodigo());
            Collection<PeproProvin> peproProvinCollectionOld = persistentPepaiPais.getPeproProvinCollection();
            Collection<PeproProvin> peproProvinCollectionNew = pepaiPais.getPeproProvinCollection();
            Collection<PeproProvin> attachedPeproProvinCollectionNew = new ArrayList<PeproProvin>();
            for (PeproProvin peproProvinCollectionNewPeproProvinToAttach : peproProvinCollectionNew) {
                peproProvinCollectionNewPeproProvinToAttach = em.getReference(peproProvinCollectionNewPeproProvinToAttach.getClass(), peproProvinCollectionNewPeproProvinToAttach.getPeproCodigo());
                attachedPeproProvinCollectionNew.add(peproProvinCollectionNewPeproProvinToAttach);
            }
            peproProvinCollectionNew = attachedPeproProvinCollectionNew;
            pepaiPais.setPeproProvinCollection(peproProvinCollectionNew);
            pepaiPais = em.merge(pepaiPais);
            for (PeproProvin peproProvinCollectionOldPeproProvin : peproProvinCollectionOld) {
                if (!peproProvinCollectionNew.contains(peproProvinCollectionOldPeproProvin)) {
                    peproProvinCollectionOldPeproProvin.setPepaisCodigo(null);
                    peproProvinCollectionOldPeproProvin = em.merge(peproProvinCollectionOldPeproProvin);
                }
            }
            for (PeproProvin peproProvinCollectionNewPeproProvin : peproProvinCollectionNew) {
                if (!peproProvinCollectionOld.contains(peproProvinCollectionNewPeproProvin)) {
                    PepaiPais oldPepaisCodigoOfPeproProvinCollectionNewPeproProvin = peproProvinCollectionNewPeproProvin.getPepaisCodigo();
                    peproProvinCollectionNewPeproProvin.setPepaisCodigo(pepaiPais);
                    peproProvinCollectionNewPeproProvin = em.merge(peproProvinCollectionNewPeproProvin);
                    if (oldPepaisCodigoOfPeproProvinCollectionNewPeproProvin != null && !oldPepaisCodigoOfPeproProvinCollectionNewPeproProvin.equals(pepaiPais)) {
                        oldPepaisCodigoOfPeproProvinCollectionNewPeproProvin.getPeproProvinCollection().remove(peproProvinCollectionNewPeproProvin);
                        oldPepaisCodigoOfPeproProvinCollectionNewPeproProvin = em.merge(oldPepaisCodigoOfPeproProvinCollectionNewPeproProvin);
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
                String id = pepaiPais.getPepaisCodigo();
                if (findPepaiPais(id) == null) {
                    throw new NonexistentEntityException("The pepaiPais with id " + id + " no longer exists.");
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
            PepaiPais pepaiPais;
            try {
                pepaiPais = em.getReference(PepaiPais.class, id);
                pepaiPais.getPepaisCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pepaiPais with id " + id + " no longer exists.", enfe);
            }
            Collection<PeproProvin> peproProvinCollection = pepaiPais.getPeproProvinCollection();
            for (PeproProvin peproProvinCollectionPeproProvin : peproProvinCollection) {
                peproProvinCollectionPeproProvin.setPepaisCodigo(null);
                peproProvinCollectionPeproProvin = em.merge(peproProvinCollectionPeproProvin);
            }
            em.remove(pepaiPais);
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

    public List<PepaiPais> findPepaiPaisEntities() {
        return findPepaiPaisEntities(true, -1, -1);
    }

    public List<PepaiPais> findPepaiPaisEntities(int maxResults, int firstResult) {
        return findPepaiPaisEntities(false, maxResults, firstResult);
    }

    private List<PepaiPais> findPepaiPaisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PepaiPais.class));
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

    public PepaiPais findPepaiPais(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PepaiPais.class, id);
        } finally {
            em.close();
        }
    }

    public int getPepaiPaisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PepaiPais> rt = cq.from(PepaiPais.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
