/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author isaac
 */
@Entity
@Table(name = "talla")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Talla.findAll", query = "SELECT t FROM Talla t"),
    @NamedQuery(name = "Talla.findByIdtalla", query = "SELECT t FROM Talla t WHERE t.idtalla = :idtalla"),
    @NamedQuery(name = "Talla.findByNombre", query = "SELECT t FROM Talla t WHERE t.nombre = :nombre")})
public class Talla implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idtalla")
    private String idtalla;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTalla")
    private List<ArticuloTalla> articuloTallaList;

    public Talla() {
    }

    public Talla(String idtalla) {
        this.idtalla = idtalla;
    }

    public Talla(String idtalla, String nombre) {
        this.idtalla = idtalla;
        this.nombre = nombre;
    }

    public String getIdtalla() {
        return idtalla;
    }

    public void setIdtalla(String idtalla) {
        this.idtalla = idtalla;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<ArticuloTalla> getArticuloTallaList() {
        return articuloTallaList;
    }

    public void setArticuloTallaList(List<ArticuloTalla> articuloTallaList) {
        this.articuloTallaList = articuloTallaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtalla != null ? idtalla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Talla)) {
            return false;
        }
        Talla other = (Talla) object;
        if ((this.idtalla == null && other.idtalla != null) || (this.idtalla != null && !this.idtalla.equals(other.idtalla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelos.Talla[ idtalla=" + idtalla + " ]";
    }
    
}
