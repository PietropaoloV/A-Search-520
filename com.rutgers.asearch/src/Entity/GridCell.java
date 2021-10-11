package Entity;

import Utility.Point;
import Utility.Sentiment;

public class GridCell implements Cloneable {
    private final int x;
    private final int y;
    private Sentiment blockSentiment;
    private int numAdj; // N_x
    private int numSensedBlocked; // C_x
    private int numAdjBlocked; // B_x (confirmed blocked)
    private int numAdjEmpty; // E_x (confirmed empty)
    private int numAdjHidden; // H_x -> has no setter since it's determined by other fields
    private boolean isBlocked;
    private boolean isVisited;

    public GridCell(int x, int y, int numAdj, boolean isBlocked) {
        this.x = x;
        this.y = y;
        this.numAdj = numAdj;
        this.numAdjHidden = numAdj; // all cells start off hidden
        this.isBlocked = isBlocked;

        // set default values
        this.blockSentiment = Sentiment.Unsure; // all cells start off undetermined
        this.numSensedBlocked = 0;
        this.numAdjBlocked = 0;
        this.numAdjEmpty = 0;
        this.isVisited = false;
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

    public Sentiment getBlockSentiment() {
        return blockSentiment;
    }

    public void setBlockSentiment(Sentiment blockSentiment) {
        this.blockSentiment = blockSentiment;
    }

    public int getNumAdj() {
        return numAdj;
    }

    public int getNumSensedBlocked() {
        return numSensedBlocked;
    }

    public void addNumSensedBlocked(int numSensedBlocked) {
        this.numSensedBlocked += numSensedBlocked;
    }

    public int getNumAdjBlocked() {
        return numAdjBlocked;
    }

    public void addNumAdjBlocked(int numAdjBlocked) {
        this.numAdjBlocked += numAdjBlocked;
        this.numAdjHidden -= numAdjBlocked;
    }

    public int getNumAdjEmpty() {
        return numAdjEmpty;
    }

    public void addNumAdjEmpty(int numAdjEmpty) {
        this.numAdjEmpty += numAdjEmpty;
        this.numAdjHidden -= numAdjEmpty;
    }

    public int getNumAdjHidden() {
        return numAdjHidden;
    }

    public boolean isBlocked() {
        return isBlocked || blockSentiment == Sentiment.Blocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
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
