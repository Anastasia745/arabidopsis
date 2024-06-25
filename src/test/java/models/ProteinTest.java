package models;

import arabidopsis.models.Protein;

import static models.GeneTest.*;

public class ProteinTest {

    public static final Protein proteinTest1;
    public static final Protein proteinTest2;
    public static final Protein proteinTest3;

    static {
        proteinTest1 = new Protein(geneTest1.getType(), 120);
        proteinTest2 = new Protein(geneTest2.getType(), 150);
        proteinTest3 = new Protein(geneTest3.getType(), 190);
    }
}
