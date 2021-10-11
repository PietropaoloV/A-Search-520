package Entity;

import Agents.InferenceAgent;
import Algorithms.SearchAlgo;
import Utility.Point;
import Utility.Sentiment;
import Utility.Tuple;

import java.util.List;

public class Robot {
    private Point current;
    private Point goal;
    private InferenceAgent agent;
    private Grid kb; // knowledge base
    private SearchAlgo searchAlgo;

    public Robot(Point start, Point goal, InferenceAgent agent, Grid grid, SearchAlgo searchAlgo) {
        this.current = start;
        this.goal = goal;
        this.agent = agent;
        this.kb = grid;
        this.searchAlgo = searchAlgo;

        kb.setSentiment(start, Sentiment.Free); // the agent starts off knowing that the start is unblocked
        kb.getCell(start).setVisited(true);
        this.agent.learn(this.kb, this.current);
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

    // attempt to follow a path, updating known obstacles along the way
    // stops prematurely if it bumps into an obstacle
    // returns the number of steps succesfully moved as well as if the robot bumped into something
    private Tuple<Integer, Boolean> runPath(List<Point> path) {
        int numStepsTaken = 0;
        boolean bumped = false;
        boolean obstructed = false;
        while (!path.isEmpty() && !obstructed) {
            Point position = path.get(0);
            GridCell nextCell = kb.getCell(position);

            // attempt to move into next space, and update KB accordingly
            if (nextCell.isBlocked()) {
                kb.setSentiment(position, Sentiment.Blocked);
                bumped = true;
            } else {
                kb.setSentiment(position, Sentiment.Free);
                move(position);
                nextCell.setVisited(true); // mark cell as visited (implies we've sensed the info in that cell)
                numStepsTaken++;
                path.remove(0);
            }
            agent.learn(kb, current); // learn anything possible

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

    public GridWorldInfo run() {
        GridWorldInfo gridWorldInfoGlobal = new GridWorldInfo(0, 0, null);

        long start = System.nanoTime();
        while (!current.equals(goal)) { // loop while robot has not reached the destination
            // find path
            gridWorldInfoGlobal.numPlans++;
            GridWorldInfo result = searchAlgo.search(current, goal, kb,
                    cell -> cell.isBlocked());

            // if no path found, exit with failure
            if (result == null || result.path == null) {
                gridWorldInfoGlobal.trajectoryLength = Double.NaN;
                return gridWorldInfoGlobal;
            }

            // attempt to travel down returned path, and update statistics
            Tuple<Integer, Boolean> pair = runPath(result.path);
            gridWorldInfoGlobal.trajectoryLength += pair.f1;
            gridWorldInfoGlobal.numberOfCellsProcessed += result.numberOfCellsProcessed;
            if (pair.f2) gridWorldInfoGlobal.numBumps++;
        }
        long end = System.nanoTime();
        gridWorldInfoGlobal.runtime = end - start;

        // count number of cells determined
        for(int y = 0; y < kb.getYSize(); y++) {
            for(int x = 0; x < kb.getXSize(); x++) {
                if(kb.getCell(x, y).getBlockSentiment() != Sentiment.Unsure) {
                    gridWorldInfoGlobal.numCellsDetermined++;
                }
            }
        }

        return gridWorldInfoGlobal;
    }
}
