package models;

import arabidopsis.db.GlobalCells;
import arabidopsis.db.GlobalStates;
import arabidopsis.models.Cell;
import arabidopsis.models.Plant;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static models.CellTest.*;
import static models.StateTest.stateTest1;
import static models.StateTest.stateTest2;

public class PlantTest {

    public static final Plant plantTest;

    static {
        try {
            plantTest = new Plant();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCells() {
        GlobalCells.cells = new ArrayList<>();
        GlobalCells.cells.add(cellTest1);
        GlobalCells.cells.add(cellTest2);
        GlobalCells.cells.add(cellTest3);
        GlobalCells.cells.add(cellTest4);
        GlobalCells.cells.add(cellTest5);
        GlobalCells.cells.add(cellTest6);
        GlobalCells.cells.add(cellTest7);
    }

    @Test
    public void testDeleteCell() {
        setCells();
        plantTest.setCells(GlobalCells.cells);
        plantTest.deleteCell(cellTest1);
        Integer[][] actual = new Integer[plantTest.getCells().size()][];
        actual[0] = cellTest2.getNs().get("Right").toArray(new Integer[]{});
        actual[1] = cellTest3.getNs().get("Right").toArray(new Integer[]{});
        actual[2] = cellTest4.getNs().get("Left").toArray(new Integer[]{});
        actual[3] = cellTest5.getNs().get("Down").toArray(new Integer[]{});
        actual[4] = cellTest6.getNs().get("Down").toArray(new Integer[]{});
        Integer[][] expected = new Integer[6][];
        expected[0] = new Integer[]{};
        expected[1] = new Integer[]{};
        expected[2] = new Integer[]{};
        expected[3] = new Integer[]{2};
        expected[4] = new Integer[]{};
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testShift1() {
        setCells();
        plantTest.shift(cellTest1, "Left", -1, 5);
        double[] expected = {30,5,25,35};
        double[] actual = {cellTest1.getX(), cellTest1.getY(), cellTest1.getWidth(), cellTest1.getHeight()};
        Assert.assertArrayEquals(expected, actual, 0.1);
    }

    @Test
    public void testShift2() {
        setCells();
        plantTest.shift(cellTest1, "Right", 1, 5);
        double[] expected = {40,5,25,35};
        double[] actual = {cellTest1.getX(), cellTest1.getY(), cellTest1.getWidth(), cellTest1.getHeight()};
        Assert.assertArrayEquals(expected, actual, 0.1);
    }

    @Test
    public void testShift3() {
        setCells();
        plantTest.shift(cellTest1, "Up", 1, 5);
        double[] expected = {35,10,25,35};
        double[] actual = {cellTest1.getX(), cellTest1.getY(), cellTest1.getWidth(), cellTest1.getHeight()};
        Assert.assertArrayEquals(expected, actual, 0.1);
    }

    @Test
    public void testShift4() {
        setCells();
        plantTest.shift(cellTest1, "Down", -1, 5);
        double[] expected = {35,0,25,35};
        double[] actual = {cellTest1.getX(), cellTest1.getY(), cellTest1.getWidth(), cellTest1.getHeight()};
        Assert.assertArrayEquals(expected, actual, 0.1);
    }

//    @Test
//    public void testSetDiv() {
//        setCells();
//        plantTest.setCells(GlobalCells.cells);
//        plantTest.setDiv();
//        int[] expected = {0,0,0,1,0,0,0};
//        int[] actual = new int[plantTest.getCells().size()];
//        for (int i = 0; i < plantTest.getCells().size(); i++)
//            actual[i] = plantTest.getCells().get(i).getDiv();
//        Assert.assertArrayEquals(expected, actual);
//    }

    @Test
    public void testSetQuadrant() {
        setCells();
        plantTest.setCells(GlobalCells.cells);
        plantTest.setQuadrant();
        int[] expected = {1,1,1,1,1,1,1};
        int[] actual = new int[plantTest.getCells().size()];
        for (int i = 0; i < plantTest.getCells().size(); i++)
            actual[i] = plantTest.getCells().get(i).getQ();
        Assert.assertArrayEquals(expected, actual);
    }
}
