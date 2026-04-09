package com.testtask.dsu;

import java.util.Arrays;

public class DisjointSetUnion {
    private int[] parent = new int[1 << 20];
    private int[] size = new int[1 << 20];
    private int n = 0;

    public int addNode() {
        ensureCapacity(n + 1);
        int id = n++;
        parent[id] = id;
        size[id] = 1;
        return id;
    }

    public int find(int x) {
        int root = x;
        while (root != parent[root]) {
            root = parent[root];
        }
        int current = x;
        while (current != root) {
            int next = parent[current];
            parent[current] = root;
            current = next;
        }
        return root;
    }

    public void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) {
            return;
        }

        int sa = size[ra];
        int sb = size[rb];
        if (sa < sb) {
            int tmp = ra;
            ra = rb;
            rb = tmp;
            sa = size[ra];
            sb = size[rb];
        }

        parent[rb] = ra;
        size[ra] = sa + sb;
    }

    public int componentSize(int x) {
        return size[find(x)];
    }

    private void ensureCapacity(int required) {
        if (required <= parent.length) {
            return;
        }
        int newCap = parent.length << 1;
        while (newCap < required) {
            newCap <<= 1;
        }
        parent = Arrays.copyOf(parent, newCap);
        size = Arrays.copyOf(size, newCap);
    }
}
