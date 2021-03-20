package com.natuan.citysimulator.model;

import com.natuan.citysimulator.GUI.Building;

public class Floor extends Place {
    public Place neighborUP;
    public Place neighborDOWN;
    public Building foundation;

    public Floor() {
        this.neighborUP = null;
        this.neighborDOWN = null;
    }

    public void init(final TransPoint transPoint, final TransPoint transPoint2, final int n, final int altitude, final double n2, final double n3, final double n4, final Building foundation, final double upBound, final double downBound) {
        super.init(transPoint, transPoint2, n, altitude, n2, n3, n4);
        this.setAltitude(altitude);
        this.foundation = foundation;
        super.setUpBound(upBound);
        super.setDownBound(downBound);
    }

    public void init(final int n, final int n2, final int n3, final int n4, final int n5, final int altitude, final Building foundation, final double upBound, final double downBound) {
        super.init(new TransPoint(n, (n2 + n4) / 2), new TransPoint(n3, (n2 + n4) / 2), n5, altitude, 0.0, this.fabs(n4 - n2), this.fabs(n3 - n));
        this.setAltitude(altitude);
        this.foundation = foundation;
        super.setUpBound(upBound);
        super.setDownBound(downBound);
    }

    public boolean movePerson(final Person person) {
        Place.ix = person.xpos;
        Place.iy = person.ypos;
        Place.iz = super.altitude;
        Place.deltaX = this.moveX();
        Place.deltaY = this.moveY();
        Place.newPlace = Place.city.getPlaceMap(Place.ix + Place.deltaX, Place.iy + Place.deltaY);
        if (!(Place.newPlace instanceof Building) || Place.newPlace != this.foundation) {
            return true;
        }
        Place.ix += Place.deltaX;
        Place.iy += Place.deltaY;
        Place.prob = Place.rnd.nextDouble();
        if (Place.prob < super.upBound && this.neighborUP != null) {
            Place.iz = this.neighborUP.altitude;
            person.setPosition(Place.ix, Place.iy, Place.iz);
            person.setPlace(this.neighborUP);
            return true;
        }
        Place.prob = Place.rnd.nextDouble();
        if (Place.prob < super.downBound) {
            Place.iz = this.neighborDOWN.altitude;
            person.setPosition(Place.ix, Place.iy, Place.iz);
            person.setPlace(this.neighborDOWN);
            return true;
        }
        person.setPosition(Place.ix, Place.iy, Place.iz);
        return true;
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
}

