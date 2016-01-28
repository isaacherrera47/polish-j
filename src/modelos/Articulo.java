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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "articulo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Articulo.findAll", query = "SELECT a FROM Articulo a"),
    @NamedQuery(name = "Articulo.findByCodigo", query = "SELECT a FROM Articulo a WHERE a.codigo = :codigo"),
    @NamedQuery(name = "Articulo.findByNombre", query = "SELECT a FROM Articulo a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "Articulo.findByDetalles", query = "SELECT a FROM Articulo a WHERE a.detalles = :detalles"),
    @NamedQuery(name = "Articulo.findByImagen", query = "SELECT a FROM Articulo a WHERE a.imagen = :imagen"),
    @NamedQuery(name = "Articulo.findByUrl", query = "SELECT a FROM Articulo a WHERE a.url = :url"),
    @NamedQuery(name = "Articulo.findByPrecioReal", query = "SELECT a FROM Articulo a WHERE a.precioReal = :precioReal"),
    @NamedQuery(name = "Articulo.findByPrecioDescuento", query = "SELECT a FROM Articulo a WHERE a.precioDescuento = :precioDescuento"),
    @NamedQuery(name = "Articulo.findByPrecioCupon", query = "SELECT a FROM Articulo a WHERE a.precioCupon = :precioCupon"),
    @NamedQuery(name = "Articulo.findByPrecioVenta", query = "SELECT a FROM Articulo a WHERE a.precioVenta = :precioVenta")})
public class Articulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "detalles")
    private String detalles;
    @Basic(optional = false)
    @Column(name = "imagen")
    private String imagen;
    @Column(name = "url")
    private String url;
    @Basic(optional = false)
    @Column(name = "precio_real")
    private float precioReal;
    @Basic(optional = false)
    @Column(name = "precio_descuento")
    private float precioDescuento;
    @Basic(optional = false)
    @Column(name = "precio_cupon")
    private float precioCupon;
    @Basic(optional = false)
    @Column(name = "precio_venta")
    private float precioVenta;
    @JoinColumn(name = "id_categoria", referencedColumnName = "idcategoria")
    @ManyToOne(optional = false)
    private Categoria idCategoria;
    @JoinColumn(name = "id_tienda", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Tienda idTienda;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idArticulo")
    private List<ArticuloTalla> articuloTallaList;

    public Articulo() {
    }

    public Articulo(String codigo) {
        this.codigo = codigo;
    }

    public Articulo(String codigo, String nombre, String detalles, String imagen, float precioReal, float precioDescuento, float precioCupon, float precioVenta) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.detalles = detalles;
        this.imagen = imagen;
        this.precioReal = precioReal;
        this.precioDescuento = precioDescuento;
        this.precioCupon = precioCupon;
        this.precioVenta = precioVenta;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getPrecioReal() {
        return precioReal;
    }

    public void setPrecioReal(float precioReal) {
        this.precioReal = precioReal;
    }

    public float getPrecioDescuento() {
        return precioDescuento;
    }

    public void setPrecioDescuento(float precioDescuento) {
        this.precioDescuento = precioDescuento;
    }

    public float getPrecioCupon() {
        return precioCupon;
    }

    public void setPrecioCupon(float precioCupon) {
        this.precioCupon = precioCupon;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Categoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Categoria idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Tienda getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(Tienda idTienda) {
        this.idTienda = idTienda;
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
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Articulo)) {
            return false;
        }
        Articulo other = (Articulo) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelos.Articulo[ codigo=" + codigo + " ]";
    }
    
}
