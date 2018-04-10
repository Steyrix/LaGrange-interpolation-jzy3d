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
    private JButton clearPointsButton;
    private JRadioButton laGrangeRbutton;
    private JRadioButton linearRbutton;
    private JTextField inputField;
    private JTextArea pointsField;

    private ArrayList<PointD> points;

    public MainForm() {
        points = new ArrayList<>();
        initGui();
    }

    private void initGui(){
        plotPanel = new JPanel();
        controlPanel = new JPanel();
        makePlotButton = new JButton("Нарисовать график");
        addPointButton = new JButton("Добавить точку");
        clearPointsButton = new JButton("Очистить точки");
        laGrangeRbutton = new JRadioButton("Полином Ла-Гранжа");
        linearRbutton = new JRadioButton("Линейная интерполяция");
        inputField = new JTextField();
        pointsField = new JTextArea();

        addPointButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String input = inputField.getText();
                    input.replaceAll("\\s+", "");
                    String[] strPoints = input.split(",");
                    if(strPoints.length != 2) throw new Exception("Wrong size");
                    points.add(new PointD(Double.parseDouble(strPoints[0]),Double.parseDouble(strPoints[1])));
                    pointsField.setText(pointsField.getText()+points.get(points.size()-1)+"\n");

                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        makePlotButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(points.size() < 2)
                        throw new Exception("Недостаточно точек для построения графика!");

                    for (Component c : plotPanel.getComponents()) {
                        if (c instanceof ICanvas)
                            plotPanel.remove(c);
                    }

                    Chart chart;

                    if (linearRbutton.isSelected())
                        chart = ChartBuilder.getChart(points);
                    else {
                        ArrayList<InterpolationPoint> iPoints = Interpolator.getInterpolationPoints(points);
                        double x0 = points.get(0).getX();
                        double xn = points.get(points.size() - 1).getX();
                        chart = ChartBuilder.getChart(Interpolator.getLaGrangeGraphPoints(iPoints, x0, xn));
                    }
                    plotPanel.add((Component) chart.getCanvas(), BorderLayout.CENTER);
                    plotPanel.revalidate();
                    plotPanel.repaint();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        clearPointsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                points.clear();
                pointsField.setText("Points: \n");
            }
        });

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
        setPreferredSize(new Dimension(500,450));
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
        plotPanel.setPreferredSize(new Dimension(350,200));
        plotPanel.add((Component)ChartBuilder.getInitialChart().getCanvas(), BorderLayout.CENTER);

        controlPanel.setBackground(Color.GRAY);
        controlPanel.setLayout(new GridBagLayout());
        controlPanel.setMinimumSize(new Dimension(50,50));
        controlPanel.setMaximumSize(new Dimension(300,300));
        controlPanel.setPreferredSize(new Dimension(350,200));
        setUpControlPanelComponents();
    }

    private void setUpControlPanelComponents(){
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.gridx = 0;
        controlPanel.add(laGrangeRbutton, c);
        c.gridy = 1;
        c.gridx = 0;
        controlPanel.add(linearRbutton, c);
        linearRbutton.setSelected(true);
        c.gridy = 0;
        c.gridx = 1;
        controlPanel.add(inputField,c);
        c.gridy = 1;
        c.gridx = 1;
        controlPanel.add(addPointButton,c);
        c.gridy = 0;
        c.gridx = 2;
        controlPanel.add(pointsField, c);
        pointsField.setBackground(new Color(1,1,1,0));
        pointsField.setEditable(false);
        pointsField.setFocusable(false);
        pointsField.setHighlighter(null);
        pointsField.setOpaque(false);
        pointsField.setText("Points: \n");
        c.gridy = 1;
        c.gridx = 2;
        controlPanel.add(clearPointsButton, c);
        c.gridy = 3;
        c.gridwidth = 2;
        c.gridx = 0;
        controlPanel.add(makePlotButton,c );
    }
}
