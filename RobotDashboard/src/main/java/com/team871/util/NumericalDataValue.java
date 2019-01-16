package com.team871.util;

/**
 * @author TPfaffe-871
 * Contains a double value constrained by a max and min that is mutable.
 */
public class NumericalDataValue implements IData<Double> {

    private Double value;
    private double maxValue;
    private double minValue;
    private boolean isNormalized;
    private double normalMax;
    private double normalMin;

    /**
     * value of the NumericalDataValue is set to 0 by default
     */
    protected NumericalDataValue() {
        set(new Double(0));
    }

    /**
     * @param value of the NumericalDataValue
     */
    public NumericalDataValue(Double value) {
        this(value, Double.MAX_VALUE - 1, Double.MIN_VALUE - 1);
    }

    /**
     * value of NumericalDataValue is set to 0 by default.
     * @param maxValue maximum value that this data can reach.
     * @param minValue minimum value that this data can reach.
     */
    public NumericalDataValue(double maxValue, double minValue) {
        this(new Double(0), maxValue, minValue);
    }

    /**
     * value of NumericalDataValue is set to 0 by default.
     * @param maxValue maximum value that this data can reach.
     * @param minValue minimum value that this data can reach.
     * @param normalMax will normalize the dataValue to this value as maximum.
     * @param normalMin will normalize the dataValue to this value as minimum.
     */
    public NumericalDataValue(double maxValue, double minValue, int normalMax, int normalMin){
        this(new Double(0), maxValue, minValue);

        this.normalMax = normalMax;
        this.normalMin = normalMin;
        this.isNormalized = true;
    }

    /**
     * value of NumericalDataValue is set to 0 by default.
     * @param value    of the NumericalDataValue.
     * @param maxValue maximum value that this data can reach.
     * @param minValue minimum value that this data can reach.
     */
    public NumericalDataValue(Double value, double maxValue, double minValue) {
        this.maxValue = maxValue;
        this.minValue = minValue;

        this.isNormalized = false;
        this.normalMax = 0;
        this.normalMin = 0;

        set(value);
    }

    /**
     *
     * @returns the formatted data stored within this dataValue
     */
    public Double get() {

        if(isNormalized){
          double normVal = this.value;
            normVal = normVal % normalMax;

            if (normVal < normalMin)
            {
                normVal += normalMax;
            }
            return normVal;
        }

        return this.value;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public String toString() {
        return "" + this.value;
    }

    /**
     * This is only for testing purposes and breaks encapsulation
     * so should never be utilized in real builds
     *
     * @param delta the amount added to the preexisting value
     */
    @Deprecated
    public void changeBy(double delta) { //TODO: should probably remove but eh?
        set(value + delta);
    }

    protected void set(Double value) {
        Double newVal = Math.min(maxValue, value);
        newVal = Math.max(minValue, newVal);
        this.value = newVal;

    }
}
