/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author isaac
 */
@Entity
@Table(name = "articulo_talla")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ArticuloTalla.findAll", query = "SELECT a FROM ArticuloTalla a"),
    @NamedQuery(name = "ArticuloTalla.findByIdarticuloTalla", query = "SELECT a FROM ArticuloTalla a WHERE a.idarticuloTalla = :idarticuloTalla")})
public class ArticuloTalla implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idarticulo_talla")
    private Integer idarticuloTalla;
    @JoinColumn(name = "id_articulo", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Articulo idArticulo;
    @JoinColumn(name = "id_pedido", referencedColumnName = "idpedido")
    @ManyToOne(optional = false)
    private Pedido idPedido;
    @JoinColumn(name = "id_talla", referencedColumnName = "idtalla")
    @ManyToOne(optional = false)
    private Talla idTalla;

    public ArticuloTalla() {
    }

    public ArticuloTalla(Integer idarticuloTalla) {
        this.idarticuloTalla = idarticuloTalla;
    }

    public Integer getIdarticuloTalla() {
        return idarticuloTalla;
    }

    public void setIdarticuloTalla(Integer idarticuloTalla) {
        this.idarticuloTalla = idarticuloTalla;
    }

    public Articulo getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Articulo idArticulo) {
        this.idArticulo = idArticulo;
    }

    public Pedido getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Pedido idPedido) {
        this.idPedido = idPedido;
    }

    public Talla getIdTalla() {
        return idTalla;
    }

    public void setIdTalla(Talla idTalla) {
        this.idTalla = idTalla;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idarticuloTalla != null ? idarticuloTalla.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ArticuloTalla)) {
            return false;
        }
        ArticuloTalla other = (ArticuloTalla) object;
        if ((this.idarticuloTalla == null && other.idarticuloTalla != null) || (this.idarticuloTalla != null && !this.idarticuloTalla.equals(other.idarticuloTalla))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelos.ArticuloTalla[ idarticuloTalla=" + idarticuloTalla + " ]";
    }
    
}
