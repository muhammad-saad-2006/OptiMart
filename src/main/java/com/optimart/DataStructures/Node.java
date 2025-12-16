package com.optimart.DataStructures;

public class Node<T> {
    T data;
    Node<T> next, left, right, parent;

    public Node(T data){
        this.data = data;
        next = left = right = parent = null;
    }
}
