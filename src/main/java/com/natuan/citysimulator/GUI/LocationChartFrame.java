package com.natuan.citysimulator.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LocationChartFrame extends JInternalFrame implements ActionListener {
    public static final int Y_DISPLAY_LEFT = 4;
    public static final int Y_DISPLAY_RIGHT = 4;
    public static final int X_DISPLAY = 5;
    public static final Color COLOR_LEFT;
    public static final Color COLOR_RIGHT;
    public static boolean display_flag;
    private static JLabel[] y1Labels;

    static {
        COLOR_LEFT = Color.blue;
        COLOR_RIGHT = Color.magenta;
        LocationChartFrame.y1Labels = new JLabel[5];
        LocationChartFrame.display_flag = true;
    }

    private String chartTitle;
    private String xUnits;
    private String yUnitsLeft;
    private String yUnitsRight;
    private String yLegendLeft;
    private String yLegendRight;
    private int xMax;
    private int xMin;
    private int yMaxLeft;
    private int yMinLeft;
    private int yMaxRight;
    private int yMinRight;
    private ChartPanel chartPanel;
    private JPanel y1LabelsPanel;
    private GridBagLayout y1GridBag;
    private JMenuItem p;
    private JMenuItem c;

    public LocationChartFrame(final String title, final int width, final int height, final String chartTitle, final String xUnits, final String yUnitsLeft, final String yUnitsRight, final String yLegendLeft, final String yLegendRight, final int xMax, final int yMaxLeft, final int yMaxRight) {
        super(title);
        this.xMax = 0;
        this.xMin = 0;
        this.yMaxLeft = 0;
        this.yMinLeft = 0;
        this.yMaxRight = 0;
        this.yMinRight = 0;
        this.chartTitle = chartTitle;
        this.xUnits = xUnits;
        this.yUnitsLeft = yUnitsLeft;
        this.yUnitsRight = yUnitsRight;
        this.yLegendLeft = yLegendLeft;
        this.yLegendRight = yLegendRight;
        this.xMax = xMax;
        this.yMaxLeft = yMaxLeft;
        this.yMaxRight = yMaxRight;
        this.setSize(new Dimension(width, height));
        try {
            this.setFrameIcon(new ImageIcon(this.getClass().getResource("lib/city2.gif")));
        } catch (Exception ex) {
        }
        this.initComponents();
        final JMenuBar jMenuBar = new JMenuBar();
        this.setJMenuBar(jMenuBar);
        jMenuBar.add(new JMenuItem("File"));
        (this.p = new JMenuItem("Print")).setMnemonic(80);
        this.p.addActionListener(this);
        (this.c = new JMenuItem("Close")).setMnemonic(80);
        this.c.addActionListener(this);
        jMenuBar.add(this.p);
        jMenuBar.add(this.c);
        this.setVisible(true);
    }

    public static void main(final String[] array) {
        final LocationChartFrame locationChartFrame = new LocationChartFrame("Chart Frame Title", 600, 400, "Chart Title", "Number of Floors", "People", "Something", "Legend People", "Legend Something", 30, 100, 5000);
        final JButton comp = new JButton("Cause Update");
        comp.addActionListener(locationChartFrame);
        final JFrame frame = new JFrame("This is the main frame");
        frame.setSize(700, 500);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(locationChartFrame, "Center");
        frame.getContentPane().add(comp, "South");
        frame.setVisible(true);
        locationChartFrame.updateData(1000, new int[]{0, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000}, new int[]{3000, 4000, 4500, 4000, 3000, 4000, 4500, 4000, 3000, 4000, 4500, 4000, 3000, 4000, 4500, 4000, 3000, 4000, 4500, 4000, 3000, 4000, 4500, 4000, 3000, 4000, 4500, 4000, 3000, 4000, 4500});
    }

    public void updateData(final int yMaxLeft, final int[] array, final int[] array2) {
        this.yMaxLeft = yMaxLeft;
        for (int i = 0; i < LocationChartFrame.y1Labels.length; ++i) {
            LocationChartFrame.y1Labels[i].setText(String.valueOf(this.yMaxLeft - i * this.yMaxLeft / 4));
        }
        this.chartPanel.update(yMaxLeft, array, array2);
    }

    private void initComponents() {
        this.getContentPane().removeAll();
        this.getContentPane().setLayout(new GridLayout(1, 1));
        final JPanel comp = new JPanel(new BorderLayout());
        final JPanel comp2 = new JPanel(new BorderLayout());
        final JPanel comp3 = new JPanel(new BorderLayout());
        final JPanel comp4 = new JPanel(new BorderLayout());
        final JPanel comp5 = new JPanel(new BorderLayout());
        final JPanel comp6 = new JPanel(new BorderLayout());
        final JPanel comp7 = new JPanel(new BorderLayout());
        final GridBagLayout layout = new GridBagLayout();
        this.y1GridBag = new GridBagLayout();
        final GridBagLayout layout2 = new GridBagLayout();
        final GridBagConstraints constraints = new GridBagConstraints();
        final JPanel comp8 = new JPanel();
        final JPanel comp9 = new JPanel(new GridLayout(2, 1));
        final JPanel comp10 = new JPanel(layout);
        this.y1LabelsPanel = new JPanel(this.y1GridBag);
        final JPanel comp11 = new JPanel(layout2);
        this.chartPanel = new ChartPanel(this.yMaxRight, this.xMax);
        final JPanel comp12 = new JPanel();
        final JPanel comp13 = new JPanel();
        final JPanel comp14 = new JPanel(new GridLayout(2, 1));
        this.getContentPane().add(comp);
        comp.add(comp8, "North");
        comp.add(comp2, "Center");
        comp2.add(comp4, "East");
        comp2.add(comp3, "Center");
        comp3.add(comp5, "West");
        comp3.add(comp7, "East");
        comp3.add(comp6, "Center");
        comp4.add(comp9, "North");
        comp5.add(this.y1LabelsPanel, "Center");
        comp5.add(comp12, "South");
        comp7.add(comp11, "Center");
        comp7.add(comp13, "South");
        comp6.add(this.chartPanel, "Center");
        comp6.add(comp14, "South");
        comp14.add(comp10);
        this.chartPanel.setBorder(BorderFactory.createLineBorder(LocationChartFrame.COLOR_LEFT));
        comp9.setBorder(BorderFactory.createTitledBorder("Legend"));
        comp9.setBackground(Color.white);
        final JLabel comp15 = new JLabel(this.chartTitle);
        final JLabel comp16 = new JLabel(this.yLegendLeft);
        final JLabel comp17 = new JLabel(this.yLegendRight);
        final JLabel comp18 = new JLabel(this.xUnits, 0);
        final JLabel comp19 = new JLabel(this.yUnitsLeft, 0);
        final JLabel comp20 = new JLabel(this.yUnitsRight, 0);
        comp16.setForeground(LocationChartFrame.COLOR_LEFT);
        comp17.setForeground(LocationChartFrame.COLOR_RIGHT);
        comp8.add(comp15);
        comp14.add(comp18);
        comp12.add(comp19);
        comp13.add(comp20);
        comp9.add(comp16);
        comp9.add(comp17);
        constraints.weightx = 0.5;
        constraints.gridwidth = 1;
        constraints.anchor = 18;
        constraints.gridheight = 0;
        final JLabel label = new JLabel(String.valueOf(this.xMin), 2);
        final JLabel label2 = new JLabel(String.valueOf(this.xMax), 4);
        layout.setConstraints(label, constraints);
        comp10.add(label);
        final int n = (this.xMax - this.xMin) / 5;
        constraints.weightx = 1.0;
        constraints.anchor = 11;
        for (int i = 0; i < 4; ++i) {
            final JLabel label3 = new JLabel(String.valueOf(n * (i + 1)), 0);
            layout.setConstraints(label3, constraints);
            comp10.add(label3);
        }
        constraints.weightx = 0.5;
        constraints.gridwidth = 0;
        constraints.anchor = 12;
        layout.setConstraints(label2, constraints);
        comp10.add(label2);
        constraints.weighty = 0.5;
        constraints.gridwidth = 0;
        constraints.gridheight = 1;
        constraints.anchor = 12;
        LocationChartFrame.y1Labels[0] = new JLabel(String.valueOf(this.yMaxLeft), 4);
        LocationChartFrame.y1Labels[4] = new JLabel(String.valueOf(this.yMinLeft), 4);
        LocationChartFrame.y1Labels[0].setVerticalAlignment(1);
        LocationChartFrame.y1Labels[4].setVerticalAlignment(3);
        this.y1GridBag.setConstraints(LocationChartFrame.y1Labels[0], constraints);
        this.y1LabelsPanel.add(LocationChartFrame.y1Labels[0]);
        constraints.weighty = 1.0;
        constraints.anchor = 13;
        final int n2 = (this.yMaxLeft - this.yMinLeft) / 4;
        for (int j = 0; j < 3; ++j) {
            LocationChartFrame.y1Labels[j + 1] = new JLabel(String.valueOf(this.yMaxLeft - n2 * (j + 1)), 4);
            this.y1GridBag.setConstraints(LocationChartFrame.y1Labels[j + 1], constraints);
            this.y1LabelsPanel.add(LocationChartFrame.y1Labels[j + 1]);
        }
        constraints.weighty = 0.5;
        constraints.gridheight = 0;
        constraints.anchor = 14;
        this.y1GridBag.setConstraints(LocationChartFrame.y1Labels[4], constraints);
        this.y1LabelsPanel.add(LocationChartFrame.y1Labels[4]);
        constraints.weightx = 0.0;
        constraints.weighty = 0.5;
        constraints.gridwidth = 0;
        constraints.gridheight = 1;
        constraints.anchor = 18;
        final JLabel label4 = new JLabel(String.valueOf(this.yMaxRight), 2);
        final JLabel label5 = new JLabel(String.valueOf(this.yMinRight), 2);
        label4.setVerticalAlignment(1);
        label5.setVerticalAlignment(3);
        layout2.setConstraints(label4, constraints);
        comp11.add(label4);
        constraints.weighty = 1.0;
        constraints.anchor = 17;
        final int n3 = (this.yMaxRight - this.yMinRight) / 4;
        for (int k = 0; k < 3; ++k) {
            final JLabel label6 = new JLabel(String.valueOf(this.yMaxRight - n3 * (k + 1)), 2);
            layout2.setConstraints(label6, constraints);
            comp11.add(label6);
        }
        constraints.anchor = 16;
        constraints.weighty = 0.5;
        constraints.gridheight = 0;
        layout2.setConstraints(label5, constraints);
        comp11.add(label5);
    }

    public void actionPerformed(final ActionEvent actionEvent) {
        final Object source = actionEvent.getSource();
        if (source == this.p) {
        }
        if (source == this.c) {
            this.close();
        }
    }

    public void close() {
        this.setVisible(false);
        LocationChartFrame.display_flag = false;
    }
}

