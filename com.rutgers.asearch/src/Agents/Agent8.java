package Agents;

import java.util.Arrays;

import Entity.Grid;
import Entity.GridCell;
import Utility.Heuristics;
import Utility.Point;
import Utility.Terrain;

public class Agent8 implements DecisionAgent {
    private GridCell dummyCell;

    public Agent8() {
        this.dummyCell = new GridCell(-1, -1, Terrain.Blocked, null);
        this.dummyCell.setProbGoal(0.0);
    }

    /**
     * Computes a heuristic utility function
     * 
     * @param kb
     * @param p
     * @param dest
     * @return
     */
    private double utility(Point start, GridCell cell) {
        Point p = cell.getLocation();
        double dist = Heuristics.manhattanDistance(start, p) + 1;
        return cell.getProbSuccess()/dist;
    }

    @Override
    public Point getDestination(Grid kb, Point current, Point oldDestination) {
        kb.renormalize();
        GridCell bestChoice =
            Arrays.stream(kb.getGrid())
                  .reduce(dummyCell, (cell1, cell2) -> {
                      double u1 = utility(current, cell1);
                      double u2 = utility(current, cell2);
                      if(u2 == u1) {
                          return (Heuristics.manhattanDistance(current, cell2.getLocation())  <
                                  Heuristics.manhattanDistance(current, cell1.getLocation())) ?
                                  cell2 : cell1;
                      }
                      return (u2 > u1) ? cell2 : cell1;
                  });
        return bestChoice.getLocation();
    }

    @Override
    public boolean doExamine(Grid kb, Point current, Point destination) {
        return kb.getCell(current).getProbSuccess() > kb.getCell(destination).getProbSuccess();
    }
}
