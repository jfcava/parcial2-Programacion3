package com.tp.jpa.repository;

import com.tp.jpa.model.Producto;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ProductoRepository extends BaseRepository<Producto>{
    public ProductoRepository() {
        super(Producto.class);
    }

    public List<Object[]> listarProductosConCategoriaJPQL() {
        EntityManager em = JPAUtil.getEntityManager();
        try {

            String jpql = "SELECT p, c.nombre FROM Categoria c JOIN c.productos p WHERE p.eliminado = false";
            return em.createQuery(jpql, Object[].class).getResultList();
        } finally {
            em.close();
        }
    }
}


