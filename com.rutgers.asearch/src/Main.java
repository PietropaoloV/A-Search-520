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
     * @param args Input arguments X - Gird X size, Y - Grid Y size, Iterations - Number of Iterations (defaults to 100)
     */
    // Input args: x, y, number of iterations
    public static void main(String[] args) {
        // int x = Integer.parseInt(args[0]);
        // int y = Integer.parseInt(args[1]);
        // int iterations = args.length > 2 ? Integer.parseInt(args[2]): 100;
    }

    /**
     * This method returns a solvable maze for given inputs. It uses a search algo to make sure there is a path to the
     * end.
     *
     * @param xDim Dimension of the Grid's X coord
     * @param yDim Dimension of the Grid's Y coord
     * @param algo Algorithm to use for searching
     * @param prob Probability of a space being blocked
     * @return returns a solvable maze
     */
    public static Grid getSolvableMaze(int xDim, int yDim, SearchAlgo algo, int prob){
        Point start = new Point(0, 0);
        Point end = new Point(xDim-1, yDim-1);
        GridWorldInfo completeResult;
        Grid grid;

        do{
            grid = new Grid(xDim, yDim, prob);
            completeResult = algo.search(start, end, grid, cell -> cell.isBlocked());
        } while (completeResult.path == null);

        return grid;
    }

    /**
     * Takes a list of GridWorldInfo: {@link GridWorldInfo} and prints it to a pre-designed csv template
     *
     * @param fileName name of the file
     * @param gridWorldInfo List of GridWorldInfos
     */
    public static void printResultsToCsv(String fileName, List<GridWorldInfo> gridWorldInfo){
        try (PrintWriter writer = new PrintWriter(new File(fileName))) {

            StringBuilder sb = new StringBuilder();
            sb.append("Probability");
            sb.append(',');
            sb.append("Solvable");
            sb.append(',');
            sb.append("Runtime");
            sb.append(',');
            sb.append("Path Length (Repeated A*");
            sb.append(',');
            sb.append("Path Length (Discovered)");
            sb.append(',');
            sb.append("Path Length (Complete)");
            sb.append(',');
            sb.append("Number of Cells Processed");
            sb.append(',');
            sb.append("Runtime Weight (EC)");
            sb.append(',');
            sb.append("Backtrack Steps (EC)");
            sb.append('\n');
            writer.write(sb.toString());

            for (GridWorldInfo info: gridWorldInfo) {
                sb = new StringBuilder();
                sb.append(info.probability);
                sb.append(',');
                sb.append(!Double.isNaN(info.trajectoryLength));
                sb.append(',');
                sb.append(info.runtime);
                sb.append(',');
                sb.append(info.trajectoryLength);
                sb.append(',');
                sb.append(info.numberOfCellsProcessed);
                sb.append('\n');
                writer.write(sb.toString());
            }

            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }


}
