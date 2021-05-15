package com.natuan.citysimulator.GUI;

import com.natuan.citysimulator.model.Person;
import com.natuan.citysimulator.model.Place;
import com.natuan.citysimulator.model.Road;
import com.natuan.citysimulator.model.TransPoint;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GrassyField extends Place implements ActionListener, ChangeListener {
    static double exitProb;

    static {
        GrassyField.exitProb = 0.0;
    }

    public Place neighborUP;
    public Place neighborDOWN;
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

    public GrassyField() {
        this.neighborUP = null;
        this.neighborDOWN = null;
    }

    public void init(final int n, final int n2, final int n3, final int n4, final double exitProb) {
        super.init(new TransPoint(n, (n2 + n4) / 2), new TransPoint(n3, (n2 + n4) / 2), 0.0, this.fabs(n4 - n2), this.fabs(n3 - n));
        super.placeColor = 6;
        super.setUpBound(0.0);
        GrassyField.exitProb = exitProb;
    }

    public void init(final TransPoint transPoint, final TransPoint transPoint2, final double n, final double n2, final double n3, final double exitProb) {
        super.init(transPoint, transPoint2, n, n2, n3);
        super.setUpBound(0.0);
        super.placeColor = 6;
        GrassyField.exitProb = exitProb;
    }

    public boolean movePerson(final Person person) {
        Place.ix = person.xpos;
        Place.iy = person.ypos;
        Place.iz = 0;
        Place.deltaX = this.moveX();
        Place.deltaY = this.moveY();
        Place.newPlace = Place.city.getPlaceMap(Place.ix + Place.deltaX, Place.iy + Place.deltaY);
        if (Place.newPlace instanceof GrassyField && Place.newPlace == this) {
            Place.ix += Place.deltaX;
            Place.iy += Place.deltaY;
            person.setPosition(Place.ix, Place.iy, Place.iz);
            return true;
        }
        if (!(Place.newPlace instanceof Road)) {
            return true;
        }
        Place.prob = Place.rnd.nextDouble();
        if (Place.prob < GrassyField.exitProb) {
            Place.ix += Place.deltaX;
            Place.iy += Place.deltaY;
            person.setPosition(Place.ix, Place.iy, 0);
            person.setPlace(Place.newPlace);
            return true;
        }
        return true;
    }

    public void actionPerformed(final ActionEvent actionEvent) {
    }

    public void showOptionsFrame() {
    }

    public void stateChanged(final ChangeEvent changeEvent) {
    }
}
