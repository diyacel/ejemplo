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
@Table(name = "pepro_provin")
@NamedQueries({
    @NamedQuery(name = "PeproProvin.findAll", query = "SELECT p FROM PeproProvin p")
    , @NamedQuery(name = "PeproProvin.findByPeproCodigo", query = "SELECT p FROM PeproProvin p WHERE p.peproCodigo = :peproCodigo")
    , @NamedQuery(name = "PeproProvin.findByPeproNombre", query = "SELECT p FROM PeproProvin p WHERE p.peproNombre = :peproNombre")})
public class PeproProvin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "PEPRO_CODIGO")
    private String peproCodigo;
    @Size(max = 50)
    @Column(name = "PEPRO_NOMBRE")
    private String peproNombre;
    @JoinColumn(name = "PEPAIS_CODIGO", referencedColumnName = "PEPAIS_CODIGO")
    @ManyToOne
    private PepaiPais pepaisCodigo;
    @OneToMany(mappedBy = "peproCodigo")
    private Collection<Ciudad> ciudadCollection;

    public PeproProvin() {
    }

    public PeproProvin(String peproCodigo) {
        this.peproCodigo = peproCodigo;
    }

    public String getPeproCodigo() {
        return peproCodigo;
    }

    public void setPeproCodigo(String peproCodigo) {
        this.peproCodigo = peproCodigo;
    }

    public String getPeproNombre() {
        return peproNombre;
    }

    public void setPeproNombre(String peproNombre) {
        this.peproNombre = peproNombre;
    }

    public PepaiPais getPepaisCodigo() {
        return pepaisCodigo;
    }

    public void setPepaisCodigo(PepaiPais pepaisCodigo) {
        this.pepaisCodigo = pepaisCodigo;
    }

    public Collection<Ciudad> getCiudadCollection() {
        return ciudadCollection;
    }

    public void setCiudadCollection(Collection<Ciudad> ciudadCollection) {
        this.ciudadCollection = ciudadCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (peproCodigo != null ? peproCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PeproProvin)) {
            return false;
        }
        PeproProvin other = (PeproProvin) object;
        if ((this.peproCodigo == null && other.peproCodigo != null) || (this.peproCodigo != null && !this.peproCodigo.equals(other.peproCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.monster.entidades.PeproProvin[ peproCodigo=" + peproCodigo + " ]";
    }
    
}
