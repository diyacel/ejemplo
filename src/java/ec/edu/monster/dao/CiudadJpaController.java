/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.dao;

import ec.edu.monster.dao.exceptions.NonexistentEntityException;
import ec.edu.monster.dao.exceptions.PreexistingEntityException;
import ec.edu.monster.dao.exceptions.RollbackFailureException;
import ec.edu.monster.entidades.Ciudad;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ec.edu.monster.entidades.PeproProvin;
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
public class CiudadJpaController implements Serializable {

    public CiudadJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ciudad ciudad) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (ciudad.getPeempEmpleadoCollection() == null) {
            ciudad.setPeempEmpleadoCollection(new ArrayList<PeempEmpleado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PeproProvin peproCodigo = ciudad.getPeproCodigo();
            if (peproCodigo != null) {
                peproCodigo = em.getReference(peproCodigo.getClass(), peproCodigo.getPeproCodigo());
                ciudad.setPeproCodigo(peproCodigo);
            }
            Collection<PeempEmpleado> attachedPeempEmpleadoCollection = new ArrayList<PeempEmpleado>();
            for (PeempEmpleado peempEmpleadoCollectionPeempEmpleadoToAttach : ciudad.getPeempEmpleadoCollection()) {
                peempEmpleadoCollectionPeempEmpleadoToAttach = em.getReference(peempEmpleadoCollectionPeempEmpleadoToAttach.getClass(), peempEmpleadoCollectionPeempEmpleadoToAttach.getPeempCodig());
                attachedPeempEmpleadoCollection.add(peempEmpleadoCollectionPeempEmpleadoToAttach);
            }
            ciudad.setPeempEmpleadoCollection(attachedPeempEmpleadoCollection);
            em.persist(ciudad);
            if (peproCodigo != null) {
                peproCodigo.getCiudadCollection().add(ciudad);
                peproCodigo = em.merge(peproCodigo);
            }
            for (PeempEmpleado peempEmpleadoCollectionPeempEmpleado : ciudad.getPeempEmpleadoCollection()) {
                Ciudad oldCiuCodigoOfPeempEmpleadoCollectionPeempEmpleado = peempEmpleadoCollectionPeempEmpleado.getCiuCodigo();
                peempEmpleadoCollectionPeempEmpleado.setCiuCodigo(ciudad);
                peempEmpleadoCollectionPeempEmpleado = em.merge(peempEmpleadoCollectionPeempEmpleado);
                if (oldCiuCodigoOfPeempEmpleadoCollectionPeempEmpleado != null) {
                    oldCiuCodigoOfPeempEmpleadoCollectionPeempEmpleado.getPeempEmpleadoCollection().remove(peempEmpleadoCollectionPeempEmpleado);
                    oldCiuCodigoOfPeempEmpleadoCollectionPeempEmpleado = em.merge(oldCiuCodigoOfPeempEmpleadoCollectionPeempEmpleado);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCiudad(ciudad.getCiuCodigo()) != null) {
                throw new PreexistingEntityException("Ciudad " + ciudad + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ciudad ciudad) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ciudad persistentCiudad = em.find(Ciudad.class, ciudad.getCiuCodigo());
            PeproProvin peproCodigoOld = persistentCiudad.getPeproCodigo();
            PeproProvin peproCodigoNew = ciudad.getPeproCodigo();
            Collection<PeempEmpleado> peempEmpleadoCollectionOld = persistentCiudad.getPeempEmpleadoCollection();
            Collection<PeempEmpleado> peempEmpleadoCollectionNew = ciudad.getPeempEmpleadoCollection();
            if (peproCodigoNew != null) {
                peproCodigoNew = em.getReference(peproCodigoNew.getClass(), peproCodigoNew.getPeproCodigo());
                ciudad.setPeproCodigo(peproCodigoNew);
            }
            Collection<PeempEmpleado> attachedPeempEmpleadoCollectionNew = new ArrayList<PeempEmpleado>();
            for (PeempEmpleado peempEmpleadoCollectionNewPeempEmpleadoToAttach : peempEmpleadoCollectionNew) {
                peempEmpleadoCollectionNewPeempEmpleadoToAttach = em.getReference(peempEmpleadoCollectionNewPeempEmpleadoToAttach.getClass(), peempEmpleadoCollectionNewPeempEmpleadoToAttach.getPeempCodig());
                attachedPeempEmpleadoCollectionNew.add(peempEmpleadoCollectionNewPeempEmpleadoToAttach);
            }
            peempEmpleadoCollectionNew = attachedPeempEmpleadoCollectionNew;
            ciudad.setPeempEmpleadoCollection(peempEmpleadoCollectionNew);
            ciudad = em.merge(ciudad);
            if (peproCodigoOld != null && !peproCodigoOld.equals(peproCodigoNew)) {
                peproCodigoOld.getCiudadCollection().remove(ciudad);
                peproCodigoOld = em.merge(peproCodigoOld);
            }
            if (peproCodigoNew != null && !peproCodigoNew.equals(peproCodigoOld)) {
                peproCodigoNew.getCiudadCollection().add(ciudad);
                peproCodigoNew = em.merge(peproCodigoNew);
            }
            for (PeempEmpleado peempEmpleadoCollectionOldPeempEmpleado : peempEmpleadoCollectionOld) {
                if (!peempEmpleadoCollectionNew.contains(peempEmpleadoCollectionOldPeempEmpleado)) {
                    peempEmpleadoCollectionOldPeempEmpleado.setCiuCodigo(null);
                    peempEmpleadoCollectionOldPeempEmpleado = em.merge(peempEmpleadoCollectionOldPeempEmpleado);
                }
            }
            for (PeempEmpleado peempEmpleadoCollectionNewPeempEmpleado : peempEmpleadoCollectionNew) {
                if (!peempEmpleadoCollectionOld.contains(peempEmpleadoCollectionNewPeempEmpleado)) {
                    Ciudad oldCiuCodigoOfPeempEmpleadoCollectionNewPeempEmpleado = peempEmpleadoCollectionNewPeempEmpleado.getCiuCodigo();
                    peempEmpleadoCollectionNewPeempEmpleado.setCiuCodigo(ciudad);
                    peempEmpleadoCollectionNewPeempEmpleado = em.merge(peempEmpleadoCollectionNewPeempEmpleado);
                    if (oldCiuCodigoOfPeempEmpleadoCollectionNewPeempEmpleado != null && !oldCiuCodigoOfPeempEmpleadoCollectionNewPeempEmpleado.equals(ciudad)) {
                        oldCiuCodigoOfPeempEmpleadoCollectionNewPeempEmpleado.getPeempEmpleadoCollection().remove(peempEmpleadoCollectionNewPeempEmpleado);
                        oldCiuCodigoOfPeempEmpleadoCollectionNewPeempEmpleado = em.merge(oldCiuCodigoOfPeempEmpleadoCollectionNewPeempEmpleado);
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
                String id = ciudad.getCiuCodigo();
                if (findCiudad(id) == null) {
                    throw new NonexistentEntityException("The ciudad with id " + id + " no longer exists.");
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
            Ciudad ciudad;
            try {
                ciudad = em.getReference(Ciudad.class, id);
                ciudad.getCiuCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ciudad with id " + id + " no longer exists.", enfe);
            }
            PeproProvin peproCodigo = ciudad.getPeproCodigo();
            if (peproCodigo != null) {
                peproCodigo.getCiudadCollection().remove(ciudad);
                peproCodigo = em.merge(peproCodigo);
            }
            Collection<PeempEmpleado> peempEmpleadoCollection = ciudad.getPeempEmpleadoCollection();
            for (PeempEmpleado peempEmpleadoCollectionPeempEmpleado : peempEmpleadoCollection) {
                peempEmpleadoCollectionPeempEmpleado.setCiuCodigo(null);
                peempEmpleadoCollectionPeempEmpleado = em.merge(peempEmpleadoCollectionPeempEmpleado);
            }
            em.remove(ciudad);
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

    public List<Ciudad> findCiudadEntities() {
        return findCiudadEntities(true, -1, -1);
    }

    public List<Ciudad> findCiudadEntities(int maxResults, int firstResult) {
        return findCiudadEntities(false, maxResults, firstResult);
    }

    private List<Ciudad> findCiudadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ciudad.class));
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

    public Ciudad findCiudad(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ciudad.class, id);
        } finally {
            em.close();
        }
    }

    public int getCiudadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ciudad> rt = cq.from(Ciudad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
