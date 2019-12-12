package ec.edu.monster.jsf.clases;

import ec.edu.monster.entidades.XeperPerfil;
import ec.edu.monster.jsf.clases.util.JsfUtil;
import ec.edu.monster.jsf.clases.util.JsfUtil.PersistAction;
import ec.edu.monster.beans.sessions.XeperPerfilFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("xeperPerfilController")
@SessionScoped
public class XeperPerfilController implements Serializable {

    @EJB
    private ec.edu.monster.beans.sessions.XeperPerfilFacade ejbFacade;
    private List<XeperPerfil> items = null;
    private XeperPerfil selected;

    public XeperPerfilController() {
    }

    public XeperPerfil getSelected() {
        return selected;
    }

    public void setSelected(XeperPerfil selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private XeperPerfilFacade getFacade() {
        return ejbFacade;
    }

    public XeperPerfil prepareCreate() {
        selected = new XeperPerfil();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("XeperPerfilCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("XeperPerfilUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("XeperPerfilDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<XeperPerfil> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }
    public List<XeperPerfil> getNombres()
    {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public XeperPerfil getXeperPerfil(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<XeperPerfil> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<XeperPerfil> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = XeperPerfil.class)
    public static class XeperPerfilControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            XeperPerfilController controller = (XeperPerfilController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "xeperPerfilController");
            return controller.getXeperPerfil(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof XeperPerfil) {
                XeperPerfil o = (XeperPerfil) object;
                return getStringKey(o.getXeperCodigo());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), XeperPerfil.class.getName()});
                return null;
            }
        }

    }

}
