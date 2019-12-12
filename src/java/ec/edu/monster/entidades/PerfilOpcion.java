/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Diego
 */
@Entity
@Table(name = "perfil_opcion")
@NamedQueries({
    @NamedQuery(name = "PerfilOpcion.findAll", query = "SELECT p FROM PerfilOpcion p")
    , @NamedQuery(name = "PerfilOpcion.findByPopFechaAsignacion", query = "SELECT p FROM PerfilOpcion p WHERE p.popFechaAsignacion = :popFechaAsignacion")})
public class PerfilOpcion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "POP_FECHA_ASIGNACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date popFechaAsignacion;
    @JoinColumn(name = "XEOPC_CODIGO", referencedColumnName = "XEOPC_CODIGO")
    @ManyToOne(optional = false)
    private XeOpcOpcion xeopcCodigo;
    @JoinColumn(name = "XEPER_CODIGO", referencedColumnName = "XEPER_CODIGO")
    @ManyToOne(optional = false)
    private XeperPerfil xeperCodigo;

    public PerfilOpcion() {
    }

    public PerfilOpcion(Date popFechaAsignacion) {
        this.popFechaAsignacion = popFechaAsignacion;
    }

    public Date getPopFechaAsignacion() {
        return popFechaAsignacion;
    }

    public void setPopFechaAsignacion(Date popFechaAsignacion) {
        this.popFechaAsignacion = popFechaAsignacion;
    }

    public XeOpcOpcion getXeopcCodigo() {
        return xeopcCodigo;
    }

    public void setXeopcCodigo(XeOpcOpcion xeopcCodigo) {
        this.xeopcCodigo = xeopcCodigo;
    }

    public XeperPerfil getXeperCodigo() {
        return xeperCodigo;
    }

    public void setXeperCodigo(XeperPerfil xeperCodigo) {
        this.xeperCodigo = xeperCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (popFechaAsignacion != null ? popFechaAsignacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PerfilOpcion)) {
            return false;
        }
        PerfilOpcion other = (PerfilOpcion) object;
        if ((this.popFechaAsignacion == null && other.popFechaAsignacion != null) || (this.popFechaAsignacion != null && !this.popFechaAsignacion.equals(other.popFechaAsignacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.monster.entidades.PerfilOpcion[ popFechaAsignacion=" + popFechaAsignacion + " ]";
    }
    
}
