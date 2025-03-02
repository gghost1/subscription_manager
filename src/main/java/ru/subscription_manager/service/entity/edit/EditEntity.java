package ru.subscription_manager.service.entity.edit;

/**
 * Interface for entity editing
 * @param <T> entity for edit
 * @param <D> entity id
 */
public interface EditEntity <T, D> {

    T edit(T entity);

    D id();

}
