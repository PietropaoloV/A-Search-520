package Agents;

import Entity.Grid;
import Entity.GridCell;
import Utility.Point;
import Utility.Sentiment;

public class FourNeighbourAgent implements InferenceAgent {
    @Override
    public void learn(Grid kb, Point location) {
        for (Point adj : location.get4Neighbours()) {
            GridCell neighbour = kb.getCell(adj);
            if (neighbour == null) continue;
            if (neighbour.isBlocked()) {
                // kb.setSentiment(adj, Utility.Sentiment.Blocked);
                neighbour.setBlockSentiment(Sentiment.Blocked);
            } else {
                // kb.setSentiment(adj, Utility.Sentiment.Free);
                neighbour.setBlockSentiment(Sentiment.Free);
            }
        }
    }

}