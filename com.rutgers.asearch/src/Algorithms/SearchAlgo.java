package Algorithms;

import Entity.Grid;
import Entity.GridCell;
import Entity.GridWorldInfo;
import Utility.Point;

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
     * @return The path found (if any) as well as some runtime statistics
     *         ({@link Entity.GridWorldInfo}).
     */
    GridWorldInfo search(Point start, Point end, Grid grid, Predicate<GridCell> isBlocked);
}
