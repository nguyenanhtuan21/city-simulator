package com.natuan.citysimulator.GUI;

import com.natuan.citysimulator.abstracts.GridBagHelper;
import com.natuan.citysimulator.model.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class Intersection extends Place implements ActionListener, ChangeListener {
    public static boolean fluid;
    public static ExitPoint exitPoint;
    private static int direction;
    private static int FOURTHWIDTH;
    private static int THREEFOURTHS;
    private static int l1;
    private static int l2;
    private static int rx;
    private static int ry;
    private static int iprob;
    private static int iMax;
    private static boolean outBound;
    private static String roadLabel;

    static {
        Intersection.direction = 0;
        Intersection.FOURTHWIDTH = 0;
        Intersection.THREEFOURTHS = 0;
        Intersection.rx = 0;
        Intersection.ry = 0;
        Intersection.iprob = 0;
        Intersection.iMax = 0;
        Intersection.outBound = false;
        Intersection.roadLabel = "";
    }

    public int[] obstruction;
    public Vector allExitPoints;
    public Vector activeExitPoints;
    public Vector exitPlaceList;
    private JInternalFrame optionsFrame;
    private JSlider[] obsSlider;
    private JLabel[] obsPosLabel;
    private JLabel[] obsLabel;
    private JCheckBox rightObsCheck;
    private JCheckBox leftObsCheck;
    private JCheckBox topObsCheck;
    private JCheckBox bottomObsCheck;
    private JCheckBox fluidCheck;

    public Intersection() {
        this.allExitPoints = new Vector();
        this.activeExitPoints = new Vector();
        this.exitPlaceList = new Vector();
    }

    public void init(final TransPoint transPoint, final TransPoint transPoint2, final double n, final double n2, final double n3) {
        super.init(transPoint, transPoint2, n3, n, n2);
        this.allExitPoints = new Vector();
        Intersection.FOURTHWIDTH = (int) (n / 4.0);
        Intersection.THREEFOURTHS = 3 * Intersection.FOURTHWIDTH;
        super.placeColor = 5;
        if (n <= 4.0) {
            System.out.println("Intersection Error: Intersection place too small in cityplan. Change scale or design. ");
            System.exit(0);
        }
    }

    public void findAllExitPoints() {
        final int n = (int) (super.length / 2.0) + Intersection.FOURTHWIDTH;
        final int n2 = (int) super.halfWidth + Intersection.FOURTHWIDTH;
        final double n3 = super.centerX + (double) n;
        final double n4 = super.centerX - (double) n;
        for (int i = super.centerY - n2; i < super.centerY + n2; ++i) {
            Intersection.rx = (int) (n3 * Math.cos(super.orientRads) - i * Math.sin(super.orientRads));
            Intersection.ry = (int) (i * Math.cos(super.orientRads) + n3 * Math.sin(super.orientRads));
            Place.newPlace = Place.city.getPlaceMap(Intersection.rx, Intersection.ry);
            if (Place.newPlace != null && ((Place.newPlace instanceof Road && this.roadOutBound(Intersection.rx, Intersection.ry)) || Place.newPlace instanceof Intersection)) {
                Intersection.exitPoint = new ExitPoint(Intersection.rx, Intersection.ry, Place.newPlace);
                this.allExitPoints.addElement(Intersection.exitPoint);
            }
            Intersection.rx = (int) (n4 * Math.cos(super.orientRads) - i * Math.sin(super.orientRads));
            Intersection.ry = (int) (i * Math.cos(super.orientRads) + n4 * Math.sin(super.orientRads));
            Place.newPlace = Place.city.getPlaceMap(Intersection.rx, Intersection.ry);
            if (Place.newPlace != null && ((Place.newPlace instanceof Road && this.roadOutBound(Intersection.rx, Intersection.ry)) || Place.newPlace instanceof Intersection)) {
                Intersection.exitPoint = new ExitPoint(Intersection.rx, Intersection.ry, Place.newPlace);
                this.allExitPoints.addElement(Intersection.exitPoint);
            }
        }
        final double n5 = super.centerY + (double) n2;
        final double n6 = super.centerY - (double) n2;
        for (int j = super.centerX - n; j < super.centerX + n; ++j) {
            Intersection.rx = (int) (j * Math.cos(super.orientRads) - n5 * Math.sin(super.orientRads));
            Intersection.ry = (int) (n5 * Math.cos(super.orientRads) + j * Math.sin(super.orientRads));
            Place.newPlace = Place.city.getPlaceMap(Intersection.rx, Intersection.ry);
            if (Place.newPlace != null && ((Place.newPlace instanceof Road && this.roadOutBound(Intersection.rx, Intersection.ry)) || Place.newPlace instanceof Intersection)) {
                Intersection.exitPoint = new ExitPoint(Intersection.rx, Intersection.ry, Place.newPlace);
                this.allExitPoints.addElement(Intersection.exitPoint);
            }
            Intersection.rx = (int) (j * Math.cos(super.orientRads) - n6 * Math.sin(super.orientRads));
            Intersection.ry = (int) (n6 * Math.cos(super.orientRads) + j * Math.sin(super.orientRads));
            Place.newPlace = Place.city.getPlaceMap(Intersection.rx, Intersection.ry);
            if (Place.newPlace != null && ((Place.newPlace instanceof Road && this.roadOutBound(Intersection.rx, Intersection.ry)) || Place.newPlace instanceof Intersection)) {
                Intersection.exitPoint = new ExitPoint(Intersection.rx, Intersection.ry, Place.newPlace);
                this.allExitPoints.addElement(Intersection.exitPoint);
            }
        }
        this.updateActiveExitPoints();
        this.updateExitPlaceList();
    }

    public boolean movePerson(final Person person) {
        Place.iz = 0;
        Intersection.iMax = this.activeExitPoints.size();
        if (Intersection.iMax >= 1) {
            Intersection.direction = Place.rnd.nextInt(Intersection.iMax);
            Intersection.exitPoint = (ExitPoint) this.activeExitPoints.elementAt(Intersection.direction);
            Place.ix = Intersection.exitPoint.rx;
            Place.iy = Intersection.exitPoint.ry;
            Place.newPlace = Intersection.exitPoint.p;
            if ((Place.newPlace != null && !Intersection.fluid) || (Intersection.fluid && Place.city.noTraffic(Place.ix, Place.iy))) {
                person.setPosition(Place.ix, Place.iy, Place.iz);
                person.setPlace(Place.newPlace);
            }
        }
        return true;
    }

    public boolean roadOutBound(final int n, final int n2) {
        Intersection.outBound = false;
        final PointVelocity velocity = Place.city.getVelocity(n, n2);
        Intersection.l1 = (n - super.centerX) * (n - super.centerX) + (n2 - super.centerY) * (n2 - super.centerY);
        Intersection.l2 = (velocity.vX + n - super.centerX) * (velocity.vX + n - super.centerX) + (velocity.vY + n2 - super.centerY) * (velocity.vY + n2 - super.centerY);
        if (Intersection.l2 > Intersection.l1) {
            Intersection.outBound = true;
        }
        return Intersection.outBound;
    }

    private void updateActiveExitPoints() {
        this.activeExitPoints.removeAllElements();
        for (int i = 0; i < this.allExitPoints.size(); ++i) {
            final ExitPoint exitPoint = (ExitPoint) this.allExitPoints.elementAt(i);
            final int index = this.exitPlaceList.indexOf(exitPoint.p);
            if (index >= 0) {
                Intersection.iprob = Place.rnd.nextInt(100);
                if (Intersection.iprob >= this.obstruction[index]) {
                    this.activeExitPoints.addElement(exitPoint);
                }
            } else {
                this.activeExitPoints.addElement(exitPoint);
            }
        }
    }

    private void updateExitPlaceList() {
        for (int i = 0; i < this.allExitPoints.size(); ++i) {
            Place.newPlace = ((ExitPoint) this.allExitPoints.elementAt(i)).p;
            if (!this.exitPlaceList.contains(Place.newPlace)) {
                this.exitPlaceList.addElement(Place.newPlace);
            }
        }
        this.obstruction = new int[this.exitPlaceList.size()];
        for (int j = 0; j < this.exitPlaceList.size(); ++j) {
            this.obstruction[j] = 0;
        }
    }

    public void actionPerformed(final ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.fluidCheck) {
            Intersection.fluid = this.fluidCheck.isSelected();
        }
    }

    public void showOptionsFrame() {
        if (this.optionsFrame != null && this.optionsFrame.isVisible()) {
            return;
        }
        this.obsSlider = new JSlider[this.exitPlaceList.size()];
        this.obsLabel = new JLabel[this.exitPlaceList.size()];
        this.obsPosLabel = new JLabel[this.exitPlaceList.size()];
        this.optionsFrame = new JInternalFrame("Intersection Options", false, true);
        this.optionsFrame.getContentPane().setLayout(new BorderLayout());
        try {
            this.optionsFrame.setFrameIcon(new ImageIcon(this.getClass().getResource("lib/city2.gif")));
        } catch (Exception ex) {
        }
        final JPanel comp = new JPanel(new GridLayout(1, 1));
        comp.setBorder(BorderFactory.createTitledBorder("Enable Traffic Flow Model"));
        final JPanel comp2 = new JPanel(new GridLayout(1, 1));
        comp2.setBorder(BorderFactory.createTitledBorder("Percent Obstructed"));
        final JPanel comp3 = new JPanel(new GridBagLayout());
        comp2.add(comp3);
        for (int i = 0; i < this.exitPlaceList.size(); ++i) {
            Place.newPlace = (Place) this.exitPlaceList.elementAt(i);
            Place.ix = super.centerX - Place.newPlace.centerX;
            Place.iy = super.centerY - Place.newPlace.centerY;
            Intersection.roadLabel = " ";
            if (Place.iy < 0) {
                Intersection.roadLabel += "S";
            }
            if (Place.iy > 0) {
                Intersection.roadLabel += "N";
            }
            if (Place.iy == 0) {
                Intersection.roadLabel += " ";
            }
            if (Place.ix < 0) {
                Intersection.roadLabel += "E";
            }
            if (Place.ix > 0) {
                Intersection.roadLabel += "W";
            }
            if (Place.ix == 0) {
                Intersection.roadLabel += " ";
            }
            this.obsPosLabel[i] = new JLabel(Intersection.roadLabel);
            this.obsSlider[i] = new JSlider(0, 0, 100, this.obstruction[i]);
            this.obsLabel[i] = new JLabel(String.valueOf(this.obstruction[i]) + "%");
            this.obsSlider[i].addChangeListener(this);
            this.obsSlider[i].setMinimumSize(new Dimension(200, 24));
            this.obsSlider[i].setPreferredSize(new Dimension(200, 24));
            this.obsSlider[i].setSize(new Dimension(200, 24));
            this.obsSlider[i].setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            GridBagHelper.add(comp3, this.obsPosLabel[i], GridBagHelper.labelLike(0, i, 1));
            GridBagHelper.add(comp3, this.obsSlider[i], GridBagHelper.fieldLike(1, i, 3));
            GridBagHelper.add(comp3, this.obsLabel[i], GridBagHelper.labelLike(4, i, 1));
        }
        comp3.doLayout();
        (this.fluidCheck = new JCheckBox("Traffic Flow Model", Intersection.fluid)).addActionListener(this);
        comp.add(this.fluidCheck);
        this.optionsFrame.getContentPane().add(comp, "North");
        this.optionsFrame.getContentPane().add(comp2, "South");
        City.desk.add(this.optionsFrame);
        this.optionsFrame.validate();
        this.optionsFrame.pack();
        this.optionsFrame.setLocation(450 + super.centerX / 20, 50 + super.centerY / 20);
        this.optionsFrame.setVisible(true);
    }

    public void stateChanged(final ChangeEvent changeEvent) {
        for (int i = 0; i < this.exitPlaceList.size(); ++i) {
            if (changeEvent.getSource() == this.obsSlider[i]) {
                final int value = this.obsSlider[i].getValue();
                this.obsLabel[i].setText(String.valueOf(value) + "%");
                this.obstruction[i] = value;
            }
        }
        this.updateActiveExitPoints();
    }
}
