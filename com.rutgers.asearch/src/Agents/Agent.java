package Agents;

import Entity.Grid;
import Entity.GridCell;
import Utility.Terrain;
import Utility.Tuple;

import java.util.Arrays;

public interface Agent {
    int numConfBlocked = 0;

    default Tuple<Terrain, Boolean> examineCell(GridCell cell){
        return new Tuple(observeCell(cell) ,cell.examineTerrain());
    }
    default Terrain observeCell(GridCell cell){
        return cell.observeTerrain();
    }

    default void updateFromBlocked(Grid grid){
        int len = grid.getGrid().length;
        int assumedFree = len - numConfBlocked;
        double newGoalProb  = 1d/((double)assumedFree);
        updateProb(grid, newGoalProb);
    }
    default void updateProb(Grid grid, double probability){
        Arrays.stream(grid.getGrid()).forEach(gridCell -> {
            if(!gridCell.isBlocked()) {
                double cellProb = gridCell.getProbGoal();
                gridCell.setProbGoal(cellProb / (1 - probability));
            }
        });

    }

}
