package com.tp.jpa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, of = {"producto", "cantidad"})
@SuperBuilder
@NoArgsConstructor
public class DetallePedido extends Base {
    private int cantidad;
    private Double subtotal;

    @ManyToOne
    private Producto producto;

    public DetallePedido(int cantidad, Producto producto) {
        super();
        if(producto == null){
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }

        if(cantidad <= 0){
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        this.cantidad = cantidad;
        this.producto = producto;
        this.subtotal = cantidad * producto.getPrecio();
    }

    public void setCantidad(int cantidad){
        this.cantidad = cantidad;
        if(this.producto != null){
            this.subtotal = this.cantidad * this.producto.getPrecio();
        }
    }



}
