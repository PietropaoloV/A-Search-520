package Agents;

import java.util.Arrays;

import Entity.Grid;
import Entity.GridCell;
import Utility.Point;
import Utility.Terrain;
import Utility.Heuristics;

public class Agent6 implements DecisionAgent {
    private GridCell dummyCell;

    public Agent6() {
        dummyCell = new GridCell(0, 0, Terrain.Blocked, null);
        dummyCell.setProbGoal(-1.0);
    }

    @Override
    public Point getDestination(Grid kb, Point current, Point oldDestination) {
        GridCell bestChoice =
            Arrays.stream(kb.getGrid())
                  .reduce(dummyCell, (cell1, cell2) -> {
                      if(cell2.getProbGoal() == cell1.getProbGoal()) {
                          return (Heuristics.manhattanDistance(current, cell2.getLocation())  <
                                  Heuristics.manhattanDistance(current, cell1.getLocation())) ?
                                  cell2 : cell1;
                      }
                      return (cell2.getProbGoal() > cell1.getProbGoal()) ? cell2 : cell1;
                  });
        return bestChoice.getLocation();
    }

    @Override
    public boolean doExamine(Grid kb, Point current, Point destination) {
        return false;
    }
}
