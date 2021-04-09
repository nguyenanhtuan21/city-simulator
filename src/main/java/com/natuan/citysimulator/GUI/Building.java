package com.natuan.citysimulator.GUI;

import com.natuan.citysimulator.abstracts.GridBagHelper;
import com.natuan.citysimulator.model.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Building extends Place implements ActionListener, ChangeListener {
    public Place neighborUP;
    public Place neighborDOWN;
    public double exitProb;
    private JInternalFrame optionsFrame;
    private JSlider upSlider;
    private JSlider downSlider;
    private JLabel upLabel;
    private JLabel downLabel;
    private JLabel upProbLabel;
    private JLabel downProbLabel;
    private JSlider upLobbySlider;
    private JSlider exitLobbySlider;
    private JLabel upLobbyLabel;
    private JLabel exitLobbyLabel;
    private JLabel upLobbyProbLabel;
    private JLabel exitLobbyProbLabel;

    public Building() {
        this.exitProb = 0.0;
        this.neighborUP = null;
        this.neighborDOWN = null;
    }

    public void init(final int n, final int n2, final int n3, final int n4, final double upBound, final double exitProb) {
        super.init(new TransPoint(n, (n2 + n4) / 2), new TransPoint(n3, (n2 + n4) / 2), 0.0, this.fabs(n4 - n2), this.fabs(n3 - n));
        super.placeColor = 63;
        this.setUpBound(upBound);
        this.exitProb = exitProb;
    }

    public void init(final TransPoint transPoint, final TransPoint transPoint2, final double n, final double n2, final double n3, final double upBound, final double exitProb) {
        super.init(transPoint, transPoint2, n, n2, n3);
        super.placeColor = 63;
        this.setUpBound(upBound);
        this.exitProb = exitProb;
    }

    public boolean movePerson(final Person person) {
        Place.ix = person.xpos;
        Place.iy = person.ypos;
        Place.iz = 0;
        Place.deltaX = this.moveX();
        Place.deltaY = this.moveY();
        Place.newPlace = Place.city.getPlaceMap(Place.ix + Place.deltaX, Place.iy + Place.deltaY);
        if (Place.newPlace instanceof Building && Place.newPlace == this) {
            Place.ix += Place.deltaX;
            Place.iy += Place.deltaY;
            Place.prob = Place.rnd.nextDouble();
            if (Place.prob < super.upBound && this.neighborUP != null && this.neighborUP instanceof Floor) {
                Place.iz = this.neighborUP.altitude;
                person.setPosition(Place.ix, Place.iy, Place.iz);
                person.setPlace(this.neighborUP);
                return true;
            }
            person.setPosition(Place.ix, Place.iy, Place.iz);
            return true;
        } else {
            if (!(Place.newPlace instanceof Road)) {
                return true;
            }
            Place.prob = Place.rnd.nextDouble();
            if (Place.prob < this.exitProb) {
                Place.ix += Place.deltaX;
                Place.iy += Place.deltaY;
                person.setPosition(Place.ix, Place.iy, 0);
                person.setPlace(Place.newPlace);
                return true;
            }
            return true;
        }
    }

    public void setNeighborUp(final Place neighborUP) {
        this.neighborUP = neighborUP;
    }

    public void setNeighborDown(final Place neighborDOWN) {
        this.neighborDOWN = neighborDOWN;
    }

    public Place getNeighborUP() {
        return this.neighborUP;
    }

    public Place getNeighborDOWN() {
        return this.neighborDOWN;
    }

    public void actionPerformed(final ActionEvent actionEvent) {
    }

    public void showOptionsFrame() {
        if (this.optionsFrame != null && this.optionsFrame.isVisible()) {
            return;
        }
        this.optionsFrame = new JInternalFrame("Building Options", false, true);
        this.optionsFrame.getContentPane().setLayout(new BorderLayout());
        try {
            this.optionsFrame.setFrameIcon(new ImageIcon(this.getClass().getResource("lib/city2.gif")));
        } catch (Exception ex) {
        }
        final JPanel comp = new JPanel(new GridLayout(1, 1));
        comp.setBorder(BorderFactory.createTitledBorder("Building Lobby"));
        final JPanel comp2 = new JPanel(new GridBagLayout());
        comp.add(comp2);
        this.upLobbyLabel = new JLabel("  Up Prob");
        this.upLobbySlider = new JSlider(0, 0, 100, (int) (super.upBound * 100.0));
        this.upLobbyProbLabel = new JLabel(String.valueOf((int) (super.upBound * 100.0)) + "%");
        this.exitLobbyLabel = new JLabel("Exit Prob");
        this.exitLobbySlider = new JSlider(0, 0, 100, (int) (this.exitProb * 100.0));
        this.exitLobbyProbLabel = new JLabel(String.valueOf((int) (this.exitProb * 100.0)) + "%");
        this.upLobbySlider.addChangeListener(this);
        this.upLobbySlider.setMinimumSize(new Dimension(200, 24));
        this.upLobbySlider.setPreferredSize(new Dimension(200, 24));
        this.upLobbySlider.setSize(new Dimension(200, 24));
        this.upLobbySlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        this.exitLobbySlider.addChangeListener(this);
        this.exitLobbySlider.setMinimumSize(new Dimension(200, 24));
        this.exitLobbySlider.setPreferredSize(new Dimension(200, 24));
        this.exitLobbySlider.setSize(new Dimension(200, 24));
        this.exitLobbySlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        GridBagHelper.add(comp2, this.upLobbyLabel, GridBagHelper.labelLike(0, 0, 1));
        GridBagHelper.add(comp2, this.upLobbySlider, GridBagHelper.fieldLike(1, 0, 3));
        GridBagHelper.add(comp2, this.upLobbyProbLabel, GridBagHelper.labelLike(4, 0, 1));
        GridBagHelper.add(comp2, this.exitLobbyLabel, GridBagHelper.labelLike(0, 1, 1));
        GridBagHelper.add(comp2, this.exitLobbySlider, GridBagHelper.fieldLike(1, 1, 3));
        GridBagHelper.add(comp2, this.exitLobbyProbLabel, GridBagHelper.labelLike(4, 1, 1));
        comp2.doLayout();
        final JPanel comp3 = new JPanel(new GridLayout(1, 1));
        comp3.setBorder(BorderFactory.createTitledBorder("Upper Floors"));
        final JPanel comp4 = new JPanel(new GridBagLayout());
        comp3.add(comp4);
        if (this.neighborUP != null && this.neighborUP instanceof Floor) {
            this.upLabel = new JLabel(" Up Probs");
            this.downLabel = new JLabel(" Dn Probs");
            if (((Floor) this.neighborUP).neighborUP != null && ((Floor) this.neighborUP).neighborUP instanceof Floor) {
                this.upSlider = new JSlider(0, 0, 100, (int) (this.neighborUP.upBound * 100.0));
                this.upProbLabel = new JLabel(String.valueOf((int) (this.neighborUP.upBound * 100.0)) + "%");
                this.upSlider.addChangeListener(this);
                this.upSlider.setMinimumSize(new Dimension(200, 24));
                this.upSlider.setPreferredSize(new Dimension(200, 24));
                this.upSlider.setSize(new Dimension(200, 24));
                this.upSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
                GridBagHelper.add(comp4, this.upLabel, GridBagHelper.labelLike(0, 0, 1));
                GridBagHelper.add(comp4, this.upSlider, GridBagHelper.fieldLike(1, 0, 3));
                GridBagHelper.add(comp4, this.upProbLabel, GridBagHelper.labelLike(4, 0, 1));
            }
            this.downSlider = new JSlider(0, 0, 100, (int) (this.neighborUP.downBound * 100.0));
            this.downProbLabel = new JLabel(String.valueOf((int) (this.neighborUP.upBound * 100.0)) + "%");
            this.downSlider.addChangeListener(this);
            this.downSlider.setMinimumSize(new Dimension(200, 24));
            this.downSlider.setPreferredSize(new Dimension(200, 24));
            this.downSlider.setSize(new Dimension(200, 24));
            this.downSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            GridBagHelper.add(comp4, this.downLabel, GridBagHelper.labelLike(0, 1, 1));
            GridBagHelper.add(comp4, this.downSlider, GridBagHelper.fieldLike(1, 1, 3));
            GridBagHelper.add(comp4, this.downProbLabel, GridBagHelper.labelLike(4, 1, 1));
        }
        comp4.doLayout();
        this.optionsFrame.getContentPane().add(comp, "North");
        this.optionsFrame.getContentPane().add(comp3, "South");
        City.desk.add(this.optionsFrame);
        this.optionsFrame.validate();
        this.optionsFrame.pack();
        this.optionsFrame.setLocation(455 + super.centerX / 25, 55 + super.centerY / 25);
        this.optionsFrame.setVisible(true);
    }

    public void stateChanged(final ChangeEvent changeEvent) {
        if (changeEvent.getSource() == this.upLobbySlider) {
            final int value = this.upLobbySlider.getValue();
            this.upLobbyProbLabel.setText(String.valueOf(value) + "%");
            super.upBound = value / 100.0;
        } else if (changeEvent.getSource() == this.exitLobbySlider) {
            final int value2 = this.exitLobbySlider.getValue();
            this.exitLobbyProbLabel.setText(String.valueOf(value2) + "%");
            this.exitProb = value2 / 100.0;
        } else if (changeEvent.getSource() == this.upSlider) {
            final int value3 = this.upSlider.getValue();
            Place.prob = value3 / 100.0;
            this.upProbLabel.setText(String.valueOf(value3) + "%");
            if (this.neighborUP != null && this.neighborUP instanceof Floor) {
                Floor floor = (Floor) this.neighborUP;
                floor.upBound = Place.prob;
                while (floor.neighborUP != null) {
                    floor = (Floor) floor.neighborUP;
                    floor.upBound = Place.prob;
                }
            }
        } else if (changeEvent.getSource() == this.downSlider) {
            final int value4 = this.downSlider.getValue();
            Place.prob = value4 / 100.0;
            this.downProbLabel.setText(String.valueOf(value4) + "%");
            if (this.neighborUP != null && this.neighborUP instanceof Floor) {
                Floor floor2 = (Floor) this.neighborUP;
                floor2.downBound = Place.prob;
                while (floor2.neighborUP != null) {
                    floor2 = (Floor) floor2.neighborUP;
                    floor2.downBound = Place.prob;
                }
            }
        }
    }
}
