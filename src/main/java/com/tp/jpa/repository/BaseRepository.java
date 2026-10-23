package com.tp.jpa.repository;

import com.tp.jpa.model.Base;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

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


}
