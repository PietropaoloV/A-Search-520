package Agents;

import Entity.Grid;
import Entity.GridCell;
import Utility.Point;

public class Agent6 extends UtilityAgent {
    /**
     * Defines utility by the probability of a cell containing the target.
     */
    @Override
    public double utility(Grid kb, Point current, GridCell cell) {
        return cell.getProbGoal();
    }
}
