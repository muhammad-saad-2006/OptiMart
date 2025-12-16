package com.optimart.DataStructures;

import java.util.ArrayList;
import java.util.List;

public class LinkedList<T> {
    Node<T> head;
    Node<T> tail;
    private int size = 0;

    public void addLast(T val){
        Node<T> temp = new Node<>(val);
        if(head == null){
            head = tail = temp;
        } else {
            tail.next = temp;
            tail = temp;
        }
        size++;
    }

    public void addFirst(T val){
        Node<T> temp = new Node<>(val);
        if(head == null){
            head = tail = temp;
        } else {
            temp.next = head;
            head = temp;
        }
        size++;
    }

    public void removeFirst(){
        if (head == null) {
            System.out.println("list is empty!!!");
            return;
        }

        head = head.next;
        size--;
    }

    public void removeLast(){
        Node<T> temp = head;

        if(head == null){
            System.out.println("list is empty!!!");
            return;
        }

        if(head.next == null){
            removeFirst();
            return;
        }

        while(temp.next.next != null){
            temp = temp.next;
        }
        temp.next = null;
        tail = temp;

        size--;
    }

    public boolean removeFrom(T value) {
        if (head == null) return false;

        // If first node is the target
        if (head.data.equals(value)) {
            removeFirst();
            return true;
        }

        Node<T> prev = head;
        Node<T> curr = head.next;

        while (curr != null) {
            if (curr.data.equals(value)) {
                prev.next = curr.next;
                if (curr == tail) tail = prev;
                size--;
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }


    public T get(int index) {
        if (index < 0 || index >= size) return null;

        Node<T> temp = head;
        for (int i = 0; i < index; i++) temp = temp.next;

        return temp.data;
    }

    public List<T> toList() {
        List<T> list = new ArrayList<>();
        Node<T> current = head;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }

    public void clear() {
        head = tail = null;
        size = 0;
    }

    public int size(){
        if(isEmpty()) return 0;
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    public T[] toArray() {
        if (isEmpty()) {
            return (T[]) new Object[0];
        }

        T[] array = (T[]) new Object[size];
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            array[index++] = current.data;
            current = current.next;
        }
        return array;
    }

    public LinkedList<T> copy() {
        LinkedList<T> newList = new LinkedList<>();
        Node<T> current = head;
        while (current != null) {
            newList.addLast(current.data);
            current = current.next;
        }
        return newList;
    }

    public void display(){
        if(isEmpty()){
            System.out.println("List is empty!!!");
        }
        Node<T> temp = head;
        while (temp != null){
            System.out.print(temp.data);
            if(temp.next != null){
                System.out.print(" -> ");
            }
            temp = temp.next;
        }
        System.out.println();
    }
}
