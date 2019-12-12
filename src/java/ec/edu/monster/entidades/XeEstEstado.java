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
@Table(name = "xe_est_estado")
@NamedQueries({
    @NamedQuery(name = "XeEstEstado.findAll", query = "SELECT x FROM XeEstEstado x")
    , @NamedQuery(name = "XeEstEstado.findByXeuestCodigo", query = "SELECT x FROM XeEstEstado x WHERE x.xeuestCodigo = :xeuestCodigo")
    , @NamedQuery(name = "XeEstEstado.findByXeuestNombre", query = "SELECT x FROM XeEstEstado x WHERE x.xeuestNombre = :xeuestNombre")})
public class XeEstEstado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "XEUEST_CODIGO")
    private String xeuestCodigo;
    @Size(max = 50)
    @Column(name = "XEUEST_NOMBRE")
    private String xeuestNombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xeuestCodigo")
    private Collection<XeusuUsuar> xeusuUsuarCollection;

    public XeEstEstado() {
    }

    public XeEstEstado(String xeuestCodigo) {
        this.xeuestCodigo = xeuestCodigo;
    }

    public String getXeuestCodigo() {
        return xeuestCodigo;
    }

    public void setXeuestCodigo(String xeuestCodigo) {
        this.xeuestCodigo = xeuestCodigo;
    }

    public String getXeuestNombre() {
        return xeuestNombre;
    }

    public void setXeuestNombre(String xeuestNombre) {
        this.xeuestNombre = xeuestNombre;
    }

    public Collection<XeusuUsuar> getXeusuUsuarCollection() {
        return xeusuUsuarCollection;
    }

    public void setXeusuUsuarCollection(Collection<XeusuUsuar> xeusuUsuarCollection) {
        this.xeusuUsuarCollection = xeusuUsuarCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (xeuestCodigo != null ? xeuestCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XeEstEstado)) {
            return false;
        }
        XeEstEstado other = (XeEstEstado) object;
        if ((this.xeuestCodigo == null && other.xeuestCodigo != null) || (this.xeuestCodigo != null && !this.xeuestCodigo.equals(other.xeuestCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.monster.entidades.XeEstEstado[ xeuestCodigo=" + xeuestCodigo + " ]";
    }
    
}
