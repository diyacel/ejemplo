/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Diego
 */
@Entity
@Table(name = "peemp_empleado")
@NamedQueries({
    @NamedQuery(name = "PeempEmpleado.findAll", query = "SELECT p FROM PeempEmpleado p")
    , @NamedQuery(name = "PeempEmpleado.findByPeempCodig", query = "SELECT p FROM PeempEmpleado p WHERE p.peempCodig = :peempCodig")
    , @NamedQuery(name = "PeempEmpleado.findByPeempCedula", query = "SELECT p FROM PeempEmpleado p WHERE p.peempCedula = :peempCedula")
    , @NamedQuery(name = "PeempEmpleado.findByPeempApellido", query = "SELECT p FROM PeempEmpleado p WHERE p.peempApellido = :peempApellido")
    , @NamedQuery(name = "PeempEmpleado.findByPeempNombre", query = "SELECT p FROM PeempEmpleado p WHERE p.peempNombre = :peempNombre")
    , @NamedQuery(name = "PeempEmpleado.findByPeempRubro", query = "SELECT p FROM PeempEmpleado p WHERE p.peempRubro = :peempRubro")
    , @NamedQuery(name = "PeempEmpleado.findByPeempArea", query = "SELECT p FROM PeempEmpleado p WHERE p.peempArea = :peempArea")
    , @NamedQuery(name = "PeempEmpleado.findByPeempFechaNac", query = "SELECT p FROM PeempEmpleado p WHERE p.peempFechaNac = :peempFechaNac")
    , @NamedQuery(name = "PeempEmpleado.findByPeempFechaContrato", query = "SELECT p FROM PeempEmpleado p WHERE p.peempFechaContrato = :peempFechaContrato")
    , @NamedQuery(name = "PeempEmpleado.findByPeempTipoSangre", query = "SELECT p FROM PeempEmpleado p WHERE p.peempTipoSangre = :peempTipoSangre")})
public class PeempEmpleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "PEEMP_CODIG")
    private Integer peempCodig;
    @Size(max = 10)
    @Column(name = "PEEMP_CEDULA")
    private String peempCedula;
    @Size(max = 30)
    @Column(name = "PEEMP_APELLIDO")
    private String peempApellido;
    @Size(max = 30)
    @Column(name = "PEEMP_NOMBRE")
    private String peempNombre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PEEMP_RUBRO")
    private Float peempRubro;
    @Size(max = 10)
    @Column(name = "PEEMP_AREA")
    private String peempArea;
    @Column(name = "PEEMP_FECHA_NAC")
    @Temporal(TemporalType.DATE)
    private Date peempFechaNac;
    @Column(name = "PEEMP_FECHA_CONTRATO")
    @Temporal(TemporalType.DATE)
    private Date peempFechaContrato;
    @Size(max = 5)
    @Column(name = "PEEMP_TIPO_SANGRE")
    private String peempTipoSangre;
    @JoinColumn(name = "CIU_CODIGO", referencedColumnName = "CIU_CODIGO")
    @ManyToOne
    private Ciudad ciuCodigo;
    @JoinColumn(name = "PEESC_CODIGO", referencedColumnName = "PEESC_CODIGO")
    @ManyToOne(optional = false)
    private PeescEstciv peescCodigo;
    @JoinColumn(name = "PESEX_CODIGO", referencedColumnName = "PESEX_CODIGO")
    @ManyToOne(optional = false)
    private PesexSexo pesexCodigo;
    @JoinColumn(name = "PECAR_CODIGO", referencedColumnName = "PECAR_CODIGO")
    @ManyToOne
    private PecarCargo pecarCodigo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "peempEmpleado")
    private Collection<XeusuUsuar> xeusuUsuarCollection;

    public PeempEmpleado() {
    }

    public PeempEmpleado(Integer peempCodig) {
        this.peempCodig = peempCodig;
    }

    public Integer getPeempCodig() {
        return peempCodig;
    }

    public void setPeempCodig(Integer peempCodig) {
        this.peempCodig = peempCodig;
    }

    public String getPeempCedula() {
        return peempCedula;
    }

    public void setPeempCedula(String peempCedula) {
        this.peempCedula = peempCedula;
    }

    public String getPeempApellido() {
        return peempApellido;
    }

    public void setPeempApellido(String peempApellido) {
        this.peempApellido = peempApellido;
    }

    public String getPeempNombre() {
        return peempNombre;
    }

    public void setPeempNombre(String peempNombre) {
        this.peempNombre = peempNombre;
    }

    public Float getPeempRubro() {
        return peempRubro;
    }

    public void setPeempRubro(Float peempRubro) {
        this.peempRubro = peempRubro;
    }

    public String getPeempArea() {
        return peempArea;
    }

    public void setPeempArea(String peempArea) {
        this.peempArea = peempArea;
    }

    public Date getPeempFechaNac() {
        return peempFechaNac;
    }

    public void setPeempFechaNac(Date peempFechaNac) {
        this.peempFechaNac = peempFechaNac;
    }

    public Date getPeempFechaContrato() {
        return peempFechaContrato;
    }

    public void setPeempFechaContrato(Date peempFechaContrato) {
        this.peempFechaContrato = peempFechaContrato;
    }

    public String getPeempTipoSangre() {
        return peempTipoSangre;
    }

    public void setPeempTipoSangre(String peempTipoSangre) {
        this.peempTipoSangre = peempTipoSangre;
    }

    public Ciudad getCiuCodigo() {
        return ciuCodigo;
    }

    public void setCiuCodigo(Ciudad ciuCodigo) {
        this.ciuCodigo = ciuCodigo;
    }

    public PeescEstciv getPeescCodigo() {
        return peescCodigo;
    }

    public void setPeescCodigo(PeescEstciv peescCodigo) {
        this.peescCodigo = peescCodigo;
    }

    public PesexSexo getPesexCodigo() {
        return pesexCodigo;
    }

    public void setPesexCodigo(PesexSexo pesexCodigo) {
        this.pesexCodigo = pesexCodigo;
    }

    public PecarCargo getPecarCodigo() {
        return pecarCodigo;
    }

    public void setPecarCodigo(PecarCargo pecarCodigo) {
        this.pecarCodigo = pecarCodigo;
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
        hash += (peempCodig != null ? peempCodig.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PeempEmpleado)) {
            return false;
        }
        PeempEmpleado other = (PeempEmpleado) object;
        if ((this.peempCodig == null && other.peempCodig != null) || (this.peempCodig != null && !this.peempCodig.equals(other.peempCodig))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.monster.entidades.PeempEmpleado[ peempCodig=" + peempCodig + " ]";
    }
    
}
