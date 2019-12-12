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
import ec.edu.monster.entidades.PesexSexo;
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
public class PesexSexoJpaController implements Serializable {

    public PesexSexoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PesexSexo pesexSexo) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (pesexSexo.getPeempEmpleadoCollection() == null) {
            pesexSexo.setPeempEmpleadoCollection(new ArrayList<PeempEmpleado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<PeempEmpleado> attachedPeempEmpleadoCollection = new ArrayList<PeempEmpleado>();
            for (PeempEmpleado peempEmpleadoCollectionPeempEmpleadoToAttach : pesexSexo.getPeempEmpleadoCollection()) {
                peempEmpleadoCollectionPeempEmpleadoToAttach = em.getReference(peempEmpleadoCollectionPeempEmpleadoToAttach.getClass(), peempEmpleadoCollectionPeempEmpleadoToAttach.getPeempCodig());
                attachedPeempEmpleadoCollection.add(peempEmpleadoCollectionPeempEmpleadoToAttach);
            }
            pesexSexo.setPeempEmpleadoCollection(attachedPeempEmpleadoCollection);
            em.persist(pesexSexo);
            for (PeempEmpleado peempEmpleadoCollectionPeempEmpleado : pesexSexo.getPeempEmpleadoCollection()) {
                PesexSexo oldPesexCodigoOfPeempEmpleadoCollectionPeempEmpleado = peempEmpleadoCollectionPeempEmpleado.getPesexCodigo();
                peempEmpleadoCollectionPeempEmpleado.setPesexCodigo(pesexSexo);
                peempEmpleadoCollectionPeempEmpleado = em.merge(peempEmpleadoCollectionPeempEmpleado);
                if (oldPesexCodigoOfPeempEmpleadoCollectionPeempEmpleado != null) {
                    oldPesexCodigoOfPeempEmpleadoCollectionPeempEmpleado.getPeempEmpleadoCollection().remove(peempEmpleadoCollectionPeempEmpleado);
                    oldPesexCodigoOfPeempEmpleadoCollectionPeempEmpleado = em.merge(oldPesexCodigoOfPeempEmpleadoCollectionPeempEmpleado);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPesexSexo(pesexSexo.getPesexCodigo()) != null) {
                throw new PreexistingEntityException("PesexSexo " + pesexSexo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PesexSexo pesexSexo) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PesexSexo persistentPesexSexo = em.find(PesexSexo.class, pesexSexo.getPesexCodigo());
            Collection<PeempEmpleado> peempEmpleadoCollectionOld = persistentPesexSexo.getPeempEmpleadoCollection();
            Collection<PeempEmpleado> peempEmpleadoCollectionNew = pesexSexo.getPeempEmpleadoCollection();
            List<String> illegalOrphanMessages = null;
            for (PeempEmpleado peempEmpleadoCollectionOldPeempEmpleado : peempEmpleadoCollectionOld) {
                if (!peempEmpleadoCollectionNew.contains(peempEmpleadoCollectionOldPeempEmpleado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PeempEmpleado " + peempEmpleadoCollectionOldPeempEmpleado + " since its pesexCodigo field is not nullable.");
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
            pesexSexo.setPeempEmpleadoCollection(peempEmpleadoCollectionNew);
            pesexSexo = em.merge(pesexSexo);
            for (PeempEmpleado peempEmpleadoCollectionNewPeempEmpleado : peempEmpleadoCollectionNew) {
                if (!peempEmpleadoCollectionOld.contains(peempEmpleadoCollectionNewPeempEmpleado)) {
                    PesexSexo oldPesexCodigoOfPeempEmpleadoCollectionNewPeempEmpleado = peempEmpleadoCollectionNewPeempEmpleado.getPesexCodigo();
                    peempEmpleadoCollectionNewPeempEmpleado.setPesexCodigo(pesexSexo);
                    peempEmpleadoCollectionNewPeempEmpleado = em.merge(peempEmpleadoCollectionNewPeempEmpleado);
                    if (oldPesexCodigoOfPeempEmpleadoCollectionNewPeempEmpleado != null && !oldPesexCodigoOfPeempEmpleadoCollectionNewPeempEmpleado.equals(pesexSexo)) {
                        oldPesexCodigoOfPeempEmpleadoCollectionNewPeempEmpleado.getPeempEmpleadoCollection().remove(peempEmpleadoCollectionNewPeempEmpleado);
                        oldPesexCodigoOfPeempEmpleadoCollectionNewPeempEmpleado = em.merge(oldPesexCodigoOfPeempEmpleadoCollectionNewPeempEmpleado);
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
                String id = pesexSexo.getPesexCodigo();
                if (findPesexSexo(id) == null) {
                    throw new NonexistentEntityException("The pesexSexo with id " + id + " no longer exists.");
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
            PesexSexo pesexSexo;
            try {
                pesexSexo = em.getReference(PesexSexo.class, id);
                pesexSexo.getPesexCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pesexSexo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<PeempEmpleado> peempEmpleadoCollectionOrphanCheck = pesexSexo.getPeempEmpleadoCollection();
            for (PeempEmpleado peempEmpleadoCollectionOrphanCheckPeempEmpleado : peempEmpleadoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PesexSexo (" + pesexSexo + ") cannot be destroyed since the PeempEmpleado " + peempEmpleadoCollectionOrphanCheckPeempEmpleado + " in its peempEmpleadoCollection field has a non-nullable pesexCodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pesexSexo);
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

    public List<PesexSexo> findPesexSexoEntities() {
        return findPesexSexoEntities(true, -1, -1);
    }

    public List<PesexSexo> findPesexSexoEntities(int maxResults, int firstResult) {
        return findPesexSexoEntities(false, maxResults, firstResult);
    }

    private List<PesexSexo> findPesexSexoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PesexSexo.class));
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

    public PesexSexo findPesexSexo(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PesexSexo.class, id);
        } finally {
            em.close();
        }
    }

    public int getPesexSexoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PesexSexo> rt = cq.from(PesexSexo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
