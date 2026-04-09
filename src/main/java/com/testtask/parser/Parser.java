package com.testtask.parser;

public interface Parser<T> {
    T parse(String line);
}
