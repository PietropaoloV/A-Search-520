package Entity;

import Utility.NoPathToGoalException;
import Utility.Point;

import java.util.ArrayList;

public class RobotPrey implements RobotEC{
    private Point currLoc = null;
    public RobotPrey( Point currLoc) {

        this.currLoc = currLoc;
    }

    public Point getCurrLoc() {
        return currLoc;
    }

    public void setCurrLoc(Point currLoc) {
        this.currLoc = currLoc;
    }

    @Override
    public Decision getDecision(Grid grid) throws NoPathToGoalException {
        Point[] dirs = grid.getCell(currLoc).getLocation().get4Neighbours();
        ArrayList<Integer> possibleDirs = new ArrayList<>();
        for(int i = 0; i< dirs.length; i++) {
            if( grid.inBounds(dirs[i]) && !grid.getCell(dirs[i]).isBlocked() ){
                possibleDirs.add(i);
            }
        }

        if(possibleDirs.size() == 0 )
            throw new NoPathToGoalException();
        int ranDir = (int)(Math.random()* possibleDirs.size() )%4;
        switch (possibleDirs.get(ranDir)){
            case 0: return Decision.Right;
            case 1: return Decision.Left;
            case 2: return Decision.Up;
            case 3: return Decision.Down;
            default:
                throw new NoPathToGoalException();
        }

    }

    @Override
    public void setLocation(Point newLoc) {
        this.currLoc = newLoc;
    }

    @Override
    public Point getLocation() {
        return currLoc;
    }

}
