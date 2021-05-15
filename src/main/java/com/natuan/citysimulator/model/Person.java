package com.natuan.citysimulator.model;

import com.natuan.citysimulator.Resources.Constants;

public class Person implements Constants {
    public int personKey;
    public Place place;
    public int xpos;
    public int ypos;
    public int zpos;
    public double time;

    public Person(final int personKey) {
        this.personKey = personKey;
        this.time = 0.0;
    }

    public void setPlace(final Place place) {
        this.place = place;
    }

    public void setTime(final double time) {
        this.time = time;
    }

    public void setPosition(final int xpos, final int ypos, final int zpos) {
        this.xpos = xpos;
        this.ypos = ypos;
        this.zpos = zpos;
    }
}

