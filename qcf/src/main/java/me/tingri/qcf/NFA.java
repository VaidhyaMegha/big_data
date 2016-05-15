package me.tingri.qcf;

import me.tingri.qcf.adt.Bag;
import me.tingri.qcf.graph.DiGraph;
import me.tingri.qcf.graph.DirectedDFS;

import java.util.Arrays;
import java.util.Stack;

/**
 * Created by sandeepkunkunuru on 5/14/16.
 */
public class NFA {

    private char[] re;
    private DiGraph g;
    private int m;

    public NFA(String regExp) {
        Stack<Integer> ops = new Stack<>();
        re = regExp.toCharArray();
        m = re.length;
        g = new DiGraph(m + 1);

        for (int i = 0; i < m; i++) {
            int lp = i;

            if (re[i] == '(' || re[i] == '|') ops.push(i);
            else if (re[i] == ')') {
                int or = ops.pop();
                // 2-way or operator (needs more code for 3-way or ex: a|b|c )
                if (re[or] == '|') {
                    lp = ops.pop();
                    g.addEdge(lp, or + 1);
                    g.addEdge(or, i);
                } else
                    lp = or;
            }

            if (i < m - 1 && re[i + 1] == '*') {
                g.addEdge(lp, i + 1);
                g.addEdge(i + 1, lp);
            }

            if (re[i] == '(' || re[i] == '*' || re[i] == ')')
                g.addEdge(i, i + 1);
        }
    }

    public boolean recognizes(String text) {
        DirectedDFS dfs = new DirectedDFS(g, 0); //start dfs from 0 i.e. first node
        Iterable<Integer> pc = getMarked(dfs);

        for (int i = 0; i < text.length(); i++) {
            Bag<Integer> states = new Bag<>();

            for (int v : pc)
                if (v < m)
                    if (re[v] == text.charAt(i) || re[v] == '.')
                        states.add(v + 1);

            dfs = new DirectedDFS(g, states); // try dfs from all states
            pc = getMarked(dfs);
        }

        for (int v : pc) if (v == m) return true;
        return false;
    }

    public String toString() {
        return "Reg Expression = " + Arrays.toString(re) + "\n NFA =" + g.toString();
    }

    private Iterable<Integer> getMarked(DirectedDFS dfs) {
        Bag<Integer> pc = new Bag<>();

        for (int i = 0; i < g.V(); i++)
            if (dfs.marked(i)) pc.add(i);

        return pc;
    }
}
