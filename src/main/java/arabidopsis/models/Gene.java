package arabidopsis.models;

import arabidopsis.GlobalVariables;
import arabidopsis.db.GlobalGenes;
import javafx.scene.shape.Rectangle;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Gene
{
    private String type; // Название гена ('TOC', 'AP1' и т.п.)
    private double a; // Активность гена (числовое значение)
    private double[] b;
    private int[] g;
    private double[] K;
    private int N;
    private double r;
    private Rectangle[] rectangle;

    public Gene(String type, double a, double[] b, int[] g, double[] K, int N, double r) throws IOException, ParseException {
        this.type = type;
        this.a = a;
        this.b = b;
        this.g = g;
        this.K = K;
        this.N = N;
        this.r = r;
        rectangle = new Rectangle[16];
    }

    public String getType() {
        return this.type;
    }
    public double getActivity() {
        return this.a;
    }
    public double[] getB() {
        return this.b;
    }
    public int[] getG() {
        return this.g;
    }
    public double[] getK() {
        return this.K;
    }
    public int getN() {
        return this.N;
    }
    public double getR() {
        return this.r;
    }
    public Rectangle[] getRectangles() {
        return rectangle;
    }

    public void setActivity(double a) {
        this.a = a;
    }

    public void setB(double b, int j) {
        this.b[j] = b;
    }

    public void setG(int g, int j) {
        this.g[j] = g;
    }

    public void setK(double k, int j) {
        K[j] = k;
    }

    public void setN(int n) {
        N = n;
    }

    public void setR(double r) {
        this.r = r;
    }

    public void setRectangle(Rectangle rectangle, int j) {
        this.rectangle[j] = rectangle;
    }
}
