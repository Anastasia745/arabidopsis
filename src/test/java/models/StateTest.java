package models;

import arabidopsis.models.State;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static models.GeneTest.*;

public class StateTest {

    public static final State stateTest1;
    public static final State stateTest2;

    static {

        int[] id = {1, 2};
        String[] name = {"State 1", "State 2"};
        String[] description = {"Description for state 1", "Description for state 2"};

        Map proteins1 = new HashMap<>();
        proteins1.put(geneTest1.getType(),100);
        proteins1.put(geneTest2.getType(), 150);

        Map proteins2 = new HashMap<>();
        proteins2.put(geneTest2.getType(),200);
        proteins2.put(geneTest3.getType(), 250);

        double[][] influence = {{0, 0.1, 0.2}, {0, 0.01, 0}};
        Color[] color = {Color.BLUE, Color.RED};

        Set<Integer> statesNS1 = new HashSet<>();
        Set<Integer> statesNS2 = new HashSet<>();
        statesNS2.add(1);

        stateTest1 = new State(id[0], proteins1, name[0], description[0], influence[0], color[0], statesNS1);
        stateTest2 = new State(id[1], proteins2, name[1], description[1], influence[1], color[1], statesNS2);
    }
}
