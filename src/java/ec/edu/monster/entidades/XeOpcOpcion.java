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
@Table(name = "xe_opc_opcion")
@NamedQueries({
    @NamedQuery(name = "XeOpcOpcion.findAll", query = "SELECT x FROM XeOpcOpcion x")
    , @NamedQuery(name = "XeOpcOpcion.findByXeopcCodigo", query = "SELECT x FROM XeOpcOpcion x WHERE x.xeopcCodigo = :xeopcCodigo")
    , @NamedQuery(name = "XeOpcOpcion.findByXeopcNombre", query = "SELECT x FROM XeOpcOpcion x WHERE x.xeopcNombre = :xeopcNombre")})
public class XeOpcOpcion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "XEOPC_CODIGO")
    private String xeopcCodigo;
    @Size(max = 50)
    @Column(name = "XEOPC_NOMBRE")
    private String xeopcNombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "xeopcCodigo")
    private Collection<PerfilOpcion> perfilOpcionCollection;

    public XeOpcOpcion() {
    }

    public XeOpcOpcion(String xeopcCodigo) {
        this.xeopcCodigo = xeopcCodigo;
    }

    public String getXeopcCodigo() {
        return xeopcCodigo;
    }

    public void setXeopcCodigo(String xeopcCodigo) {
        this.xeopcCodigo = xeopcCodigo;
    }

    public String getXeopcNombre() {
        return xeopcNombre;
    }

    public void setXeopcNombre(String xeopcNombre) {
        this.xeopcNombre = xeopcNombre;
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
        hash += (xeopcCodigo != null ? xeopcCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XeOpcOpcion)) {
            return false;
        }
        XeOpcOpcion other = (XeOpcOpcion) object;
        if ((this.xeopcCodigo == null && other.xeopcCodigo != null) || (this.xeopcCodigo != null && !this.xeopcCodigo.equals(other.xeopcCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.monster.entidades.XeOpcOpcion[ xeopcCodigo=" + xeopcCodigo + " ]";
    }
    
}
