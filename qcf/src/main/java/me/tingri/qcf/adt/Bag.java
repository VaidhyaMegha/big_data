package me.tingri.qcf.adt;

import java.util.Iterator;

/**
 * Created by sandeepkunkunuru on 5/15/16.
 */
public class Bag<Item> implements Iterable<Item> {
    private Node<Item> root;
    private Node<Item> last;

    public void add(Item value) {
        if (root == null) {
            root = new Node<>(value);
            last = root;
        } else {
            last.next = new Node<>(value);
            last = last.next;
        }
    }

    @Override
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            Node<Item> cursor = root;

            @Override
            public boolean hasNext() {
                return cursor != null ;
            }

            @Override
            public Item next() {
                Node<Item> current = cursor;
                cursor = current.next;
                return current.value;
            }
        };
    }

    private class Node<Item> {
        Node<Item> next;
        Item value;

        Node(Item value) {
            this.value = value;
        }
    }
}
