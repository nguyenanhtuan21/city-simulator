package com.natuan.citysimulator.model;

import com.natuan.citysimulator.GUI.RunSimulation;
import com.natuan.citysimulator.Resources.Constants;

import java.util.Random;
import java.util.Vector;

public abstract class Place implements Constants {
    public static City city;
    public static RunSimulation currentSim;
    public static double prob;
    public static int i;
    public static int ix;
    public static int iy;
    public static int iz;
    public static int deltaX;
    public static int deltaY;
    public static Place newPlace;
    public static double walkMax;
    public static Random rnd;

    static {
        Place.prob = 0.0;
        Place.i = 0;
        Place.ix = 0;
        Place.iy = 0;
        Place.iz = 0;
        Place.deltaX = 0;
        Place.deltaY = 0;
        Place.newPlace = null;
        Place.rnd = new Random(17L);
    }

    public int floor;
    public int placeColor;
    public double upBound;
    public double downBound;
    public Vector occupants;
    public double orientRads;
    public TransPoint endPoint1;
    public TransPoint endPoint2;
    public double halfWidth;
    public double length;
    public int altitude;
    public int centerX;
    public int centerY;

    public Place() {
        this.floor = 0;
        this.orientRads = 0.0;
        this.halfWidth = 0.0;
        this.length = 0.0;
        this.altitude = 0;
        this.upBound = 0.0;
        this.downBound = 0.0;
        this.occupants = new Vector();
    }

    public void init(final TransPoint endPoint1, final TransPoint endPoint2, final double orientRads, final double n, final double length) {
        this.endPoint1 = endPoint1;
        this.endPoint2 = endPoint2;
        this.orientRads = orientRads;
        this.halfWidth = this.fabs(n / 2.0);
        this.length = length;
        this.centerX = (endPoint1.x + endPoint2.x) / 2;
        this.centerY = (endPoint1.y + endPoint2.y) / 2;
        this.floor = 0;
        this.altitude = 0;
    }

    public void init(final TransPoint endPoint1, final TransPoint endPoint2, final int floor, final int altitude, final double orientRads, final double n, final double length) {
        this.endPoint1 = endPoint1;
        this.endPoint2 = endPoint2;
        this.orientRads = orientRads;
        this.halfWidth = this.fabs(n / 2.0);
        this.length = length;
        this.centerX = (endPoint1.x + endPoint2.x) / 2;
        this.centerY = (endPoint1.y + endPoint2.y) / 2;
        this.floor = floor;
        this.altitude = altitude;
    }

    public void showOptionsFrame() {
    }

    public void setUpBound(final double upBound) {
        this.upBound = upBound;
    }

    public void setDownBound(final double downBound) {
        this.downBound = downBound;
    }

    public void addPerson(final String obj) {
        this.occupants.addElement(obj);
    }

    public void removePerson(final String s) {
        if (!this.occupants.removeElement(s)) {
            System.out.println("failed to remove person " + s);
        }
    }

    public int moveX() {
        Place.prob = Place.rnd.nextDouble();
        Place.prob *= 2.0;
        --Place.prob;
        Place.prob *= Place.walkMax;
        return (int) Place.prob;
    }

    public int moveY() {
        Place.prob = Place.rnd.nextDouble();
        Place.prob *= 2.0;
        --Place.prob;
        Place.prob *= Place.walkMax;
        return (int) Place.prob;
    }

    public double fabs(final double n) {
        if (n >= 0.0) {
            return n;
        }
        return -1.0 * n;
    }

    public void setAltitude(final int altitude) {
        this.altitude = altitude;
    }

    public abstract boolean movePerson(final Person p0);
}
