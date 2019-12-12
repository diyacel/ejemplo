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
@Table(name = "rol_usuario")
@NamedQueries({
    @NamedQuery(name = "RolUsuario.findAll", query = "SELECT r FROM RolUsuario r")
    , @NamedQuery(name = "RolUsuario.findByRusFechaAsignacion", query = "SELECT r FROM RolUsuario r WHERE r.rusFechaAsignacion = :rusFechaAsignacion")})
public class RolUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "RUS_FECHA_ASIGNACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rusFechaAsignacion;
    @JoinColumn(name = "XEROL_CODIGO", referencedColumnName = "XEROL_CODIGO")
    @ManyToOne(optional = false)
    private XerolRol xerolCodigo;
    @JoinColumn(name = "XEPER_CODIGO", referencedColumnName = "XEPER_CODIGO")
    @ManyToOne(optional = false)
    private XeperPerfil xeperCodigo;

    public RolUsuario() {
    }

    public RolUsuario(Date rusFechaAsignacion) {
        this.rusFechaAsignacion = rusFechaAsignacion;
    }

    public Date getRusFechaAsignacion() {
        return rusFechaAsignacion;
    }

    public void setRusFechaAsignacion(Date rusFechaAsignacion) {
        this.rusFechaAsignacion = rusFechaAsignacion;
    }

    public XerolRol getXerolCodigo() {
        return xerolCodigo;
    }

    public void setXerolCodigo(XerolRol xerolCodigo) {
        this.xerolCodigo = xerolCodigo;
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
        hash += (rusFechaAsignacion != null ? rusFechaAsignacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RolUsuario)) {
            return false;
        }
        RolUsuario other = (RolUsuario) object;
        if ((this.rusFechaAsignacion == null && other.rusFechaAsignacion != null) || (this.rusFechaAsignacion != null && !this.rusFechaAsignacion.equals(other.rusFechaAsignacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.monster.entidades.RolUsuario[ rusFechaAsignacion=" + rusFechaAsignacion + " ]";
    }
    
}
