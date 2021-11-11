import Algorithms.*;
import Entity.*;
import Utility.*;
// import Agents.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
// import java.util.function.BiFunction;
// import java.util.function.Predicate;


public class Main {
    /**
     * Main Execution Method
     *
     * @param args Input arguments X - Grid X size, Y - Entity.Grid Y size,
     *             Iterations - Number of Iterations (defaults to 100)
     */
    public static void main(String[] args) {
    }

    /**
     * This method returns a solvable maze for given inputs. It uses a search algo
     * to make sure there is a path to the end.
     *
     * @param xDim Dimension of the Entity.Grid's X coord
     * @param yDim Dimension of the Entity.Grid's Y coord
     * @param algo Algorithm to use for searching
     * @param prob Probability of a space being blocked
     * @return returns a solvable maze
     */
    public static Grid getSolvableMaze(int xDim, int yDim, SearchAlgo algo, int prob) {
        Point start = new Point(0, 0);
        Tuple<List<Point>, Integer> completeResult;
        Grid grid;

        do {
            grid = new Grid(xDim, yDim);
            completeResult = algo.search(start, grid.getGoal(), grid, cell -> cell.isBlocked());
        } while (completeResult.f1 == null);

        return grid;
    }

    /**
     * Takes a list of Entity.GridWorldInfo: {@link GridWorldInfo} and prints it to
     * a pre-designed csv template
     *
     * @param fileName      name of the file
     * @param gridWorldInfo List of GridWorldInfos
     */
    public static void printResultsToCsv(String fileName, List<GridWorldInfo> gridWorldInfo) {
        try (PrintWriter writer = new PrintWriter(new File(fileName))) {

            StringBuilder sb = new StringBuilder();
            sb.append("probability");
            sb.append(',');
            sb.append("num_steps_taken");
            sb.append(',');
            sb.append("num_examinations");
            sb.append(',');
            sb.append("num_cells_processed");
            sb.append(',');
            sb.append("num_bumps");
            sb.append(',');
            sb.append("num_plans");
            sb.append(',');
            sb.append("runtime");
            sb.append('\n');
            writer.write(sb.toString());

            for (GridWorldInfo info : gridWorldInfo) {
                sb = new StringBuilder();
                sb.append(info.probability);
                sb.append(',');
                sb.append(info.numStepsTaken);
                sb.append(',');
                sb.append(info.numExaminations);
                sb.append(',');
                sb.append(info.numberOfCellsProcessed);
                sb.append(',');
                sb.append(info.numBumps);
                sb.append(',');
                sb.append(info.numPlans);
                sb.append(',');
                sb.append(info.runtime);
                sb.append('\n');
                writer.write(sb.toString());
            }

            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}
