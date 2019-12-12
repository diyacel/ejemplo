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
import ec.edu.monster.entidades.Ciudad;
import ec.edu.monster.entidades.PeescEstciv;
import ec.edu.monster.entidades.PesexSexo;
import ec.edu.monster.entidades.PecarCargo;
import ec.edu.monster.entidades.PeempEmpleado;
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
public class PeempEmpleadoJpaController implements Serializable {

    public PeempEmpleadoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PeempEmpleado peempEmpleado) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (peempEmpleado.getXeusuUsuarCollection() == null) {
            peempEmpleado.setXeusuUsuarCollection(new ArrayList<XeusuUsuar>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ciudad ciuCodigo = peempEmpleado.getCiuCodigo();
            if (ciuCodigo != null) {
                ciuCodigo = em.getReference(ciuCodigo.getClass(), ciuCodigo.getCiuCodigo());
                peempEmpleado.setCiuCodigo(ciuCodigo);
            }
            PeescEstciv peescCodigo = peempEmpleado.getPeescCodigo();
            if (peescCodigo != null) {
                peescCodigo = em.getReference(peescCodigo.getClass(), peescCodigo.getPeescCodigo());
                peempEmpleado.setPeescCodigo(peescCodigo);
            }
            PesexSexo pesexCodigo = peempEmpleado.getPesexCodigo();
            if (pesexCodigo != null) {
                pesexCodigo = em.getReference(pesexCodigo.getClass(), pesexCodigo.getPesexCodigo());
                peempEmpleado.setPesexCodigo(pesexCodigo);
            }
            PecarCargo pecarCodigo = peempEmpleado.getPecarCodigo();
            if (pecarCodigo != null) {
                pecarCodigo = em.getReference(pecarCodigo.getClass(), pecarCodigo.getPecarCodigo());
                peempEmpleado.setPecarCodigo(pecarCodigo);
            }
            Collection<XeusuUsuar> attachedXeusuUsuarCollection = new ArrayList<XeusuUsuar>();
            for (XeusuUsuar xeusuUsuarCollectionXeusuUsuarToAttach : peempEmpleado.getXeusuUsuarCollection()) {
                xeusuUsuarCollectionXeusuUsuarToAttach = em.getReference(xeusuUsuarCollectionXeusuUsuarToAttach.getClass(), xeusuUsuarCollectionXeusuUsuarToAttach.getXeusuUsuarPK());
                attachedXeusuUsuarCollection.add(xeusuUsuarCollectionXeusuUsuarToAttach);
            }
            peempEmpleado.setXeusuUsuarCollection(attachedXeusuUsuarCollection);
            em.persist(peempEmpleado);
            if (ciuCodigo != null) {
                ciuCodigo.getPeempEmpleadoCollection().add(peempEmpleado);
                ciuCodigo = em.merge(ciuCodigo);
            }
            if (peescCodigo != null) {
                peescCodigo.getPeempEmpleadoCollection().add(peempEmpleado);
                peescCodigo = em.merge(peescCodigo);
            }
            if (pesexCodigo != null) {
                pesexCodigo.getPeempEmpleadoCollection().add(peempEmpleado);
                pesexCodigo = em.merge(pesexCodigo);
            }
            if (pecarCodigo != null) {
                pecarCodigo.getPeempEmpleadoCollection().add(peempEmpleado);
                pecarCodigo = em.merge(pecarCodigo);
            }
            for (XeusuUsuar xeusuUsuarCollectionXeusuUsuar : peempEmpleado.getXeusuUsuarCollection()) {
                PeempEmpleado oldPeempEmpleadoOfXeusuUsuarCollectionXeusuUsuar = xeusuUsuarCollectionXeusuUsuar.getPeempEmpleado();
                xeusuUsuarCollectionXeusuUsuar.setPeempEmpleado(peempEmpleado);
                xeusuUsuarCollectionXeusuUsuar = em.merge(xeusuUsuarCollectionXeusuUsuar);
                if (oldPeempEmpleadoOfXeusuUsuarCollectionXeusuUsuar != null) {
                    oldPeempEmpleadoOfXeusuUsuarCollectionXeusuUsuar.getXeusuUsuarCollection().remove(xeusuUsuarCollectionXeusuUsuar);
                    oldPeempEmpleadoOfXeusuUsuarCollectionXeusuUsuar = em.merge(oldPeempEmpleadoOfXeusuUsuarCollectionXeusuUsuar);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPeempEmpleado(peempEmpleado.getPeempCodig()) != null) {
                throw new PreexistingEntityException("PeempEmpleado " + peempEmpleado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PeempEmpleado peempEmpleado) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PeempEmpleado persistentPeempEmpleado = em.find(PeempEmpleado.class, peempEmpleado.getPeempCodig());
            Ciudad ciuCodigoOld = persistentPeempEmpleado.getCiuCodigo();
            Ciudad ciuCodigoNew = peempEmpleado.getCiuCodigo();
            PeescEstciv peescCodigoOld = persistentPeempEmpleado.getPeescCodigo();
            PeescEstciv peescCodigoNew = peempEmpleado.getPeescCodigo();
            PesexSexo pesexCodigoOld = persistentPeempEmpleado.getPesexCodigo();
            PesexSexo pesexCodigoNew = peempEmpleado.getPesexCodigo();
            PecarCargo pecarCodigoOld = persistentPeempEmpleado.getPecarCodigo();
            PecarCargo pecarCodigoNew = peempEmpleado.getPecarCodigo();
            Collection<XeusuUsuar> xeusuUsuarCollectionOld = persistentPeempEmpleado.getXeusuUsuarCollection();
            Collection<XeusuUsuar> xeusuUsuarCollectionNew = peempEmpleado.getXeusuUsuarCollection();
            List<String> illegalOrphanMessages = null;
            for (XeusuUsuar xeusuUsuarCollectionOldXeusuUsuar : xeusuUsuarCollectionOld) {
                if (!xeusuUsuarCollectionNew.contains(xeusuUsuarCollectionOldXeusuUsuar)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XeusuUsuar " + xeusuUsuarCollectionOldXeusuUsuar + " since its peempEmpleado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (ciuCodigoNew != null) {
                ciuCodigoNew = em.getReference(ciuCodigoNew.getClass(), ciuCodigoNew.getCiuCodigo());
                peempEmpleado.setCiuCodigo(ciuCodigoNew);
            }
            if (peescCodigoNew != null) {
                peescCodigoNew = em.getReference(peescCodigoNew.getClass(), peescCodigoNew.getPeescCodigo());
                peempEmpleado.setPeescCodigo(peescCodigoNew);
            }
            if (pesexCodigoNew != null) {
                pesexCodigoNew = em.getReference(pesexCodigoNew.getClass(), pesexCodigoNew.getPesexCodigo());
                peempEmpleado.setPesexCodigo(pesexCodigoNew);
            }
            if (pecarCodigoNew != null) {
                pecarCodigoNew = em.getReference(pecarCodigoNew.getClass(), pecarCodigoNew.getPecarCodigo());
                peempEmpleado.setPecarCodigo(pecarCodigoNew);
            }
            Collection<XeusuUsuar> attachedXeusuUsuarCollectionNew = new ArrayList<XeusuUsuar>();
            for (XeusuUsuar xeusuUsuarCollectionNewXeusuUsuarToAttach : xeusuUsuarCollectionNew) {
                xeusuUsuarCollectionNewXeusuUsuarToAttach = em.getReference(xeusuUsuarCollectionNewXeusuUsuarToAttach.getClass(), xeusuUsuarCollectionNewXeusuUsuarToAttach.getXeusuUsuarPK());
                attachedXeusuUsuarCollectionNew.add(xeusuUsuarCollectionNewXeusuUsuarToAttach);
            }
            xeusuUsuarCollectionNew = attachedXeusuUsuarCollectionNew;
            peempEmpleado.setXeusuUsuarCollection(xeusuUsuarCollectionNew);
            peempEmpleado = em.merge(peempEmpleado);
            if (ciuCodigoOld != null && !ciuCodigoOld.equals(ciuCodigoNew)) {
                ciuCodigoOld.getPeempEmpleadoCollection().remove(peempEmpleado);
                ciuCodigoOld = em.merge(ciuCodigoOld);
            }
            if (ciuCodigoNew != null && !ciuCodigoNew.equals(ciuCodigoOld)) {
                ciuCodigoNew.getPeempEmpleadoCollection().add(peempEmpleado);
                ciuCodigoNew = em.merge(ciuCodigoNew);
            }
            if (peescCodigoOld != null && !peescCodigoOld.equals(peescCodigoNew)) {
                peescCodigoOld.getPeempEmpleadoCollection().remove(peempEmpleado);
                peescCodigoOld = em.merge(peescCodigoOld);
            }
            if (peescCodigoNew != null && !peescCodigoNew.equals(peescCodigoOld)) {
                peescCodigoNew.getPeempEmpleadoCollection().add(peempEmpleado);
                peescCodigoNew = em.merge(peescCodigoNew);
            }
            if (pesexCodigoOld != null && !pesexCodigoOld.equals(pesexCodigoNew)) {
                pesexCodigoOld.getPeempEmpleadoCollection().remove(peempEmpleado);
                pesexCodigoOld = em.merge(pesexCodigoOld);
            }
            if (pesexCodigoNew != null && !pesexCodigoNew.equals(pesexCodigoOld)) {
                pesexCodigoNew.getPeempEmpleadoCollection().add(peempEmpleado);
                pesexCodigoNew = em.merge(pesexCodigoNew);
            }
            if (pecarCodigoOld != null && !pecarCodigoOld.equals(pecarCodigoNew)) {
                pecarCodigoOld.getPeempEmpleadoCollection().remove(peempEmpleado);
                pecarCodigoOld = em.merge(pecarCodigoOld);
            }
            if (pecarCodigoNew != null && !pecarCodigoNew.equals(pecarCodigoOld)) {
                pecarCodigoNew.getPeempEmpleadoCollection().add(peempEmpleado);
                pecarCodigoNew = em.merge(pecarCodigoNew);
            }
            for (XeusuUsuar xeusuUsuarCollectionNewXeusuUsuar : xeusuUsuarCollectionNew) {
                if (!xeusuUsuarCollectionOld.contains(xeusuUsuarCollectionNewXeusuUsuar)) {
                    PeempEmpleado oldPeempEmpleadoOfXeusuUsuarCollectionNewXeusuUsuar = xeusuUsuarCollectionNewXeusuUsuar.getPeempEmpleado();
                    xeusuUsuarCollectionNewXeusuUsuar.setPeempEmpleado(peempEmpleado);
                    xeusuUsuarCollectionNewXeusuUsuar = em.merge(xeusuUsuarCollectionNewXeusuUsuar);
                    if (oldPeempEmpleadoOfXeusuUsuarCollectionNewXeusuUsuar != null && !oldPeempEmpleadoOfXeusuUsuarCollectionNewXeusuUsuar.equals(peempEmpleado)) {
                        oldPeempEmpleadoOfXeusuUsuarCollectionNewXeusuUsuar.getXeusuUsuarCollection().remove(xeusuUsuarCollectionNewXeusuUsuar);
                        oldPeempEmpleadoOfXeusuUsuarCollectionNewXeusuUsuar = em.merge(oldPeempEmpleadoOfXeusuUsuarCollectionNewXeusuUsuar);
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
                Integer id = peempEmpleado.getPeempCodig();
                if (findPeempEmpleado(id) == null) {
                    throw new NonexistentEntityException("The peempEmpleado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PeempEmpleado peempEmpleado;
            try {
                peempEmpleado = em.getReference(PeempEmpleado.class, id);
                peempEmpleado.getPeempCodig();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The peempEmpleado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<XeusuUsuar> xeusuUsuarCollectionOrphanCheck = peempEmpleado.getXeusuUsuarCollection();
            for (XeusuUsuar xeusuUsuarCollectionOrphanCheckXeusuUsuar : xeusuUsuarCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PeempEmpleado (" + peempEmpleado + ") cannot be destroyed since the XeusuUsuar " + xeusuUsuarCollectionOrphanCheckXeusuUsuar + " in its xeusuUsuarCollection field has a non-nullable peempEmpleado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Ciudad ciuCodigo = peempEmpleado.getCiuCodigo();
            if (ciuCodigo != null) {
                ciuCodigo.getPeempEmpleadoCollection().remove(peempEmpleado);
                ciuCodigo = em.merge(ciuCodigo);
            }
            PeescEstciv peescCodigo = peempEmpleado.getPeescCodigo();
            if (peescCodigo != null) {
                peescCodigo.getPeempEmpleadoCollection().remove(peempEmpleado);
                peescCodigo = em.merge(peescCodigo);
            }
            PesexSexo pesexCodigo = peempEmpleado.getPesexCodigo();
            if (pesexCodigo != null) {
                pesexCodigo.getPeempEmpleadoCollection().remove(peempEmpleado);
                pesexCodigo = em.merge(pesexCodigo);
            }
            PecarCargo pecarCodigo = peempEmpleado.getPecarCodigo();
            if (pecarCodigo != null) {
                pecarCodigo.getPeempEmpleadoCollection().remove(peempEmpleado);
                pecarCodigo = em.merge(pecarCodigo);
            }
            em.remove(peempEmpleado);
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

    public List<PeempEmpleado> findPeempEmpleadoEntities() {
        return findPeempEmpleadoEntities(true, -1, -1);
    }

    public List<PeempEmpleado> findPeempEmpleadoEntities(int maxResults, int firstResult) {
        return findPeempEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<PeempEmpleado> findPeempEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PeempEmpleado.class));
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

    public PeempEmpleado findPeempEmpleado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PeempEmpleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getPeempEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PeempEmpleado> rt = cq.from(PeempEmpleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
