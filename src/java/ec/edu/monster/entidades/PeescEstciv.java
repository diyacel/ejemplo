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
@Table(name = "peesc_estciv")
@NamedQueries({
    @NamedQuery(name = "PeescEstciv.findAll", query = "SELECT p FROM PeescEstciv p")
    , @NamedQuery(name = "PeescEstciv.findByPeescCodigo", query = "SELECT p FROM PeescEstciv p WHERE p.peescCodigo = :peescCodigo")
    , @NamedQuery(name = "PeescEstciv.findByPeescDescripcion", query = "SELECT p FROM PeescEstciv p WHERE p.peescDescripcion = :peescDescripcion")})
public class PeescEstciv implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "PEESC_CODIGO")
    private String peescCodigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PEESC_DESCRIPCION")
    private String peescDescripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "peescCodigo")
    private Collection<PeempEmpleado> peempEmpleadoCollection;

    public PeescEstciv() {
    }

    public PeescEstciv(String peescCodigo) {
        this.peescCodigo = peescCodigo;
    }

    public PeescEstciv(String peescCodigo, String peescDescripcion) {
        this.peescCodigo = peescCodigo;
        this.peescDescripcion = peescDescripcion;
    }

    public String getPeescCodigo() {
        return peescCodigo;
    }

    public void setPeescCodigo(String peescCodigo) {
        this.peescCodigo = peescCodigo;
    }

    public String getPeescDescripcion() {
        return peescDescripcion;
    }

    public void setPeescDescripcion(String peescDescripcion) {
        this.peescDescripcion = peescDescripcion;
    }

    public Collection<PeempEmpleado> getPeempEmpleadoCollection() {
        return peempEmpleadoCollection;
    }

    public void setPeempEmpleadoCollection(Collection<PeempEmpleado> peempEmpleadoCollection) {
        this.peempEmpleadoCollection = peempEmpleadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (peescCodigo != null ? peescCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PeescEstciv)) {
            return false;
        }
        PeescEstciv other = (PeescEstciv) object;
        if ((this.peescCodigo == null && other.peescCodigo != null) || (this.peescCodigo != null && !this.peescCodigo.equals(other.peescCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.monster.entidades.PeescEstciv[ peescCodigo=" + peescCodigo + " ]";
    }
    
}
