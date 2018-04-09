package plotting;

import interpolation.PointD;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import java.util.ArrayList;

public class ChartBuilder {
    public static Chart getChart(ArrayList<PointD> points){
        Chart chart = AWTChartComponentFactory.chart(Quality.Advanced, IChartComponentFactory.Toolkit.swing);
        LineStrip interpolationStrip = getLineStrip(points);

        interpolationStrip.setDisplayed(true);
        interpolationStrip.setFaceDisplayed(true);
        interpolationStrip.setWireframeDisplayed(false);

        chart.getScene().getGraph().add(interpolationStrip);
        return chart;
    }

    public static LineStrip getLineStrip(ArrayList<PointD> points){
        LineStrip out = new LineStrip();
        for(PointD pd : points)
            out.add(new Point(new Coord3d(pd.getX(),pd.getY(),0),new Color(1,0,0,0.5f)));

        return out;
    }

    public static Chart getInitialChart() {
        Mapper mapper = new Mapper() {
            public double f(double x, double y) {
                return Math.sin(x) + Math.cos(y);
            }
        };

        Range range = new Range(-150, 150);
        int steps = 50;
        Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(false);
        surface.setWireframeColor(Color.BLACK);
        Chart chart = AWTChartComponentFactory.chart(Quality.Advanced, IChartComponentFactory.Toolkit.swing);
        chart.getScene().getGraph().add(surface);
        return chart;
    }
}
