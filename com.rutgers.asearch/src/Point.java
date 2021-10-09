public class Point {
    public final int f1;
    public final int f2;

    public Point(int f1, int f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Point))
            return false;
        Point other = (Point) obj;
        return this.f1 == other.f1 && this.f1 == other.f2;
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
