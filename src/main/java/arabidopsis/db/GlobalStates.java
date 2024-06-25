package arabidopsis.db;

import arabidopsis.models.Cell;
import arabidopsis.models.State;
import javafx.scene.paint.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GlobalStates {

    private static final String filename = "data.json";

    public static List<State> getStates() throws IOException, ParseException {

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(filename));
        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
        JSONArray jsonStates = (JSONArray) jsonObject.get("states");


        List<State> states = new ArrayList<>();
        for (int i = 0; i < jsonStates.size(); i++) {
            JSONObject jsonState = (JSONObject) jsonStates.get(i);
            int id = Integer.parseInt(jsonState.get("id").toString());

            String name = (String) jsonState.get("name");

            String description = (String) jsonState.get("description");
            Color color = Color.valueOf((String) jsonState.get("color"));

            Map<String, Integer> proteins = new HashMap<>();
            JSONArray jsonProteins = (JSONArray) jsonState.get("proteins");
            for (int j = 0; j < jsonProteins.size(); j++) {
                String protein = (String) ((JSONObject) jsonProteins.get(j)).get("protein");
                int count = Integer.parseInt((String.valueOf(((JSONObject) jsonProteins.get(j)).get("count"))));
                proteins.put(protein, count);
            }

            JSONArray jsonStatesNS = (JSONArray) jsonState.get("neighbors");
            Set<Integer> statesNS = new HashSet<>();
            for (int j = 0; j < jsonStatesNS.size(); j++)
                statesNS.add(Integer.parseInt(String.valueOf(jsonStatesNS.get(j))));

            JSONArray jsonInfluence = (JSONArray) jsonState.get("influence");
            double[] influence = new double[jsonInfluence.size()];
            for (int j = 0; j < jsonInfluence.size(); j++)
                influence[j] = (Double.parseDouble(String.valueOf(jsonInfluence.get(j))));

            State state = new State(id, proteins, name, description, influence, color, statesNS);
            states.add(state);
        }
        return states;
    }

    public static State getState(int i) throws IOException, ParseException {

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(filename));
        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
        JSONArray jsonStates = (JSONArray) jsonObject.get("states");

        JSONObject jsonState = (JSONObject) jsonStates.get(i);
        int id = Integer.parseInt(jsonState.get("id").toString());

        String name = (String) jsonState.get("name");

        String description = (String) jsonState.get("description");
        Color color = Color.valueOf((String) jsonState.get("color"));

        Map<String, Integer> proteins = new HashMap<>();
        JSONArray jsonProteins = (JSONArray) jsonState.get("proteins");
        for (int j = 0; j < jsonProteins.size(); j++) {
            String protein = (String) ((JSONObject) jsonProteins.get(j)).get("protein");
            int count = Integer.parseInt((String.valueOf(((JSONObject) jsonProteins.get(j)).get("count"))));
            proteins.put(protein, count);
        }

        JSONArray jsonStatesNS = (JSONArray) jsonState.get("neighbors");
        Set<Integer> statesNS = new HashSet<>();
        for (int j = 0; j < jsonStatesNS.size(); j++)
            statesNS.add(Integer.parseInt(String.valueOf(jsonStatesNS.get(j))));

        JSONArray jsonInfluence = (JSONArray) jsonState.get("influence");
        double[] influence = new double[jsonInfluence.size()];
        for (int j = 0; j < jsonInfluence.size(); j++)
            influence[j] = (Double.parseDouble(String.valueOf(jsonInfluence.get(j))));

        State state = new State(id, proteins, name, description, influence, color, statesNS);

        return state;
    }

    public static void addState(State state) throws IOException, ParseException
    {
        ArrayList<JSONObject> jsonStates = new ArrayList<>();
        List<State> states = getStates();
        states.add(state);

        for (int i = 0; i < states.size(); i++) {
            JSONObject objState = new JSONObject();
            objState.put("id", i+1);
            objState.put("name", states.get(i).getName());
            objState.put("description", states.get(i).getDescription());
            objState.put("color", String.valueOf(states.get(i).getColor()));

            JSONArray jsonProteins = new JSONArray();
            for (Map.Entry<String, Integer> protein : states.get(i).getProteins().entrySet()) {
                JSONObject objProtein = new JSONObject();
                objProtein.put("protein", protein.getKey());
                objProtein.put("count", protein.getValue());
                jsonProteins.add(objProtein);
            }
            objState.put("proteins", jsonProteins);

            JSONArray jsonInfluence = new JSONArray();
            for (double inf: states.get(i).getInfluence())
                jsonInfluence.add(inf);
            objState.put("influence", jsonInfluence);

            JSONArray jsonStatesNS = new JSONArray();
            for (int ns: states.get(i).getStatesNS())
                jsonStatesNS.add(ns);
            objState.put("neighbors", jsonStatesNS);

            jsonStates.add(objState);
        }
        writeJSON(jsonStates, "states");
    }

    public static void setState(int id, Map<String, Integer> proteins, String name, String description, double[] influence, Color color, Set<Integer> statesNS) throws IOException, ParseException
    {
        ArrayList<JSONObject> jsonStates = new ArrayList<>();
        List<State> states = getStates();

        states.get(id-1).setId(id);
        states.get(id-1).setName(name);
        states.get(id-1).setDescription(description);
        states.get(id-1).setColor(color);
        states.get(id-1).setProteins(proteins);
        states.get(id-1).setStatesNS(statesNS);

        for (int i = 0; i < states.size(); i++) {
            JSONObject objState = new JSONObject();
            objState.put("id", i+1);
            objState.put("name", states.get(i).getName());
            objState.put("description", states.get(i).getDescription());
            objState.put("color", String.valueOf(states.get(i).getColor()));

            JSONArray jsonProteins = new JSONArray();
            for (Map.Entry<String, Integer> protein : states.get(i).getProteins().entrySet()) {
                JSONObject objProtein = new JSONObject();
                objProtein.put("protein", protein.getKey());
                objProtein.put("count", protein.getValue());
                jsonProteins.add(objProtein);
            }
            objState.put("proteins", jsonProteins);

            JSONArray jsonInfluence = new JSONArray();
            for (double inf: states.get(i).getInfluence())
                jsonInfluence.add(inf);
            objState.put("influence", jsonInfluence);

            JSONArray jsonStatesNS = new JSONArray();
            for (int ns: states.get(i).getStatesNS())
                jsonStatesNS.add(ns);
            objState.put("neighbors", jsonStatesNS);

            jsonStates.add(objState);
        }
        writeJSON(jsonStates, "states");
    }

    public static void deleteState(int id) throws IOException, ParseException
    {
        ArrayList<JSONObject> jsonStates = new ArrayList<>();
        List<State> states = getStates();
        states.remove(id);
        for (int i = 0; i < states.size(); i++) {
            JSONObject objState = new JSONObject();
            objState.put("id", i+1);
            objState.put("name", states.get(i).getName());
            objState.put("description", states.get(i).getDescription());
            objState.put("color", String.valueOf(states.get(i).getColor()));

            JSONArray jsonProteins = new JSONArray();
            for (Map.Entry<String, Integer> protein : states.get(i).getProteins().entrySet()) {
                JSONObject objProtein = new JSONObject();
                objProtein.put("protein", protein.getKey());
                objProtein.put("count", protein.getValue());
                jsonProteins.add(objProtein);
            }
            objState.put("proteins", jsonProteins);

            JSONArray jsonInfluence = new JSONArray();
            for (double inf: states.get(i).getInfluence())
                jsonInfluence.add(inf);
            objState.put("influence", jsonInfluence);

            JSONArray jsonStatesNS = new JSONArray();
            for (int ns: states.get(i).getStatesNS())
                jsonStatesNS.add(ns);
            objState.put("neighbors", jsonStatesNS);

            jsonStates.add(objState);
        }
        writeJSON(jsonStates, "states");
    }

    public static void writeJSON(ArrayList<JSONObject> jsonObjets, String node) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(filename));
        org.json.simple.JSONObject objets = (org.json.simple.JSONObject) obj;
        objets.put(node, jsonObjets);

        try (FileWriter file = new FileWriter(filename)) {
            file.write(objets.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
