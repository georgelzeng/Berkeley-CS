/**
 * Created by georgezeng on 4/20/17.
 */

public class NodeComparator extends Node implements Comparable<NodeComparator> {

    double distanceTraveled;
    double distanceLeft;
    double priority;

    public NodeComparator(double dT, double dL, Node n) {
        this.x = n.x;
        this.y = n.y;
        this.id = n.id;
        distanceTraveled = dT;
        distanceLeft = dL;
        priority = dT + dL;
    }
    @Override
    public int compareTo(NodeComparator y) {
        if (priority == y.priority) {
            return 0;
        } else if (priority < y.priority) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeComparator that = (NodeComparator) o;

        return Double.compare(that.id, id) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(id);
        return (int) (temp ^ (temp >>> 32));
    }
}