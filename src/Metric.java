/**
 * Represents one measurement metric
 */
public class Metric {

    private String  name;
    private int     coefficient;    // weight within its dimension
    private boolean higherBetter;   // true, higher raw value is better
    private double  rangeMin;
    private double  rangeMax;
    private String  unit;           //
    private double  rawValue;       //  measurements
    private double  score;          // score btw 1.0 - 5.0

    // calculates the score
    public Metric(String name, int coefficient, boolean higherBetter,
                  double rangeMin, double rangeMax, String unit, double rawValue) {
        this.name        = name;
        this.coefficient = coefficient;
        this.higherBetter = higherBetter;
        this.rangeMin    = rangeMin;
        this.rangeMax    = rangeMax;
        this.unit        = unit;
        this.rawValue    = rawValue;
        this.score       = calculateScore(rawValue);
    }

    /**
     * Converts value into a 1-5 score using the ISO15939
     */
    public double calculateScore(double value) {
        double range = rangeMax - rangeMin;
        if (range == 0) return 3.0; // avoid division by 0

        double raw;
        if (higherBetter) {
            raw = 1.0 + (value - rangeMin) / range * 4.0;
        } else {
            raw = 5.0 - (value - rangeMin) / range * 4.0;
        }

        raw = Math.max(1.0, Math.min(5.0, raw));       // [1, 5]
        return Math.round(raw * 2.0) / 2.0;            // round to nearest
    }

    public String getDirectionLabel() {
        return higherBetter ? "Higher is better" : "Lower is better";
    }

    public String getRangeLabel() {
        String lo = (rangeMin == (int) rangeMin) ? String.valueOf((int) rangeMin) : String.valueOf(rangeMin);
        String hi = (rangeMax == (int) rangeMax) ? String.valueOf((int) rangeMax) : String.valueOf(rangeMax);
        return lo + " - " + hi;
    }

    // Getters
    public String  getName()         { return name; }
    public int     getCoefficient()  { return coefficient; }
    public String  getUnit()         { return unit; }
    public double  getRawValue()     { return rawValue; }
    public double  getScore()        { return score; }
}