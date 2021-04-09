package com.natuan.citysimulator.model;

import com.natuan.citysimulator.GUI.*;
import com.natuan.citysimulator.Resources.Constants;
import com.natuan.citysimulator.interfaces.CityPlanInterface;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

public class City implements Constants {
    public static Vector entryPlace;
    public static Vector entryPlaceConductance;
    public static Vector entryX;
    public static Vector entryY;
    public static int numEntryPoints;
    public static JDesktopPane desk;
    public static Random rnd;
    public static double exitLow;
    public static double exitHigh;
    public static double enterProb;
    public static double exitProb;
    public static double downHigh;
    public static double downLow;
    public static double upProb;
    public static double downProb;
    public static String xmlPlanFile;
    private static double prob;
    private static int MAPDIM;
    private static int MAPSIZE;
    private static int MAPSCALE;
    private static int CITYSIZE;
    private static Vector placeVector;
    private static boolean useGraph;
    private static boolean show_console;
    private static boolean show_chart;
    private static String cityPlanClassName;
    private static double walkMax;

    static {
        City.rnd = new Random(17L);
        City.placeVector = new Vector();
        City.entryPlace = new Vector();
        City.entryPlaceConductance = new Vector();
        City.entryX = new Vector();
        City.entryY = new Vector();
        City.numEntryPoints = 0;
        City.useGraph = false;
    }

    public byte[][] viewMatrix;
    public byte[][] peopleMatrix;
    public PointVelocity[][] pointVelocity;
    public int[] totNumFloors;
    matrixDisplay peopleWindow;
    private Place[][] placeMap;
    private boolean fillCity;
    private JCheckBox console_check;
    private JCheckBox chart_check;
    private JSlider sbmin;
    private JSlider sbmax;

    public City(final JDesktopPane desk, final CitySimulator constants) {
        this.totNumFloors = new int[16];
        this.fillCity = true;
        City.desk = desk;
        City.useGraph = false;
        (Place.city = this).setConstants(constants);
        this.initArrays(constants);
        Place.walkMax = City.walkMax;
    }

    public City(final JDesktopPane desk, final boolean useGraph, final LocationChartFrame locationChartFrame, final CitySimulator constants) {
        this.totNumFloors = new int[16];
        this.fillCity = true;
        City.desk = desk;
        City.useGraph = useGraph;
        (Place.city = this).setConstants(constants);
        this.initArrays(constants);
        Place.walkMax = City.walkMax;
    }

    public void build() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, FileNotFoundException, IOException {
        System.out.print("Loading city plan  (class= " + City.cityPlanClassName + ")");
        final CityPlanInterface cityPlanInterface = (CityPlanInterface) Class.forName(City.cityPlanClassName).newInstance();
        if (cityPlanInterface == null) {
            throw new IllegalArgumentException("Null class " + City.cityPlanClassName);
        }
        this.zeroArrays();
        cityPlanInterface.buildPlan(this);
        this.findCityEntryPoints();
        this.makeViewMatrix();
        this.initPointVelocities();
        this.findIntersectionExitPoints();
        City.placeVector = null;
    }

    public boolean addEntryPlace(final Place obj, final int value, final int value2, final Double obj2) {
        try {
            City.entryPlace.addElement(obj);
            City.entryPlaceConductance.addElement(obj2);
            City.entryX.addElement(new Integer(value));
            City.entryY.addElement(new Integer(value2));
            return true;
        } catch (Exception ex) {
            System.out.println("Failed to add entry point");
            return false;
        }
    }

    public void createIntersection(final Intersection intersection) {
        City.placeVector.addElement(intersection);
        final int[] totNumFloors = this.totNumFloors;
        final int n = 0;
        ++totNumFloors[n];
        this.setPlaceMap(intersection);
    }

    public void createRoad(final Road road) {
        City.placeVector.addElement(road);
        this.setPlaceMap(road);
    }

    public void createBuilding(final Building building) {
        City.placeVector.addElement(building);
        this.setPlaceMap(building);
        final int[] totNumFloors = this.totNumFloors;
        final int n = 0;
        ++totNumFloors[n];
    }

