import Entity.*;
import Utility.ColorConstant;
import Utility.NoPathToGoalException;
import Utility.Point;

import javax.naming.TimeLimitExceededException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Simulator {
    private static final double NANO_SECONDS = 1000000000d;
    public static int STEPS_TO_FIND = 10000000;
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

    public static GridWorldInfo runSimulator(int xSize, int ySize) throws TimeLimitExceededException, NoPathToGoalException {
        Grid grid = new Grid(xSize, ySize, true);
        RobotHunter rh = new RobotHunter(new Point((int) (Math.random() * xSize), (int) (Math.random() * ySize)));
        RobotPrey rp = new RobotPrey(new Point((int) (Math.random() * xSize), (int) (Math.random() * ySize)));
        grid.initProbs(0);
        GridWorldInfo gridWorldInfo = new GridWorldInfo();
        int counter = 0;
        long start = System.nanoTime();
        while (!rh.isGoalFound()) {
            if (counter == STEPS_TO_FIND) throw new TimeLimitExceededException();
            // printWorld(grid,rh,rp);
                rh.setSensed(grid.senseTarget(rh.getCurrLoc(), rp.getCurrLoc()));
                Decision tDec = rp.getDecision(grid);
                Decision hDec = rh.getDecision(grid);
                ExecuteDecision(hDec, rh, rp.getLocation(), grid, gridWorldInfo);
                ExecuteDecision(tDec, rp, grid);




            counter++;
            //waitAndClear();

        }
        long end = System.nanoTime();
        gridWorldInfo.numStepsTaken = counter;
        gridWorldInfo.numberOfCellsProcessed = counter;
        gridWorldInfo.runtime = (end - start)/NANO_SECONDS;
        gridWorldInfo.probability = 30;
        gridWorldInfo.numPlans = 0;
        return gridWorldInfo;
    }

    public static void main(String args[]) throws IOException, NoPathToGoalException, TimeLimitExceededException {
        ArrayList<GridWorldInfo> list = new ArrayList<>();
        for (int i = 0; i<1000; i++){
            try {
                list.add(runSimulator(101,101));

            }catch (NoPathToGoalException e){
                continue;
            }catch(  TimeLimitExceededException t) {
                continue;
            }
        }
        printResultsToCsv("Agent9",list);
    }


    public static void waitAndClear() throws IOException {
        System.in.read();
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printWorld(Grid world, RobotHunter hunter, RobotPrey prey) {
        for (int j = 0; j < world.getYSize(); ++j) {
            for (int i = 0; i < world.getXSize(); ++i) {
                GridCell cell = world.getCell(i, j);
                String symbol = generateColorGradient(cell.getProbGoal(), 'o'); // default symbol
                if (cell.getLocation().equals(hunter.getLocation()))
                    symbol = ColorConstant.ANSI_RED+ "H" + ColorConstant.ANSI_RESET;
                else if (cell.getLocation().equals(prey.getLocation()))
                    symbol = ColorConstant.ANSI_YELLOW + "P" + ColorConstant.ANSI_RESET;
                else if (cell.isBlocked() && cell.isBumped())
                    symbol = generateColorGradient(cell.getProbGoal(), 'x');
                else if (cell.isVisited())
                    symbol = generateColorGradient(cell.getProbGoal(), '-');
                else if (cell.isBlocked())
                    symbol = generateColorGradient(cell.getProbGoal(), 'b');


                System.out.print(symbol);
            }
            System.out.print('\n');
        }
    }
    public static String generateColorGradient(double prob, char g){
        prob*=100;
        if(prob <= 0)
            return   ColorConstant.ANSI_BLACK + g + ColorConstant.ANSI_RESET;
        if(prob <= 10)
            return   ColorConstant.ANSI_GREEN + g + ColorConstant.ANSI_RESET;
        if(prob <= 25)
            return   ColorConstant.ANSI_YELLOW + g + ColorConstant.ANSI_RESET;
        if(prob <= 30)
            return   ColorConstant.ANSI_BLUE + g + ColorConstant.ANSI_RESET;
        if(prob <= 100)
            return  ColorConstant.ANSI_RED + g + ColorConstant.ANSI_RESET;
        return "";
    }

    private static int ExecuteDecision(Decision decision, RobotPrey robot,Grid grid) {
        Point currLocation = robot.getLocation();
        Point newLocation = null;
        switch (decision) {
            case Up: {
                newLocation = currLocation.get4Neighbours()[2];
                break;
            }
            case Down: {
                newLocation = currLocation.get4Neighbours()[3];
                break;
            }
            case Left: {
                newLocation = currLocation.get4Neighbours()[1];
                break;
            }
            case Right: {
                newLocation = currLocation.get4Neighbours()[0];
                break;
            }
        }
        if(newLocation != null) {
            if (grid.inBounds(newLocation)) {
                if (grid.getCell(newLocation).isBlocked()) {

                    return -1;
                }
                robot.setLocation(newLocation);
            }
        }
        return 0;
    }


    public static int ExecuteDecision(Decision decision, RobotHunter robot,Point goal, Grid grid, GridWorldInfo gridWorldInfo) {
        Point currLocation = robot.getLocation();
        Point newLocation = null;
        switch (decision){
            case Up:{
                newLocation = currLocation.get4Neighbours()[2];
                break;
            }
            case Down:{
               newLocation = currLocation.get4Neighbours()[3];
                break;
            }
            case Left:{
                 newLocation = currLocation.get4Neighbours()[1];
                break;
            }
            case Right:{
                newLocation = currLocation.get4Neighbours()[0];
                break;}

            case NOOP:
            default:
        }

        if(newLocation != null) {
            if (grid.inBounds(newLocation)) {
                if (grid.getCell(newLocation).isBlocked()) {
                    grid.getCell(newLocation).setBumped(true);
                    robot.setNumBlocked(robot.getNumBlocked()+1);
                    grid.initProbs(robot.getNumBlocked());
                    gridWorldInfo.numBumps++;
                    return -1;
                }
                if(robot.isShouldExamine()){
                    gridWorldInfo.numExaminations++;
                        if(newLocation.equals(goal)){
                            robot.setFoundGoal(true);
                        }else{
                            grid.getCell(currLocation).setProbGoal(0);
                        }
                }

                robot.setLocation(newLocation);
                grid.getCell(newLocation).setVisited(true);
            }
        }
        return 0;
    }
}
