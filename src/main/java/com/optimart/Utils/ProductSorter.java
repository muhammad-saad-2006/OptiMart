package com.optimart.Utils;

import com.optimart.DataStructures.LinkedList;
import com.optimart.Models.Product;

public class ProductSorter {

    public static LinkedList<Product> sortByName(LinkedList<Product> products) {
        if (products == null || products.size() <= 1) {
            return products;
        }

        Product[] array = convertToArray(products);
        mergeSortByName(array, 0, array.length - 1);
        return convertToLinkedList(array);
    }

    private static void mergeSortByName(Product[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            mergeSortByName(array, left, mid);
            mergeSortByName(array, mid + 1, right);

            mergeByName(array, left, mid, right);
        }
    }

    private static void mergeByName(Product[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Product[] leftArray = new Product[n1];
        Product[] rightArray = new Product[n2];

        for (int i = 0; i < n1; i++) {
            leftArray[i] = array[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = array[mid + 1 + j];
        }

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (leftArray[i].getName().compareToIgnoreCase(rightArray[j].getName()) <= 0) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }

    public static LinkedList<Product> sortByPriceLowToHigh(LinkedList<Product> products) {
        if (products == null || products.size() <= 1) {
            return products;
        }

        Product[] array = convertToArray(products);
        mergeSortByPrice(array, 0, array.length - 1, true);
        return convertToLinkedList(array);
    }

    public static LinkedList<Product> sortByPriceHighToLow(LinkedList<Product> products) {
        if (products == null || products.size() <= 1) {
            return products;
        }

        Product[] array = convertToArray(products);
        mergeSortByPrice(array, 0, array.length - 1, false);
        return convertToLinkedList(array);
    }

    private static void mergeSortByPrice(Product[] array, int left, int right, boolean ascending) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            mergeSortByPrice(array, left, mid, ascending);
            mergeSortByPrice(array, mid + 1, right, ascending);

            mergeByPrice(array, left, mid, right, ascending);
        }
    }

    private static void mergeByPrice(Product[] array, int left, int mid, int right, boolean ascending) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Product[] leftArray = new Product[n1];
        Product[] rightArray = new Product[n2];

        for (int i = 0; i < n1; i++) {
            leftArray[i] = array[left + i];
        }
        for (int j = 0; j < n2; j++) {
            rightArray[j] = array[mid + 1 + j];
        }

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            boolean condition = ascending ?
                    leftArray[i].getPrice() <= rightArray[j].getPrice() :
                    leftArray[i].getPrice() >= rightArray[j].getPrice();

            if (condition) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }

    public static void displayProducts(LinkedList<Product> products, String sortType) {
        if (products == null || products.isEmpty()) {
            System.out.println("No products to display.");
            return;
        }

        System.out.println("\nPRODUCTS (Sorted by " + sortType + ") ");
        System.out.println("ID         | Name                      | Category        | Price          | Stock");

        for (int i = 0; i < products.size(); i++) {
            System.out.println(products.get(i).toDisplayString());
        }


    }

    private static Product[] convertToArray(LinkedList<Product> linkedList) {
        Product[] array = new Product[linkedList.size()];
        for (int i = 0; i < linkedList.size(); i++) {
            array[i] = linkedList.get(i);
        }
        return array;
    }

    private static LinkedList<Product> convertToLinkedList(Product[] array) {
        LinkedList<Product> linkedList = new LinkedList<>();
        for (Product product : array) {
            linkedList.addLast(product);
        }
        return linkedList;
    }
}
