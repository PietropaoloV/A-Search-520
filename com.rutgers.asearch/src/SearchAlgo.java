import java.util.function.Predicate;

@FunctionalInterface
public interface SearchAlgo {
    GridWorldInfo search(Point start, Point end, Grid grid, Predicate<GridCell> isBlocked);
}
