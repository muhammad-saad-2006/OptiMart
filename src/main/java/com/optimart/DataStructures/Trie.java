package com.optimart.DataStructures;

import java.util.ArrayList;
import java.util.List;

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String productName) {
        if (productName == null || productName.isEmpty()) {
            return;
        }

        TrieNode current = root;
        String lowerCaseName = productName.toLowerCase();

        for (char ch : lowerCaseName.toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
        }

        current.isEndOfWord = true;
        current.productName = productName; // Store original name
    }

    public boolean search(String word) {
        TrieNode node = searchNode(word);
        return node != null && node.isEndOfWord;
    }

    public boolean startsWith(String prefix) {
        return searchNode(prefix) != null;
    }

    private TrieNode searchNode(String word) {
        if (word == null) return null;

        TrieNode current = root;
        String lowerCaseWord = word.toLowerCase();

        for (char ch : lowerCaseWord.toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return null;
            }
            current = current.children.get(ch);
        }

        return current;
    }

    public List<String> autocomplete(String prefix) {
        List<String> results = new ArrayList<>();

        if (prefix == null || prefix.isEmpty()) {
            return results;
        }

        // Find the node corresponding to prefix
        TrieNode prefixNode = searchNode(prefix);

        if (prefixNode == null) {
            return results; // Prefix doesn't exist
        }

        collectAllWords(prefixNode, results);

        return results;
    }

    private void collectAllWords(TrieNode node, List<String> results) {
        if (node == null) return;

        if (node.isEndOfWord) {
            results.add(node.productName);
        }

        for (TrieNode child : node.children.values()) {
            collectAllWords(child, results);
        }
    }

    public boolean delete(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        return deleteHelper(root, word.toLowerCase(), 0);
    }

    private boolean deleteHelper(TrieNode current, String word, int index) {
        if (index == word.length()) {
            if (!current.isEndOfWord) {
                return false; // Word doesn't exist
            }
            current.isEndOfWord = false;
            current.productName = null;
            return current.children.isEmpty();
        }

        char ch = word.charAt(index);
        TrieNode node = current.children.get(ch);

        if (node == null) {
            return false; // Word doesn't exist
        }

        boolean shouldDeleteCurrentNode = deleteHelper(node, word, index + 1);

        if (shouldDeleteCurrentNode) {
            current.children.remove(ch);
            return current.children.isEmpty() && !current.isEndOfWord;
        }

        return false;
    }

    public List<String> getTopSuggestions(String prefix, int limit) {
        List<String> allResults = autocomplete(prefix);

        // Sort alphabetically
        allResults.sort(String.CASE_INSENSITIVE_ORDER);

        // Return top N
        return allResults.subList(0, Math.min(limit, allResults.size()));
    }

    public int countWords() {
        return countWordsHelper(root);
    }

    private int countWordsHelper(TrieNode node) {
        if (node == null) return 0;

        int count = node.isEndOfWord ? 1 : 0;

        for (TrieNode child : node.children.values()) {
            count += countWordsHelper(child);
        }

        return count;
    }

    public void displayStats() {
        System.out.println("\nTrie Statistics:");
        System.out.println("Total words: " + countWords());
        System.out.println("Root children: " + root.children.size());
    }
}
