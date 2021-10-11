package Agents;

import java.util.ArrayDeque;
import java.util.Queue;

import Entity.Grid;
import Entity.GridCell;
import Utility.Point;
import Utility.Sentiment;

public class BetterInferenceAgent implements InferenceAgent {
    BasicInferenceAgent deterministicAgent;

    public BetterInferenceAgent() {
        deterministicAgent = new BasicInferenceAgent();
    }

    @Override
    public void learn(Grid kb, Point location) {
        // first make all the deterministic inferences possible
        deterministicAgent.learn(kb, location);

        // do contradicting testing to find further inferences
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
                    boolean status = propagateInferences(copy, adj);
                    if (status == false) { // contradiction found -> nbr is empty
                        done = false;
                        kb.setSentiment(adj, Sentiment.Free);
                        continue;
                    }

                    // test if setting nbr -> empty yields a contradiction
                    copy = new Grid(kb, false);
                    copy.setSentiment(adj, Sentiment.Free);
                    status = propagateInferences(copy, adj);
                    if (status == false) { // contradiction found -> nbr is blocked
                        done = false;
                        kb.setSentiment(adj, Sentiment.Blocked);
                        continue;
                    }

                    // no contradictions found; test inconclusive for this nbr
                }
            }
        }
    }
}