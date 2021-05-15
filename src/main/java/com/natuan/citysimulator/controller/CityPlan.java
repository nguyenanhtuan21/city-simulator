package com.natuan.citysimulator.controller;

import com.natuan.citysimulator.GUI.Building;
import com.natuan.citysimulator.GUI.Intersection;
import com.natuan.citysimulator.Resources.Constants;
import com.natuan.citysimulator.abstracts.CityPlanAbstract;
import com.natuan.citysimulator.model.City;
import com.natuan.citysimulator.model.Floor;
import com.natuan.citysimulator.model.Road;
import com.natuan.citysimulator.model.TransPoint;
import com.natuan.citysimulator.xml.XML_CityWriter;

import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

public class CityPlan extends CityPlanAbstract {
    private static int numFloors;
    private static int zPeriod;
    private static Random rnd;
    private static double prob;
    private static XML_CityWriter cityWriter;
    private static boolean xmlFlag;

    static {
        CityPlan.numFloors = 0;
        CityPlan.zPeriod = 0;
        CityPlan.rnd = new Random(17L);
        CityPlan.prob = 0.0;
        CityPlan.xmlFlag = false;
    }

    public CityPlan() {
        (CityPlan.cityWriter = new XML_CityWriter()).init("c:/Temp/test_city.xml");
        CityPlan.xmlFlag = true;
    }

