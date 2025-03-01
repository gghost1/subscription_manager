package ru.subscription_manager.service.entity.edit;

import java.util.UUID;

public interface EditEntity <T, D> {

    T edit(T entity);

    D id();

}
