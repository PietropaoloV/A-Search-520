package Entity;

/**
 * POD class for holding statistics from running tests.
 */
public class GridWorldInfo {
    public double probability;
    public int numStepsTaken = 0;
    public int numExaminations = 0;
    public int numberOfCellsProcessed = 0;
    public int numBumps = 0;
    public int numPlans = 0;
    public double runtime = 0;

    public GridWorldInfo() {
        // set default values for other data
        this.probability = 0.30;
    }
}
