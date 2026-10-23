package com.tp.jpa.repository;

import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.Producto;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class CategoriaRepository extends BaseRepository<Categoria> {

    public CategoriaRepository() {
        super(Categoria.class);
    }

    public List<Producto> buscarProductosPorCategoria(Long categoriaId){
        if(categoriaId == null){
            throw new IllegalArgumentException("El id de la categoria no puede ser nulo.");
        }

        EntityManager em = JPAUtil.getEntityManager();

        try {
            // Consulta JPQL: retorna los productos activos de una categoría.
            // Como la relación es unidireccional y Categoria es la dueña, se
            // navega desde Categoria hacia su colección c.productos mediante JOIN.
            // Se filtra por el id de la categoría (parámetro nombrado :catId) y
            // por p.eliminado = false para excluir las bajas lógicas.
            String jpql = "SELECT p FROM Categoria c JOIN c.productos p WHERE c.id = :catId AND p.eliminado = false";
            List<Producto> productos = em.createQuery(jpql, Producto.class)
                    .setParameter("catId", categoriaId)
                    .getResultList();

            return productos;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar productos de la categoría ID: " + categoriaId, e);
        }finally {
            em.close();
        }
    }

    public Producto agregarProducto(Long categoriaId, Producto producto){
        if(categoriaId == null || producto == null){
            throw new IllegalArgumentException("El id de la categoria y el producto no pueden ser nulos.");
        }

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Categoria catEncontrada = em.find(Categoria.class, categoriaId);

            if(catEncontrada == null || catEncontrada.isEliminado()){
                throw new IllegalArgumentException("La categoria esta dada de baja o no existe.");
            }


            tx.begin();
            em.persist(producto);
            catEncontrada.addProducto(producto);
            tx.commit();

            return producto;
        } catch (Exception e) {
            if(tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al agregar el producto a la categoría: " + e.getMessage(), e);
        }finally {
            em.close();
        }
    }
}
