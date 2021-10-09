import java.util.List;

public class GridWorldInfo {
    public double probability;
    public double trajectoryLength;
    public int numberOfCellsProcessed;
    public int numBumps;
    public int numPlans;
    public int numCellsDetermined;
    public long runtime;

    List<Point> path; // path does not include start cell

    public GridWorldInfo(double trajectoryLength, int numberOfCellsProcessed, List<Point> path) {
        this.trajectoryLength = trajectoryLength;
        this.numberOfCellsProcessed = numberOfCellsProcessed;
        this.path = path;

        // set default values for other data
        this.probability = 0;
        this.numBumps = 0;
        this.numPlans = 0;
        this.numCellsDetermined = 0;
        this.runtime = 0;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public double getTrajectoryLength() {
        return trajectoryLength;
    }

    public void setTrajectoryLength(double trajectoryLength) {
        this.trajectoryLength = trajectoryLength;
    }

    public int getNumberOfCellsProcessed() {
        return numberOfCellsProcessed;
    }

    public void setNumberOfCellsProcessed(int numberOfCellsProcessed) {
        this.numberOfCellsProcessed = numberOfCellsProcessed;
    }

    public int getNumBumps() {
        return numBumps;
    }

    public void setNumBumps(int numBumps) {
        this.numBumps = numBumps;
    }

    public int getNumPlans() {
        return numPlans;
    }

    public void setNumPlans(int numPlans) {
        this.numPlans = numPlans;
    }

    public int getNumCellsDetermined() {
        return numCellsDetermined;
    }

    public void setNumCellsDetermined(int numCellsDetermined) {
        this.numCellsDetermined = numCellsDetermined;
    }

    public long getRuntime() {
        return runtime;
    }

    public void setRuntime(long runtime) {
        this.runtime = runtime;
    }

    public List<Point> getPath() {
        return path;
    }

    public void setPath(List<Point> path) {
        this.path = path;
    }
}
