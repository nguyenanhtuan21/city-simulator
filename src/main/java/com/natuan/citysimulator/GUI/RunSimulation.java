package com.natuan.citysimulator.GUI;

import com.natuan.citysimulator.Resources.Constants;
import com.natuan.citysimulator.helper.MyOutput;
import com.natuan.citysimulator.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Vector;

public class RunSimulation implements Constants {
    public static double time;
    public static Vector personVector;
    public static String fileName;
    public static String popfileName;
    static JDesktopPane desk;
    static Random rnd;
    static JInternalFrame fp;
    static double startThreshold;
    static double fillThreshold;
    static double emptyThreshold;
    static double exitLow;
    static double exitHigh;
    static double enterProb;
    static double exitProb;
    static double downHigh;
    static double downLow;
    static double upProb;
    static double downProb;
    private static int lastPersonKey;
    private static int reEnterKey;
    private static double prob;
    private static boolean useGraph;
    private static double timeDelta;
    private static int personKeyMultiplier;
    private static int reentryMultiplier;
    private static int maxRoadVel;
    private static int scaleToMeters;

    static {
        RunSimulation.reEnterKey = 0;
        RunSimulation.rnd = new Random(17L);
        RunSimulation.personVector = new Vector();
        RunSimulation.fp = new JInternalFrame("City Simulator", false, false, false, true);
        RunSimulation.useGraph = false;
    }

    public int[] numPersonsOnFloor;
    City city;
    private LocationChartFrame peopleChart;
    private SingleChartFrame popChart;
    private int plotX1;
    private int plotY1;
    private int plotY2;
    private int maxArray;
    private int plotPoints;
    private int peopleNow;
    private int maxPeopleOnFlr;
    private boolean fluidCheck;
    private int fluidStartCycle;
    private boolean fillCity;
    private boolean relaxedCity;
    private int numPeople;
    private int addMoves;
    private int finalMoves;
    private MyOutput output;
    private boolean conservePop;
    private int totPop;
    private int[] populationNow;

    public RunSimulation(final JDesktopPane desk, final boolean useGraph, final CitySimulator constants, final City city) {
        this.plotX1 = 0;
        this.plotY1 = 0;
        this.plotY2 = 0;
        this.maxArray = 500;
        this.plotPoints = 1;
        this.peopleNow = 0;
        this.maxPeopleOnFlr = 0;
        this.numPersonsOnFloor = new int[16];
        this.fluidCheck = false;
        this.fluidStartCycle = 200;
        this.fillCity = true;
        this.relaxedCity = false;
        this.conservePop = false;
        this.totPop = 0;
        RunSimulation.desk = desk;
        RunSimulation.useGraph = useGraph;
        this.setConstants(constants);
        RunSimulation.lastPersonKey = this.getPersonKeyPrefix();
        this.city = city;
        Place.currentSim = this;
    }

    public RunSimulation(final JDesktopPane desk, final boolean useGraph, final LocationChartFrame peopleChart, final CitySimulator constants, final City city) {
        this.plotX1 = 0;
        this.plotY1 = 0;
        this.plotY2 = 0;
        this.maxArray = 500;
        this.plotPoints = 1;
        this.peopleNow = 0;
        this.maxPeopleOnFlr = 0;
        this.numPersonsOnFloor = new int[16];
        this.fluidCheck = false;
        this.fluidStartCycle = 200;
        this.fillCity = true;
        this.relaxedCity = false;
        this.conservePop = false;
        this.totPop = 0;
        this.peopleChart = peopleChart;
        RunSimulation.desk = desk;
        RunSimulation.useGraph = useGraph;
        this.setConstants(constants);
        RunSimulation.lastPersonKey = this.getPersonKeyPrefix();
        this.city = city;
        Place.currentSim = this;
    }

