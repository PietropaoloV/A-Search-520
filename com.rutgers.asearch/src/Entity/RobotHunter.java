package Entity;

import Utility.NoPathToGoalException;
import Utility.Point;
import Utility.Tuple;

import java.util.ArrayList;

public class RobotHunter implements RobotEC {
    private boolean foundGoal;
    private boolean sensed = false;
    private Point currLoc;
    private boolean checkingSensedLoc;
    private boolean isSearching;
    private int numBlocked = 0;
    private boolean shouldExamine = false;
    public RobotHunter( Point currLoc) {
        this.foundGoal = false;
        this.currLoc = currLoc;
        this.isSearching =false;
        this.checkingSensedLoc = false;
    }

    public boolean isShouldExamine() {
        return shouldExamine;
    }

    public void setShouldExamine(boolean shouldExamine) {
        this.shouldExamine = shouldExamine;
    }

    public int getNumBlocked() {
        return numBlocked;
    }

    public void setNumBlocked(int numBlocked) {
        this.numBlocked = numBlocked;
    }

    public boolean isSensed() {
        return sensed;
    }

    public void setSensed(boolean sensed) {
        this.sensed = sensed;
    }

    public boolean isCheckingSensedLoc() {
        return checkingSensedLoc;
    }

    public void setCheckingSensedLoc(boolean checkingSensedLoc) {
        this.checkingSensedLoc = checkingSensedLoc;
    }

    public boolean isSearching() {
        return isSearching;
    }

    public void setSearching(boolean searching) {
        isSearching = searching;
    }

    public Point getCurrLoc() {
        return currLoc;
    }

    public void setCurrLoc(Point currLoc) {
        this.currLoc = currLoc;
    }

    private Decision getDecisionDir(int i) {
        Decision dir;
            switch (i) {
                case 0: {
                    dir = Decision.Right;
                    break;
                }


                case 3: {
                    dir = Decision.Down;
                    break;
                }


                case 1: {
                    dir = Decision.Left;
                    break;
                }

                case 2: {
                    dir = Decision.Up;
                    break;
                }
                default:
                    dir = Decision.NOOP;
            }
        return dir;
    }

    public void setFoundGoal(boolean foundGoal) {
        this.foundGoal = foundGoal;
    }

    public void updateProbs(Grid grid, boolean isSensed){
        if(isSensed){
            if(this.isSearching()){

                updateRadiusProbabilities(grid, this.getCurrLoc());
            }else {

                setInitialNeighborProbs(grid, this.getCurrLoc());
                this.setSearching(true);
            }
            this.setCheckingSensedLoc(true);
        }else if(!this.isCheckingSensedLoc()){
            shouldExamine=true;
        }else {
            this.setCheckingSensedLoc(false);
            this.setSearching(false);
            grid.initProbs(0);
        }
    }

    @Override
    public Decision getDecision(Grid grid) throws NoPathToGoalException {
        updateProbs(grid, this.isSensed());
        Tuple<Decision, Double> maxTuple = null;

        ArrayList<Decision> ties = new ArrayList<>();
        Point[] neighbors = currLoc.get4Neighbours();
        for (int i = 0; i < neighbors.length; i++) {
            if (!grid.inBounds(neighbors[i])) continue;
            if(grid.inBounds(neighbors[i]) && !grid.getCell(neighbors[i]).isBumped()) {
                double pointProb = grid.getCell(neighbors[i]).getProbGoal();
                if ((maxTuple == null || maxTuple.f2 < pointProb) ) { maxTuple = new Tuple<>(getDecisionDir(i), pointProb);
                }
            }
        }
        for (int i = 0; i < neighbors.length; i++) {
            if (!grid.inBounds(neighbors[i])) continue;
            if(grid.inBounds(neighbors[i]) && !grid.getCell(neighbors[i]).isBumped()) {
                double pointProb = grid.getCell(neighbors[i]).getProbGoal();
                if (maxTuple.f2 == pointProb) {
                    ties.add(getDecisionDir(i));
                }
            }
        }
        if(maxTuple != null)
            shouldExamine = maxTuple.f2 > .20;
        Decision decision = Decision.NOOP;
        if(ties.size() != 0)
            decision =  ties.get((int) (Math.random() * ties.size()));

        return decision;

    }

    @Override
    public void setLocation(Point newLoc) {
        this.currLoc = newLoc;
    }

    @Override
    public Point getLocation() {
        return currLoc;
    }

    public boolean isGoalFound(){
        return foundGoal;
    }
    public void locatedGoal(){
        foundGoal = true;
    }
        private  void setInitialNeighborProbs(Grid grid, Point currentLocation){
            ArrayList<Point> bPoints = grid.getKnownBlockedNeighbors(currentLocation);
            grid.setProbsToZero();
            for (Point p : currentLocation.get8Neighbours()) {
                if (grid.inBounds(p)) {
                    if (bPoints.contains(p)){
                        grid.getCell(p).setProbGoal(0);
                    }else {
                        grid.getCell(p).setProbGoal(1d / (8 - bPoints.size()));
                        grid.getCell(p).setInRadius(true);
                    }

                }
            }
        }

        private   void clearAroundRadius(Grid grid, Point currLocation) {

            for (GridCell cell : grid.getGrid()) {
                boolean inNeigh = true;
                for (Point p : currLocation.get8Neighbours())
                    if (p.equals(cell.getLocation()))
                        inNeigh = false;

                if (inNeigh) {
                    cell.setInRadius(false);
                    cell.setProbGoal(0);
                }
            }
        }

        private void updateRadiusProbabilities(Grid grid, Point currLocation){
            ArrayList<Point> bPoints = grid.getKnownBlockedNeighbors(currLocation);
            clearAroundRadius(grid,currLocation);
            ArrayList<Point> pointsToNormalize = new ArrayList<>();
            ArrayList<Point> pointsToSumm = new ArrayList<>();
            for (Point p : currLocation.get8Neighbours()) {
                if (grid.inBounds(p)) {
                    if (bPoints.contains(p)){
                        grid.getCell(p).setProbGoal(0);
                        continue;
                    }
                    if(grid.getCell(p).isInRadius()){
                        grid.getCell(p).setProbGoal(grid.getCell(p).getProbGoal() + (1d / (8 - bPoints.size())));
                        pointsToSumm.add(p);
                    }
                    else{
                        grid.getCell(p).setProbGoal((1d / (8 - bPoints.size())));
                        pointsToNormalize.add(p);
                    }
                }

            }
            grid.renormalizeEC(pointsToSumm,pointsToNormalize);


        }
}
