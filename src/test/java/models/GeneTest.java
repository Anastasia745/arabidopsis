package models;

import arabidopsis.models.Gene;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class GeneTest {

    public static final Gene geneTest1;
    public static final Gene geneTest2;
    public static final Gene geneTest3;

    static {

        String[] type = {"Gene 1", "Gene 2", "Gene 3"};

        double[] a = {5, 2.8, 3.1};

        double[][] b = {{0, 1, -1},
                        {0, 0, 1},
                        {-1, 1, 0}};

        int[][] g = {{1, 1, 1},
                     {1, 1, 1},
                     {1, 1, 1}};

        double[][] K = {{1, 1, 1},
                        {1, 1, 1},
                        {1, 1, 1}};

        int[] N = {2, 2, 2};

        double[] r = {0.1, 0.2, 0.15};

        try {
            geneTest1 = new Gene(type[0], a[0], b[0], g[0], K[0], N[0], r[0]);
            geneTest2 = new Gene(type[1], a[1], b[1], g[1], K[1], N[1], r[1]);
            geneTest3 = new Gene(type[2], a[2], b[2], g[2], K[2], N[2], r[2]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
