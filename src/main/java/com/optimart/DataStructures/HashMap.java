package com.optimart.DataStructures;

import java.util.ArrayList;
import java.util.List;

public class HashMap<K, V> {

    private static class Entry<K, V> {
        K key;
        V value;
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        // Required for removeByValue()
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Entry)) return false;
            Entry<?, ?> e = (Entry<?, ?>) o;
            return key.equals(e.key);
        }
    }

    private LinkedList<Entry<K, V>>[] buckets;
    private int capacity;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    @SuppressWarnings("unchecked")
    public HashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = new LinkedList[capacity];
        this.size = 0;

        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap(int capacity) {
        this.capacity = capacity;
        this.buckets = new LinkedList[capacity];
        this.size = 0;

        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    private int hash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode() % capacity);
    }

    public void put(K key, V value) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];

        // search key
        Node<Entry<K, V>> curr = bucket.head;
        while (curr != null) {
            if (curr.data.key.equals(key)) {
                curr.data.value = value;  // update
                return;
            }
            curr = curr.next;
        }

        // add new
        bucket.addLast(new Entry<>(key, value));
        size++;

        if ((double) size / capacity > LOAD_FACTOR) {
            rehash();
        }
    }

    public V get(K key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];

        Node<Entry<K, V>> curr = bucket.head;
        while (curr != null) {
            if (curr.data.key.equals(key)) {
                return curr.data.value;
            }
            curr = curr.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V remove(K key) {
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = buckets[index];

        Node<Entry<K, V>> curr = bucket.head;
        while (curr != null) {
            if (curr.data.key.equals(key)) {
                V val = curr.data.value;
                bucket.removeFrom(curr.data);
                size--;
                return val;
            }
            curr = curr.next;
        }
        return null;
    }

    public void clear() {
        for (LinkedList<Entry<K, V>> bucket : buckets) {
            bucket.clear();
        }
        size = 0;
    }

    public void putIfAbsent(K key, V value) {
        if (!containsKey(key)) {
            put(key, value);
        }
    }

    public List<V> values() {
        List<V> list = new ArrayList<>();

        for (LinkedList<Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket.toList()) {
                    list.add(entry.value);
                }
            }
        }

        return list;
    }

    public List<K> keySet() {
        List<K> keys = new ArrayList<>();

        for (LinkedList<Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                // Traverse your custom linked list
                for (Entry<K, V> entry : bucket.toList()) {
                    keys.add(entry.key);
                }
            }
        }

        return keys;
    }

    public List<K> keys() {
        List<K> list = new ArrayList<>();

        for (LinkedList<Entry<K, V>> bucket : buckets) {
            Node<Entry<K, V>> curr = bucket.head;
            while (curr != null) {
                list.add(curr.data.key);
                curr = curr.next;
            }
        }

        return list;
    }


    @SuppressWarnings("unchecked")
    private void rehash() {
        LinkedList<Entry<K, V>>[] oldBuckets = buckets;

        capacity *= 2;
        buckets = new LinkedList[capacity];
        size = 0;

        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }

        for (LinkedList<Entry<K, V>> bucket : oldBuckets) {
            Node<Entry<K, V>> curr = bucket.head;
            while (curr != null) {
                put(curr.data.key, curr.data.value);
                curr = curr.next;
            }
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }


    public int size() {
        return size;
    }

    public void displayStats() {
        System.out.println("HashMap Statistics:");
        System.out.println("Capacity: " + capacity);
        System.out.println("Size: " + size);
        System.out.println("Load Factor: " + ((double) size / capacity));

        int maxChainLength = 0;
        int emptyBuckets = 0;

        for (LinkedList<Entry<K, V>> bucket : buckets) {
            if (bucket.isEmpty()) {
                emptyBuckets++;
            }
            maxChainLength = Math.max(maxChainLength, bucket.size());
        }

        System.out.println("Empty Buckets: " + emptyBuckets);
        System.out.println("Max Chain Length: " + maxChainLength);
    }
}
