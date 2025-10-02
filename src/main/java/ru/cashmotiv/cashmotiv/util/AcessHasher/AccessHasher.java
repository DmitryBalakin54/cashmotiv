package ru.cashmotiv.cashmotiv.util.AcessHasher;

public interface AccessHasher<T> {
    String create(T entity);
    boolean verify(String hash, T entity);
}
