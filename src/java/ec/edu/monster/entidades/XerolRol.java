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
@Table(name = "xerol_rol")
@NamedQueries({
    @NamedQuery(name = "XerolRol.findAll", query = "SELECT x FROM XerolRol x")
    , @NamedQuery(name = "XerolRol.findByXerolCodigo", query = "SELECT x FROM XerolRol x WHERE x.xerolCodigo = :xerolCodigo")
    , @NamedQuery(name = "XerolRol.findByXerolNombre", query = "SELECT x FROM XerolRol x WHERE x.xerolNombre = :xerolNombre")})
public class XerolRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "XEROL_CODIGO")
    private String xerolCodigo;
    @Size(max = 100)
    @Column(name = "XEROL_NOMBRE")
    private String xerolNombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xerolCodigo")
    private Collection<RolUsuario> rolUsuarioCollection;

    public XerolRol() {
    }

    public XerolRol(String xerolCodigo) {
        this.xerolCodigo = xerolCodigo;
    }

    public String getXerolCodigo() {
        return xerolCodigo;
    }

    public void setXerolCodigo(String xerolCodigo) {
        this.xerolCodigo = xerolCodigo;
    }

    public String getXerolNombre() {
        return xerolNombre;
    }

    public void setXerolNombre(String xerolNombre) {
        this.xerolNombre = xerolNombre;
    }

    public Collection<RolUsuario> getRolUsuarioCollection() {
        return rolUsuarioCollection;
    }

    public void setRolUsuarioCollection(Collection<RolUsuario> rolUsuarioCollection) {
        this.rolUsuarioCollection = rolUsuarioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xerolCodigo != null ? xerolCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XerolRol)) {
            return false;
        }
        XerolRol other = (XerolRol) object;
        if ((this.xerolCodigo == null && other.xerolCodigo != null) || (this.xerolCodigo != null && !this.xerolCodigo.equals(other.xerolCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.monster.entidades.XerolRol[ xerolCodigo=" + xerolCodigo + " ]";
    }
    
}
