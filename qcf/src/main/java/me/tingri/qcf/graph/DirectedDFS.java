package me.tingri.qcf.graph;

/**
 * Created by sandeepkunkunuru on 5/15/16.
 */
public class DirectedDFS {
    private boolean[] marked;

    public DirectedDFS(DiGraph g, int s) {
        marked = new boolean[g.V()];
        dfs(g, s);
    }

    public DirectedDFS(DiGraph g, Iterable<Integer> sources) {
        marked = new boolean[g.V()];
        for (int s : sources)
            if (!marked[s]) dfs(g, s);
    }

    private void dfs(DiGraph g, int v) {
        marked[v] = true;
        for (int w : g.adj(v))
            if (!marked[w]) dfs(g, w);
    }

    public boolean marked(int v) {
        return marked[v];
    }
}
