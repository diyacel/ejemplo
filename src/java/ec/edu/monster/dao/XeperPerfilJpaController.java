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
import ec.edu.monster.entidades.XeusuUsuar;
import java.util.ArrayList;
import java.util.Collection;
import ec.edu.monster.entidades.RolUsuario;
import ec.edu.monster.entidades.PerfilOpcion;
import ec.edu.monster.entidades.XeperPerfil;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Diego
 */
public class XeperPerfilJpaController implements Serializable {

    public XeperPerfilJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(XeperPerfil xeperPerfil) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (xeperPerfil.getXeusuUsuarCollection() == null) {
            xeperPerfil.setXeusuUsuarCollection(new ArrayList<XeusuUsuar>());
        }
        if (xeperPerfil.getRolUsuarioCollection() == null) {
            xeperPerfil.setRolUsuarioCollection(new ArrayList<RolUsuario>());
        }
        if (xeperPerfil.getPerfilOpcionCollection() == null) {
            xeperPerfil.setPerfilOpcionCollection(new ArrayList<PerfilOpcion>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<XeusuUsuar> attachedXeusuUsuarCollection = new ArrayList<XeusuUsuar>();
            for (XeusuUsuar xeusuUsuarCollectionXeusuUsuarToAttach : xeperPerfil.getXeusuUsuarCollection()) {
                xeusuUsuarCollectionXeusuUsuarToAttach = em.getReference(xeusuUsuarCollectionXeusuUsuarToAttach.getClass(), xeusuUsuarCollectionXeusuUsuarToAttach.getXeusuUsuarPK());
                attachedXeusuUsuarCollection.add(xeusuUsuarCollectionXeusuUsuarToAttach);
            }
            xeperPerfil.setXeusuUsuarCollection(attachedXeusuUsuarCollection);
            Collection<RolUsuario> attachedRolUsuarioCollection = new ArrayList<RolUsuario>();
            for (RolUsuario rolUsuarioCollectionRolUsuarioToAttach : xeperPerfil.getRolUsuarioCollection()) {
                rolUsuarioCollectionRolUsuarioToAttach = em.getReference(rolUsuarioCollectionRolUsuarioToAttach.getClass(), rolUsuarioCollectionRolUsuarioToAttach.getRusFechaAsignacion());
                attachedRolUsuarioCollection.add(rolUsuarioCollectionRolUsuarioToAttach);
            }
            xeperPerfil.setRolUsuarioCollection(attachedRolUsuarioCollection);
            Collection<PerfilOpcion> attachedPerfilOpcionCollection = new ArrayList<PerfilOpcion>();
            for (PerfilOpcion perfilOpcionCollectionPerfilOpcionToAttach : xeperPerfil.getPerfilOpcionCollection()) {
                perfilOpcionCollectionPerfilOpcionToAttach = em.getReference(perfilOpcionCollectionPerfilOpcionToAttach.getClass(), perfilOpcionCollectionPerfilOpcionToAttach.getPopFechaAsignacion());
                attachedPerfilOpcionCollection.add(perfilOpcionCollectionPerfilOpcionToAttach);
            }
            xeperPerfil.setPerfilOpcionCollection(attachedPerfilOpcionCollection);
            em.persist(xeperPerfil);
            for (XeusuUsuar xeusuUsuarCollectionXeusuUsuar : xeperPerfil.getXeusuUsuarCollection()) {
                XeperPerfil oldXeperCodigoOfXeusuUsuarCollectionXeusuUsuar = xeusuUsuarCollectionXeusuUsuar.getXeperCodigo();
                xeusuUsuarCollectionXeusuUsuar.setXeperCodigo(xeperPerfil);
                xeusuUsuarCollectionXeusuUsuar = em.merge(xeusuUsuarCollectionXeusuUsuar);
                if (oldXeperCodigoOfXeusuUsuarCollectionXeusuUsuar != null) {
                    oldXeperCodigoOfXeusuUsuarCollectionXeusuUsuar.getXeusuUsuarCollection().remove(xeusuUsuarCollectionXeusuUsuar);
                    oldXeperCodigoOfXeusuUsuarCollectionXeusuUsuar = em.merge(oldXeperCodigoOfXeusuUsuarCollectionXeusuUsuar);
                }
            }
            for (RolUsuario rolUsuarioCollectionRolUsuario : xeperPerfil.getRolUsuarioCollection()) {
                XeperPerfil oldXeperCodigoOfRolUsuarioCollectionRolUsuario = rolUsuarioCollectionRolUsuario.getXeperCodigo();
                rolUsuarioCollectionRolUsuario.setXeperCodigo(xeperPerfil);
                rolUsuarioCollectionRolUsuario = em.merge(rolUsuarioCollectionRolUsuario);
                if (oldXeperCodigoOfRolUsuarioCollectionRolUsuario != null) {
                    oldXeperCodigoOfRolUsuarioCollectionRolUsuario.getRolUsuarioCollection().remove(rolUsuarioCollectionRolUsuario);
                    oldXeperCodigoOfRolUsuarioCollectionRolUsuario = em.merge(oldXeperCodigoOfRolUsuarioCollectionRolUsuario);
                }
            }
            for (PerfilOpcion perfilOpcionCollectionPerfilOpcion : xeperPerfil.getPerfilOpcionCollection()) {
                XeperPerfil oldXeperCodigoOfPerfilOpcionCollectionPerfilOpcion = perfilOpcionCollectionPerfilOpcion.getXeperCodigo();
                perfilOpcionCollectionPerfilOpcion.setXeperCodigo(xeperPerfil);
                perfilOpcionCollectionPerfilOpcion = em.merge(perfilOpcionCollectionPerfilOpcion);
                if (oldXeperCodigoOfPerfilOpcionCollectionPerfilOpcion != null) {
                    oldXeperCodigoOfPerfilOpcionCollectionPerfilOpcion.getPerfilOpcionCollection().remove(perfilOpcionCollectionPerfilOpcion);
                    oldXeperCodigoOfPerfilOpcionCollectionPerfilOpcion = em.merge(oldXeperCodigoOfPerfilOpcionCollectionPerfilOpcion);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findXeperPerfil(xeperPerfil.getXeperCodigo()) != null) {
                throw new PreexistingEntityException("XeperPerfil " + xeperPerfil + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(XeperPerfil xeperPerfil) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            XeperPerfil persistentXeperPerfil = em.find(XeperPerfil.class, xeperPerfil.getXeperCodigo());
            Collection<XeusuUsuar> xeusuUsuarCollectionOld = persistentXeperPerfil.getXeusuUsuarCollection();
            Collection<XeusuUsuar> xeusuUsuarCollectionNew = xeperPerfil.getXeusuUsuarCollection();
            Collection<RolUsuario> rolUsuarioCollectionOld = persistentXeperPerfil.getRolUsuarioCollection();
            Collection<RolUsuario> rolUsuarioCollectionNew = xeperPerfil.getRolUsuarioCollection();
            Collection<PerfilOpcion> perfilOpcionCollectionOld = persistentXeperPerfil.getPerfilOpcionCollection();
            Collection<PerfilOpcion> perfilOpcionCollectionNew = xeperPerfil.getPerfilOpcionCollection();
            List<String> illegalOrphanMessages = null;
            for (XeusuUsuar xeusuUsuarCollectionOldXeusuUsuar : xeusuUsuarCollectionOld) {
                if (!xeusuUsuarCollectionNew.contains(xeusuUsuarCollectionOldXeusuUsuar)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain XeusuUsuar " + xeusuUsuarCollectionOldXeusuUsuar + " since its xeperCodigo field is not nullable.");
                }
            }
            for (RolUsuario rolUsuarioCollectionOldRolUsuario : rolUsuarioCollectionOld) {
                if (!rolUsuarioCollectionNew.contains(rolUsuarioCollectionOldRolUsuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RolUsuario " + rolUsuarioCollectionOldRolUsuario + " since its xeperCodigo field is not nullable.");
                }
            }
            for (PerfilOpcion perfilOpcionCollectionOldPerfilOpcion : perfilOpcionCollectionOld) {
                if (!perfilOpcionCollectionNew.contains(perfilOpcionCollectionOldPerfilOpcion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PerfilOpcion " + perfilOpcionCollectionOldPerfilOpcion + " since its xeperCodigo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<XeusuUsuar> attachedXeusuUsuarCollectionNew = new ArrayList<XeusuUsuar>();
            for (XeusuUsuar xeusuUsuarCollectionNewXeusuUsuarToAttach : xeusuUsuarCollectionNew) {
                xeusuUsuarCollectionNewXeusuUsuarToAttach = em.getReference(xeusuUsuarCollectionNewXeusuUsuarToAttach.getClass(), xeusuUsuarCollectionNewXeusuUsuarToAttach.getXeusuUsuarPK());
                attachedXeusuUsuarCollectionNew.add(xeusuUsuarCollectionNewXeusuUsuarToAttach);
            }
            xeusuUsuarCollectionNew = attachedXeusuUsuarCollectionNew;
            xeperPerfil.setXeusuUsuarCollection(xeusuUsuarCollectionNew);
            Collection<RolUsuario> attachedRolUsuarioCollectionNew = new ArrayList<RolUsuario>();
            for (RolUsuario rolUsuarioCollectionNewRolUsuarioToAttach : rolUsuarioCollectionNew) {
                rolUsuarioCollectionNewRolUsuarioToAttach = em.getReference(rolUsuarioCollectionNewRolUsuarioToAttach.getClass(), rolUsuarioCollectionNewRolUsuarioToAttach.getRusFechaAsignacion());
                attachedRolUsuarioCollectionNew.add(rolUsuarioCollectionNewRolUsuarioToAttach);
            }
            rolUsuarioCollectionNew = attachedRolUsuarioCollectionNew;
            xeperPerfil.setRolUsuarioCollection(rolUsuarioCollectionNew);
            Collection<PerfilOpcion> attachedPerfilOpcionCollectionNew = new ArrayList<PerfilOpcion>();
            for (PerfilOpcion perfilOpcionCollectionNewPerfilOpcionToAttach : perfilOpcionCollectionNew) {
                perfilOpcionCollectionNewPerfilOpcionToAttach = em.getReference(perfilOpcionCollectionNewPerfilOpcionToAttach.getClass(), perfilOpcionCollectionNewPerfilOpcionToAttach.getPopFechaAsignacion());
                attachedPerfilOpcionCollectionNew.add(perfilOpcionCollectionNewPerfilOpcionToAttach);
            }
            perfilOpcionCollectionNew = attachedPerfilOpcionCollectionNew;
            xeperPerfil.setPerfilOpcionCollection(perfilOpcionCollectionNew);
            xeperPerfil = em.merge(xeperPerfil);
            for (XeusuUsuar xeusuUsuarCollectionNewXeusuUsuar : xeusuUsuarCollectionNew) {
                if (!xeusuUsuarCollectionOld.contains(xeusuUsuarCollectionNewXeusuUsuar)) {
                    XeperPerfil oldXeperCodigoOfXeusuUsuarCollectionNewXeusuUsuar = xeusuUsuarCollectionNewXeusuUsuar.getXeperCodigo();
                    xeusuUsuarCollectionNewXeusuUsuar.setXeperCodigo(xeperPerfil);
                    xeusuUsuarCollectionNewXeusuUsuar = em.merge(xeusuUsuarCollectionNewXeusuUsuar);
                    if (oldXeperCodigoOfXeusuUsuarCollectionNewXeusuUsuar != null && !oldXeperCodigoOfXeusuUsuarCollectionNewXeusuUsuar.equals(xeperPerfil)) {
                        oldXeperCodigoOfXeusuUsuarCollectionNewXeusuUsuar.getXeusuUsuarCollection().remove(xeusuUsuarCollectionNewXeusuUsuar);
                        oldXeperCodigoOfXeusuUsuarCollectionNewXeusuUsuar = em.merge(oldXeperCodigoOfXeusuUsuarCollectionNewXeusuUsuar);
                    }
                }
            }
            for (RolUsuario rolUsuarioCollectionNewRolUsuario : rolUsuarioCollectionNew) {
                if (!rolUsuarioCollectionOld.contains(rolUsuarioCollectionNewRolUsuario)) {
                    XeperPerfil oldXeperCodigoOfRolUsuarioCollectionNewRolUsuario = rolUsuarioCollectionNewRolUsuario.getXeperCodigo();
                    rolUsuarioCollectionNewRolUsuario.setXeperCodigo(xeperPerfil);
                    rolUsuarioCollectionNewRolUsuario = em.merge(rolUsuarioCollectionNewRolUsuario);
                    if (oldXeperCodigoOfRolUsuarioCollectionNewRolUsuario != null && !oldXeperCodigoOfRolUsuarioCollectionNewRolUsuario.equals(xeperPerfil)) {
                        oldXeperCodigoOfRolUsuarioCollectionNewRolUsuario.getRolUsuarioCollection().remove(rolUsuarioCollectionNewRolUsuario);
                        oldXeperCodigoOfRolUsuarioCollectionNewRolUsuario = em.merge(oldXeperCodigoOfRolUsuarioCollectionNewRolUsuario);
                    }
                }
            }
            for (PerfilOpcion perfilOpcionCollectionNewPerfilOpcion : perfilOpcionCollectionNew) {
                if (!perfilOpcionCollectionOld.contains(perfilOpcionCollectionNewPerfilOpcion)) {
                    XeperPerfil oldXeperCodigoOfPerfilOpcionCollectionNewPerfilOpcion = perfilOpcionCollectionNewPerfilOpcion.getXeperCodigo();
                    perfilOpcionCollectionNewPerfilOpcion.setXeperCodigo(xeperPerfil);
                    perfilOpcionCollectionNewPerfilOpcion = em.merge(perfilOpcionCollectionNewPerfilOpcion);
                    if (oldXeperCodigoOfPerfilOpcionCollectionNewPerfilOpcion != null && !oldXeperCodigoOfPerfilOpcionCollectionNewPerfilOpcion.equals(xeperPerfil)) {
                        oldXeperCodigoOfPerfilOpcionCollectionNewPerfilOpcion.getPerfilOpcionCollection().remove(perfilOpcionCollectionNewPerfilOpcion);
                        oldXeperCodigoOfPerfilOpcionCollectionNewPerfilOpcion = em.merge(oldXeperCodigoOfPerfilOpcionCollectionNewPerfilOpcion);
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
                String id = xeperPerfil.getXeperCodigo();
                if (findXeperPerfil(id) == null) {
                    throw new NonexistentEntityException("The xeperPerfil with id " + id + " no longer exists.");
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
            XeperPerfil xeperPerfil;
            try {
                xeperPerfil = em.getReference(XeperPerfil.class, id);
                xeperPerfil.getXeperCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The xeperPerfil with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<XeusuUsuar> xeusuUsuarCollectionOrphanCheck = xeperPerfil.getXeusuUsuarCollection();
            for (XeusuUsuar xeusuUsuarCollectionOrphanCheckXeusuUsuar : xeusuUsuarCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XeperPerfil (" + xeperPerfil + ") cannot be destroyed since the XeusuUsuar " + xeusuUsuarCollectionOrphanCheckXeusuUsuar + " in its xeusuUsuarCollection field has a non-nullable xeperCodigo field.");
            }
            Collection<RolUsuario> rolUsuarioCollectionOrphanCheck = xeperPerfil.getRolUsuarioCollection();
            for (RolUsuario rolUsuarioCollectionOrphanCheckRolUsuario : rolUsuarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XeperPerfil (" + xeperPerfil + ") cannot be destroyed since the RolUsuario " + rolUsuarioCollectionOrphanCheckRolUsuario + " in its rolUsuarioCollection field has a non-nullable xeperCodigo field.");
            }
            Collection<PerfilOpcion> perfilOpcionCollectionOrphanCheck = xeperPerfil.getPerfilOpcionCollection();
            for (PerfilOpcion perfilOpcionCollectionOrphanCheckPerfilOpcion : perfilOpcionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This XeperPerfil (" + xeperPerfil + ") cannot be destroyed since the PerfilOpcion " + perfilOpcionCollectionOrphanCheckPerfilOpcion + " in its perfilOpcionCollection field has a non-nullable xeperCodigo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(xeperPerfil);
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

    public List<XeperPerfil> findXeperPerfilEntities() {
        return findXeperPerfilEntities(true, -1, -1);
    }

    public List<XeperPerfil> findXeperPerfilEntities(int maxResults, int firstResult) {
        return findXeperPerfilEntities(false, maxResults, firstResult);
    }

    private List<XeperPerfil> findXeperPerfilEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(XeperPerfil.class));
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

    public XeperPerfil findXeperPerfil(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(XeperPerfil.class, id);
        } finally {
            em.close();
        }
    }

    public int getXeperPerfilCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<XeperPerfil> rt = cq.from(XeperPerfil.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
