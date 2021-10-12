package Agents;

import Entity.Grid;
import Entity.GridCell;
import Utility.Point;
import Utility.Sentiment;

@FunctionalInterface
public interface InferenceAgent {
    /**
     * Attempt to learn about the environment based on one's existing knowledge and
     * current location
     * 
     * @param kb       The knowledge base (will be updated with what was learned)
     * @param location The agent's current location
     */
    void learn(Grid kb, Point location);

    /**
     * Propagates inferences using rules of the Example Inference Agent, while
     * checking for contradictions.
     * 
     * @param kb       The knowledge base (will be mutated by this function)
     * @param location The location where a sentiment was changed
     * @return Returns false if an inconsistency was found, true otherwise
     */
    default boolean propagateInferences(Grid kb, Point location) {
        // each neighbour's information has updated, check those
        for (Point adj : location.get8Neighbours()) {
            GridCell cell = kb.getCell(adj);

            // check to make sure robot has visited cell (and hence sensed its surroundings)
            if (cell != null && cell.isVisited()) {
                // first check for contradictions
                if (cell.getNumAdjBlocked() > cell.getNumSensedBlocked()
                        || cell.getNumAdjEmpty() > cell.getNumSensedEmpty()) {
                    return false;
                }

                // shortcut check to see if nothing can be inferred
                if (cell.getNumAdjHidden() == 0) {
                    continue;
                }

                // check each condition
                if (cell.getNumAdjBlocked() == cell.getNumSensedBlocked()) { // C_x = B_x
                    for (Point p : adj.get8Neighbours()) {
                        GridCell nbr = kb.getCell(p);
                        if (nbr != null && nbr.getBlockSentiment() == Sentiment.Unsure) {
                            kb.setSentiment(p, Sentiment.Free);
                            boolean status = propagateInferences(kb, p); // continuing propagating changes
                            if (status == false)
                                return false; // contradiction from deeper level detected
                        }
                    }
                } else if (cell.getNumAdjEmpty() == cell.getNumSensedEmpty()) { // N_x - C_x = E_x
                    for (Point p : adj.get8Neighbours()) {
                        GridCell nbr = kb.getCell(p);
                        if (nbr != null && nbr.getBlockSentiment() == Sentiment.Unsure) {
                            kb.setSentiment(p, Sentiment.Blocked);
                            boolean status = propagateInferences(kb, p); // continuing propagating changes
                            if (status == false)
                                return false; // contradiction from deeper level detected
                        }
                    }
                }
            }
        }

        return true; // no inconsistencies detected
    }

}
