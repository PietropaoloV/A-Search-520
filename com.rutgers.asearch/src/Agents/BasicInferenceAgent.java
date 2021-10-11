package Agents;

import Entity.Grid;
import Entity.GridCell;
import Utility.Point;
import Utility.Sentiment;

public class BasicInferenceAgent implements InferenceAgent {
    @Override
    public void learn(Grid kb, Point location) {
        // the possible cells with updated sentiments are the current location and its nbrs;
        // need to run propagateInferences on all of them
        propagateInferences(kb, location);
        kb.forEachNeighbour(location, nbr -> propagateInferences(kb, nbr.getLocation()));
    }

    // backup of inefficient version to verify that behaviour is the same
    public static void naiveLearn(Grid kb, Point location) {
        boolean done = false;
        while (!done) { // keep iterating until no more updates are made
            done = true;
            for (int y = 0; y < kb.getYSize(); y++) {
                for (int x = 0; x < kb.getXSize(); x++) {
                    GridCell cell = kb.getCell(x, y);
                    if (cell.getNumAdjHidden() == 0 || !cell.isVisited()) {
                        continue; // nothing can be inferred from this cell
                    }

                    // check each condition
                    if (cell.getNumAdjBlocked() == cell.getNumSensedBlocked()) { // C_x = B_x
                        done = false;
                        kb.forEachNeighbour(cell.getLocation(), nbr -> {
                            if (nbr.getBlockSentiment() == Sentiment.Unsure) {
                                kb.setSentiment(nbr.getLocation(), Sentiment.Free);
                            }
                        });
                    } else if (cell.getNumAdjEmpty() == cell.getNumSensedEmpty()) { // N_x - C_x = E_x
                        done = false;
                        kb.forEachNeighbour(cell.getLocation(), nbr -> {
                            if (nbr.getBlockSentiment() == Sentiment.Unsure) {
                                kb.setSentiment(nbr.getLocation(), Sentiment.Blocked);
                            }
                        });
                    }
                }
            }
        }
    }
}
