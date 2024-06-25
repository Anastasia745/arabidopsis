package arabidopsis.models;

public class Protein
{
    private String gene; // Название гена, вырабатывающего данный белок
    private double count; // Имеющееся количество данного белка

    public Protein(String gene, double count)
    {
        this.gene = gene;
        this.count = count;
    }

    public String getGene() {
        return this.gene;
    }
    public double getCount() {
        return count;
    }
    public void setCount(double count) {
        this.count = count;
    }
}
