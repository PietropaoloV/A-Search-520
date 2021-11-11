package Algorithms;

import Entity.Grid;
import Entity.GridCell;
import Utility.Point;
import Utility.Tuple;

import java.util.List;
import java.util.function.Predicate;

/**
 * A general interface for describing search algorithm.
 */
@FunctionalInterface
public interface SearchAlgo {
    /**
     * Attempts to find a path through the gridworld.
     * 
     * @param start     Start Location
     * @param end       End Location
     * @param grid      Grid to Search
     * @param isBlocked Function to check whether cells are blocked
     * @return The path found (if any) as well as how many cells were processed
     */
    Tuple<List<Point>, Integer> search(Point start, Point end, Grid grid, Predicate<GridCell> isBlocked);
}