    public void runSim() {
        int i = 0;
        System.out.println("Sending Output to filename = " + RunSimulation.fileName);
        (this.output = new MyOutput(RunSimulation.fileName, RunSimulation.popfileName)).writeFileHeader(this.numPeople, this.addMoves, this.finalMoves, 17L);
        RunSimulation.time = 0.0;
        for (int j = 0; j < this.numPeople; ++j) {
            this.addPerson();
            ++this.totPop;
        }
        RunSimulation.time = 0.0;
        this.relaxedCity = false;
        for (int k = 0; k < this.addMoves / 10; ++k) {
            this.peopleNow = this.zHistogram();
            if (this.numPersonsOnFloor[0] < this.peopleNow * RunSimulation.startThreshold) {
                break;
            }
            for (int l = 0; l < 10; ++l) {
                ++i;
                this.moveEveryone();
            }
        }
        System.out.println("Done equilibrating after " + i + " cycles.");
        this.relaxedCity = true;
        this.populationNow = new int[this.finalMoves / 10 + 1];
        for (int n = 0; n <= this.finalMoves / 10; ++n) {
            this.populationNow[n] = -1;
        }
        if (!this.conservePop) {
            (this.popChart = new SingleChartFrame("Population vs. Time", 500, 400, "Pop. vs Time", "time/10", "Population", "population", this.finalMoves / 10, this.numPeople)).setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 100, 250);
            RunSimulation.desk.add(this.popChart, 7);
            this.popChart.setVisible(true);
        }
        RunSimulation.time = 0.0;
        if (this.fluidCheck && i >= this.fluidStartCycle) {
            Road.fluid = true;
            Intersection.fluid = true;
            this.fluidCheck = false;
        }
        int m = 0;
        for (int n2 = 0; n2 <= this.finalMoves / 10; ++n2) {
            this.populationNow[n2] = this.totPop;
            this.peopleNow = this.zHistogram();
            if (RunSimulation.useGraph) {
                this.peopleChart.updateData(this.maxPeopleOnFlr, this.numPersonsOnFloor, this.city.totNumFloors);
                if (!this.conservePop) {
                    this.popChart.updateData(this.numPeople, this.populationNow);
                }
            }
            if (this.fillCity && this.numPersonsOnFloor[0] < this.peopleNow * RunSimulation.fillThreshold) {
                RunSimulation.exitProb = RunSimulation.exitHigh;
                RunSimulation.downProb = RunSimulation.downHigh;
                this.fillCity = !this.fillCity;
            }
            if (!this.fillCity && this.numPersonsOnFloor[0] > this.peopleNow * RunSimulation.emptyThreshold) {
                RunSimulation.exitProb = RunSimulation.exitLow;
                RunSimulation.downProb = RunSimulation.downLow;
                this.fillCity = !this.fillCity;
            }
            System.out.println("Cycle Number: " + m);
            for (int n3 = 0; n3 < 10; ++n3) {
                ++m;
                this.moveEveryone();
                this.output.writeFileData(RunSimulation.personVector, m, this.totPop);
                if (RunSimulation.useGraph) {
                    this.city.ShowAllPeople(RunSimulation.fp, this.city.peopleMatrix);
                }
                if (this.fluidCheck && m >= this.fluidStartCycle) {
                    Road.fluid = true;
                    Intersection.fluid = true;
                    this.fluidCheck = false;
                }
            }
        }
    }

    public void addPerson() {
        final Person obj = new Person(this.getPersonKey());
        final int entryIndex = this.city.getEntryIndex();
        final int intValue = (int) City.entryX.elementAt(entryIndex);
        final int intValue2 = (int) City.entryY.elementAt(entryIndex);
        final int n = 0;
        final Place place = (Place) City.entryPlace.elementAt(entryIndex);
        obj.setTime(RunSimulation.time += this.incrementTime(RunSimulation.personVector.size()));
        obj.setPlace(place);
        obj.setPosition(intValue, intValue2, n);
        RunSimulation.personVector.addElement(obj);
    }

    public int getPersonKey() {
        return RunSimulation.lastPersonKey += (int) 1.0;
    }

    public int getPersonKeyPrefix() {
        RunSimulation.prob = RunSimulation.rnd.nextDouble();
        RunSimulation.prob *= RunSimulation.personKeyMultiplier;
        return (int) RunSimulation.prob;
    }

    public double incrementTime(final int n) {
        RunSimulation.prob = RunSimulation.rnd.nextDouble();
        RunSimulation.prob *= RunSimulation.timeDelta;
        RunSimulation.prob += RunSimulation.timeDelta;
        return RunSimulation.prob *= 100000.0 / (n + 1);
    }

    public void moveEveryone() {
        for (int i = 0; i < RunSimulation.personVector.size(); ++i) {
            final Person person = (Person) RunSimulation.personVector.elementAt(i);
            final int n = person.xpos / 10;
            final int n2 = person.ypos / 10;
            if (RunSimulation.useGraph) {
                this.city.peopleMatrix[n][n2] = this.city.viewMatrix[n][n2];
            }
            if (person.place.movePerson(person)) {
                person.setTime(RunSimulation.time += this.incrementTime(this.numPeople));
                if (RunSimulation.useGraph) {
                    final int n3 = person.xpos / 10;
                    final int n4 = person.ypos / 10;
                    this.city.peopleMatrix[n3][n4] = 1;
                    final Place place = person.place;
                    if (place instanceof Floor) {
                        if (((Floor) place).neighborUP == null) {
                            this.city.peopleMatrix[n3][n4] = 4;
                        } else {
                            this.city.peopleMatrix[n3][n4] = 2;
                        }
                    }
                }
            } else {
                --i;
            }
        }
    }

    public int zHistogram() {
        final int size = RunSimulation.personVector.size();
        this.maxPeopleOnFlr = 0;
        for (int i = 0; i <= 15; ++i) {
            this.numPersonsOnFloor[i] = 0;
        }
        for (int j = 0; j < size; ++j) {
            Person person = (Person) RunSimulation.personVector.elementAt(j);
            final int floor = person.place.floor;
            final int[] numPersonsOnFloor = this.numPersonsOnFloor;
            final int n = floor;
            ++numPersonsOnFloor[n];
            if (this.numPersonsOnFloor[floor] > this.maxPeopleOnFlr) {
                this.maxPeopleOnFlr = this.numPersonsOnFloor[floor];
            }
        }
        this.maxPeopleOnFlr /= 10;
        ++this.maxPeopleOnFlr;
        this.maxPeopleOnFlr *= 10;
        return size;
    }

    public boolean resetPerson(final Person obj) {
        ++RunSimulation.reEnterKey;
        obj.personKey += RunSimulation.reEnterKey * RunSimulation.reentryMultiplier;
        if (this.conservePop || !this.relaxedCity) {
            RunSimulation.prob = RunSimulation.rnd.nextDouble();
            final int entryIndex = this.city.getEntryIndex();
            final int intValue = (int) City.entryX.elementAt(entryIndex);
            final int intValue2 = (int) City.entryY.elementAt(entryIndex);
            final int n = 0;
            obj.setPlace((Place) City.entryPlace.elementAt(entryIndex));
            obj.setPosition(intValue, intValue2, n);
            return true;
        }
        --this.totPop;
        obj.place = null;
        RunSimulation.personVector.removeElement(obj);
        if (this.totPop <= 0) {
            System.out.println("city is empty");
            System.exit(0);
        }
        return false;
    }

    public void setConstants(final CitySimulator citySimulator) {
        this.numPeople = citySimulator.getNumPeople();
        this.conservePop = citySimulator.getConservePop();
        this.addMoves = citySimulator.getAddMoves();
        this.finalMoves = citySimulator.getFinalMoves();
        RunSimulation.fileName = citySimulator.getFileName();
        RunSimulation.popfileName = citySimulator.getPopFileName();
        RunSimulation.startThreshold = citySimulator.getStartThreshold();
        RunSimulation.fillThreshold = citySimulator.getFillThreshold();
        RunSimulation.emptyThreshold = citySimulator.getEmptyThreshold();
        RunSimulation.exitLow = citySimulator.getExitLowProb();
        RunSimulation.exitHigh = citySimulator.getExitHighProb();
        RunSimulation.enterProb = citySimulator.getEnterProb();
        RunSimulation.exitProb = citySimulator.getExitProb();
        RunSimulation.downHigh = citySimulator.getDownHighProb();
        RunSimulation.downLow = citySimulator.getDownLowProb();
        RunSimulation.upProb = citySimulator.getUpProb();
        RunSimulation.downProb = citySimulator.getDownProb();
        this.fluidCheck = citySimulator.getUseFluid();
        this.fluidStartCycle = citySimulator.getFluidStartCycle();
        RunSimulation.timeDelta = citySimulator.getTimeDelta();
        RunSimulation.personKeyMultiplier = citySimulator.getPersonKeyMultiplier();
        RunSimulation.reentryMultiplier = citySimulator.getReentryMultiplier();
        RunSimulation.maxRoadVel = citySimulator.getMaxRoadVel();
        RunSimulation.scaleToMeters = citySimulator.getScaleToMeters();
    }
}

