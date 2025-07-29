/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ADT;


public interface AVLTreeInterface<K extends Comparable<K>,V> {
    public void insert(K key, V value);
    public V search(K keyValue);
    public void delete(K keyValue);
    public boolean contains(K keyValue);
}
