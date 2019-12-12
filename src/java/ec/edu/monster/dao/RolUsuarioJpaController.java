/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.dao;

import ec.edu.monster.dao.exceptions.NonexistentEntityException;
import ec.edu.monster.dao.exceptions.PreexistingEntityException;
import ec.edu.monster.dao.exceptions.RollbackFailureException;
import ec.edu.monster.entidades.RolUsuario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ec.edu.monster.entidades.XerolRol;
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
public class RolUsuarioJpaController implements Serializable {

    public RolUsuarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RolUsuario rolUsuario) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            XerolRol xerolCodigo = rolUsuario.getXerolCodigo();
            if (xerolCodigo != null) {
                xerolCodigo = em.getReference(xerolCodigo.getClass(), xerolCodigo.getXerolCodigo());
                rolUsuario.setXerolCodigo(xerolCodigo);
            }
            XeperPerfil xeperCodigo = rolUsuario.getXeperCodigo();
            if (xeperCodigo != null) {
                xeperCodigo = em.getReference(xeperCodigo.getClass(), xeperCodigo.getXeperCodigo());
                rolUsuario.setXeperCodigo(xeperCodigo);
            }
            em.persist(rolUsuario);
            if (xerolCodigo != null) {
                xerolCodigo.getRolUsuarioCollection().add(rolUsuario);
                xerolCodigo = em.merge(xerolCodigo);
            }
            if (xeperCodigo != null) {
                xeperCodigo.getRolUsuarioCollection().add(rolUsuario);
                xeperCodigo = em.merge(xeperCodigo);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRolUsuario(rolUsuario.getRusFechaAsignacion()) != null) {
                throw new PreexistingEntityException("RolUsuario " + rolUsuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RolUsuario rolUsuario) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RolUsuario persistentRolUsuario = em.find(RolUsuario.class, rolUsuario.getRusFechaAsignacion());
            XerolRol xerolCodigoOld = persistentRolUsuario.getXerolCodigo();
            XerolRol xerolCodigoNew = rolUsuario.getXerolCodigo();
            XeperPerfil xeperCodigoOld = persistentRolUsuario.getXeperCodigo();
            XeperPerfil xeperCodigoNew = rolUsuario.getXeperCodigo();
            if (xerolCodigoNew != null) {
                xerolCodigoNew = em.getReference(xerolCodigoNew.getClass(), xerolCodigoNew.getXerolCodigo());
                rolUsuario.setXerolCodigo(xerolCodigoNew);
            }
            if (xeperCodigoNew != null) {
                xeperCodigoNew = em.getReference(xeperCodigoNew.getClass(), xeperCodigoNew.getXeperCodigo());
                rolUsuario.setXeperCodigo(xeperCodigoNew);
            }
            rolUsuario = em.merge(rolUsuario);
            if (xerolCodigoOld != null && !xerolCodigoOld.equals(xerolCodigoNew)) {
                xerolCodigoOld.getRolUsuarioCollection().remove(rolUsuario);
                xerolCodigoOld = em.merge(xerolCodigoOld);
            }
            if (xerolCodigoNew != null && !xerolCodigoNew.equals(xerolCodigoOld)) {
                xerolCodigoNew.getRolUsuarioCollection().add(rolUsuario);
                xerolCodigoNew = em.merge(xerolCodigoNew);
            }
            if (xeperCodigoOld != null && !xeperCodigoOld.equals(xeperCodigoNew)) {
                xeperCodigoOld.getRolUsuarioCollection().remove(rolUsuario);
                xeperCodigoOld = em.merge(xeperCodigoOld);
            }
            if (xeperCodigoNew != null && !xeperCodigoNew.equals(xeperCodigoOld)) {
                xeperCodigoNew.getRolUsuarioCollection().add(rolUsuario);
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
                Date id = rolUsuario.getRusFechaAsignacion();
                if (findRolUsuario(id) == null) {
                    throw new NonexistentEntityException("The rolUsuario with id " + id + " no longer exists.");
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
            RolUsuario rolUsuario;
            try {
                rolUsuario = em.getReference(RolUsuario.class, id);
                rolUsuario.getRusFechaAsignacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rolUsuario with id " + id + " no longer exists.", enfe);
            }
            XerolRol xerolCodigo = rolUsuario.getXerolCodigo();
            if (xerolCodigo != null) {
                xerolCodigo.getRolUsuarioCollection().remove(rolUsuario);
                xerolCodigo = em.merge(xerolCodigo);
            }
            XeperPerfil xeperCodigo = rolUsuario.getXeperCodigo();
            if (xeperCodigo != null) {
                xeperCodigo.getRolUsuarioCollection().remove(rolUsuario);
                xeperCodigo = em.merge(xeperCodigo);
            }
            em.remove(rolUsuario);
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

    public List<RolUsuario> findRolUsuarioEntities() {
        return findRolUsuarioEntities(true, -1, -1);
    }

    public List<RolUsuario> findRolUsuarioEntities(int maxResults, int firstResult) {
        return findRolUsuarioEntities(false, maxResults, firstResult);
    }

    private List<RolUsuario> findRolUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RolUsuario.class));
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

    public RolUsuario findRolUsuario(Date id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RolUsuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RolUsuario> rt = cq.from(RolUsuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
