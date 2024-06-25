package arabidopsis.db;

import arabidopsis.models.Gene;
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

public class GlobalGenes {

    private static final String filename = "data.json";

    public static Gene[] getGenes() throws IOException, ParseException {

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(filename));
        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
        JSONArray jsonGenes = (JSONArray) jsonObject.get("genes");

        Gene[] genes = new Gene[jsonGenes.size()];
        for (int i = 0; i < jsonGenes.size(); i++) {
            JSONObject jsonState = (JSONObject) jsonGenes.get(i);

            String type = (String) jsonState.get("name");

            double a = Double.parseDouble(String.valueOf(jsonState.get("activity")));

            JSONArray jsonB = (JSONArray) jsonState.get("b");
            double[] b = new double[jsonB.size()];
            for (int j = 0; j < jsonB.size(); j++)
                b[j] = (Double.parseDouble(String.valueOf(jsonB.get(j))));

            JSONArray jsonG = (JSONArray) jsonState.get("g");
            int[] g = new int[jsonG.size()];
            for (int j = 0; j < jsonG.size(); j++)
                g[j] = (Integer.parseInt(String.valueOf(jsonG.get(j))));

            JSONArray jsonK = (JSONArray) jsonState.get("K");
            double[] K = new double[jsonK.size()];
            for (int j = 0; j < jsonK.size(); j++)
                K[j] = (Double.parseDouble(String.valueOf(jsonK.get(j))));

            int N = Integer.parseInt(String.valueOf(jsonState.get("N")));

            double r = Double.parseDouble(String.valueOf(jsonState.get("r")));

            Gene gene = new Gene(type, a, b, g, K, N, r);
            genes[i] = gene;
        }
        return genes;
    }

    public static void setGene(int i, int j, double a, double b, int g, double k, int N, double r) throws IOException, ParseException
    {
        ArrayList<JSONObject> jsonGenes = new ArrayList<>();
        Gene[] genes = getGenes();

        genes[i].setActivity(a);
        genes[i].setB(b, j);
        genes[i].setG(g, j);
        genes[i].setK(k, j);
        genes[i].setN(N);
        genes[i].setR(r);

        for (Gene gene: genes) {
            JSONObject objGene = new JSONObject();
            objGene.put("name", gene.getType());
            objGene.put("activity", gene.getActivity());

            JSONArray jsonB = new JSONArray();
            for (double bi: gene.getB())
                jsonB.add(bi);
            objGene.put("b", jsonB);

            JSONArray jsonG = new JSONArray();
            for (int gi: gene.getG())
                jsonG.add(gi);
            objGene.put("g", jsonG);

            JSONArray jsonK = new JSONArray();
            for (double ki: gene.getK())
                jsonK.add(ki);
            objGene.put("K", jsonK);
            objGene.put("N", gene.getN());
            objGene.put("r", gene.getR());
            jsonGenes.add(objGene);
        }
        writeJSON(jsonGenes, "genes");
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