    public void createGrassyField(final GrassyField grassyField) {
        City.placeVector.addElement(grassyField);
        this.setPlaceMap(grassyField);
        final int[] totNumFloors = this.totNumFloors;
        final int n = 0;
        ++totNumFloors[n];
    }

    public void createFloor(final Floor obj) {
        City.placeVector.addElement(obj);
        final int[] totNumFloors = this.totNumFloors;
        final int floor = obj.floor;
        ++totNumFloors[floor];
    }

    public void findIntersectionExitPoints() {
        for (int size = City.placeVector.size(), i = 0; i < size; ++i) {
            final Place place = (Place) City.placeVector.elementAt(i);
            if (place instanceof Intersection) {
                System.out.print(".");
                ((Intersection) place).findAllExitPoints();
            }
        }
        System.out.println(" ");
    }

    public void findCityEntryPoints() {
        final Double n = new Double(1.0);
        final int size = City.placeVector.size();
        int n2 = 9999;
        int n3 = 9999;
        int n4 = 0;
        int n5 = 0;
        for (int i = 0; i < size; ++i) {
            final Place place = (Place) City.placeVector.elementAt(i);
            if (place instanceof Road) {
                final Road road = (Road) place;
                final int x = place.endPoint1.x;
                final int y = place.endPoint1.y;
                final int x2 = place.endPoint2.x;
                final int y2 = place.endPoint2.y;
                if (x <= n2) {
                    n2 = x;
                }
                if (x2 <= n2) {
                    n2 = x2;
                }
                if (y <= n3) {
                    n3 = y;
                }
                if (y2 <= n3) {
                    n3 = y2;
                }
                if (x >= n4) {
                    n4 = x;
                }
                if (x2 >= n4) {
                    n4 = x2;
                }
                if (y >= n5) {
                    n5 = y;
                }
                if (y2 >= n5) {
                    n5 = y2;
                }
            }
        }
        for (int j = 0; j < size; ++j) {
            final Place place2 = (Place) City.placeVector.elementAt(j);
            boolean b = false;
            if (place2 instanceof Road) {
                final Road road2 = (Road) place2;
                int x3 = place2.endPoint1.x;
                int y3 = place2.endPoint1.y;
                int x4 = place2.endPoint2.x;
                int y4 = place2.endPoint2.y;
                if (x3 <= n2 && !b) {
                    x3 += (int) place2.halfWidth;
                    y3 += City.MAPSCALE + 1;
                    b = this.addEntryPlace(place2, x3, y3, n);
                }
                if (x4 <= n2 && !b) {
                    x4 += (int) place2.halfWidth;
                    y4 += City.MAPSCALE + 1;
                    b = this.addEntryPlace(place2, x4, y4, n);
                }
                if (y3 <= n3 && !b) {
                    x3 -= City.MAPSCALE + 1;
                    y3 += (int) place2.halfWidth;
                    b = this.addEntryPlace(place2, x3, y3, n);
                }
                if (y4 <= n3 && !b) {
                    x4 -= City.MAPSCALE + 1;
                    y4 += (int) place2.halfWidth;
                    b = this.addEntryPlace(place2, x4, y4, n);
                }
                if (x3 >= n4 && !b) {
                    x3 -= (int) place2.halfWidth;
                    y3 -= City.MAPSCALE + 1;
                    b = this.addEntryPlace(place2, x3, y3, n);
                }
                if (x4 >= n4 && !b) {
                    x4 -= (int) place2.halfWidth;
                    y4 -= City.MAPSCALE + 1;
                    b = this.addEntryPlace(place2, x4, y4, n);
                }
                if (y3 >= n5 && !b) {
                    b = this.addEntryPlace(place2, x3 + (City.MAPSCALE + 1), (int) (y3 - place2.halfWidth), n);
                }
                if (y4 >= n5 && !b) {
                    this.addEntryPlace(place2, x4 + (City.MAPSCALE + 1), (int) (y4 - place2.halfWidth), n);
                }
            }
        }
        City.numEntryPoints = City.entryPlace.size();
    }

