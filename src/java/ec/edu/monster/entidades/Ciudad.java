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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "ciudad")
@NamedQueries({
    @NamedQuery(name = "Ciudad.findAll", query = "SELECT c FROM Ciudad c")
    , @NamedQuery(name = "Ciudad.findByCiuCodigo", query = "SELECT c FROM Ciudad c WHERE c.ciuCodigo = :ciuCodigo")
    , @NamedQuery(name = "Ciudad.findByCiuNombre", query = "SELECT c FROM Ciudad c WHERE c.ciuNombre = :ciuNombre")})
public class Ciudad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "CIU_CODIGO")
    private String ciuCodigo;
    @Size(max = 50)
    @Column(name = "CIU_NOMBRE")
    private String ciuNombre;
    @OneToMany(mappedBy = "ciuCodigo")
    private Collection<PeempEmpleado> peempEmpleadoCollection;
    @JoinColumn(name = "PEPRO_CODIGO", referencedColumnName = "PEPRO_CODIGO")
    @ManyToOne
    private PeproProvin peproCodigo;

    public Ciudad() {
    }

    public Ciudad(String ciuCodigo) {
        this.ciuCodigo = ciuCodigo;
    }

    public String getCiuCodigo() {
        return ciuCodigo;
    }

    public void setCiuCodigo(String ciuCodigo) {
        this.ciuCodigo = ciuCodigo;
    }

    public String getCiuNombre() {
        return ciuNombre;
    }

    public void setCiuNombre(String ciuNombre) {
        this.ciuNombre = ciuNombre;
    }

    public Collection<PeempEmpleado> getPeempEmpleadoCollection() {
        return peempEmpleadoCollection;
    }

    public void setPeempEmpleadoCollection(Collection<PeempEmpleado> peempEmpleadoCollection) {
        this.peempEmpleadoCollection = peempEmpleadoCollection;
    }

    public PeproProvin getPeproCodigo() {
        return peproCodigo;
    }

    public void setPeproCodigo(PeproProvin peproCodigo) {
        this.peproCodigo = peproCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ciuCodigo != null ? ciuCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ciudad)) {
            return false;
        }
        Ciudad other = (Ciudad) object;
        if ((this.ciuCodigo == null && other.ciuCodigo != null) || (this.ciuCodigo != null && !this.ciuCodigo.equals(other.ciuCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.monster.entidades.Ciudad[ ciuCodigo=" + ciuCodigo + " ]";
    }
    
}
