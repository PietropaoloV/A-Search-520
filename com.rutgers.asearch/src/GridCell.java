public class GridCell {
    private int x;
    private int y;
    private Sentiment blockSentiment;
    private int numAdj;
    private int numSensedBlocked;
    private int numAdjBlocked;
    private int numEmpty;
    private int numAdjHidden;
    private boolean isBlocked;
    private boolean isVisited;

    public GridCell(int x, int y, boolean isBlocked) {
        this.x = x;
        this.y = y;
        this.isBlocked = isBlocked;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public void setNumAdj(int numAdj) {
        this.numAdj = numAdj;
    }

    public int getNumSensedBlocked() {
        return numSensedBlocked;
    }

    public void setNumSensedBlocked(int numSensedBlocked) {
        this.numSensedBlocked = numSensedBlocked;
    }

    public int getNumAdjBlocked() {
        return numAdjBlocked;
    }

    public void setNumAdjBlocked(int numAdjBlocked) {
        this.numAdjBlocked = numAdjBlocked;
    }

    public int getNumEmpty() {
        return numEmpty;
    }

    public void setNumEmpty(int numEmpty) {
        this.numEmpty = numEmpty;
    }

    public int getNumAdjHidden() {
        return numAdjHidden;
    }

    public void setNumAdjHidden(int numAdjHidden) {
        this.numAdjHidden = numAdjHidden;
    }

    public boolean isBlocked() {
        return isBlocked;
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
}