    public int getColorByFloorNumber(final int n) {
        int n2 = 11 + 2 * n;
        if (n2 >= 127) {
            n2 = 126;
        }
        return n2;
    }

    public int getEntryIndex() {
        City.prob = City.rnd.nextDouble();
        City.prob *= City.numEntryPoints;
        return (int) City.prob;
    }

    public Place getMapKey(final int n, final int n2) {
        return this.placeMap[n / City.MAPSCALE][n2 / City.MAPSCALE];
    }

    public Place getMapKey(final Place place) {
        return this.placeMap[place.centerX / City.MAPSCALE][place.centerY / City.MAPSCALE];
    }

    public Place getPlaceMap(final int n, final int n2) {
        return this.placeMap[70 + n / City.MAPSCALE][70 + n2 / City.MAPSCALE];
    }

    public Place getSelectedPlacebyMouseClick(final int n, final int n2) {
        return this.placeMap[70 + n][70 + n2];
    }

    public PointVelocity getVelocity(final int n, final int n2) {
        return this.pointVelocity[n / City.MAPSCALE][n2 / City.MAPSCALE];
    }

    private void initPointVelocities() {
        for (int i = 0; i < City.MAPSIZE; ++i) {
            for (int j = 0; j < City.MAPSIZE; ++j) {
                this.pointVelocity[i][j] = this.setVelocity(i * City.MAPSCALE, j * City.MAPSCALE);
            }
        }
    }

    public void makeViewMatrix() {
        for (int i = 0; i < City.MAPSIZE; ++i) {
            for (int j = 0; j < City.MAPSIZE; ++j) {
                final Place place = this.placeMap[i + 70][j + 70];
                if (place != null) {
                    if (!(place instanceof Floor)) {
                        final byte b = (byte) place.placeColor;
                        this.viewMatrix[i][j] = b;
                        this.peopleMatrix[i][j] = b;
                    }
                } else {
                    this.viewMatrix[i][j] = 0;
                    this.peopleMatrix[i][j] = 0;
                }
            }
        }
    }

    public boolean noTraffic(final int n, final int n2) {
        boolean b = false;
        final int n3 = n / City.MAPSCALE;
        final int n4 = n2 / City.MAPSCALE;
        if (this.peopleMatrix[n3][n4] == this.viewMatrix[n3][n4]) {
            b = true;
        }
        return b;
    }

    private void setConstants(final CitySimulator citySimulator) {
        City.exitLow = citySimulator.getExitLowProb();
        City.exitHigh = citySimulator.getExitHighProb();
        City.enterProb = citySimulator.getEnterProb();
        City.exitProb = citySimulator.getExitProb();
        City.downHigh = citySimulator.getDownHighProb();
        City.downLow = citySimulator.getDownLowProb();
        City.upProb = citySimulator.getUpProb();
        City.downProb = citySimulator.getDownProb();
        City.MAPSIZE = citySimulator.getMapSize();
        City.MAPDIM = City.MAPSIZE + 140;
        City.MAPSCALE = citySimulator.getMapScale();
        City.CITYSIZE = citySimulator.getCitySize();
        City.walkMax = citySimulator.getWalkMax();
        City.cityPlanClassName = citySimulator.getCityPlanClassName();
        City.xmlPlanFile = citySimulator.getXMLPlanFile();
    }

    private void initArrays(final CitySimulator citySimulator) {
        this.placeMap = new Place[City.MAPDIM + 1][City.MAPDIM + 1];
        this.viewMatrix = new byte[City.MAPSIZE + 1][City.MAPSIZE + 1];
        this.peopleMatrix = new byte[City.MAPSIZE + 1][City.MAPSIZE + 1];
        this.pointVelocity = new PointVelocity[City.MAPSIZE + 1][City.MAPSIZE + 1];
    }

