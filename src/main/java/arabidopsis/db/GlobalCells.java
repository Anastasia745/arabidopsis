package arabidopsis.db;

import arabidopsis.models.Cell;
import javafx.animation.Timeline;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GlobalCells {

    public static Timeline timeline;
    public static List<Cell> cells;

    // ПОИСК КЛЕТКИ ПО ID
    public static Cell getCell(int id) {
        for (Cell cell: cells)
            if (cell.getId() == id)
                return cell;
        return null;
    }

    public static int getLastId() {
        int id = 0;
        for (Cell cell: cells)
            if (cell.getId() > id)
                id = cell.getId();
        return id;
    }

    public static String getCells() {
        String str = "CELLS(G): [";
        for (Cell cell: cells)
            str += (cell.getId() + " ");
        str += "]";
        return str;
    }

    public static double[][] getB() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("data.json"));
        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
        JSONArray jsonRows = (JSONArray) jsonObject.get("b");

        int size = jsonRows.size();
        double[][] b = new double[size][size];
        for (int i = 0; i < size; i++) {
            JSONArray jsonRow = (JSONArray) jsonRows.get(i);
            for (int j = 0; j < jsonRow.size(); j++) {
                b[i][j] = (double)jsonRow.get(j);
            }
        }
        return b;
    }

    public static void setB(int i, int j, double bij) throws IOException, ParseException {

        double[][] b = getB();
        b[i][j] = bij;

        JSONArray jsonB = new JSONArray();
        for (int t = 0; t < b.length; i++) {
            JSONArray jsonBStr = new JSONArray();
            for (int k = 0; k < b[t].length; k++)
                jsonBStr.add(b[t][k]);
            jsonB.add(jsonBStr);
        }

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("data.json"));
        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
        jsonObject.put("b", jsonB);

        writeJSON(jsonObject, "data.json");
    }

    public static void writeJSON(JSONObject json, String filename)
    {
        try (FileWriter file = new FileWriter(filename)) {
            file.write(json.toJSONString());
            file.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
