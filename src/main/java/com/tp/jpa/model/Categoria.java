package com.tp.jpa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "nombre")
@ToString(callSuper = true)
@SuperBuilder
public class Categoria extends Base{
    private String nombre;
    private String descripcion;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    @ToString.Exclude
    @Builder.Default
    private Set<Producto> productos = new HashSet<>();

    @ToString.Include(name = "cantidadProductos")
    public int getCantidadProductos(){
        return this.productos != null ? productos.size() : 0;
    }

    public void addProducto (Producto producto){
        this.productos.add(producto);
    }
}
