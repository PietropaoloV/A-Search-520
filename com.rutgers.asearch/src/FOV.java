// these are the agents from project 1
public class FOV {
    public static void blindfolded(Grid kb, Point location) {
        // do nothing
    }

    public static void canSeeSideways(Grid kb, Point location) {
        for (Point adj : location.get4Neighbours()) {
            GridCell neighbour = kb.getCell(adj);
            if (neighbour == null) continue;
            if (neighbour.isBlocked()) {
                // kb.setSentiment(adj, Sentiment.Blocked);
                neighbour.setBlockSentiment(Sentiment.Blocked);
            } else {
                // kb.setSentiment(adj, Sentiment.Free);
                neighbour.setBlockSentiment(Sentiment.Free);
            }
        }
    }
}