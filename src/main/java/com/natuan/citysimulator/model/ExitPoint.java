package com.natuan.citysimulator.model;

public class ExitPoint {
    public int rx;
    public int ry;
    public Place p;
    public boolean obstructed;

    public ExitPoint(final int rx, final int ry, final Place p3) {
        this.rx = 0;
        this.ry = 0;
        this.obstructed = false;
        this.rx = rx;
        this.ry = ry;
        this.p = p3;
        this.obstructed = false;
    }

    public ExitPoint() {
        this.rx = 0;
        this.ry = 0;
        this.obstructed = false;
        this.rx = 0;
        this.ry = 0;
        this.p = null;
        this.obstructed = false;
    }
}