    public void setPlaceMap(final Place place) {
        final int n = (int) place.length;
        final int n2 = 0;
        final int n3 = n;
        final int n4 = -(int) place.halfWidth;
        final int n5 = (int) place.halfWidth;
        for (int i = n2; i <= n3; ++i) {
            for (int j = n4; j <= n5; ++j) {
                final double n6 = i * Math.cos(place.orientRads) - j * Math.sin(place.orientRads);
                final double n7 = j * Math.cos(place.orientRads) + i * Math.sin(place.orientRads);
                final double n8 = n6 + place.endPoint1.x;
                final double n9 = n7 + place.endPoint1.y;
                final int n10 = (int) n8 / City.MAPSCALE;
                final int n11 = (int) n9 / City.MAPSCALE;
                final Place place2 = this.placeMap[n10 + 70][n11 + 70];
                if (place != null && !(place2 instanceof Intersection)) {
                    this.placeMap[n10 + 70][n11 + 70] = place;
                }
            }
        }
    }

    public PointVelocity setVelocity(final int n, final int n2) {
        final Place placeMap = this.getPlaceMap(n, n2);
        PointVelocity siteVelocity;
        if (placeMap instanceof Road) {
            siteVelocity = ((Road) placeMap).getSiteVelocity(n, n2);
        } else {
            siteVelocity = new PointVelocity(0, 0);
        }
        return siteVelocity;
    }

    public void ShowAllPeople(final JInternalFrame comp, final byte[][] array) {
        final int n = 11;
        final int n2 = 35;
        final int n3 = 1;
        final boolean b = false;
        if (this.peopleWindow == null) {
            this.peopleWindow = new matrixDisplay(array, 0, 0, City.MAPSIZE, n3, b, City.desk, this);
            comp.setDefaultCloseOperation(1);
            comp.setBackground(Color.lightGray);
            City.desk.add(comp, 3);
            comp.getContentPane().add(this.peopleWindow);
            comp.setSize(new Dimension(n3 * City.MAPSIZE + n, n3 * City.MAPSIZE + n2));
            comp.setLocation(20, 5);
            try {
                comp.setFrameIcon(new ImageIcon(this.getClass().getResource("lib/city2.gif")));
            } catch (Exception ex) {
            }
            comp.show();
            comp.getContentPane().repaint();
        } else {
            this.peopleWindow.refresh(array, 0, 0, City.MAPSIZE, n3, b);
            this.peopleWindow.repaint();
        }
    }

    public void ShowCity(final JInternalFrame internalFrame, final byte[][] array) {
        final int n = 11;
        final int n2 = 35;
        final int n3 = 1;
        final matrixDisplay comp = new matrixDisplay(array, 0, 0, City.MAPSIZE, n3, false, City.desk, this);
        final JInternalFrame comp2 = new JInternalFrame("City Landscape", false, false, false, true);
        comp2.setDefaultCloseOperation(1);
        comp2.setBackground(Color.lightGray);
        City.desk.add(comp2, 3);
        comp2.getContentPane().add(comp);
        comp2.setSize(new Dimension(n3 * City.MAPSIZE + n, n3 * City.MAPSIZE + n2));
        comp2.setLocation(500, 10);
        comp2.show();
    }

    public boolean testEntryPlace(final Object o) {
        return City.entryPlace.contains(o);
    }

    public void zeroArrays() {
        City.rnd.setSeed(17L);
        City.prob = City.rnd.nextDouble();
        for (int i = 0; i <= City.MAPSIZE; ++i) {
            for (int j = 0; j <= City.MAPSIZE; ++j) {
                this.viewMatrix[i][j] = 0;
                this.peopleMatrix[i][j] = 0;
                this.pointVelocity[i][j] = new PointVelocity(0, 0);
            }
        }
        for (int k = 0; k <= City.MAPDIM; ++k) {
            for (int l = 0; l <= City.MAPDIM; ++l) {
                this.placeMap[k][l] = null;
            }
        }
        for (int n = 0; n < 15; ++n) {
            this.totNumFloors[n] = 0;
        }
    }
}

