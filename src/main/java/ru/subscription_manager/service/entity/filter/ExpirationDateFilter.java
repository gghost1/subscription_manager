package ru.subscription_manager.service.entity.filter;

import java.time.LocalDate;

public record ExpirationDateFilter(
        LocalDate expirationDate,
        ComparisonType comparison
){
}