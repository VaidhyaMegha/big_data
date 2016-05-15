package me.tingri.qcf.graph;

import me.tingri.qcf.adt.Bag;

/**
 * Created by sandeepkunkunuru on 5/15/16.
 */
public class DiGraph {
    private int v;
    private int e;
    private Bag<Integer>[] adj;

    public DiGraph(int v) {
        this.v = v;
        this.e = 0;
        adj = (Bag<Integer>[]) new Bag[v];

        for (int i = 0; i < v; i++) adj[i] = new Bag<>();
    }

    public int V() {
        return v;
    }

    public int E() {
        return e;
    }

    public void addEdge(int v, int w) {
        adj[v].add(w);
        e++;
    }

    public Iterable<Integer> adj(int v) {
        return adj[v];
    }
}
