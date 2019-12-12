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
@Table(name = "pesex_sexo")
@NamedQueries({
    @NamedQuery(name = "PesexSexo.findAll", query = "SELECT p FROM PesexSexo p")
    , @NamedQuery(name = "PesexSexo.findByPesexCodigo", query = "SELECT p FROM PesexSexo p WHERE p.pesexCodigo = :pesexCodigo")
    , @NamedQuery(name = "PesexSexo.findByPesexDescripcionSexo", query = "SELECT p FROM PesexSexo p WHERE p.pesexDescripcionSexo = :pesexDescripcionSexo")})
public class PesexSexo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "PESEX_CODIGO")
    private String pesexCodigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "PESEX_DESCRIPCION_SEXO")
    private String pesexDescripcionSexo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pesexCodigo")
    private Collection<PeempEmpleado> peempEmpleadoCollection;

    public PesexSexo() {
    }

    public PesexSexo(String pesexCodigo) {
        this.pesexCodigo = pesexCodigo;
    }

    public PesexSexo(String pesexCodigo, String pesexDescripcionSexo) {
        this.pesexCodigo = pesexCodigo;
        this.pesexDescripcionSexo = pesexDescripcionSexo;
    }

    public String getPesexCodigo() {
        return pesexCodigo;
    }

    public void setPesexCodigo(String pesexCodigo) {
        this.pesexCodigo = pesexCodigo;
    }

    public String getPesexDescripcionSexo() {
        return pesexDescripcionSexo;
    }

    public void setPesexDescripcionSexo(String pesexDescripcionSexo) {
        this.pesexDescripcionSexo = pesexDescripcionSexo;
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
        hash += (pesexCodigo != null ? pesexCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PesexSexo)) {
            return false;
        }
        PesexSexo other = (PesexSexo) object;
        if ((this.pesexCodigo == null && other.pesexCodigo != null) || (this.pesexCodigo != null && !this.pesexCodigo.equals(other.pesexCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.monster.entidades.PesexSexo[ pesexCodigo=" + pesexCodigo + " ]";
    }
    
}
