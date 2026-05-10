import java.util.ArrayList;

// keeps scenario data and dimensions
public class ScenarioData {

    private String                      name;
    private String                      mode;        // Health or Education
    private String                      qualityType; // Product or process
    private ArrayList<QualityDimension> dimensions;

    public ScenarioData(String name, String mode, String qualityType) {
        this.name        = name;
        this.mode        = mode;
        this.qualityType = qualityType;
        this.dimensions  = new ArrayList<QualityDimension>();
    }

    public void addDimension(QualityDimension d) {
        dimensions.add(d);
    }

    // finds the dimension with the lowest score

    public QualityDimension getWeakestDimension() {
        QualityDimension weakest = null;
        for (QualityDimension d : dimensions) {
            if (weakest == null || d.getDimensionScore() < weakest.getDimensionScore()) {
                weakest = d;
            }
        }
        return weakest;
    }

    public String                      getName()        { return name; }
    public String                      getMode()        { return mode; }
    public String                      getQualityType() { return qualityType; }
    public ArrayList<QualityDimension> getDimensions()  { return dimensions; }
}