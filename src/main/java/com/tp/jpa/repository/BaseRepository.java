package com.tp.jpa.repository;

import com.tp.jpa.model.Base;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T extends Base> {
    private final Class<T> claseEntidad;

    public BaseRepository(Class<T> entityClass) {
        this.claseEntidad = entityClass;
    }

    public T guardar(T entity){

        if (entity == null) {
            throw new IllegalArgumentException("La entidad a guardar no puede ser nula.");
        }

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        T entidadManejada = null;

        try {
            tx.begin();

            if(entity.getId() == null){
                em.persist(entity);
                entidadManejada = entity;
            }else{
                entidadManejada = em.merge(entity);
            }
            tx.commit();

            return entidadManejada;

        } catch (Exception e) {
            if(tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al guardar la entidad " + claseEntidad.getSimpleName(), e);
        }finally{
            em.close();
        }
    }

    public Optional<T> buscarPorId(Long id){
        if (id == null) {
            throw new IllegalArgumentException("El id buscado no puede ser null.");
        }

        EntityManager em = JPAUtil.getEntityManager();

        try {
            T entidadBuscada = em.find(claseEntidad, id);
            if(entidadBuscada != null){
                return Optional.of(entidadBuscada);
            }else{
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar por ID la entidad " + claseEntidad.getSimpleName(), e);
        }finally{
            em.close();
        }
    }

    public List<T> listarActivos() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            String jpql = "SELECT e FROM " + claseEntidad.getSimpleName() + " e WHERE e.eliminado = false";
            return em.createQuery(jpql, claseEntidad).getResultList();


        } catch (Exception e) {
            throw new RuntimeException("Error al consultar la lista de entidades activas en " + claseEntidad.getSimpleName(), e);
        }finally {
            em.close();
        }
    }

    public boolean eliminarLogico(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id buscado no puede ser null.");
        }

        Optional<T> optionalEntidad = buscarPorId(id);

        if(optionalEntidad.isEmpty()){
            return false;
        }

        T entidad = optionalEntidad.get();

        if(entidad.isEliminado()){
            return false;
        }

        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            entidad.setEliminado(true);
            em.merge(entidad);
            tx.commit();
            return true;

        } catch (Exception e) {
            if(tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al realizar la baja lógica del ID: " + id, e);
        }finally{
            em.close();
        }
    }
}
