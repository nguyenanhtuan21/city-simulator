package com.natuan.citysimulator.model;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Road extends Place implements ActionListener, ChangeListener {
    public static boolean fluid;
    private static int sx;
    private static int sy;

    static {
        Road.sx = 0;
        Road.sy = 0;
    }

    public double roadVectorX;
    public double roadVectorY;
    public double velGradient;
    public double enterProb;
    public double exitProb;
    private double slope;
    private double intcpt;
    private int oneWayDirection;
    private int resistance;
    private int maxResistance;
    private JInternalFrame optionsFrame;
    private JCheckBox rightObsCheck;
    private JCheckBox leftObsCheck;
    private JCheckBox topObsCheck;
    private JCheckBox bottomObsCheck;
    private JCheckBox fluidCheck;
    private JSlider resistanceSlider;
    private JSlider exitProbSlider;
    private JLabel resistanceLabel;
    private JLabel exitProbLabel;

    public Road() {
        this.slope = 0.0;
        this.intcpt = 0.0;
        this.velGradient = 0.0;
        this.oneWayDirection = 0;
        this.resistance = 0;
        this.maxResistance = 0;
        this.enterProb = 0.0;
        this.exitProb = 0.0;
    }

    public void init(final TransPoint transPoint, final TransPoint transPoint2, final double n, final double n2, final double n3, final double velGradient, final double enterProb, final double exitProb) {
        this.velGradient = velGradient;
        this.enterProb = enterProb;
        this.exitProb = exitProb;
        super.init(transPoint, transPoint2, n3, n, n2);
        if (super.halfWidth / 10.0 < 2.0) {
            System.out.println("Road Error: Road too narrow or map scaling factor too large");
            System.exit(0);
        }
        super.placeColor = 5;
        this.oneWayDirection = 0;
        final double n4 = transPoint2.y - (double) transPoint.y;
        final double n5 = transPoint2.x - (double) transPoint.x;
        this.slope = n4 / n5;
        this.intcpt = super.endPoint1.y - this.slope * super.endPoint1.x;
        this.roadVectorX = n5 / super.length;
        this.roadVectorY = n4 / super.length;
        this.maxResistance = (int) (this.velGradient * super.halfWidth);
        if (this.maxResistance >= 201) {
            System.out.println("Road Error: velocity gradient to high in city plan");
            System.exit(0);
        }
    }

    public boolean movePerson(final Person person) {
        Place.ix = person.xpos;
        Place.iy = person.ypos;
        Place.iz = 0;
        Place.deltaX = this.moveX();
        Place.deltaY = this.moveY();
        if (this.resistance == 0) {
            Place.deltaX += Place.city.getVelocity(Place.ix, Place.iy).vX;
            Place.deltaY += Place.city.getVelocity(Place.ix, Place.iy).vY;
        } else {
            Place.deltaX += Place.city.getVelocity(Place.ix, Place.iy).vX / this.resistance;
            Place.deltaY += Place.city.getVelocity(Place.ix, Place.iy).vY / this.resistance;
        }
        Place.newPlace = Place.city.getPlaceMap(Place.ix + Place.deltaX, Place.iy + Place.deltaY);
        if (Place.newPlace != null) {
            Place.ix += Place.deltaX;
            Place.iy += Place.deltaY;
            if (!Road.fluid || Place.city.noTraffic(Place.ix, Place.iy)) {
                person.setPosition(Place.ix, Place.iy, Place.iz);
                person.setPlace(Place.newPlace);
            }
            return true;
        }
        return !Place.city.testEntryPlace(person.place) || Place.currentSim.resetPerson(person);
    }

    private double getDistToCenterLine(final int n, final int n2) {
        double n3;
        if (this.slope <= 9999.9) {
            n3 = (n2 - (this.slope * n + this.intcpt)) / Math.sqrt(this.slope * this.slope + 1.0);
        } else {
            n3 = super.endPoint1.x - n;
        }
        return n3;
    }

    public PointVelocity getSiteVelocity(final int n, final int n2) {
        final PointVelocity pointVelocity = new PointVelocity(0, 0);
        final double distToCenterLine = this.getDistToCenterLine(n, n2);
        double n3 = 0.0;
        if (distToCenterLine >= 0.0) {
            n3 = this.velGradient * (super.halfWidth - this.fabs(distToCenterLine));
            if (this.oneWayDirection == -1) {
                n3 *= -1.0;
            }
        } else if (this.oneWayDirection == 0) {
            n3 = -1.0 * this.velGradient * (super.halfWidth - this.fabs(distToCenterLine));
        }
        pointVelocity.vX = (int) (n3 * this.roadVectorX);
        pointVelocity.vY = (int) (n3 * this.roadVectorY);
        return pointVelocity;
    }

    public void actionPerformed(final ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.fluidCheck) {
            Road.fluid = this.fluidCheck.isSelected();
        }
    }

    public void stateChanged(final ChangeEvent changeEvent) {
        if (changeEvent.getSource() == this.resistanceSlider) {
            final int value = this.resistanceSlider.getValue();
            this.resistanceLabel.setText(String.valueOf(value));
            this.resistance = value;
        } else if (changeEvent.getSource() == this.exitProbSlider) {
            final int value2 = this.exitProbSlider.getValue();
            this.exitProbLabel.setText(String.valueOf(value2) + "%");
            this.exitProb = value2 / 100.0;
        }
    }

    public void showOptionsFrame() {
        if (this.optionsFrame != null && this.optionsFrame.isVisible()) {
            return;
        }
        this.optionsFrame = new JInternalFrame("Road Options", false, true);
        this.optionsFrame.getContentPane().setLayout(new BorderLayout());
        try {
            this.optionsFrame.setFrameIcon(new ImageIcon(this.getClass().getResource("lib/city2.gif")));
        } catch (Exception ignored) {
        }
        final JPanel comp = new JPanel(new GridLayout(1, 1));
        comp.setBorder(BorderFactory.createTitledBorder("Enable Traffic Flow Model"));
        final JPanel comp2 = new JPanel(new GridLayout(1, 1));
        comp2.setBorder(BorderFactory.createTitledBorder("Traffic Flow Resistance"));
        final JPanel comp3 = new JPanel(new GridLayout(4, 1));
        comp3.setBorder(BorderFactory.createTitledBorder("Probabilities"));
        final JPanel comp4 = new JPanel(new BorderLayout());
        final JPanel panel = new JPanel();
        final JPanel panel2 = new JPanel();
        final JPanel comp5 = new JPanel(new BorderLayout());
        final JPanel panel3 = new JPanel(new BorderLayout());
        final JPanel comp6 = new JPanel(new BorderLayout());
        (this.fluidCheck = new JCheckBox("Traffic Flow Model", Road.fluid)).addActionListener(this);
        (this.resistanceSlider = new JSlider(0, 0, this.maxResistance, this.resistance)).setMinimumSize(new Dimension(200, 24));
        this.resistanceSlider.setPreferredSize(new Dimension(200, 24));
        this.resistanceSlider.setSize(new Dimension(200, 24));
        this.resistanceSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        this.resistanceLabel = new JLabel(String.valueOf(this.resistance));
        (this.exitProbSlider = new JSlider(0, 0, 100, (int) (this.exitProb * 100.0))).setMinimumSize(new Dimension(200, 24));
        this.exitProbSlider.setPreferredSize(new Dimension(200, 24));
        this.exitProbSlider.setSize(new Dimension(200, 24));
        this.exitProbSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        this.exitProbLabel = new JLabel(String.valueOf((int) (this.exitProb * 100.0)) + "%");
        final Dimension dimension = new Dimension(50, this.resistanceLabel.getPreferredSize().height);
        this.resistanceLabel.setPreferredSize(dimension);
        this.exitProbLabel.setPreferredSize(dimension);
        this.resistanceSlider.addChangeListener(this);
        this.exitProbSlider.addChangeListener(this);
        final JLabel comp7 = new JLabel("Exit Probability");
        comp.add(this.fluidCheck);
        comp2.add(comp5);
        comp5.add(this.resistanceSlider, "Center");
        comp5.add(this.resistanceLabel, "East");
        comp3.add(comp7);
        comp3.add(comp6);
        comp6.add(this.exitProbSlider, "Center");
        comp6.add(this.exitProbLabel, "East");
        comp4.add(comp2, "North");
        comp4.add(comp3, "Center");
        this.optionsFrame.getContentPane().add(comp, "North");
        this.optionsFrame.getContentPane().add(comp4, "South");
        City.desk.add(this.optionsFrame);
        this.optionsFrame.validate();
        this.optionsFrame.pack();
        this.optionsFrame.setLocation(475 + super.centerX / 20, 75 + super.centerY / 20);
        this.optionsFrame.setVisible(true);
    }
}
