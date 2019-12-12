/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Diego
 */
@Embeddable
public class XeusuUsuarPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "PEEMP_CODIG")
    private int peempCodig;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "XEUSU_CODIGO")
    private String xeusuCodigo;

    public XeusuUsuarPK() {
    }

    public XeusuUsuarPK(int peempCodig, String xeusuCodigo) {
        this.peempCodig = peempCodig;
        this.xeusuCodigo = xeusuCodigo;
    }

    public int getPeempCodig() {
        return peempCodig;
    }

    public void setPeempCodig(int peempCodig) {
        this.peempCodig = peempCodig;
    }

    public String getXeusuCodigo() {
        return xeusuCodigo;
    }

    public void setXeusuCodigo(String xeusuCodigo) {
        this.xeusuCodigo = xeusuCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) peempCodig;
        hash += (xeusuCodigo != null ? xeusuCodigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof XeusuUsuarPK)) {
            return false;
        }
        XeusuUsuarPK other = (XeusuUsuarPK) object;
        if (this.peempCodig != other.peempCodig) {
            return false;
        }
        if ((this.xeusuCodigo == null && other.xeusuCodigo != null) || (this.xeusuCodigo != null && !this.xeusuCodigo.equals(other.xeusuCodigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.edu.monster.entidades.XeusuUsuarPK[ peempCodig=" + peempCodig + ", xeusuCodigo=" + xeusuCodigo + " ]";
    }
    
}
