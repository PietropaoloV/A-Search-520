public class Point {
    public final int f1;
    public final int f2;

    public Point(int f1, int f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    public Point[] get4Neighbours() {
        Point[] neighbours = new Point[4];
        neighbours[0] = new Point(f1+1, f2); // right
        neighbours[1] = new Point(f1-1, f2); // left
        neighbours[2] = new Point(f1, f2-1); // up
        neighbours[3] = new Point(f1, f2+1); // down

        return neighbours;
    }

    public Point[] get8Neighbours() {
        Point[] neighbours = new Point[8];
        neighbours[0] = new Point(f1+1, f2);    // E
        neighbours[1] = new Point(f1+1, f2+1);  // SE
        neighbours[2] = new Point(f1, f2+1);    // S
        neighbours[3] = new Point(f1-1, f2+1);  // SW
        neighbours[4] = new Point(f1-1, f2);    // W
        neighbours[5] = new Point(f1-1, f2-1);  // NW
        neighbours[6] = new Point(f1, f2-1);    // N
        neighbours[7] = new Point(f1+1, f2-1);  // NE

        return neighbours;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Point))
            return false;
        Point other = (Point) obj;
        return this.f1 == other.f1 && this.f2 == other.f2;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(f1, f2);
    }

    @Override
    public String toString() {
        return "<" + f1 + "," + f2 + '>';
    }
}
