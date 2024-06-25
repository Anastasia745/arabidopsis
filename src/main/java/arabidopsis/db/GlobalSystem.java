package arabidopsis.db;

import arabidopsis.models.State;
import javafx.scene.paint.Color;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GlobalSystem {

    private static final String filename = "data.json";

    public static double RSystem;
    public static double RmaxSepals;
    public static double RminSepals;
    public static double RminPetals;
    public static double RminStamens;
    public static double DSepals;
    public static double DPetals;
    public static double DStamens;
    public static double d0Sepals;
    public static double d0Petals;
    public static double d0Stamens;

    static {
        try {
            RSystem = getSystemParameter("R");
            RmaxSepals = getSystemParameter("RmaxSepals");
            RminSepals = getSystemParameter("RminSepals");
            RminPetals = getSystemParameter("RminPetals");
            RminStamens = getSystemParameter("RminStamens");
            DSepals = getSystemParameter("DSepals");
            DPetals = getSystemParameter("DPetals");
            DStamens = getSystemParameter("DStamens");
            d0Sepals = getSystemParameter("d0Sepals");
            d0Petals = getSystemParameter("d0Petals");
            d0Stamens = getSystemParameter("d0Stamens");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static double getSystemParameter(String node) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(filename));
        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
        Object jsonSystemParameter = jsonObject.get(node);
        return Double.parseDouble(String.valueOf(jsonSystemParameter));
    }

    public static void setSystemParameter(String node, double value) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(filename));
        org.json.simple.JSONObject objets = (org.json.simple.JSONObject) obj;
        objets.put(node, value);

        try (FileWriter file = new FileWriter(filename)) {
            file.write(objets.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
