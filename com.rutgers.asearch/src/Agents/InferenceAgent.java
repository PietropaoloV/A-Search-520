package Agents;

import Entity.Grid;
import Utility.Point;

@FunctionalInterface
public interface InferenceAgent {
    void learn(Grid kb, Point location);
}
