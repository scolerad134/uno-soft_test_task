package com.testtask.model;

import java.util.Arrays;

public class IntList {
    private int[] data = new int[16];
    private int size = 0;

    public void add(int value) {
        if (size == data.length) {
            data = Arrays.copyOf(data, data.length << 1);
        }
        data[size++] = value;
    }

    public int get(int idx) {
        return data[idx];
    }

    public int size() {
        return size;
    }
}
