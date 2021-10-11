package Agents;

import Entity.Grid;
import Utility.Point;
import Utility.Sentiment;

public class BetterInferenceAgent implements InferenceAgent{
    public BasicInferenceAgent basicInferenceAgent;

    public BetterInferenceAgent() {
        this.basicInferenceAgent = new BasicInferenceAgent();
    }

    @Override
    public void learn(Grid kb, Point location) {
        // first make all the deterministic inferences possible
        basicInferenceAgent.learn(kb, location);

        // do contradicting testing to find further inferences
        boolean done = false;
        while(!done) {
            done = true;
            for (int y = 0; y < kb.getYSize(); y++) {
                for (int x = 0; x < kb.getXSize(); x++) {
                    Point coord = new Point(x, y);
                    if(kb.getCell(coord).getBlockSentiment() != Sentiment.Unsure) {
                        continue;
                    }

                    // check if setting (x, y) -> blocked yields a contradiction
                    Grid copy = new Grid(kb);
                    copy.setSentiment(coord, Sentiment.Blocked);
                    boolean status = basicInferenceAgent.propagateInferences(copy);
                    if(status == false) { // contradiction found, must be free
                        kb.setSentiment(coord, Sentiment.Free);
                        done = false;
                        continue;
                    }

                    // check if setting (x, y) -> free yields a contradiction
                    copy = new Grid(kb);
                    copy.setSentiment(coord, Sentiment.Free);
                    status = basicInferenceAgent.propagateInferences(copy);
                    if(status == false) { // contradiction found, must be blocked
                        kb.setSentiment(coord, Sentiment.Blocked);
                        done = false;
                        continue;
                    }
                }
            }
        }
    }
}