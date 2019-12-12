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
@Table(name = "pepai_pais")
@NamedQueries({
    @NamedQuery(name = "PepaiPais.findAll", query = "SELECT p FROM PepaiPais p")
    , @NamedQuery(name = "PepaiPais.findByPepaisCodigo", query = "SELECT p FROM PepaiPais p WHERE p.pepaisCodigo = :pepaisCodigo")
    , @NamedQuery(name = "PepaiPais.findByPepaisNombre", query = "SELECT p FROM PepaiPais p WHERE p.pepaisNombre = :pepaisNombre")})
public class PepaiPais implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "PEPAIS_CODIGO")
    private String pepaisCodigo;
    @Size(max = 40)
    @Column(name = "PEPAIS_NOMBRE")
    private String pepaisNombre;
    @OneToMany(mappedBy = "pepaisCodigo")
    private Collection<PeproProvin> peproProvinCollection;

    public PepaiPais() {
    }

    public PepaiPais(String pepaisCodigo) {
        this.pepaisCodigo = pepaisCodigo;
    }

    public String getPepaisCodigo() {
        return pepaisCodigo;
    }

    public void setPepaisCodigo(String pepaisCodigo) {
        this.pepaisCodigo = pepaisCodigo;
    }

    public String getPepaisNombre() {
        return pepaisNombre;
    }

    public void setPepaisNombre(String pepaisNombre) {
        this.pepaisNombre = pepaisNombre;
    }

    public Collection<PeproProvin> getPeproProvinCollection() {
        return peproProvinCollection;
    }

    public void setPeproProvinCollection(Collection<PeproProvin> peproProvinCollection) {
        this.peproProvinCollection = peproProvinCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pepaisCodigo != null ? pepaisCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PepaiPais)) {
            return false;
        }
        PepaiPais other = (PepaiPais) object;
        if ((this.pepaisCodigo == null && other.pepaisCodigo != null) || (this.pepaisCodigo != null && !this.pepaisCodigo.equals(other.pepaisCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.monster.entidades.PepaiPais[ pepaisCodigo=" + pepaisCodigo + " ]";
    }
    
}
