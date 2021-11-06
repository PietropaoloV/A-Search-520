import Agents.*;
import Algorithms.*;
import Entity.*;
import Utility.*;

import java.text.DecimalFormat;

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


    public static void main(String[] args) {

    }
}
