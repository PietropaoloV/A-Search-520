package Entity;

import Utility.Point;
import Utility.Sentiment;
import Utility.Terrain;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Consolidates all the information associated with a cell in the gridworld.
 */
public class GridCell implements Cloneable {
    private final int x;
    private final int y;
    private Sentiment blockSentiment;
    private Terrain terrain;
    private boolean isGoal;
    private boolean isVisited;
    private Grid owner; // used for lazy copying
    private double probBlocked = 0;

    public GridCell(int x, int y, boolean isGoal,  Grid owner) {
        this.x = x;
        this.y = y;
        this.owner = owner;
        this.isGoal = isGoal;
        this.blockSentiment = Sentiment.Unsure; // all cells start off undetermined
        this.isVisited = false;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public boolean isGoal() {
        return isGoal;
    }

    public void setGoal(boolean goal) {
        isGoal = goal;
    }

    public double getProbBlocked() {
      return this.probBlocked;
    }

    public void setProbBlocked(double probBlocked) {
        this.probBlocked = probBlocked;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point getLocation() {
        return new Point(x, y);
    }

    public boolean isBlocked(){
        return terrain.equals(Terrain.Blocked);
    }

    public Sentiment getBlockSentiment() {
        return blockSentiment;
    }

    public void setBlockSentiment(Sentiment blockSentiment) {
        this.blockSentiment = blockSentiment;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    protected Grid getOwner() {
        return owner;
    }

    protected void setOwner(Grid owner) {
        this.owner = owner;
    }

    // TODO: better way to do this?
    @Override
    public GridCell clone() {
        GridCell copy = null;
        try {
            copy = (GridCell) super.clone();
        } catch (CloneNotSupportedException e) {
            // this should never be entered
            System.err.println(e.toString());
        }
        return copy;
    }
}
