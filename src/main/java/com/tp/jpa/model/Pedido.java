package com.tp.jpa.model;

import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, of = {"fecha", "formaPago"})
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido extends Base implements Calculable {

    private LocalDate fecha;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.PENDIENTE;

    private double total;

    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "pedido_id")
    @Builder.Default
    private Set<DetallePedido> detallesPedidos = new HashSet<>();


    public Pedido(LocalDate fecha, FormaPago formaPago) {
        super();
        this.fecha = fecha;
        this.estado = Estado.PENDIENTE;
        this.total = 0.0;
        this.formaPago = formaPago;
        this.detallesPedidos = new HashSet<>();
    }


    public void addDetallePedido(int cantidad, Producto producto){
        if(producto == null || cantidad <= 0){
            return;
        }

        DetallePedido detalleExiste = findDetallePedidoByProducto(producto);
        if(detalleExiste != null){
            detalleExiste.setCantidad(detalleExiste.getCantidad() + cantidad);
        }else{
            this.detallesPedidos.add(new DetallePedido(cantidad, producto));
        }
        this.calcularTotal();
    }

    public void deleteDetallePedidoByProducto(Producto producto){
        DetallePedido detalleEncontrado = findDetallePedidoByProducto(producto);
        if(detalleEncontrado != null){
            this.detallesPedidos.remove(detalleEncontrado);
            this.calcularTotal();
        }
    }

    public DetallePedido findDetallePedidoByProducto(Producto producto){
        if(producto == null || this.detallesPedidos == null){
            return null;
        }

        for (DetallePedido detalle : detallesPedidos){
            if(producto.equals(detalle.getProducto())){
                return detalle;
            }
        }
        return null;
    }

    @Override
    public void calcularTotal() {
        double suma = 0.0;
        if(this.detallesPedidos != null){
            for(DetallePedido detalle: detallesPedidos){
                if(detalle != null){
                    suma += detalle.getSubtotal();
                }
            }
        }
        this.total = suma;
    }
}
