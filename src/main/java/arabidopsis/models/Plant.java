package arabidopsis.models;
import arabidopsis.db.GlobalCells;
import arabidopsis.db.GlobalStates;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Plant {
    private List<Cell> cells;
    private List<State> states;

    public Plant() throws IOException, ParseException {
        states = GlobalStates.getStates();

        cells = new ArrayList<>();
        Map<String, List<Integer>> ns = new HashMap<>();
        ns.put("Up", new ArrayList<>());
        ns.put("Down", new ArrayList<>());
        ns.put("Left", new ArrayList<>());
        ns.put("Right", new ArrayList<>());
        cells.add(new Cell(0, 0,0,10, 10, states.get(0), ns));
        GlobalCells.cells = cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public List<Cell> getCells() {
        return this.cells;
    }

    // СДВИГ ПРИ РОСТЕ
    public void shift(Cell cell, String dir, int s, int h) {

        List<Integer> ns = cell.getNs().get(dir);
        int y = cell.getY();
        int x = cell.getX();
        int d = cell.getD();
        if (h > d & cell.getIsGrowth())
            return;
        if (h == 0 & cell.getIsGrowth())
            return;

        cell.setD(d + h);
        if (!cell.getIsGrowth()) {
            if (dir.equals("Up") | dir.equals("Down")) {
                cell.setY(y + h * s);
                cell.setH(cell.getHeight() + cell.getDy());
            } else {
                cell.setX(x + h * s);
                cell.setW(cell.getWidth() + cell.getDx());
            }
        }
        else {
            if (dir.equals("Up") | dir.equals("Down")) {
                cell.setY(y - (d - h) * s);
                cell.setH(cell.getHeight() - (d - h));
            } else {
                cell.setX(x - (d - h) * s);
                cell.setW(cell.getWidth() - (d - h));
            }
        }
        for (int n: ns) {
            Cell cn = GlobalCells.getCell(n);
            if (cn == null)
                continue;
            if (dir.equals("Up") | dir.equals("Down"))
                shift(cn, dir, s, h + cell.getDy());
            else
                shift(cn, dir, s, h + cell.getDx());
        }
        cell.setGrowth(true);
    }

    // ЭКСПРЕССИЯ
    public void expression() throws IOException, ParseException {
        // ВРЕМЯ ШАГА
        double time = 5;
        for (Cell cell: cells) {
            double timeExpression = cell.timeExpression();
            if (timeExpression <= time)
                time = timeExpression;
        }
        // ЭКСПРЕССИЯ
        for (Cell cell: cells)
            cell.expression(time);
    }

    public void setDiv() {
        Random random = new Random();
        for (Cell cell: this.cells) {
            List<Integer> nsLeft = new ArrayList<>();
            for (int id : cell.getNs().get("Left"))
                nsLeft.add(GlobalCells.getCell(id).getState().getId());
            List<Integer> nsRight = new ArrayList<>();
            for (int id : cell.getNs().get("Right"))
                nsRight.add(GlobalCells.getCell(id).getState().getId());
            List<Integer> nsUp = new ArrayList<>();
            for (int id : cell.getNs().get("Up"))
                nsUp.add(GlobalCells.getCell(id).getState().getId());
            List<Integer> nsDown = new ArrayList<>();
            for (int id : cell.getNs().get("Down"))
                nsDown.add(GlobalCells.getCell(id).getState().getId());

            if ((nsLeft == nsRight & nsLeft == nsUp & nsLeft != nsDown) | (nsLeft == nsRight & nsLeft == nsDown & nsLeft != nsUp))
                cell.setDiv(1);
            else {
                if ((nsLeft == nsUp & nsLeft == nsDown & nsLeft != nsRight) | (nsRight == nsUp & nsRight == nsDown & nsRight != nsLeft))
                    cell.setDiv(0);
                else
                    cell.setDiv(random.nextBoolean() ? 1 : 0);
            }
        }
    }

    public void division() throws IOException, ParseException {
        for (int i = 0; i < this.cells.size(); i++) {
            Cell cell = cells.get(i);
            if (cell.getReadyDiv()) {
                List<Integer> ln = cell.getNs().get("Left");
                List<Integer> rn = cell.getNs().get("Right");
                List<Integer> un = cell.getNs().get("Up");
                List<Integer> dn = cell.getNs().get("Down");
                State state = cell.getState();
                int id1 = GlobalCells.getLastId() + 1;
                int id2 = id1 + 1;
                int x1, x2, y1, y2, w1, w2, h1, h2;
                List<Integer> ln1, rn1, un1, dn1, ln2, rn2, un2, dn2;
                if (cell.getDiv() == 0) {
                    x1 = cell.getX();
                    x2 = x1 + cell.getWidth() / 2;
                    int y = cell.getY();
                    y1 = y; y2 = y;
                    int w = cell.getWidth() / 2;
                    w1 = w; w2 = w;
                    int h = cell.getHeight();
                    h1 = h; h2 = h;
                    ln1 = ln; rn2 = rn;
                    rn1 = new ArrayList<>();
                    rn1.add(id2);
                    ln2 = new ArrayList<>();
                    ln2.add(id1);
                    un1 = new ArrayList<>(); un2 = new ArrayList<>(); dn1 = new ArrayList<>(); dn2 = new ArrayList<>();
                    for (Integer id: un) {
                        int l = GlobalCells.getCell(id).getX();
                        int r = l + GlobalCells.getCell(id).getWidth();
                        if (!(r < x1 | l > x2))
                            un1.add(id);
                        if (!(r < x2 | l > x2+w))
                            un2.add(id);
                    }
                    for (Integer id: dn) {
                        int l = GlobalCells.getCell(id).getX();
                        int r = l + GlobalCells.getCell(id).getWidth();
                        if (!(r < x1 | l > x2))
                            dn1.add(id);
                        if (!(r < x2 | l > x2+w))
                            dn2.add(id);
                    }
                } else {
                    int x = cell.getX();
                    x1 = x; x2 = x;
                    y1 = cell.getY();
                    y2 = y1 - cell.getHeight() / 2;
                    int w = cell.getWidth();
                    w1 = w; w2 = w;
                    int h = cell.getHeight() / 2;
                    h1 = h; h2 = h;

                    un1 = un; dn2 = dn;
                    dn1 = new ArrayList<>();
                    dn1.add(id2);
                    un2 = new ArrayList<>();
                    un2.add(id1);
                    ln1 = new ArrayList<>(); ln2 = new ArrayList<>(); rn1 = new ArrayList<>(); rn2 = new ArrayList<>();
                    for (Integer id: ln) {
                        int u = GlobalCells.getCell(id).getY();
                        int d = u - GlobalCells.getCell(id).getHeight();
                        if (!(u < y2 | d > y1))
                            ln1.add(id);
                        if (!(u < y2-h | d > y2))
                            ln2.add(id);
                    }
                    for (Integer id: rn) {
                        int u = GlobalCells.getCell(id).getY();
                        int d = u - GlobalCells.getCell(id).getHeight();
                        if (!(u < y2 | d > y1))
                            rn1.add(id);
                        if (!(u < y2-h | d > y2))
                            rn2.add(id);
                    }
                }
                Map<String, List<Integer>> ns1 = new HashMap<>();
                ns1.put("Left", ln1); ns1.put("Right", rn1); ns1.put("Up", un1); ns1.put("Down", dn1);
                Map<String, List<Integer>> ns2 = new HashMap<>();
                ns2.put("Left", ln2); ns2.put("Right", rn2); ns2.put("Up", un2); ns2.put("Down", dn2);
                Cell cell1 = new Cell(id1, x1, y1, w1, h1, state, ns1);
                Cell cell2 = new Cell(id2, x2, y2, w2, h2, state, ns2);
                this.cells.add(cell1);
                this.cells.add(cell2);
                deleteCell(cell);
            }
        }
    }

    public void deleteCell(Cell cell) {
        int id = cell.getId();
        this.cells.remove(cell);
        for (Cell c: this.cells) {
            c.getNs().get("Left").removeIf(element -> element.equals(id));
            c.getNs().get("Right").removeIf(element -> element.equals(id));
            c.getNs().get("Up").removeIf(element -> element.equals(id));
            c.getNs().get("Down").removeIf(element -> element.equals(id));
        }
    }

    public void setQuadrant() {
        int xl = 1000; int xr = -1000; int yd = 1000; int yu = -1000;
        for (Cell cell: this.cells) {
            if (cell.getX() < xl)
                xl = cell.getX();
            if (cell.getX() > xr)
                xr = cell.getX();
            if (cell.getY() < yd)
                yd = cell.getY();
            if (cell.getY() > yu)
                yu = cell.getY();
        }
        int xc = (xr - xl) / 2;
        int yc = (yu - yd) / 2;
        for (Cell cell: this.cells) {
            if (cell.getY() > yc & cell.getX() < xc & cell.getY()-cell.getHeight() < yc & cell.getX()+cell.getWidth() > xc)
                cell.setQ(0);
            if (cell.getX() > xc & cell.getY() > yc)
                cell.setQ(1);
            else if (cell.getY() > yc & cell.getX() < xc)
                cell.setQ(2);
            else if (cell.getY() < yc & cell.getX() < xc)
                cell.setQ(3);
            else
                cell.setQ(4);
        }
    }

    public void growth() throws IOException, ParseException {
        Random random = new Random();
        expression(); // ЭКСПРЕССИЯ
        setQuadrant(); // ОПРЕДЕЛЕНИЕ КВАДРАНТА
        for (Cell cell: this.cells) {
            cell.setGrowth(false);
            cell.setDxDy(); // ОПРЕДЕЛЕНИЕ DX И DY
            // СДВИГ
            if (cell.getQ() == 0) {
                int dirW = random.nextBoolean() ? 1 : 0;
                int dirH = random.nextBoolean() ? 1 : 0;
                if (dirW == 1)
                    shift(cell, "Left", -1, 0);
                else
                    shift(cell, "Right", 1, 0);
                cell.setGrowth(false);
                if (dirH == 1)
                    shift(cell, "Up", 1, 0);
                else
                    shift(cell, "Down", -1, 0);
            }
            else if (cell.getQ() == 1) {
                shift(cell, "Up", 1, 0);
                cell.setGrowth(false);
                shift(cell, "Right", 1, 0);
            }
            else if (cell.getQ() == 2) {
                shift(cell, "Up", 1, 0);
                cell.setGrowth(false);
                shift(cell, "Left", -1, 0);
            }
            else if (cell.getQ() == 3) {
                shift(cell, "Down", -1, 0);
                cell.setGrowth(false);
                shift(cell, "Left", -1, 0);
            }
            else if (cell.getQ() == 4) {
                shift(cell, "Down", -1, 0);
                cell.setGrowth(false);
                shift(cell, "Right", 1, 0);
            }
            //cell.setReadyDiv();
        }
        setDiv(); // УСТАНОВИТЬ ПЛОСКОСТЬ ДЕЛЕНИЯ
        division(); // ДЕЛЕНИЕ
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Cell cell: this.getCells())
            str.append(cell.getId()).append(" ");
        return str.toString();
    }
}
