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
import ec.edu.monster.entidades.PeempEmpleado;
import ec.edu.monster.entidades.XeEstEstado;
import ec.edu.monster.entidades.XeperPerfil;
import ec.edu.monster.entidades.XeusuUsuar;
import ec.edu.monster.entidades.XeusuUsuarPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Diego
 */
public class XeusuUsuarJpaController implements Serializable {

    public XeusuUsuarJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XeusuUsuar xeusuUsuar) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (xeusuUsuar.getXeusuUsuarPK() == null) {
            xeusuUsuar.setXeusuUsuarPK(new XeusuUsuarPK());
        }
        xeusuUsuar.getXeusuUsuarPK().setPeempCodig(xeusuUsuar.getPeempEmpleado().getPeempCodig());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PeempEmpleado peempEmpleado = xeusuUsuar.getPeempEmpleado();
            if (peempEmpleado != null) {
                peempEmpleado = em.getReference(peempEmpleado.getClass(), peempEmpleado.getPeempCodig());
                xeusuUsuar.setPeempEmpleado(peempEmpleado);
            }
            XeEstEstado xeuestCodigo = xeusuUsuar.getXeuestCodigo();
            if (xeuestCodigo != null) {
                xeuestCodigo = em.getReference(xeuestCodigo.getClass(), xeuestCodigo.getXeuestCodigo());
                xeusuUsuar.setXeuestCodigo(xeuestCodigo);
            }
            XeperPerfil xeperCodigo = xeusuUsuar.getXeperCodigo();
            if (xeperCodigo != null) {
                xeperCodigo = em.getReference(xeperCodigo.getClass(), xeperCodigo.getXeperCodigo());
                xeusuUsuar.setXeperCodigo(xeperCodigo);
            }
            em.persist(xeusuUsuar);
            if (peempEmpleado != null) {
                peempEmpleado.getXeusuUsuarCollection().add(xeusuUsuar);
                peempEmpleado = em.merge(peempEmpleado);
            }
            if (xeuestCodigo != null) {
                xeuestCodigo.getXeusuUsuarCollection().add(xeusuUsuar);
                xeuestCodigo = em.merge(xeuestCodigo);
            }
            if (xeperCodigo != null) {
                xeperCodigo.getXeusuUsuarCollection().add(xeusuUsuar);
                xeperCodigo = em.merge(xeperCodigo);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findXeusuUsuar(xeusuUsuar.getXeusuUsuarPK()) != null) {
                throw new PreexistingEntityException("XeusuUsuar " + xeusuUsuar + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XeusuUsuar xeusuUsuar) throws NonexistentEntityException, RollbackFailureException, Exception {
        xeusuUsuar.getXeusuUsuarPK().setPeempCodig(xeusuUsuar.getPeempEmpleado().getPeempCodig());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            XeusuUsuar persistentXeusuUsuar = em.find(XeusuUsuar.class, xeusuUsuar.getXeusuUsuarPK());
            PeempEmpleado peempEmpleadoOld = persistentXeusuUsuar.getPeempEmpleado();
            PeempEmpleado peempEmpleadoNew = xeusuUsuar.getPeempEmpleado();
            XeEstEstado xeuestCodigoOld = persistentXeusuUsuar.getXeuestCodigo();
            XeEstEstado xeuestCodigoNew = xeusuUsuar.getXeuestCodigo();
            XeperPerfil xeperCodigoOld = persistentXeusuUsuar.getXeperCodigo();
            XeperPerfil xeperCodigoNew = xeusuUsuar.getXeperCodigo();
            if (peempEmpleadoNew != null) {
                peempEmpleadoNew = em.getReference(peempEmpleadoNew.getClass(), peempEmpleadoNew.getPeempCodig());
                xeusuUsuar.setPeempEmpleado(peempEmpleadoNew);
            }
            if (xeuestCodigoNew != null) {
                xeuestCodigoNew = em.getReference(xeuestCodigoNew.getClass(), xeuestCodigoNew.getXeuestCodigo());
                xeusuUsuar.setXeuestCodigo(xeuestCodigoNew);
            }
            if (xeperCodigoNew != null) {
                xeperCodigoNew = em.getReference(xeperCodigoNew.getClass(), xeperCodigoNew.getXeperCodigo());
                xeusuUsuar.setXeperCodigo(xeperCodigoNew);
            }
            xeusuUsuar = em.merge(xeusuUsuar);
            if (peempEmpleadoOld != null && !peempEmpleadoOld.equals(peempEmpleadoNew)) {
                peempEmpleadoOld.getXeusuUsuarCollection().remove(xeusuUsuar);
                peempEmpleadoOld = em.merge(peempEmpleadoOld);
            }
            if (peempEmpleadoNew != null && !peempEmpleadoNew.equals(peempEmpleadoOld)) {
                peempEmpleadoNew.getXeusuUsuarCollection().add(xeusuUsuar);
                peempEmpleadoNew = em.merge(peempEmpleadoNew);
            }
            if (xeuestCodigoOld != null && !xeuestCodigoOld.equals(xeuestCodigoNew)) {
                xeuestCodigoOld.getXeusuUsuarCollection().remove(xeusuUsuar);
                xeuestCodigoOld = em.merge(xeuestCodigoOld);
            }
            if (xeuestCodigoNew != null && !xeuestCodigoNew.equals(xeuestCodigoOld)) {
                xeuestCodigoNew.getXeusuUsuarCollection().add(xeusuUsuar);
                xeuestCodigoNew = em.merge(xeuestCodigoNew);
            }
            if (xeperCodigoOld != null && !xeperCodigoOld.equals(xeperCodigoNew)) {
                xeperCodigoOld.getXeusuUsuarCollection().remove(xeusuUsuar);
                xeperCodigoOld = em.merge(xeperCodigoOld);
            }
            if (xeperCodigoNew != null && !xeperCodigoNew.equals(xeperCodigoOld)) {
                xeperCodigoNew.getXeusuUsuarCollection().add(xeusuUsuar);
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
                XeusuUsuarPK id = xeusuUsuar.getXeusuUsuarPK();
                if (findXeusuUsuar(id) == null) {
                    throw new NonexistentEntityException("The xeusuUsuar with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(XeusuUsuarPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            XeusuUsuar xeusuUsuar;
            try {
                xeusuUsuar = em.getReference(XeusuUsuar.class, id);
                xeusuUsuar.getXeusuUsuarPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xeusuUsuar with id " + id + " no longer exists.", enfe);
            }
            PeempEmpleado peempEmpleado = xeusuUsuar.getPeempEmpleado();
            if (peempEmpleado != null) {
                peempEmpleado.getXeusuUsuarCollection().remove(xeusuUsuar);
                peempEmpleado = em.merge(peempEmpleado);
            }
            XeEstEstado xeuestCodigo = xeusuUsuar.getXeuestCodigo();
            if (xeuestCodigo != null) {
                xeuestCodigo.getXeusuUsuarCollection().remove(xeusuUsuar);
                xeuestCodigo = em.merge(xeuestCodigo);
            }
            XeperPerfil xeperCodigo = xeusuUsuar.getXeperCodigo();
            if (xeperCodigo != null) {
                xeperCodigo.getXeusuUsuarCollection().remove(xeusuUsuar);
                xeperCodigo = em.merge(xeperCodigo);
            }
            em.remove(xeusuUsuar);
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

    public List<XeusuUsuar> findXeusuUsuarEntities() {
        return findXeusuUsuarEntities(true, -1, -1);
    }

    public List<XeusuUsuar> findXeusuUsuarEntities(int maxResults, int firstResult) {
        return findXeusuUsuarEntities(false, maxResults, firstResult);
    }

    private List<XeusuUsuar> findXeusuUsuarEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XeusuUsuar.class));
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

    public XeusuUsuar findXeusuUsuar(XeusuUsuarPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XeusuUsuar.class, id);
        } finally {
            em.close();
        }
    }

    public int getXeusuUsuarCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XeusuUsuar> rt = cq.from(XeusuUsuar.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
