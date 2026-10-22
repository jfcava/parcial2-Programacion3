package com.tp.jpa.model;

import com.tp.jpa.model.enums.Rol;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false, of = "mail")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends Base{
    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private String contrasenia;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @Builder.Default
    private Set<Pedido> pedidos = new HashSet<>();

    public void addPedido (Pedido pedido){
        this.pedidos.add(pedido);
    }

    public int getCantidadPedidos() {
        return pedidos != null ? pedidos.size() : 0;
    }

    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }

}
