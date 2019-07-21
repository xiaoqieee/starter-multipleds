package com.hawcore.framework.multipleds.util;

import java.util.LinkedList;

public class Stack<E> {
    LinkedList<E> list;

    public Stack() {
        list = new LinkedList();
    }

    public E pop() {
        E e = list.removeLast();
        return e;
    }

    public void push(E o) {
        list.add(o);
    }

    public E getTop() {
        if(isEmpty()){
            return null;
        }
        return list.getLast();
    }

    public boolean isEmpty() {
        return list.size() == 0;
    }

    public int size() {
        return list.size();
    }
}
