/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.dao;

import ec.edu.monster.dao.exceptions.NonexistentEntityException;
import ec.edu.monster.dao.exceptions.PreexistingEntityException;
import ec.edu.monster.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ec.edu.monster.entidades.PepaiPais;
import ec.edu.monster.entidades.Ciudad;
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
public class PeproProvinJpaController implements Serializable {

    public PeproProvinJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PeproProvin peproProvin) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (peproProvin.getCiudadCollection() == null) {
            peproProvin.setCiudadCollection(new ArrayList<Ciudad>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PepaiPais pepaisCodigo = peproProvin.getPepaisCodigo();
            if (pepaisCodigo != null) {
                pepaisCodigo = em.getReference(pepaisCodigo.getClass(), pepaisCodigo.getPepaisCodigo());
                peproProvin.setPepaisCodigo(pepaisCodigo);
            }
            Collection<Ciudad> attachedCiudadCollection = new ArrayList<Ciudad>();
            for (Ciudad ciudadCollectionCiudadToAttach : peproProvin.getCiudadCollection()) {
                ciudadCollectionCiudadToAttach = em.getReference(ciudadCollectionCiudadToAttach.getClass(), ciudadCollectionCiudadToAttach.getCiuCodigo());
                attachedCiudadCollection.add(ciudadCollectionCiudadToAttach);
            }
            peproProvin.setCiudadCollection(attachedCiudadCollection);
            em.persist(peproProvin);
            if (pepaisCodigo != null) {
                pepaisCodigo.getPeproProvinCollection().add(peproProvin);
                pepaisCodigo = em.merge(pepaisCodigo);
            }
            for (Ciudad ciudadCollectionCiudad : peproProvin.getCiudadCollection()) {
                PeproProvin oldPeproCodigoOfCiudadCollectionCiudad = ciudadCollectionCiudad.getPeproCodigo();
                ciudadCollectionCiudad.setPeproCodigo(peproProvin);
                ciudadCollectionCiudad = em.merge(ciudadCollectionCiudad);
                if (oldPeproCodigoOfCiudadCollectionCiudad != null) {
                    oldPeproCodigoOfCiudadCollectionCiudad.getCiudadCollection().remove(ciudadCollectionCiudad);
                    oldPeproCodigoOfCiudadCollectionCiudad = em.merge(oldPeproCodigoOfCiudadCollectionCiudad);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPeproProvin(peproProvin.getPeproCodigo()) != null) {
                throw new PreexistingEntityException("PeproProvin " + peproProvin + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PeproProvin peproProvin) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PeproProvin persistentPeproProvin = em.find(PeproProvin.class, peproProvin.getPeproCodigo());
            PepaiPais pepaisCodigoOld = persistentPeproProvin.getPepaisCodigo();
            PepaiPais pepaisCodigoNew = peproProvin.getPepaisCodigo();
            Collection<Ciudad> ciudadCollectionOld = persistentPeproProvin.getCiudadCollection();
            Collection<Ciudad> ciudadCollectionNew = peproProvin.getCiudadCollection();
            if (pepaisCodigoNew != null) {
                pepaisCodigoNew = em.getReference(pepaisCodigoNew.getClass(), pepaisCodigoNew.getPepaisCodigo());
                peproProvin.setPepaisCodigo(pepaisCodigoNew);
            }
            Collection<Ciudad> attachedCiudadCollectionNew = new ArrayList<Ciudad>();
            for (Ciudad ciudadCollectionNewCiudadToAttach : ciudadCollectionNew) {
                ciudadCollectionNewCiudadToAttach = em.getReference(ciudadCollectionNewCiudadToAttach.getClass(), ciudadCollectionNewCiudadToAttach.getCiuCodigo());
                attachedCiudadCollectionNew.add(ciudadCollectionNewCiudadToAttach);
            }
            ciudadCollectionNew = attachedCiudadCollectionNew;
            peproProvin.setCiudadCollection(ciudadCollectionNew);
            peproProvin = em.merge(peproProvin);
            if (pepaisCodigoOld != null && !pepaisCodigoOld.equals(pepaisCodigoNew)) {
                pepaisCodigoOld.getPeproProvinCollection().remove(peproProvin);
                pepaisCodigoOld = em.merge(pepaisCodigoOld);
            }
            if (pepaisCodigoNew != null && !pepaisCodigoNew.equals(pepaisCodigoOld)) {
                pepaisCodigoNew.getPeproProvinCollection().add(peproProvin);
                pepaisCodigoNew = em.merge(pepaisCodigoNew);
            }
            for (Ciudad ciudadCollectionOldCiudad : ciudadCollectionOld) {
                if (!ciudadCollectionNew.contains(ciudadCollectionOldCiudad)) {
                    ciudadCollectionOldCiudad.setPeproCodigo(null);
                    ciudadCollectionOldCiudad = em.merge(ciudadCollectionOldCiudad);
                }
            }
            for (Ciudad ciudadCollectionNewCiudad : ciudadCollectionNew) {
                if (!ciudadCollectionOld.contains(ciudadCollectionNewCiudad)) {
                    PeproProvin oldPeproCodigoOfCiudadCollectionNewCiudad = ciudadCollectionNewCiudad.getPeproCodigo();
                    ciudadCollectionNewCiudad.setPeproCodigo(peproProvin);
                    ciudadCollectionNewCiudad = em.merge(ciudadCollectionNewCiudad);
                    if (oldPeproCodigoOfCiudadCollectionNewCiudad != null && !oldPeproCodigoOfCiudadCollectionNewCiudad.equals(peproProvin)) {
                        oldPeproCodigoOfCiudadCollectionNewCiudad.getCiudadCollection().remove(ciudadCollectionNewCiudad);
                        oldPeproCodigoOfCiudadCollectionNewCiudad = em.merge(oldPeproCodigoOfCiudadCollectionNewCiudad);
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
                String id = peproProvin.getPeproCodigo();
                if (findPeproProvin(id) == null) {
                    throw new NonexistentEntityException("The peproProvin with id " + id + " no longer exists.");
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
            PeproProvin peproProvin;
            try {
                peproProvin = em.getReference(PeproProvin.class, id);
                peproProvin.getPeproCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The peproProvin with id " + id + " no longer exists.", enfe);
            }
            PepaiPais pepaisCodigo = peproProvin.getPepaisCodigo();
            if (pepaisCodigo != null) {
                pepaisCodigo.getPeproProvinCollection().remove(peproProvin);
                pepaisCodigo = em.merge(pepaisCodigo);
            }
            Collection<Ciudad> ciudadCollection = peproProvin.getCiudadCollection();
            for (Ciudad ciudadCollectionCiudad : ciudadCollection) {
                ciudadCollectionCiudad.setPeproCodigo(null);
                ciudadCollectionCiudad = em.merge(ciudadCollectionCiudad);
            }
            em.remove(peproProvin);
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

    public List<PeproProvin> findPeproProvinEntities() {
        return findPeproProvinEntities(true, -1, -1);
    }

    public List<PeproProvin> findPeproProvinEntities(int maxResults, int firstResult) {
        return findPeproProvinEntities(false, maxResults, firstResult);
    }

    private List<PeproProvin> findPeproProvinEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PeproProvin.class));
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

    public PeproProvin findPeproProvin(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PeproProvin.class, id);
        } finally {
            em.close();
        }
    }

    public int getPeproProvinCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PeproProvin> rt = cq.from(PeproProvin.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