    public void buildPlan(final City city) {
        CityPlanAbstract.city = city;
        CityPlan.rnd.setSeed(17L);
        CityPlan.prob = CityPlan.rnd.nextDouble();
        for (int i = 0; i < 6; ++i) {
            final int n = Constants.interX[i];
            final int n2 = Constants.interY[i];
            (CityPlanAbstract.intersection[i] = new Intersection()).init(new TransPoint(n, n2 + 50), new TransPoint(n + 100, n2 + 50), 100.0, 100.0, 0.0);
            CityPlanAbstract.city.createIntersection(CityPlanAbstract.intersection[i]);
            if (CityPlan.xmlFlag) {
                CityPlan.cityWriter.addPlace(CityPlanAbstract.intersection[i]);
            }
        }
        for (int j = 0; j < 6; ++j) {
            System.out.print(".");
            final int x = CityPlanAbstract.intersection[j].endPoint1.x;
            final int n3 = CityPlanAbstract.intersection[j].endPoint1.x + 100;
            final int n4 = CityPlanAbstract.intersection[j].endPoint1.y - 50;
            final int n5 = CityPlanAbstract.intersection[j].endPoint1.y + 50;
            final int n6 = x - 1000;
            final int n7 = n6 + 500 - 100 + this.getRoadFluct();
            final int n8 = n4;
            final int n9 = n5;
            final TransPoint transPoint = new TransPoint(n6, (n8 + n9) / 2);
            final TransPoint transPoint2 = new TransPoint(n7, (n8 + n9) / 2);
            final double n10 = n7 - n6;
            final int n11 = n7 + 1;
            final int n12 = n4;
            final int n13 = x - 1;
            final int n14 = n5;
            final TransPoint transPoint3 = new TransPoint(n11, (n12 + n14) / 2);
            final TransPoint transPoint4 = new TransPoint(n13, (n12 + n14) / 2);
            final double n15 = n13 - n11;
            final int n16 = n3 + 1;
            final int n17 = n4;
            final int n18 = n3 + 500 - 100 + this.getRoadFluct() + 1;
            final int n19 = n5;
            final TransPoint transPoint5 = new TransPoint(n16, (n17 + n19) / 2);
            final TransPoint transPoint6 = new TransPoint(n18, (n17 + n19) / 2);
            final double n20 = n18 - n16;
            final int n21 = n18 + 1;
            final int n22 = n4;
            final int n23 = n3 + 1000;
            final int n24 = n5;
            final TransPoint transPoint7 = new TransPoint(n21, (n22 + n24) / 2);
            final TransPoint transPoint8 = new TransPoint(n23, (n22 + n24) / 2);
            final double n25 = n23 - n21;
            final int n26 = n4 - 1000;
            final int n27 = n26 + 500 - 100 + this.getRoadFluct();
            final int n28 = x;
            final int n29 = n3;
            final TransPoint transPoint9 = new TransPoint((n28 + n29) / 2, n26);
            final TransPoint transPoint10 = new TransPoint((n28 + n29) / 2, n27);
            final double n30 = n27 - n26;
            final int n31 = n27 + 1;
            final int n32 = x;
            final int n33 = n4 - 1;
            final int n34 = n3;
            final TransPoint transPoint11 = new TransPoint((n32 + n34) / 2, n31);
            final TransPoint transPoint12 = new TransPoint((n32 + n34) / 2, n33);
            final double n35 = n33 - n31;
            final int n36 = n5 + 1;
            final int n37 = x;
            final int n38 = n5 + 500 - 100 + this.getRoadFluct() + 1;
            final int n39 = n3;
            final TransPoint transPoint13 = new TransPoint((n37 + n39) / 2, n36);
            final TransPoint transPoint14 = new TransPoint((n37 + n39) / 2, n38);
            final double n40 = n38 - n36;
            final int n41 = n38 + 1;
            final int n42 = x;
            final int n43 = n5 + 1000;
            final int n44 = n3;
            final TransPoint transPoint15 = new TransPoint((n42 + n44) / 2, n41);
            final TransPoint transPoint16 = new TransPoint((n42 + n44) / 2, n43);
            final double n45 = n43 - n41;
            final double n46 = 2.4;
            (CityPlanAbstract.road = new Road()).init(transPoint, transPoint2, 100.0, n10, 0.0, n46, City.enterProb, City.exitProb);
            CityPlanAbstract.city.createRoad(CityPlanAbstract.road);
            if (CityPlan.xmlFlag) {
                CityPlan.cityWriter.addPlace(CityPlanAbstract.road);
            }
            (CityPlanAbstract.road = new Road()).init(transPoint3, transPoint4, 100.0, n15, 0.0, n46, City.enterProb, City.exitProb);
            CityPlanAbstract.city.createRoad(CityPlanAbstract.road);
            if (CityPlan.xmlFlag) {
                CityPlan.cityWriter.addPlace(CityPlanAbstract.road);
            }
            (CityPlanAbstract.road = new Road()).init(transPoint5, transPoint6, 100.0, n20, 0.0, n46, City.enterProb, City.exitProb);
            CityPlanAbstract.city.createRoad(CityPlanAbstract.road);
            if (CityPlan.xmlFlag) {
                CityPlan.cityWriter.addPlace(CityPlanAbstract.road);
            }
            (CityPlanAbstract.road = new Road()).init(transPoint7, transPoint8, 100.0, n25, 0.0, n46, City.enterProb, City.exitProb);
            CityPlanAbstract.city.createRoad(CityPlanAbstract.road);
            if (CityPlan.xmlFlag) {
                CityPlan.cityWriter.addPlace(CityPlanAbstract.road);
            }
            final double n47 = 1.570796326795;
            (CityPlanAbstract.road = new Road()).init(transPoint9, transPoint10, 100.0, n30, n47, n46, City.enterProb, City.exitProb);
            CityPlanAbstract.city.createRoad(CityPlanAbstract.road);
            if (CityPlan.xmlFlag) {
                CityPlan.cityWriter.addPlace(CityPlanAbstract.road);
            }
            (CityPlanAbstract.road = new Road()).init(transPoint11, transPoint12, 100.0, n35, n47, n46, City.enterProb, City.exitProb);
            CityPlanAbstract.city.createRoad(CityPlanAbstract.road);
            if (CityPlan.xmlFlag) {
                CityPlan.cityWriter.addPlace(CityPlanAbstract.road);
            }
            (CityPlanAbstract.road = new Road()).init(transPoint13, transPoint14, 100.0, n40, n47, n46, City.enterProb, City.exitProb);
            CityPlanAbstract.city.createRoad(CityPlanAbstract.road);
            if (CityPlan.xmlFlag) {
                CityPlan.cityWriter.addPlace(CityPlanAbstract.road);
            }
            (CityPlanAbstract.road = new Road()).init(transPoint15, transPoint16, 100.0, n45, n47, n46, City.enterProb, City.exitProb);
            CityPlanAbstract.city.createRoad(CityPlanAbstract.road);
            if (CityPlan.xmlFlag) {
                CityPlan.cityWriter.addPlace(CityPlanAbstract.road);
            }
            final int n48 = n11 + 10;
            final int n49 = n31 + 10;
            final int n50 = n13;
            final int n51 = n33;
            final int n52 = n11 + 10;
            final int n53 = n36;
            final int n54 = n13;
            final int n55 = n38 - 10;
            final int n56 = n16;
            final int n57 = n36;
            final int n58 = n18 - 10;
            final int n59 = n38 - 10;
            final int n60 = n16;
            final int n61 = n31 + 10;
            final int n62 = n18 - 10;
            final int n63 = n33;
            final int n64 = n6 + 10;
            final int n65 = n31 + 100 + 10 - this.getRoadFluct();
            final int n66 = n7 - 10;
            final int n67 = n33;
            final int n68 = n6 + 10;
            final int n69 = n36;
            final int n70 = n7 - 10;
            final int n71 = n38 - 100 - 10 + this.getRoadFluct();
            final int n72 = n21 + 10;
            final int n73 = n31 + 100 + 10 - this.getRoadFluct();
            final int n74 = n23 - 10;
            final int n75 = n33;
            final int n76 = n21 + 10;
            final int n77 = n36;
            final int n78 = n23 - 10;
            final int n79 = n38 - 100 - 10 + this.getRoadFluct();
            final int n80 = n6 + this.getRoadFluct();
            final int n81 = n26 + 10;
            final int n82 = n13;
            final int n83 = n27 - 10;
            final int n84 = n16;
            final int n85 = n26 + 10;
            final int n86 = n23 - this.getRoadFluct();
            final int n87 = n27 - 10;
            final int n88 = n6 + this.getRoadFluct();
            final int n89 = n41 + 10;
            final int n90 = n13;
            final int n91 = n43 - 10;
            final int n92 = n16;
            final int n93 = n41 + 10;
            final int n94 = n23 - this.getRoadFluct();
            final int n95 = n43 - 10;
            (CityPlanAbstract.building = new Building()).init(n48, n49, n50, n51, City.upProb, City.exitProb);
            CityPlanAbstract.city.createBuilding(CityPlanAbstract.building);
            this.computeFloors(CityPlanAbstract.building);
            (CityPlanAbstract.building = new Building()).init(n52, n53, n54, n55, City.upProb, City.exitProb);
            CityPlanAbstract.city.createBuilding(CityPlanAbstract.building);
            this.computeFloors(CityPlanAbstract.building);
            (CityPlanAbstract.building = new Building()).init(n56, n57, n58, n59, City.upProb, City.exitProb);
            CityPlanAbstract.city.createBuilding(CityPlanAbstract.building);
            this.computeFloors(CityPlanAbstract.building);
            (CityPlanAbstract.building = new Building()).init(n60, n61, n62, n63, City.upProb, City.exitProb);
            CityPlanAbstract.city.createBuilding(CityPlanAbstract.building);
            this.computeFloors(CityPlanAbstract.building);
            (CityPlanAbstract.building = new Building()).init(n64, n65, n66, n67, City.upProb, City.exitProb);
            CityPlanAbstract.city.createBuilding(CityPlanAbstract.building);
            this.computeFloors(CityPlanAbstract.building);
            (CityPlanAbstract.building = new Building()).init(n68, n69, n70, n71, City.upProb, City.exitProb);
            CityPlanAbstract.city.createBuilding(CityPlanAbstract.building);
            this.computeFloors(CityPlanAbstract.building);
            (CityPlanAbstract.building = new Building()).init(n72, n73, n74, n75, City.upProb, City.exitProb);
            CityPlanAbstract.city.createBuilding(CityPlanAbstract.building);
            this.computeFloors(CityPlanAbstract.building);
            (CityPlanAbstract.building = new Building()).init(n76, n77, n78, n79, City.upProb, City.exitProb);
            CityPlanAbstract.city.createBuilding(CityPlanAbstract.building);
            this.computeFloors(CityPlanAbstract.building);
            (CityPlanAbstract.building = new Building()).init(n80, n81, n82, n83, City.upProb, City.exitProb);
            CityPlanAbstract.city.createBuilding(CityPlanAbstract.building);
            this.computeFloors(CityPlanAbstract.building);
            (CityPlanAbstract.building = new Building()).init(n84, n85, n86, n87, City.upProb, City.exitProb);
            CityPlanAbstract.city.createBuilding(CityPlanAbstract.building);
            this.computeFloors(CityPlanAbstract.building);
            (CityPlanAbstract.building = new Building()).init(n88, n89, n90, n91, City.upProb, City.exitProb);
            CityPlanAbstract.city.createBuilding(CityPlanAbstract.building);
            this.computeFloors(CityPlanAbstract.building);
            (CityPlanAbstract.building = new Building()).init(n92, n93, n94, n95, City.upProb, City.exitProb);
            CityPlanAbstract.city.createBuilding(CityPlanAbstract.building);
            this.computeFloors(CityPlanAbstract.building);
        }
        XML_CityWriter.write();
        CityPlanAbstract.intersection = null;
    }

