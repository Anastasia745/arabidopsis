package arabidopsis.models;

import arabidopsis.db.GlobalGenes;
import arabidopsis.db.GlobalCells;
import arabidopsis.db.GlobalStates;
import javafx.scene.shape.Circle;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.Math.*;
import static java.lang.Math.abs;

public class Cell
{
    private int id; // Идентификатор клетки (уникальное значение в пределах всей системы)
    private int x; // Координата x реальной клетки
    private int y; // Координата y реальной клетки
    private int w; // Ширина реальной клетки
    private int h; // Высота реальной клетки
    private int dx; // Величина роста в ширь
    private int dy; // Величина роста в высоту
    private int q; // Квадрант
    private int d; // Смещение
    private State state; // Состояние клетки
    private int div; // Плоскость деления (0 - горизонтальная, 1 - вертикальная)
    private Gene genes[]; // Набор генов (одинаков для всех клеток)
    private double[] a;
    private double[][] b;
    private double[][] K;
    private int[] N;
    private double[] r;
    private int[][] g;
    private Protein[] products; // Набор белков (продуктов генов)
    private double[][] solutions;
    private Map<String, List<Integer>> ns;
    private boolean isGrowth;
    private boolean readyDiv;
    private double time;
    private Circle circle;

    public Cell(int id, int x, int y, int w, int h, State state, Map<String, List<Integer>> ns) throws IOException, ParseException {
        this.id = id;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.state = state;
        this.genes = GlobalGenes.getGenes();
        this.products = new Protein[genes.length];
        for (int i = 0; i < this.genes.length; i++)
            products[i] = new Protein(genes[i].getType(), 0);

        a = Arrays.stream(genes).mapToDouble(Gene::getActivity).toArray();
        b = new double[genes.length][];
        K = new double[genes.length][];
        N = new int[genes.length];
        r = new double[genes.length];
        g = new int[genes.length][];
        for (int i = 0; i < genes.length; i++)
        {
            b[i] = genes[i].getB();
            K[i] = genes[i].getK();
            N[i] = genes[i].getN();
            r[i] = genes[i].getR();
            g[i] = genes[i].getG();
        }
        this.ns = ns;
        this.d = 0;
        this.dx = 0;
        this.dy = 0;
        this.isGrowth = false;
        this.q = -1;
        this.readyDiv = false;
    }

