package interpolation;

import javafx.util.Pair;
import org.jzy3d.plot3d.primitives.Point;

import java.util.ArrayList;

public class Interpolator {
   public static ArrayList<InterpolationPoint> getInterpolationPoints(ArrayList<PointD> initials){
       ArrayList<InterpolationPoint> out = new ArrayList<>();
       ArrayList<Double> mults = new ArrayList<>();

       for(int i = 0; i < initials.size(); i++) {
           double initialMult = 0;
           double x0 = initials.get(i).getX();
           for (PointD pd : initials) {
               if (initialMult == 0 || pd.getX() == x0)
                   initialMult += x0 - pd.getX();
               else
                   initialMult *= x0 - pd.getX();
           }
           initialMult = 1 / initialMult;
           mults.add(initialMult);
       }

       for(Double mult : mults) {

           ArrayList<Double> xs = new ArrayList<>();
           for(int i = 0; i < initials.size(); i++){
               if(i != out.size())
                   xs.add(initials.get(i).getX());
           }

           double fxi = initials.get(out.size()).getY();
           out.add(new InterpolationPoint(mult,fxi,xs));
       }

       return out;
   }

    public static double computeLaGrange(ArrayList<InterpolationPoint> interpolationPoints, double xVal){
       double res = 0;

       for(InterpolationPoint ip : interpolationPoints){
           res += ip.calculateP(xVal);
       }

       return res;
    }

    public static ArrayList<PointD> getLaGrangeGraphPoints(ArrayList<InterpolationPoint> iPoints,
                                                           double f0, double fn){
        ArrayList<PointD> pointDS = new ArrayList<>();
        for(double i = f0; i < fn; i+=0.1){
            PointD point = new PointD(i,computeLaGrange(iPoints,i));
            pointDS.add(point);
        }

        return pointDS;
    }
}
