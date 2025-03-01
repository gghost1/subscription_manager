package ru.subscription_manager.controller.entity.response;

import java.util.List;

public record PaginatedList <T> (
    int page,
    int size,
    long total,
    List<T> items
) {
}
