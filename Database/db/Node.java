package db;

/**
 * Created by georgelzeng on 2/27/2017.
 */
public class Node<T> {
    private T value;

    public Node(T item) {
        value = item;
    }

    public T getItem() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node<?> node = (Node<?>) o;

        return value != null ? value.equals(node.value) : node.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}