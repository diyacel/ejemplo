/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.dao;

import ec.edu.monster.dao.exceptions.NonexistentEntityException;
import ec.edu.monster.dao.exceptions.PreexistingEntityException;
import ec.edu.monster.dao.exceptions.RollbackFailureException;
import ec.edu.monster.entidades.PerfilOpcion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ec.edu.monster.entidades.XeOpcOpcion;
import ec.edu.monster.entidades.XeperPerfil;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Diego
 */
public class PerfilOpcionJpaController implements Serializable {

    public PerfilOpcionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PerfilOpcion perfilOpcion) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            XeOpcOpcion xeopcCodigo = perfilOpcion.getXeopcCodigo();
            if (xeopcCodigo != null) {
                xeopcCodigo = em.getReference(xeopcCodigo.getClass(), xeopcCodigo.getXeopcCodigo());
                perfilOpcion.setXeopcCodigo(xeopcCodigo);
            }
            XeperPerfil xeperCodigo = perfilOpcion.getXeperCodigo();
            if (xeperCodigo != null) {
                xeperCodigo = em.getReference(xeperCodigo.getClass(), xeperCodigo.getXeperCodigo());
                perfilOpcion.setXeperCodigo(xeperCodigo);
            }
            em.persist(perfilOpcion);
            if (xeopcCodigo != null) {
                xeopcCodigo.getPerfilOpcionCollection().add(perfilOpcion);
                xeopcCodigo = em.merge(xeopcCodigo);
            }
            if (xeperCodigo != null) {
                xeperCodigo.getPerfilOpcionCollection().add(perfilOpcion);
                xeperCodigo = em.merge(xeperCodigo);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPerfilOpcion(perfilOpcion.getPopFechaAsignacion()) != null) {
                throw new PreexistingEntityException("PerfilOpcion " + perfilOpcion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PerfilOpcion perfilOpcion) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PerfilOpcion persistentPerfilOpcion = em.find(PerfilOpcion.class, perfilOpcion.getPopFechaAsignacion());
            XeOpcOpcion xeopcCodigoOld = persistentPerfilOpcion.getXeopcCodigo();
            XeOpcOpcion xeopcCodigoNew = perfilOpcion.getXeopcCodigo();
            XeperPerfil xeperCodigoOld = persistentPerfilOpcion.getXeperCodigo();
            XeperPerfil xeperCodigoNew = perfilOpcion.getXeperCodigo();
            if (xeopcCodigoNew != null) {
                xeopcCodigoNew = em.getReference(xeopcCodigoNew.getClass(), xeopcCodigoNew.getXeopcCodigo());
                perfilOpcion.setXeopcCodigo(xeopcCodigoNew);
            }
            if (xeperCodigoNew != null) {
                xeperCodigoNew = em.getReference(xeperCodigoNew.getClass(), xeperCodigoNew.getXeperCodigo());
                perfilOpcion.setXeperCodigo(xeperCodigoNew);
            }
            perfilOpcion = em.merge(perfilOpcion);
            if (xeopcCodigoOld != null && !xeopcCodigoOld.equals(xeopcCodigoNew)) {
                xeopcCodigoOld.getPerfilOpcionCollection().remove(perfilOpcion);
                xeopcCodigoOld = em.merge(xeopcCodigoOld);
            }
            if (xeopcCodigoNew != null && !xeopcCodigoNew.equals(xeopcCodigoOld)) {
                xeopcCodigoNew.getPerfilOpcionCollection().add(perfilOpcion);
                xeopcCodigoNew = em.merge(xeopcCodigoNew);
            }
            if (xeperCodigoOld != null && !xeperCodigoOld.equals(xeperCodigoNew)) {
                xeperCodigoOld.getPerfilOpcionCollection().remove(perfilOpcion);
                xeperCodigoOld = em.merge(xeperCodigoOld);
            }
            if (xeperCodigoNew != null && !xeperCodigoNew.equals(xeperCodigoOld)) {
                xeperCodigoNew.getPerfilOpcionCollection().add(perfilOpcion);
                xeperCodigoNew = em.merge(xeperCodigoNew);
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
                Date id = perfilOpcion.getPopFechaAsignacion();
                if (findPerfilOpcion(id) == null) {
                    throw new NonexistentEntityException("The perfilOpcion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Date id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PerfilOpcion perfilOpcion;
            try {
                perfilOpcion = em.getReference(PerfilOpcion.class, id);
                perfilOpcion.getPopFechaAsignacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The perfilOpcion with id " + id + " no longer exists.", enfe);
            }
            XeOpcOpcion xeopcCodigo = perfilOpcion.getXeopcCodigo();
            if (xeopcCodigo != null) {
                xeopcCodigo.getPerfilOpcionCollection().remove(perfilOpcion);
                xeopcCodigo = em.merge(xeopcCodigo);
            }
            XeperPerfil xeperCodigo = perfilOpcion.getXeperCodigo();
            if (xeperCodigo != null) {
                xeperCodigo.getPerfilOpcionCollection().remove(perfilOpcion);
                xeperCodigo = em.merge(xeperCodigo);
            }
            em.remove(perfilOpcion);
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

    public List<PerfilOpcion> findPerfilOpcionEntities() {
        return findPerfilOpcionEntities(true, -1, -1);
    }

    public List<PerfilOpcion> findPerfilOpcionEntities(int maxResults, int firstResult) {
        return findPerfilOpcionEntities(false, maxResults, firstResult);
    }

    private List<PerfilOpcion> findPerfilOpcionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PerfilOpcion.class));
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

    public PerfilOpcion findPerfilOpcion(Date id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PerfilOpcion.class, id);
        } finally {
            em.close();
        }
    }

    public int getPerfilOpcionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PerfilOpcion> rt = cq.from(PerfilOpcion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
