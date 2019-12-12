/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Diego
 */
@Entity
@Table(name = "xeusu_usuar")
@NamedQueries({
    @NamedQuery(name = "XeusuUsuar.findAll", query = "SELECT x FROM XeusuUsuar x")
    , @NamedQuery(name = "XeusuUsuar.findByPeempCodig", query = "SELECT x FROM XeusuUsuar x WHERE x.xeusuUsuarPK.peempCodig = :peempCodig")
    , @NamedQuery(name = "XeusuUsuar.findByXeusuCodigo", query = "SELECT x FROM XeusuUsuar x WHERE x.xeusuUsuarPK.xeusuCodigo = :xeusuCodigo")
    , @NamedQuery(name = "XeusuUsuar.findByXeusuNick", query = "SELECT x FROM XeusuUsuar x WHERE x.xeusuNick = :xeusuNick")
    , @NamedQuery(name = "XeusuUsuar.findByXeusuClave", query = "SELECT x FROM XeusuUsuar x WHERE x.xeusuClave = :xeusuClave")
    , @NamedQuery(name = "XeusuUsuar.findByXeusuFechaCreacion", query = "SELECT x FROM XeusuUsuar x WHERE x.xeusuFechaCreacion = :xeusuFechaCreacion")})
public class XeusuUsuar implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected XeusuUsuarPK xeusuUsuarPK;
    @Size(max = 100)
    @Column(name = "XEUSU_NICK")
    private String xeusuNick;
    @Size(max = 100)
    @Column(name = "XEUSU_CLAVE")
    private String xeusuClave;
    @Column(name = "XEUSU_FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date xeusuFechaCreacion;
    @JoinColumn(name = "PEEMP_CODIG", referencedColumnName = "PEEMP_CODIG", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PeempEmpleado peempEmpleado;
    @JoinColumn(name = "XEUEST_CODIGO", referencedColumnName = "XEUEST_CODIGO")
    @ManyToOne(optional = false)
    private XeEstEstado xeuestCodigo;
    @JoinColumn(name = "XEPER_CODIGO", referencedColumnName = "XEPER_CODIGO")
    @ManyToOne(optional = false)
    private XeperPerfil xeperCodigo;

    public XeusuUsuar() {
    }

    public XeusuUsuar(XeusuUsuarPK xeusuUsuarPK) {
        this.xeusuUsuarPK = xeusuUsuarPK;
    }

    public XeusuUsuar(int peempCodig, String xeusuCodigo) {
        this.xeusuUsuarPK = new XeusuUsuarPK(peempCodig, xeusuCodigo);
    }

    public XeusuUsuarPK getXeusuUsuarPK() {
        return xeusuUsuarPK;
    }

    public void setXeusuUsuarPK(XeusuUsuarPK xeusuUsuarPK) {
        this.xeusuUsuarPK = xeusuUsuarPK;
    }

    public String getXeusuNick() {
        return xeusuNick;
    }

    public void setXeusuNick(String xeusuNick) {
        this.xeusuNick = xeusuNick;
    }

    public String getXeusuClave() {
        return xeusuClave;
    }

    public void setXeusuClave(String xeusuClave) {
        this.xeusuClave = xeusuClave;
    }

    public Date getXeusuFechaCreacion() {
        return xeusuFechaCreacion;
    }

    public void setXeusuFechaCreacion(Date xeusuFechaCreacion) {
        this.xeusuFechaCreacion = xeusuFechaCreacion;
    }

    public PeempEmpleado getPeempEmpleado() {
        return peempEmpleado;
    }

    public void setPeempEmpleado(PeempEmpleado peempEmpleado) {
        this.peempEmpleado = peempEmpleado;
    }

    public XeEstEstado getXeuestCodigo() {
        return xeuestCodigo;
    }

    public void setXeuestCodigo(XeEstEstado xeuestCodigo) {
        this.xeuestCodigo = xeuestCodigo;
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
        hash += (xeusuUsuarPK != null ? xeusuUsuarPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XeusuUsuar)) {
            return false;
        }
        XeusuUsuar other = (XeusuUsuar) object;
        if ((this.xeusuUsuarPK == null && other.xeusuUsuarPK != null) || (this.xeusuUsuarPK != null && !this.xeusuUsuarPK.equals(other.xeusuUsuarPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return xeusuNick;
    }
    
}
