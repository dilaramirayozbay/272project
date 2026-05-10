import java.util.ArrayList;

// represents a quality dimension
public class QualityDimension {

    private String             name;
    private int                coefficient; // weight
    private ArrayList<Metric>  metrics;

    public QualityDimension(String name, int coefficient) {
        this.name        = name;
        this.coefficient = coefficient;
        this.metrics     = new ArrayList<Metric>();
    }

    public void addMetric(Metric m) {
        metrics.add(m);
    }

    // calculates weighted average score of the dimension

    public double getDimensionScore() {
        double weightedSum = 0;
        int    totalCoeff  = 0;
        for (Metric m : metrics) {
            weightedSum += m.getScore() * m.getCoefficient();
            totalCoeff  += m.getCoefficient();
        }
        if (totalCoeff == 0) return 0;
        return weightedSum / totalCoeff;
    }

    // Getters
    public String            getName()        { return name; }
    public int               getCoefficient() { return coefficient; }
    public ArrayList<Metric> getMetrics()     { return metrics; }
}