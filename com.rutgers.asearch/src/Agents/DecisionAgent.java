package Agents;

import Entity.Grid;
import Utility.Point;

public interface DecisionAgent {
    /**
     * Determine which cell to set the destination to based on the current
     * information and robot state.
     * 
     * @param kb
     * @param current
     * @param destination
     * @return
     */
    Point getDestination(Grid kb, Point current, Point oldDestination);

    /**
     * Decides whether the robot should examine the current cell.
     * 
     * @param kb
     * @param current
     * @param destination
     * @return
     */
    boolean doExamine(Grid kb, Point current, Point destination);
}
