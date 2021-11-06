package Entity;

import Agents.Agent;
import Algorithms.SearchAlgo;
import Utility.Point;
import Utility.Sentiment;
import Utility.Tuple;

import java.util.List;

/**
 * Represents the actual agent that will move through the gridworld.
 */
public class Robot {
    private Point current;
    private Point goal;
    private Agent agent;
    private Grid kb; // knowledge base
    private SearchAlgo searchAlgo;
    private static double NANO_SECONDS = 1000000000d;

    /**
     * Constructs the agent with the specified parameters.
     * 
     * @param start      Where the agent starts
     * @param goal       Where the agent is trying to reach
     * @param agent      The algorithm used to make inferences/learn about the
     *                   surroundings
     * @param grid       The initial knowledge base
     * @param searchAlgo The algorithm used to path-plan
     */
    public Robot(Point start, Point goal, Agent agent, Grid grid, SearchAlgo searchAlgo) {
        this.current = start;
        this.goal = goal;
        this.agent = agent;
        this.kb = grid;
        this.searchAlgo = searchAlgo;
        kb.setCell(start).setVisited(true);
    }

    public Point getLocation() {
        return current;
    }

    public Point getGoal() {
        return goal;
    }

    public void move(Point pos) {
        current = pos;
    }

    public Grid getKnowledgeBase() {
        return kb;
    }

    public SearchAlgo getSearchAlgo() {
        return searchAlgo;
    }

    /**
     * Attempts to follow a path, making inferences/learning along the way. Stops
     * prematurely if it detects or bumps into an obstacle.
     * 
     * @param path The path to follow
     * @return The number of steps taken before stopping, and whether the agent
     *         bumped into an obstacle
     */
    private Tuple<Integer, Boolean> runPath(List<Point> path) {
        int numStepsTaken = 0;
        boolean bumped = false;
        boolean obstructed = false;
        while (!path.isEmpty() && !obstructed) {
            Point position = path.get(0);
            GridCell nextCell = kb.getCell(position);

            // attempt to move into next space,
            // and update KB accordingly based on if cell was blocked or empty
            if (nextCell.isBlocked()) {

            } else {

            }


            // check if any steps on remaining path are confirmed to be blocked
            for (Point p : path) {
                if (kb.getCell(p).getBlockSentiment() == Sentiment.Blocked) {
                    obstructed = true;
                    break;
                }
            }
        }

        return new Tuple<>(numStepsTaken, bumped);
    }

    /**
     * Runs the entire agent workflow, and records runtime statistics.
     * 
     * @return A {@link Entity.GridWorldInfo} instance containing runtime statistics
     */
    public GridWorldInfo run() {
        GridWorldInfo gridWorldInfoGlobal = new GridWorldInfo(0, 0, null);

        long start = System.nanoTime();
        while (!current.equals(goal)) { // loop while robot has not reached the destination
            // find path
            gridWorldInfoGlobal.numPlans++;
            GridWorldInfo result = searchAlgo.search(current, goal, kb,
                    cell -> cell.getBlockSentiment() == Sentiment.Blocked);

            // if no path found, exit with failure
            if (result == null || result.path == null) {
                gridWorldInfoGlobal.trajectoryLength = Double.NaN;
                return gridWorldInfoGlobal;
            }

            // attempt to travel down returned path, and update statistics
            Tuple<Integer, Boolean> pair = runPath(result.path);
            gridWorldInfoGlobal.trajectoryLength += pair.f1;
            gridWorldInfoGlobal.numberOfCellsProcessed += result.numberOfCellsProcessed;
            if (pair.f2)
                gridWorldInfoGlobal.numBumps++;
        }
        long end = System.nanoTime();
        gridWorldInfoGlobal.runtime = (end - start) / NANO_SECONDS;

        // count number of cells determined
        for (int y = 0; y < kb.getYSize(); y++) {
            for (int x = 0; x < kb.getXSize(); x++) {
                if (kb.getCell(x, y).getBlockSentiment() != Sentiment.Unsure) {
                    gridWorldInfoGlobal.numCellsDetermined++;
                }
            }
        }

        return gridWorldInfoGlobal;
    }
}
