package interpolation;

import java.util.ArrayList;

public class InterpolationPoint {
    private double mult;
    private double fxi;
    private ArrayList<Double> xs;

    public InterpolationPoint(double mult, double fxi, ArrayList<Double> xs){
        this.xs = new ArrayList<>(xs);
        this.mult = mult;
        this.fxi = fxi;
    }

    public double calculateP(double xVal){
        double res = mult * fxi;
        for(Double d : xs)
            res *= xVal - d;

        return res;
    }

    public String toString(){
        String out = mult + " * ";
        for(Double d : xs){
            out += "(" + "x - " + d + ")";
        }
        out += "; Fxi = " + fxi;
        return out;
    }
}
