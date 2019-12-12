/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Diego
 */
@Entity
@Table(name = "xeper_perfil")
@NamedQueries({
    @NamedQuery(name = "XeperPerfil.findAll", query = "SELECT x FROM XeperPerfil x")
    , @NamedQuery(name = "XeperPerfil.findByXeperCodigo", query = "SELECT x FROM XeperPerfil x WHERE x.xeperCodigo = :xeperCodigo")
    , @NamedQuery(name = "XeperPerfil.findByXeperNombre", query = "SELECT x FROM XeperPerfil x WHERE x.xeperNombre = :xeperNombre")})
public class XeperPerfil implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "XEPER_CODIGO")
    private String xeperCodigo;
    @Size(max = 100)
    @Column(name = "XEPER_NOMBRE")
    private String xeperNombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xeperCodigo")
    private Collection<XeusuUsuar> xeusuUsuarCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xeperCodigo")
    private Collection<RolUsuario> rolUsuarioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xeperCodigo")
    private Collection<PerfilOpcion> perfilOpcionCollection;

    public XeperPerfil() {
    }

    public XeperPerfil(String xeperCodigo) {
        this.xeperCodigo = xeperCodigo;
    }

    public String getXeperCodigo() {
        return xeperCodigo;
    }

    public void setXeperCodigo(String xeperCodigo) {
        this.xeperCodigo = xeperCodigo;
    }

    public String getXeperNombre() {
        return xeperNombre;
    }

    public void setXeperNombre(String xeperNombre) {
        this.xeperNombre = xeperNombre;
    }

    public Collection<XeusuUsuar> getXeusuUsuarCollection() {
        return xeusuUsuarCollection;
    }

    public void setXeusuUsuarCollection(Collection<XeusuUsuar> xeusuUsuarCollection) {
        this.xeusuUsuarCollection = xeusuUsuarCollection;
    }

    public Collection<RolUsuario> getRolUsuarioCollection() {
        return rolUsuarioCollection;
    }

    public void setRolUsuarioCollection(Collection<RolUsuario> rolUsuarioCollection) {
        this.rolUsuarioCollection = rolUsuarioCollection;
    }

    public Collection<PerfilOpcion> getPerfilOpcionCollection() {
        return perfilOpcionCollection;
    }

    public void setPerfilOpcionCollection(Collection<PerfilOpcion> perfilOpcionCollection) {
        this.perfilOpcionCollection = perfilOpcionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xeperCodigo != null ? xeperCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XeperPerfil)) {
            return false;
        }
        XeperPerfil other = (XeperPerfil) object;
        if ((this.xeperCodigo == null && other.xeperCodigo != null) || (this.xeperCodigo != null && !this.xeperCodigo.equals(other.xeperCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  xeperNombre;
    }
    
}
