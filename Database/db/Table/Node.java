package Table;

/**
 * Created by danie on 2/27/2017.
 */
public class Node<T> {
    private T value;

    public Node(T item) {
        value = item;
    }
    public T getItem() {
        return value;
    }
}