    public int getWidth() {
        return this.w;
    }
    public int getHeight() {
        return this.h;
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public int getDx() {
        return this.dx;
    }
    public int getDy() {
        return this.dy;
    }
    public int getQ() {
        return this.q;
    }
    public int getD() {
        return this.d;
    }
    public Map<String, List<Integer>> getNs() {
        return ns;
    }
    public int getId()
    {
        return id;
    }
    public boolean getIsGrowth(){
        return isGrowth;
    }
    public State getState()
    {
        return this.state;
    }
    public int getDiv() {
        return div;
    }
    public boolean getReadyDiv()
    {
        return this.readyDiv;
    }
    public Gene[] getGenes() {
        return genes;
    }
    public Protein[] getProducts() {
        return products;
    }
    public Circle getCircle() {
        return this.circle;
    }

    public void setW(int w)
    {
        this.w = w;
    }
    public void setH(int h)
    {
        this.h = h;
    }
    public void setX(int x)
    {
        this.x = x;
    }
    public void setY(int y)
    {
        this.y = y;
    }
    public void setDxDy() {
        double products = 0;
        for (Protein product: this.products)
            products = products + product.getCount();
        this.dx = (int) (products * 0.5);
        this.dy = (int) (products * 0.5);
    }
    public void setQ(int q)
    {
        this.q = q;
    }
    public void setD(int d)
    {
        this.d = d;
    }
    public void setGrowth(boolean growth)
    {
        this.isGrowth = growth;
    }
    public void setNs(String side, List<Integer> n)
    {
        ns.put(side, n);
    }
    public void setDiv(int div) {
        this.div = div;
    }
    public void setCircle(Circle circle) {
        this.circle = circle;
    }
    public void setState(State state) {
        this.state = state;
    }
    public void setProducts(Protein[] products) {
        this.products = products;
    }

    // РЕШЕНИЕ СИСТЕМЫ ОДУ
    public double[][] solve(double start, double end, int n, double[] p0,
                                   double[] a, double[][] b, double[] r,
                                   int[] N, double[][] K, int[][] g) {
        //int countGenes = GlobalGenes.getGenes().length;
        double[] negative = new double[16];
        for (int i = 0; i < 16; i++)
            negative[i] = 0;
        for (int i = 0; i < this.ns.get("Left").size(); i++) {
            if (GlobalCells.getCell(this.ns.get("Left").get(i)) == null)
                continue;
            double[] influence = GlobalCells.getCell(this.ns.get("Left").get(i)).getState().getInfluence();
            for (int j = 0; j < influence.length; j++)
                negative[j] += influence[j];
        }
        for (int i = 0; i < this.ns.get("Right").size(); i++) {
            if (GlobalCells.getCell(this.ns.get("Right").get(i)) == null)
                continue;
            double[] influence = GlobalCells.getCell(this.ns.get("Right").get(i)).getState().getInfluence();
            for (int j = 0; j < influence.length; j++)
                negative[j] += influence[j];
        }
        for (int i = 0; i < this.ns.get("Up").size(); i++) {
            if (GlobalCells.getCell(this.ns.get("Up").get(i)) == null)
                continue;
            double[] influence = GlobalCells.getCell(this.ns.get("Up").get(i)).getState().getInfluence();
            for (int j = 0; j < influence.length; j++)
                negative[j] += influence[j];
        }
        for (int i = 0; i < this.ns.get("Down").size(); i++) {
            if (GlobalCells.getCell(this.ns.get("Down").get(i)) == null)
                continue;
            double[] influence = GlobalCells.getCell(this.ns.get("Down").get(i)).getState().getInfluence();
            for (int j = 0; j < influence.length; j++)
                negative[j] += influence[j];
        }

        double h = (end-start)/n;
        double[] p = Arrays.copyOf(p0, b.length);
        double[][] results = new double[a.length][n];
        for (int t = 0; t < n; t++) {
            int equCount = a.length;
            double[] dp = new double[equCount];
            for (int i = 0; i < equCount; i++) {
                double V = 0;
                for (int j = 0; j < equCount; j++)
                    if (b[i][j] != 0)
                        V += (N[i] * b[i][j] * Math.pow(p[j]/K[i][j], g[i][j]) / (1 + Math.pow(p[j]/K[i][j], g[i][j])) + a[i] * N[i] / (1 + Math.pow(p[j]/K[i][j], g[i][j])));
                if (V == 0)
                    V = a[i];
                dp[i] = V - r[i] * p[i] + negative[i];
            }
            for (int i = 0; i < b.length; i++) {
                p[i] += dp[i] * h;
                results[i][t] = p[i];
            }
        }
        return results;
    }

    // ДИФФЕРЕНЦИАЛ В ТОЧКЕ
    public double[] derivative(double start, double end, int n, double[] p0, double[] a, double[][] b, double[] r,
                               int[] N, double[][] K, int[][] g) {
        double h = (end-start)/n;
        double[] p = Arrays.copyOf(p0, b.length);
        double[] results = new double[a.length];

        int equCount = a.length;
        double[] dp = new double[equCount];
        for (int i = 0; i < equCount; i++) {
            double V = 0;
            for (int j = 0; j < equCount; j++)
                if (b[i][j] != 0)
                    V += (N[i] * b[i][j] * Math.pow(p[j]/K[i][j], g[i][j]) / (1 + Math.pow(p[j]/K[i][j], g[i][j])) + a[i] * N[i] / (1 + Math.pow(p[j]/K[i][j], g[i][j])));
            if (V == 0)
                V = a[i];
            dp[i] = V - r[i] * p[i];
        }
        for (int i = 0; i < b.length; i++) {
            p[i] += dp[i] * h * end;
            results[i] = p[i];
        }
        return results;
    }

    // Суммирование количества имеющихся белков с количеством продуктов генов, произведённых на некотором шаге
    public void sumProteins(Protein[] proteins) {
        for (Protein protein: proteins) {
            String gene = protein.getGene();
            for (Protein product: this.products) {
                if (product.getGene().equals(gene)) {
                    double count = product.getCount() + protein.getCount();
                    product.setCount(count);
                    break;
                }
            }
//            Protein protein = Arrays.stream(this.products).filter(p -> Objects.equals(p.getGene(), gene)).findFirst().get();
//            double count = protein.getCount() + proteins[i].getCount();
//            protein.setCount(count);
        }
    }

    // БЛИЖАЙШЕЕ ВРЕМЯ, НУЖНОЕ ДЛЯ ГОТОВНОСТИ К ДЕЛЕНИЮ, В РЕЗУЛЬТАТЕ
    // КОТОРОГО ОБРАЗУЮТСЯ 2 КЛЕТКИ ТЕКУЩЕГО СОСТОЯНИЯ ИЛИ СОСТОЯНИЯ "ВЫШЕ" ТЕКУЩЕГО
    public double timeExpression() throws IOException, ParseException {
        double[] p0 = new double[genes.length];
        for (int i = 0 ; i < products.length; i++)
            p0[i] = products[i].getCount();
        double start = 0;
        double end = 5;
        int n = 500;
        solutions = solve(start, end, n, p0, a, b, r, N, K, g);
        double time = 0;
        for (State state: GlobalStates.getStates()) {
            if (state.getId() >= this.state.getId()) {
                for (int t = 0; t < n; t++) {
                    for (int i = 0; i < genes.length; i++) {
                        double productsCount = solutions[i][t];
                        if (state.getProteins().containsKey(genes[i].getType())) {
//                            int number = 3;
//                            try (FileWriter fileWriter = new FileWriter("file.txt", true)) {
//                                fileWriter.append(String.valueOf(state.getProteins().get(genes[i].getType())) + " ");
//                                fileWriter.flush();
//                            }
                            if (productsCount < state.getProteins().get(genes[i].getType()))
                                continue;
                            if (t >= time) {
                                time = t * (end - start) / n;
                                this.time = time;
                                break;
                            }
                        }
                    }
                }
            }
        }
        System.out.println(time);
        return time;
    }

    // ЭКСПРЕССИЯ
    public void expression(double time)
    {
        if (time == this.time)
            this.readyDiv = true;
        double[] p0 = new double[genes.length];
        for (int i = 0 ; i < products.length; i++)
            p0[i] = products[i].getCount();
        double start = 0;
        double end = time;
        int n = 500;
        for(int i=0; i<genes.length; i++) {
            double productsCount = solutions[i][n-1];
            products[i] = new Protein(genes[i].getType(), productsCount);
            genes[i].setActivity(derivative(start, end, n, p0, a, b, r, N, K, g)[i]);
        }
        sumProteins(products);
    }

    @Override
    public String toString() {
        return "CELL id: " + this.id + ", x: " + this.x + ", y: " + this.y + ", w: " + this.w + ", h: " + this.h;
    }

    public String nsToString() {
        String str = "NS id: "+ this.id + "\n";
        str += "   L: [";
        for (int i : this.ns.get("Left"))
            str += (i + ", ");
        str += "]\n   R: [";
        for (int i : this.ns.get("Right"))
            str += (i + ", ");
        str += "]\n   U: [";
        for (int i : this.ns.get("Up"))
            str += (i + ", ");
        str += "]\n   D: [";
        for (int i : this.ns.get("Down"))
            str += (i + ", ");
        str += "]";
        return str;
    }

    public double[] calcXY(double x2, double y2, double r2)
    {
        double x1 = this.circle.getCenterX();
        double y1 = this.circle.getCenterY();
        double r1 = this.circle.getRadius();

        double[] xy = new double[4];
        double dx = x1 - x2;
        double dy = y1 - y2;
        double R = sqrt(pow(abs(dx), 2) + pow(abs(dy), 2));
        double xStart;
        double yStart;
        double xEnd;
        double yEnd;

        if (dx >= 0) {
            xStart = x1 - r1 * dx / R;
            xEnd = x2 + r2 * dx / R;
        }
        else {
            xStart = x1 + r1 * abs(dx) / R;
            xEnd = x2 - r2 * abs(dx) / R;
        }
        if (dy >= 0) {
            yStart = y1 - r1 * dy / R;
            yEnd = y2 + r2 * dy / R;
        }
        else {
            yStart = y1 + r1 * abs(dy) / R;
            yEnd = y2 - r2 * abs(dy) / R;
        }
        xy[0] = xStart;
        xy[1] = yStart;
        xy[2] = xEnd;
        xy[3] = yEnd;
        return xy;
    }
}
