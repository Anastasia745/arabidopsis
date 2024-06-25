package models;

import arabidopsis.db.GlobalCells;
import arabidopsis.db.GlobalGenes;
import arabidopsis.db.GlobalStates;
import arabidopsis.models.Cell;
import arabidopsis.models.Protein;
import javafx.scene.shape.Circle;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static models.ProteinTest.*;
import static models.StateTest.stateTest1;
import static models.StateTest.stateTest2;

public class CellTest {

    public static final Cell cellTest1;
    public static final Cell cellTest2;
    public static final Cell cellTest3;
    public static final Cell cellTest4;
    public static final Cell cellTest5;
    public static final Cell cellTest6;
    public static final Cell cellTest7;

    static {
        int[] id = {1, 2, 3, 4, 5, 6, 7};
        int[] x = {35, 20, 20, 60, 30, 50, 0};
        int[] y = {5, 40, 20, 40, 65, 65, 65};
        int[] w = {25, 15, 15, 30, 20, 30, 30};
        int[] h = {35, 20, 20, 20, 25, 25, 25};
        Map<String, List<Integer>> ns1 = makeNs(new Integer[]{2, 3}, new Integer[]{4}, new Integer[]{5, 6}, new Integer[]{});
        Map<String, List<Integer>> ns2 = makeNs(new Integer[]{}, new Integer[]{1}, new Integer[]{5, 7}, new Integer[]{3});
        Map<String, List<Integer>> ns3 = makeNs(new Integer[]{}, new Integer[]{1}, new Integer[]{2}, new Integer[]{});
        Map<String, List<Integer>> ns4 = makeNs(new Integer[]{1}, new Integer[]{}, new Integer[]{6}, new Integer[]{});
        Map<String, List<Integer>> ns5 = makeNs(new Integer[]{7}, new Integer[]{6}, new Integer[]{}, new Integer[]{1, 2});
        Map<String, List<Integer>> ns6 = makeNs(new Integer[]{5}, new Integer[]{}, new Integer[]{6}, new Integer[]{});
        Map<String, List<Integer>> ns7 = makeNs(new Integer[]{}, new Integer[]{5}, new Integer[]{}, new Integer[]{2});

        try {
            cellTest1 = new Cell(id[0], x[0], y[0], w[0], h[0], stateTest1, ns1);
            cellTest2 = new Cell(id[0], x[0], y[0], w[0], h[0], stateTest1, ns2);
            cellTest3 = new Cell(id[0], x[0], y[0], w[0], h[0], stateTest2, ns3);
            cellTest4 = new Cell(id[0], x[0], y[0], w[0], h[0], stateTest1, ns4);
            cellTest5 = new Cell(id[0], x[0], y[0], w[0], h[0], stateTest2, ns5);
            cellTest6 = new Cell(id[0], x[0], y[0], w[0], h[0], stateTest2, ns6);
            cellTest7 = new Cell(id[0], x[0], y[0], w[0], h[0], stateTest1, ns7);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, List<Integer>> makeNs(Integer[] left, Integer[] right, Integer[] up, Integer[] down) {
        Map<String, List<Integer>> ns = new HashMap<>();
        ns.put("Left", new ArrayList<>(Arrays.asList(left)));
        ns.put("Right", new ArrayList<>(Arrays.asList(right)));
        ns.put("Up", new ArrayList<>(Arrays.asList(up)));
        ns.put("Down", new ArrayList<>(Arrays.asList(down)));
        return ns;
    }

    @Test
    public void testSetDxDy() {
        Protein[] products = {proteinTest1, proteinTest2, proteinTest3};
        cellTest1.setProducts(products);
        cellTest1.setDxDy();
        int[] dxdy = {cellTest1.getX(), cellTest1.getY()};
        Assert.assertArrayEquals(new int[]{35, 5}, dxdy);
    }

    @Test
    public void testSumProteins1() {
        Protein[] products = {proteinTest1, proteinTest2};
        cellTest1.setProducts(products);
        Protein[] proteins = {proteinTest1, proteinTest3};
        cellTest1.sumProteins(proteins);
        double[] count = new double[cellTest1.getProducts().length];
        for (int i = 0; i < cellTest1.getProducts().length; i++)
            count[i] = cellTest1.getProducts()[i].getCount();
        Assert.assertArrayEquals(new double[]{240, 150}, count, 0.1);
    }

    @Test
    public void testSumProteins2() {
        Protein[] products = {proteinTest1, proteinTest3};
        cellTest1.setProducts(products);
        Protein[] proteins = {proteinTest1, proteinTest3};
        cellTest1.sumProteins(proteins);
        double[] count = new double[cellTest1.getProducts().length];
        for (int i = 0; i < cellTest1.getProducts().length; i++)
            count[i] = cellTest1.getProducts()[i].getCount();
        Assert.assertArrayEquals(new double[]{240, 380}, count, 0.1);
    }

    @Test
    public void testSumProteins3() {
        Protein[] products = {proteinTest1, proteinTest3};
        cellTest1.setProducts(products);
        Protein[] proteins = {};
        cellTest1.sumProteins(proteins);
        double[] count = new double[cellTest1.getProducts().length];
        for (int i = 0; i < cellTest1.getProducts().length; i++)
            count[i] = cellTest1.getProducts()[i].getCount();
        Assert.assertArrayEquals(new double[]{120, 190}, count, 0.1);
    }

    @Test
    public void testCalcXY1() {
        Circle circle3 = new Circle(100, 100, 30);
        Circle circle6 = new Circle(200, 120, 20);
        cellTest3.setCircle(circle3);
        cellTest6.setCircle(circle6);
        double[] xy = cellTest3.calcXY(cellTest6.getCircle().getCenterX(), cellTest6.getCircle().getCenterY(), cellTest6.getCircle().getRadius());
        Assert.assertArrayEquals(new double[]{129.4, 105.9, 180.4, 116.1}, xy, 0.1);
    }

    @Test
    public void testCalcXY2() {
        Circle circle3 = new Circle(100, 100, 30);
        Circle circle6 = new Circle(200, 80, 20);
        cellTest3.setCircle(circle3);
        cellTest6.setCircle(circle6);
        double[] xy = cellTest3.calcXY(cellTest6.getCircle().getCenterX(), cellTest6.getCircle().getCenterY(), cellTest6.getCircle().getRadius());
        Assert.assertArrayEquals(new double[]{129.4, 94.1, 180.4, 83.9}, xy, 0.1);
    }

    @Test
    public void testCalcXY3() {
        Circle circle3 = new Circle(100, 100, 30);
        Circle circle6 = new Circle(50, 120, 20);
        cellTest3.setCircle(circle3);
        cellTest6.setCircle(circle6);
        double[] xy = cellTest3.calcXY(cellTest6.getCircle().getCenterX(), cellTest6.getCircle().getCenterY(), cellTest6.getCircle().getRadius());
        Assert.assertArrayEquals(new double[]{72.1, 111.1, 68.6, 112.6}, xy, 0.1);
    }

    @Test
    public void testCalcXY4() {
        Circle circle3 = new Circle(100, 100, 30);
        Circle circle6 = new Circle(50, 80, 20);
        cellTest3.setCircle(circle3);
        cellTest6.setCircle(circle6);
        double[] xy = cellTest3.calcXY(cellTest6.getCircle().getCenterX(), cellTest6.getCircle().getCenterY(), cellTest6.getCircle().getRadius());
        Assert.assertArrayEquals(new double[]{72.1, 88.9, 68.6, 87.4}, xy, 0.1);
    }

    @Test
    public void testSolve() {
        double start = 0;
        double end = 5;
        int n = 500;
        double[] p0 = {0.5, 0.2};
        double[] a = {3, 0};
        double[][] b = {{0, 0},
                        {5, 0}};
        int[][] g = {{0, 0},
                     {1, 0}};
        double[][] K = {{0, 0},
                        {1, 0}};
        int[] N = {2, 2};
        double[] r = {1, 1};
        double[] actual = cellTest1.derivative(start, end, n, p0, a, b, r, N, K, g);
        double[] expected = {0.625,0.356};
        Assert.assertArrayEquals(expected, actual, 0.1);
    }
}
