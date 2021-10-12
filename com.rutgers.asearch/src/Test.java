import Agents.*;
import Algorithms.*;
import Entity.*;
import Utility.*;

// just tests the search algos to make sure they work
// usage: java Test xSize ySize blockedProbability%
public class Test {
    public static void printResults(GridWorldInfo result) {
        System.out.println("num cells expanded: " + result.numberOfCellsProcessed);
        System.out.println("trajectory length: " + result.trajectoryLength);
        System.out.println("number of bumps: " + result.numBumps);
        System.out.println("number of (re)-plans: " + result.numPlans);
        System.out.println("number of cells determined: " + result.numCellsDetermined);
        System.out.println("runtime: " + result.runtime + "s");
    }

    public static void printWorld(Grid world) {
        for (int j = 0; j < world.getYSize(); ++j) {
            for (int i = 0; i < world.getXSize(); ++i) {
                GridCell cell = world.getCell(i, j);
                String symbol = "o"; // default symbol
                if (cell.isVisited())
                    symbol = ColorConstant.ANSI_CYAN + "-" + ColorConstant.ANSI_RESET;
                else if (cell.getBlockSentiment() == Sentiment.Free)
                    symbol = ColorConstant.ANSI_GREEN + "o" + ColorConstant.ANSI_RESET;
                else if (cell.getBlockSentiment() == Sentiment.Blocked)
                    symbol = ColorConstant.ANSI_YELLOW + "x" + ColorConstant.ANSI_RESET;
                else if (cell.isBlocked())
                    symbol = ColorConstant.ANSI_RED + "x" + ColorConstant.ANSI_RESET;
                System.out.print(symbol);
            }
            System.out.print('\n');
        }
    }

    public static void printMineSweeper(Grid world) {
        for (int j = 0; j < world.getYSize(); j++) {
            for (int i = 0; i < world.getXSize(); i++) {
                GridCell cell = world.getCell(i, j);
                System.out.print(cell.isBlocked() ? ColorConstant.ANSI_RED : ColorConstant.ANSI_RESET); // set color
                System.out.print(cell.getNumSensedBlocked());
            }
            System.out.print('\n');
        }
        System.out.print(ColorConstant.ANSI_RESET);
    }

    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        int prob = Integer.parseInt(args[2]);
        Grid world = new Grid(x, y, prob);
        Grid world2 = new Grid(world, true);
        Point start = new Point(0, 0);
        Point goal = new Point(x - 1, y - 1);

        SearchAlgo algo = new AStarSearch(Heuristics::manhattanDistance);
        InferenceAgent agent = new BasicInferenceAgent();
        InferenceAgent betterAgent = new BetterInferenceAgent();

        System.out.println("Example version:");
        Robot robot = new Robot(start, goal, agent, world, algo);
        GridWorldInfo result = robot.run();
        printResults(result);
        printWorld(world);
        System.out.println();

        System.out.println("'Better' version:");
        Robot robot2 = new Robot(start, goal, betterAgent, world2, algo);
        GridWorldInfo result2 = robot2.run();
        printResults(result2);
        printWorld(world2);
    }
}
