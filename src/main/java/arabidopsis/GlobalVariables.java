package arabidopsis;

import arabidopsis.models.Cell;
import arabidopsis.models.Gene;
import arabidopsis.models.State;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GlobalVariables
{

//    public static double[][] b = new double[][]{{0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
//            {-1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
//            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
//            {0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0},
//            {0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0},
//            {0,0,0,0,0,0,-1,0,0,0,0,0,0,0,0,0},
//            {0,0,0,0,0,0,0,-1,0,0,0,0,0,0,0,0},
//            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
//            {0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0},
//            {0,0,0,0,1,0,0,-1,0,0,0,0,0,0,0,0},
//            {0,0,0,0,0,1,0,0,1,0,0,0,-1,0,0,-1},
//            {0,0,0,0,1,0,0,0,1,0,-1,0,0,0,0,0},
//            {0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},
//            {0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},
//            {0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0},
//            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};

//    public static Gene[] globalGenes()
//    {
//        R = 200;
//        RmaxSepals = 180;
//        RminSepals = 150;
//        RminPetals = 120;
//        RminStamens = 90;
//        DSepals = 150;
//        DPetals = 120;
//        DStamens = 80;
//        d0Sepals = 10;
//        d0Petals = 10;
//        d0Stamens = 8;
//        String[] types = new String[]{"LHY/CCA1", "TOC1", "GI", "CO", "FT", "SOC1", "FLC", "AP1",
//                "LFY", "WUS", "CLV3", "AP2", "AP3", "PI", "AG", "SEP(1,2,3)"};
//        double a[] = new double[]{ 3.5, 2.8, 3.95, 2.75, 4.2, 4.9, 3.61, 5.2,
//                2.14, 2.96, 4.89, 5.05, 2.01, 3.6, 4.68, 3.0};
//
//        int[][] g = new int[types.length][types.length];
//        for (int i = 0; i < 16; i++)
//            for (int j = 0; j < 16; j++)
//                g[i][j] = 1;
//
//        double[][] K = new double[types.length][types.length];
//        for (int i = 0; i < 16; i++)
//            for (int j = 0; j < 16; j++)
//                K[i][j] = 1;
//        int[] N = new int[types.length];
//        for (int i = 0; i < 16; i++)
//            N[i] = 2;
//        double[] r = new double[types.length];
//        for (int i = 0; i < 16; i++)
//            r[i] = 0.1;
//        Gene genes[] = new Gene[a.length];
//        for (int i = 0; i < a.length; i++)
//            genes[i] = new Gene(types[i], a[i], b[i], g[i], K[i], N[i], r[i]);
//        return genes;
//    }
//    public static List<State> globalStates()
//    {
//        List<State> states = new ArrayList<>();
//        Map<String, Integer> proteins1 = new HashMap<>();
//        proteins1.put("LHY/CCA1", 150);
//        proteins1.put("TOC1", 100);
//        proteins1.put("GI", 100);
//        proteins1.put("CO", 150);
//        String name1 = "Stem Tissue";
//        String description1 = "Undifferentiated (immature) cells capable of transforming into any shape";
//        int[] geneMatr1 = new int[]{0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0};
//        Set<Integer> statesNS1 = new HashSet<>();
//        states.add(new State(1, proteins1, name1, description1, geneMatr1, Color.BLUE, statesNS1));
//
//        Map<String, int[]> proteins2 = new HashMap<>();
//        proteins2.put("LHY/CCA1", new int[]{150, 200});
//        proteins2.put("TOC1", new int[]{150, 200});
//        proteins2.put("GI", new int[]{150, 200});
//        proteins2.put("CO", new int[]{150, 200});
//        String name2 = "Central zone";
//        String description2 = "Central area of meristem";
//        int[] geneMatr2 = new int[]{0,1,1,0,0,0,0,1,0,0,1,0,0,0,0,0};
//        Set<Integer> statesNS2 = new HashSet<>();
//        states.add(new State(1, proteins1, name2, description2, geneMatr2, Color.RED, statesNS2));
//
//        return states;
//    }


}


