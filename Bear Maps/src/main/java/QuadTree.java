/**
 * Created by georgezeng on 4/15/17.
 */

public class QuadTree {

    int name;
    int toTraverse;
    int depth;

    double ullon;
    double ullat;
    double lrlon;
    double lrlat;

    QuadTree one;
    QuadTree two;
    QuadTree three;
    QuadTree four;


    public QuadTree(int root, int layer, double ulx, double uly, double lrx, double lry) {
        name = root;
        toTraverse = layer;
        depth = 7 - layer;

        ullon = ulx;
        ullat = uly;
        lrlon = lrx;
        lrlat = lry;

        if (toTraverse > 0) {
            one = new QuadTree(root * 10 + 1, layer - 1, ullon, ullat,
                    (ullon + lrlon) / 2, (ullat + lrlat) / 2);
            two = new QuadTree(root * 10 + 2, layer - 1,
                    (ullon + lrlon) / 2, ullat, lrlon, (ullat + lrlat) / 2);
            three = new QuadTree(root * 10 + 3, layer - 1, ullon,
                    (ullat + lrlat) / 2, (ullon + lrlon) / 2, lrlat);
            four = new QuadTree(root * 10 + 4, layer - 1,
                    (ullon + lrlon) / 2, (ullat + lrlat) / 2, lrlon, lrlat);
        }
    }


    //find intersecting tiles given query points, and depth

    public QuadTree findTileIntersect(double x, double y, int d) {
        if (d > 0) {
            if (x >= (ullon + lrlon) / 2) {
                if (y > (lrlat + ullat) / 2) {
                    return two.findTileIntersect(x, y, d - 1);
                } else {
                    return four.findTileIntersect(x, y, d - 1);
                }
            } else {
                if (y > (lrlat + ullat) / 2) {
                    return one.findTileIntersect(x, y, d - 1);
                } else {
                    return three.findTileIntersect(x, y, d - 1);
                }
            }
        } else {
            return this;
        }
    }

    public boolean intersection(double ax1, double ay1, double ax2, double ay2) {
        return ((ax1 <= lrlon) && (ax2 >= ullon) && (ay1 >= lrlat) && (ay2 <= ullat));
    }

    //find all intersections in order at a given level given query box points

    public String[][] findTileGrid(double x1, double y1, double x2, double y2, int d) {

        QuadTree first = findTileIntersect(x1, y1, d);
        QuadTree last = findTileIntersect(x2, y2, d);
        x1 = (first.ullon + first.lrlon) / 2;
//        y1 = (first.ullat + first.lrlat) / 2;
        x2 = (last.ullon + last.lrlon) / 2;
//        y2 = (last.ullat + last.lrlat) / 2;

        double nextTile = 256 * 0.000343322 * Math.pow(.5, d);

        int width = (int) Math.round((x2 - x1) / nextTile) + 1;
        int height = (int) Math.round((first.ullat - last.lrlat) / nextTile) + 1;

        String[][] grid = new String[height][];

        for (int j = 0; j < height; j++) {
            String[] temp = new String[width];

            x1 = (first.ullon + first.lrlon) / 2;
            y1 = (first.ullat + first.lrlat) / 2;

            for (int i = 0; i < width; i++) {
                temp[i] = "img/" + Integer.toString(first.name) + ".png";
                if (first == last) {
                    break;
                } else {
                    x1 = x1 + nextTile;
                    first = findTileIntersect(x1, y1, d);
                }
            }
            grid[j] = temp;

            if (first == last) {
                break;
            } else {
                x1 = x1 - nextTile * width;
                y1 = y1 - nextTile;
                first = findTileIntersect(x1, y1, d);
            }
        }
        return grid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuadTree quadTree = (QuadTree) o;

        if (Double.compare(quadTree.ullon, ullon) != 0) {
            return false;
        }
        if (Double.compare(quadTree.ullat, ullat) != 0) {
            return false;
        }
        if (Double.compare(quadTree.lrlon, lrlon) != 0) {
            return false;
        }
        return Double.compare(quadTree.lrlat, lrlat) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(ullon);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ullat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lrlon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lrlat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
