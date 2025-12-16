package com.optimart.DataStructures;

public class TrieNode {
    HashMap<Character, TrieNode> children;
    boolean isEndOfWord;
    String productName;

    public TrieNode() {
        children = new HashMap<>();
        isEndOfWord = false;
        productName = null;
    }
}
