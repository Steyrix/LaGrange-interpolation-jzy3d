import javax.swing.*;

import interpolation.InterpolationPoint;
import interpolation.Interpolator;
import interpolation.PointD;
import org.jzy3d.chart.Chart;
import org.jzy3d.plot3d.rendering.canvas.ICanvas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import plotting.*;
public class MainForm extends JFrame {

    private JPanel plotPanel;
    private JPanel controlPanel;

    private JButton makePlotButton;
    private JButton addPointButton;

    private JRadioButton laGrangeRbutton;
    private JRadioButton linearRbutton;

    private JTextField inputField;

    private ArrayList<PointD> points;

    public static void main(String[] args){
        MainForm form = new MainForm();
        form.setVisible(true);
        System.out.println("I'm alive!");
//        PointD p1 = new PointD(0.351,-0.572);
//        PointD p2 = new PointD(0.867, -2.015);
//        PointD p3 = new PointD(3.315, -3.342);
//        PointD p4 = new PointD(5.013, -5.752);
//        PointD p5 = new PointD(6.432, -6.911);
//        ArrayList<PointD> pointDS = new ArrayList<>();
//        pointDS.add(p1);
//        pointDS.add(p2);
//        pointDS.add(p3);
//        pointDS.add(p4);
//        pointDS.add(p5);
//        ArrayList<InterpolationPoint> interpolationPoints = Interpolator.getInterpolationPoints(pointDS);
//        for(InterpolationPoint ip : interpolationPoints){
//            System.out.println(ip.toString());
//        }

        //System.out.println("LaGrange: " + Interpolator.computeLaGrange(interpolationPoints, p2.getX()+p3.getX()));
    }

    private void initGui(){
        plotPanel = new JPanel();
        controlPanel = new JPanel();
        makePlotButton = new JButton("Нарисовать график");
        addPointButton = new JButton("Добавить точку");
        laGrangeRbutton = new JRadioButton("Полином Ла-Гранжа");
        linearRbutton = new JRadioButton("Линейная интерполяция");
        inputField = new JTextField();

        addPointButton.addActionListener(new AddPointListener());
        makePlotButton.addActionListener(new MakePlotListener());

        laGrangeRbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(laGrangeRbutton.isSelected())
                    linearRbutton.setSelected(false);
                else linearRbutton.setSelected(true);
            }
        });

        linearRbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(linearRbutton.isSelected())
                    laGrangeRbutton.setSelected(false);
                else laGrangeRbutton.setSelected(true);
            }
        });

        setMinimumSize(new Dimension(300,300));
        setMaximumSize(new Dimension(750,750));
        setPreferredSize(new Dimension(400,400));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0,1));
        add(plotPanel);
        add(controlPanel);
        setUpPanelsLayout();
        pack();
    }

    private void setUpPanelsLayout(){
        plotPanel.setBackground(Color.darkGray);
        plotPanel.setLayout(new BorderLayout());
        plotPanel.setMinimumSize(new Dimension(50,50));
        plotPanel.setMaximumSize(new Dimension(300,300));
        plotPanel.setPreferredSize(new Dimension(200,200));
        plotPanel.add((Component)ChartBuilder.getInitialChart().getCanvas(), BorderLayout.CENTER);

        controlPanel.setBackground(Color.GRAY);
        controlPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        controlPanel.setMinimumSize(new Dimension(50,50));
        controlPanel.setMaximumSize(new Dimension(300,300));
        controlPanel.setPreferredSize(new Dimension(200,200));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 0;
        controlPanel.add(laGrangeRbutton, c);
        c.gridy = 1;
        c.gridx = 0;
        controlPanel.add(linearRbutton, c);
        c.gridy = 0;
        c.gridx = 1;
        controlPanel.add(inputField,c);
        c.gridy = 1;
        c.gridx = 1;
        controlPanel.add(addPointButton,c);
        c.gridy = 3;
        c.gridwidth = 2;
        c.gridx = 0;
        controlPanel.add(makePlotButton,c );

        linearRbutton.setSelected(true);
    }

    private class AddPointListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                String input = inputField.getText();
                input.replaceAll("\\s+", "");
                String[] strPoints = input.split(",");
                if(strPoints.length != 2) throw new Exception("Wrong size");
                points.add(new PointD(Double.parseDouble(strPoints[0]),Double.parseDouble(strPoints[1])));

            }catch(Exception ex){
                System.out.println(ex.toString());
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private class MakePlotListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            for(Component c : plotPanel.getComponents()){
                if (c instanceof ICanvas)
                    plotPanel.remove(c);
            }

            Chart chart;

            if(linearRbutton.isSelected())
                chart = ChartBuilder.getChart(points);
            else {
                ArrayList<InterpolationPoint> iPoints = Interpolator.getInterpolationPoints(points);
                double x0 = points.get(0).getX();
                double xn = points.get(points.size()-1).getX();
                chart = ChartBuilder.getChart(Interpolator.getLaGrangeGraphPoints(iPoints, x0, xn));
            }
            plotPanel.add((Component)chart.getCanvas(), BorderLayout.CENTER);
            plotPanel.revalidate();
            plotPanel.repaint();
        }
    }

    public MainForm() {
        SwingUtilities.invokeLater(()->initGui());
        points = new ArrayList<>();
    }
}
