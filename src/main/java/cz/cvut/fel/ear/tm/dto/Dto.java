package cz.cvut.fel.ear.tm.dto;

import cz.cvut.fel.ear.tm.model.AbstractEntity;

public interface Dto<T> {
    public T buildFromDto();
    public T update(T t);
}
