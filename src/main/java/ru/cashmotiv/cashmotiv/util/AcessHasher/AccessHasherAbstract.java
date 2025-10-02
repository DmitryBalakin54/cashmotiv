package ru.cashmotiv.cashmotiv.util.AcessHasher;

public abstract class AccessHasherAbstract<T> implements AccessHasher<T> {
    @Override
    public boolean verify(String hash, T entity) {
        String newHash = create(entity);
        if (hash == null) {
            return false;
        }

        return newHash.equals(hash);
    }
}
