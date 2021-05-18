package com.natuan.citysimulator.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SingleChartFrame extends JInternalFrame implements ActionListener {
    public static final int Y_DISPLAY_LEFT = 4;
    public static final int X_DISPLAY = 5;
    public static final Color COLOR_LEFT;
    public static boolean display_flag;
    private static JLabel[] y1Labels;

    static {
        COLOR_LEFT = Color.blue;
        SingleChartFrame.y1Labels = new JLabel[5];
        SingleChartFrame.display_flag = true;
    }

    private String chartTitle;
    private String xUnits;
    private String yUnitsLeft;
    private String yLegendLeft;
    private int xMax;
    private int xMin;
    private int yMaxLeft;
    private int yMinLeft;
    private SingleChartPanel chartPanel;
    private JPanel y1LabelsPanel;
    private GridBagLayout y1GridBag;
    private JMenuItem p;
    private JMenuItem c;

    public SingleChartFrame(final String title, final int width, final int height, final String chartTitle, final String xUnits, final String yUnitsLeft, final String yLegendLeft, final int xMax, final int yMaxLeft) {
        super(title);
        this.xMax = 0;
        this.xMin = 0;
        this.yMaxLeft = 0;
        this.yMinLeft = 0;
        this.chartTitle = chartTitle;
        this.xUnits = xUnits;
        this.yUnitsLeft = yUnitsLeft;
        this.yLegendLeft = yLegendLeft;
        this.xMax = xMax;
        this.yMaxLeft = yMaxLeft;
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
    }

    public void updateData(final int yMaxLeft, final int[] array) {
        this.yMaxLeft = yMaxLeft;
        for (int i = 0; i < SingleChartFrame.y1Labels.length; ++i) {
            SingleChartFrame.y1Labels[i].setText(String.valueOf(this.yMaxLeft - i * this.yMaxLeft / 4));
        }
        this.chartPanel.update(yMaxLeft, array);
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
        final GridBagLayout layout = new GridBagLayout();
        this.y1GridBag = new GridBagLayout();
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        final JPanel comp7 = new JPanel();
        final JPanel comp8 = new JPanel(new GridLayout(2, 1));
        final JPanel comp9 = new JPanel(layout);
        this.y1LabelsPanel = new JPanel(this.y1GridBag);
        this.chartPanel = new SingleChartPanel(this.yMaxLeft, this.xMax);
        final JPanel comp10 = new JPanel();
        final JPanel comp11 = new JPanel(new GridLayout(2, 1));
        this.getContentPane().add(comp);
        comp.add(comp7, "North");
        comp.add(comp2, "Center");
        comp2.add(comp4, "East");
        comp2.add(comp3, "Center");
        comp3.add(comp5, "West");
        comp3.add(comp6, "Center");
        comp4.add(comp8, "North");
        comp5.add(this.y1LabelsPanel, "Center");
        comp5.add(comp10, "South");
        comp6.add(this.chartPanel, "Center");
        comp6.add(comp11, "South");
        comp11.add(comp9);
        this.chartPanel.setBorder(BorderFactory.createLineBorder(LocationChartFrame.COLOR_LEFT));
        comp8.setBorder(BorderFactory.createTitledBorder("Legend"));
        comp8.setBackground(Color.white);
        final JLabel comp12 = new JLabel(this.chartTitle);
        final JLabel comp13 = new JLabel(this.yLegendLeft);
        final JLabel comp14 = new JLabel(this.xUnits, 0);
        final JLabel comp15 = new JLabel(this.yUnitsLeft, 0);
        comp13.setForeground(LocationChartFrame.COLOR_LEFT);
        comp7.add(comp12);
        comp11.add(comp14);
        comp10.add(comp15);
        comp8.add(comp13);
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.anchor = 18;
        gridBagConstraints.gridheight = 0;
        final JLabel label = new JLabel(String.valueOf(this.xMin), 2);
        final JLabel label2 = new JLabel(String.valueOf(this.xMax), 4);
        layout.setConstraints(label, gridBagConstraints);
        comp9.add(label);
        final int n = (this.xMax - this.xMin) / 5;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.anchor = 11;
        for (int i = 0; i < 4; ++i) {
            final JLabel label3 = new JLabel(String.valueOf(n * (i + 1)), 0);
            layout.setConstraints(label3, gridBagConstraints);
            comp9.add(label3);
        }
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.gridwidth = 0;
        gridBagConstraints.anchor = 12;
        layout.setConstraints(label2, gridBagConstraints);
        comp9.add(label2);
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.gridwidth = 0;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.anchor = 12;
        SingleChartFrame.y1Labels[0] = new JLabel(String.valueOf(this.yMaxLeft), 4);
        SingleChartFrame.y1Labels[4] = new JLabel(String.valueOf(this.yMinLeft), 4);
        SingleChartFrame.y1Labels[0].setVerticalAlignment(1);
        SingleChartFrame.y1Labels[4].setVerticalAlignment(3);
        this.y1GridBag.setConstraints(SingleChartFrame.y1Labels[0], gridBagConstraints);
        this.y1LabelsPanel.add(SingleChartFrame.y1Labels[0]);
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.anchor = 13;
        final int n2 = (this.yMaxLeft - this.yMinLeft) / 4;
        for (int j = 0; j < 3; ++j) {
            SingleChartFrame.y1Labels[j + 1] = new JLabel(String.valueOf(this.yMaxLeft - n2 * (j + 1)), 4);
            this.y1GridBag.setConstraints(SingleChartFrame.y1Labels[j + 1], gridBagConstraints);
            this.y1LabelsPanel.add(SingleChartFrame.y1Labels[j + 1]);
        }
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.gridheight = 0;
        gridBagConstraints.anchor = 14;
        this.y1GridBag.setConstraints(SingleChartFrame.y1Labels[4], gridBagConstraints);
        this.y1LabelsPanel.add(SingleChartFrame.y1Labels[4]);
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
        SingleChartFrame.display_flag = false;
    }
}

