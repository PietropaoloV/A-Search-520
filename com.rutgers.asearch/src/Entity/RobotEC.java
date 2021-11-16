package Entity;

import Utility.NoPathToGoalException;
import Utility.Point;

public interface RobotEC {

    Decision getDecision(Grid grid) throws NoPathToGoalException;
    void setLocation(Point newLoc);
    Point getLocation();
}