    private void computeFloors(final Building neighborDown) {
        final Vector<Floor> vector = new Vector<Floor>();
        CityPlan.zPeriod = this.getZperiod();
        CityPlan.numFloors = this.getNumFloors();
        int n = 11 + 2 * CityPlan.numFloors;
        if (n >= 127) {
            n = 126;
        }
        neighborDown.placeColor = (byte) n;
        final TransPoint endPoint1 = neighborDown.endPoint1;
        final TransPoint endPoint2 = neighborDown.endPoint2;
        final double orientRads = neighborDown.orientRads;
        final double n2 = 2.0 * neighborDown.halfWidth;
        final double length = neighborDown.length;
        final double upProb = City.upProb;
        final double downProb = City.downProb;
        Floor neighborDown2 = null;
        for (int i = 1; i <= CityPlan.numFloors; ++i) {
            final int n3 = CityPlan.zPeriod * i;
            final Floor obj = new Floor();
            obj.init(endPoint1, endPoint2, i, n3, orientRads, n2, length, neighborDown, upProb, downProb);
            if (i == 1) {
                obj.setNeighborDown(neighborDown);
                neighborDown.setNeighborUp(obj);
            } else {
                obj.setNeighborDown(neighborDown2);
                neighborDown2.setNeighborUp(obj);
                obj.setNeighborUp(null);
            }
            if (i >= CityPlan.numFloors) {
                obj.setUpBound(-1.0);
            }
            neighborDown2 = obj;
            CityPlanAbstract.city.createFloor(obj);
            vector.addElement(obj);
        }
        final Enumeration<Floor> elements = vector.elements();
        if (CityPlan.xmlFlag) {
            CityPlan.cityWriter.addBuilding(neighborDown, elements);
        }
    }

    private int getNumFloors() {
        CityPlan.prob = CityPlan.rnd.nextDouble();
        CityPlan.prob *= 10.0;
        CityPlan.prob += 5.0;
        CityPlan.prob += 0.5;
        return (int) CityPlan.prob;
    }

    private int getRoadFluct() {
        CityPlan.prob = CityPlan.rnd.nextDouble();
        CityPlan.prob *= 100.0;
        CityPlan.prob += 0.5;
        return (int) CityPlan.prob;
    }

    private int getZperiod() {
        CityPlan.prob = CityPlan.rnd.nextDouble();
        CityPlan.prob *= 10.0;
        CityPlan.prob += 9.0;
        CityPlan.prob += 0.5;
        return (int) CityPlan.prob;
    }
}

