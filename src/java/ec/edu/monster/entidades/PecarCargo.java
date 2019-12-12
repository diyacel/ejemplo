/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
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
@Table(name = "pecar_cargo")
@NamedQueries({
    @NamedQuery(name = "PecarCargo.findAll", query = "SELECT p FROM PecarCargo p")
    , @NamedQuery(name = "PecarCargo.findByPecarCodigo", query = "SELECT p FROM PecarCargo p WHERE p.pecarCodigo = :pecarCodigo")
    , @NamedQuery(name = "PecarCargo.findByPecarDescripcion", query = "SELECT p FROM PecarCargo p WHERE p.pecarDescripcion = :pecarDescripcion")})
public class PecarCargo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "PECAR_CODIGO")
    private String pecarCodigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PECAR_DESCRIPCION")
    private String pecarDescripcion;
    @OneToMany(mappedBy = "pecarCodigo")
    private Collection<PeempEmpleado> peempEmpleadoCollection;

    public PecarCargo() {
    }

    public PecarCargo(String pecarCodigo) {
        this.pecarCodigo = pecarCodigo;
    }

    public PecarCargo(String pecarCodigo, String pecarDescripcion) {
        this.pecarCodigo = pecarCodigo;
        this.pecarDescripcion = pecarDescripcion;
    }

    public String getPecarCodigo() {
        return pecarCodigo;
    }

    public void setPecarCodigo(String pecarCodigo) {
        this.pecarCodigo = pecarCodigo;
    }

    public String getPecarDescripcion() {
        return pecarDescripcion;
    }

    public void setPecarDescripcion(String pecarDescripcion) {
        this.pecarDescripcion = pecarDescripcion;
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
        hash += (pecarCodigo != null ? pecarCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PecarCargo)) {
            return false;
        }
        PecarCargo other = (PecarCargo) object;
        if ((this.pecarCodigo == null && other.pecarCodigo != null) || (this.pecarCodigo != null && !this.pecarCodigo.equals(other.pecarCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.monster.entidades.PecarCargo[ pecarCodigo=" + pecarCodigo + " ]";
    }
    
}
