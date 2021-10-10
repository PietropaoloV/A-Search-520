public class BasicInferenceAgent {
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
                        kb.forEachNeighbour(new Point(x, y), nbr -> {
                            if (nbr.getBlockSentiment() == Sentiment.Unsure) {
                                kb.setSentiment(new Point(nbr.getX(), nbr.getY()), Sentiment.Free);
                            }
                        });
                    } else if (cell.getNumAdjEmpty() == cell.getNumAdj() - cell.getNumSensedBlocked()) { // N_x - C_x = E_x
                        done = false;
                        kb.forEachNeighbour(new Point(x, y), nbr -> {
                            if (nbr.getBlockSentiment() == Sentiment.Unsure) {
                                kb.setSentiment(new Point(nbr.getX(), nbr.getY()), Sentiment.Blocked);
                            }
                        });
                    }
                }
            }
        }
    }
}
