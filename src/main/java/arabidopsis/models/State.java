package arabidopsis.models;

import java.util.Map;
import java.util.Set;

import javafx.scene.paint.Color;

public class State
{
    private int id;
    private Map<String, Integer> proteins;
    private String name;
    private String description;
    private double[] influence;
    private Color color;
    private Set<Integer> statesNS;

    public State(int id, Map<String, Integer> proteins, String name, String description, double[] influence, Color color, Set<Integer> statesNS)
    {
        this.influence = influence;
        this.id = id;
        this.proteins = proteins;
        this.name = name;
        this.description = description;
        this.color = color;
        this.statesNS = statesNS;
    }

    public int getId()
    {
        return this.id;
    }
    public Map<String, Integer> getProteins(){
        return proteins;
    }
    public double[] getInfluence()
    {
        return influence;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return this.description;
    }
    public Color getColor() {
        return color;
    }
    public Set<Integer> getStatesNS() {
        return statesNS;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setStatesNS(Set<Integer> statesNS) {
        this.statesNS = statesNS;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public void setProteins(Map<String, Integer> proteins) {
        this.proteins = proteins;
    }
}
