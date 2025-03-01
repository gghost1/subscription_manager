package ru.subscription_manager.configuration.exception;

import lombok.Getter;

@Getter
public class DbException extends RuntimeException {

    private final Exception exception;

    public DbException(String message, Exception e) {
        super(message);
        this.exception = e;
    }
}
