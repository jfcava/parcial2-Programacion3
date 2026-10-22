package com.tp.jpa.model;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, of = "nombre")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Producto extends Base{
    private String nombre;
    private double precio;
    private String descripcion;
    private int stock;
    private String imagen;

    @Builder.Default
    private boolean disponible = true;
}
