package com.tech.parking.model;

public interface CardItemProvider<T> {
    CardItem provide(T model);
}
