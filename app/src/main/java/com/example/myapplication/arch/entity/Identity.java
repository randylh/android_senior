package com.example.myapplication.arch.entity;

import java.io.Serializable;

public interface Identity<T> extends Serializable {

    T value();

    boolean isEmpty();

    T emptyId();
}
