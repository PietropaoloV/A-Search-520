package Agents;

import java.util.Arrays;

import Entity.Grid;
import Entity.GridCell;
import Utility.Heuristics;
import Utility.Point;
import Utility.Terrain;

public class Agent7 implements DecisionAgent {
    private GridCell dummyCell;

    public Agent7() {
        dummyCell = new GridCell(0, 0, Terrain.Blocked, null);
        dummyCell.setProbGoal(-1.0);
    }

    @Override
    public Point getDestination(Grid kb, Point current, Point oldDestination) {
        GridCell bestChoice =
            Arrays.stream(kb.getGrid())
                  .reduce(dummyCell, (cell1, cell2) -> {
                      if(cell2.getProbSuccess() == cell1.getProbSuccess()) {
                          return (Heuristics.manhattanDistance(current, cell2.getLocation())  <
                                  Heuristics.manhattanDistance(current, cell1.getLocation())) ?
                                  cell2 : cell1;
                      }
                      return (cell2.getProbSuccess() > cell1.getProbSuccess()) ? cell2 : cell1;
                  });
        return bestChoice.getLocation();
    }

    @Override
    public boolean doExamine(Grid kb, Point current, Point destination) {
        return kb.getCell(current).getProbSuccess() > kb.getCell(destination).getProbSuccess();
    }
}
