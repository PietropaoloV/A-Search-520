package Agents;

import java.util.Queue;
import java.util.ArrayDeque;

import Entity.*;
import Utility.*;

public class PerfectInferenceAgent implements InferenceAgent {
    private BasicInferenceAgent deterministicAgent;
    private int depth;

    
    /**
     * Default constructor: initializes agent to explore to infinite depth
     */
    public PerfectInferenceAgent() {
        deterministicAgent = new BasicInferenceAgent();
        depth = -1;
    }

    /**
     * Initializes agent to explore only 'depth' levels deep
     * 
     * @param depth
     */
    public PerfectInferenceAgent(int depth) {
        deterministicAgent = new BasicInferenceAgent();
        this.depth = depth;
    }



    @Override
    public void learn(Grid kb, Point location) {
        // first make all the deterministic inferences possible
        deterministicAgent.learn(kb, location);

        // do satisfiability testing to find further inferences
        Queue<GridCell> openCells = new ArrayDeque<>(); // queue to hold relevant cells
        boolean done = false;
        while (!done) {
            done = true;

            // get collection of relevant cells to test
            for (int y = 0; y < kb.getYSize(); y++) {
                for (int x = 0; x < kb.getXSize(); x++) {
                    GridCell cell = kb.getCell(x, y);
                    if (cell.isVisited() && cell.getNumAdjHidden() > 0)
                        openCells.add(cell);
                }
            }

            // perform contradiction testing on neighbours of each open cell
            while (!openCells.isEmpty()) {
                GridCell cell = openCells.poll();
                for (Point adj : cell.getLocation().get8Neighbours()) {
                    GridCell nbr = kb.getCell(adj);
                    if (nbr == null || nbr.getBlockSentiment() != Sentiment.Unsure) {
                        continue;
                    }

                    // test if setting nbr -> blocked yields a contradiction
                    Grid copy = new Grid(kb, false); // make shallow copy so original grid isn't mangled up
                    copy.setSentiment(adj, Sentiment.Blocked);
                    boolean status = totalSolve(copy, depth);
                    if (status == false) { // contradiction found -> nbr is empty
                        done = false;
                        kb.setSentiment(adj, Sentiment.Free);
                        propagateInferences(kb, adj);
                        continue;
                    }

                    // test if setting nbr -> empty yields a contradiction
                    copy = new Grid(kb, false);
                    copy.setSentiment(adj, Sentiment.Free);
                    status = totalSolve(copy, depth);
                    if (status == false) { // contradiction found -> nbr is blocked
                        done = false;
                        kb.setSentiment(adj, Sentiment.Blocked);
                        propagateInferences(kb, adj);
                        continue;
                    }

                    // no contradictions found; test inconclusive for this nbr
                }
            }
        }

    }

    /**
     * Attempts to find a partial assignment satisfying the constraints.
     * Updates the original board with cell statuses that are inferred along the way.
     * Do not rely on the state of kb after calling this function.
     * 
     * @param kb The current state of the knowledge base
     * @return If the current KB is consistent or not
     */
    public boolean totalSolve(Grid kb, int depth) {
        // check if max allowed depth has been reached
        if(depth == 0) {
            return true;
        }

        // find a relevant cell to test
        GridCell openCell = null;
        for (int y = 0; y < kb.getYSize(); y++) {
            for (int x = 0; x < kb.getXSize(); x++) {
                GridCell cell = kb.getCell(x, y);
                if (cell.isVisited() && cell.getNumAdjHidden() > 0)
                    openCell = cell;
            }
        }

        if(openCell == null) {
            return true; // no open cells -> partial assignment already found
        }

        GridCell nbr = null;
        for(Point adj : openCell.getLocation().get8Neighbours()) {
            nbr = kb.getCell(adj);
            if (nbr != null && nbr.getBlockSentiment() == Sentiment.Unsure) {
                break;
            }
        }
        Point adj = nbr.getLocation();

        // attempt to solve setting nbr -> blocked
        Grid copy = new Grid(kb, false);
        copy.setSentiment(adj, Sentiment.Blocked);
        boolean status = propagateInferences(copy, adj) && totalSolve(copy, depth-1); // use short-circuiting
        if(status == true) { // found a valid partial assignment
            return true;
        }

        // attempt to solve setting nbr -> free
        kb.setSentiment(adj, Sentiment.Free);
        status = propagateInferences(kb, adj) && totalSolve(kb, depth-1); // use short-circuiting
        if(status == true) { // found a valid partial assignment
            return true;
        }

        return false; // kb is not satisfiable
    }

    // class GridCellComparator implements Comparator<GridCell> {
    //     @Override
    //     public int compare(GridCell g1, GridCell g2) {

    //     }
    // }
    
}
