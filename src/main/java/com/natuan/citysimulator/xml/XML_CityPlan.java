package com.natuan.citysimulator.xml;

import com.natuan.citysimulator.GUI.Building;
import com.natuan.citysimulator.GUI.GrassyField;
import com.natuan.citysimulator.GUI.Intersection;
import com.natuan.citysimulator.abstracts.CityPlanAbstract;
import com.natuan.citysimulator.model.City;
import com.natuan.citysimulator.model.Floor;
import com.natuan.citysimulator.model.Road;
import com.natuan.citysimulator.model.TransPoint;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.parsers.XMLParser;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class XML_CityPlan extends CityPlanAbstract {
    private static int numFloors;
    private static int zPeriod;
    private static Random rnd;
    private static double prob;
    private static XML_CityWriter cityWriter;
    private static String s;
    private static Double d;
    private static Integer anInt;
    private static double angle;
    private static double width;
    private static double length;
    private static double scale;
    private static double xMin;
    private static double yMin;
    private static int floorNum;
    private static int altitude;
    private static double enterProb;
    private static double exitProb;
    private static double velGrad;
    private static double upProb;
    private static double downProb;
    private static double x1;
    private static double y1;
    private static double x2;
    private static double y2;

    static {
        XML_CityPlan.numFloors = 0;
        XML_CityPlan.zPeriod = 0;
        XML_CityPlan.rnd = new Random(17L);
        XML_CityPlan.prob = 0.0;
        XML_CityPlan.angle = 0.0;
        XML_CityPlan.width = 0.0;
        XML_CityPlan.length = 0.0;
        XML_CityPlan.scale = 1.0;
        XML_CityPlan.xMin = 0.0;
        XML_CityPlan.yMin = 0.0;
    }

    public static Document getAndValidateDocument(final String str, final String anObject) throws IOException, FileNotFoundException {
        Document document = null;
        InputSource inputSource;
        try {
            inputSource = new InputSource(new FileReader(str));
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("XmlUtilities.getAndValidateDocument. File " + str + " not found - " + ex.toString());
        } catch (IOException ex2) {
            throw new IOException("XmlUtilities.getAndValidateDocument. IOException on file " + str + " - " + ex2.toString());
        }
        try {
            final DOMParser domParser = new DOMParser();
            domParser.parse(inputSource);
            document = domParser.getDocument();
            if (document == null) {
                System.out.println("CityPlan Document is empty");
                System.exit(0);
            }
            if (document.getNodeType() != 9) {
                System.out.println("CityPlan Document: invalid document node");
                System.exit(0);
            }
            final Element documentElement = document.getDocumentElement();
            if (anObject != null && (documentElement.getNodeType() != 1 || !documentElement.getNodeName().equals(anObject))) {
                System.out.println("CityPlan Document: invalid document node");
                System.exit(0);
            }
        } catch (Exception ex3) {
            System.out.println("CityPlan Document: invalid city plan document");
            System.exit(0);
        }
        return document;
    }

    public static Node findChildNodeByName(final Node node, final String anObject) {
        final NodeList childNodes = node.getChildNodes();
        Node node2 = null;
        for (int i = 0; i < childNodes.getLength(); ++i) {
            final Node item = childNodes.item(i);
            if (item.getNodeName().equals(anObject)) {
                node2 = item;
                break;
            }
        }
        return node2;
    }

    public static double validateLength(final TransPoint transPoint, final TransPoint transPoint2, final double d) {
        final int x = transPoint.x;
        final int y = transPoint.y;
        final int x2 = transPoint2.x;
        final int y2 = transPoint2.y;
        final double sqrt = Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
        double n = sqrt - d;
        if (n < 0.0) {
            n *= -1.0;
        }
        if (n > 2.0) {
            System.out.println("XML_CityPlan: error defining place. Specified Length does not correspond to centerline length. Check for roundoff error");
            System.out.println("XML_CityPlan: x1= " + x + ", y1= " + y);
            System.out.println("XML_CityPlan: x2= " + x2 + ", y2= " + y2);
            System.out.println("XML_CityPlan: length = " + d + ", measureLength = " + sqrt);
            System.exit(0);
        }
        if (x < 0 || y < 0 || x2 < 0 || y2 < 0) {
            System.out.println("XML_CityPlan: error defining place. Place out of bounds - Negative coordinate");
            System.exit(0);
        }
        if (x > 6300 || y > 6300 || x2 > 6300 || y2 > 6300) {
            System.out.println("XML_CityPlan: error defining place. Place out of bounds - Coordinate >= CITYSIZE");
            System.out.println("XML_CityPlan: x1= " + x + ", y1= " + y + ", length= " + d);
            System.out.println("XML_CityPlan: x2= " + x2 + ", y2= " + y2 + ", length= " + d);
            System.out.println("XML_CityPlan: CitySize is now set to 6300");
            System.exit(0);
        }
        return sqrt;
    }

    public static TransPoint ScalePoint(double n, double n2) {
        n -= XML_CityPlan.xMin;
        n2 -= XML_CityPlan.yMin;
        n *= XML_CityPlan.scale;
        n2 *= XML_CityPlan.scale;
        n += 0.5;
        n2 += 0.5;
        return new TransPoint((int) n, (int) n2);
    }

    private static TransPoint getPoint(final Node node) {
        if (node == null) {
            System.out.println("XML_CityPlan: Place not properly defined. Missing EndPoint");
            System.exit(0);
        }
        final Element element = (Element) node;
        XML_CityPlan.s = element.getAttribute("x");
        final double double1 = Double.parseDouble(XML_CityPlan.s);
        XML_CityPlan.s = element.getAttribute("y");
        return ScalePoint(double1, Double.parseDouble(XML_CityPlan.s));
    }

    private static double getScaledValue(final Element element, final String s) {
        if (element == null) {
            System.out.println("XML_CityPlan: Element null");
            System.exit(0);
        }
        return Double.valueOf(element.getAttribute(s)) * XML_CityPlan.scale;
    }

    public void buildPlan(final City city) {
        CityPlanAbstract.city = city;
        final String s = "CityPlan";
        final String xmlPlanFile = City.xmlPlanFile;
        Document andValidateDocument = null;
        try {
            andValidateDocument = getAndValidateDocument(xmlPlanFile, s);
        } catch (Exception ex) {
            System.out.println("XML_CityPlan: unable to open plan document " + xmlPlanFile);
            ex.getMessage();
            System.exit(0);
        }
        final Element documentElement = andValidateDocument.getDocumentElement();
        XML_CityPlan.s = documentElement.getAttribute("CityScale");
        XML_CityPlan.scale = Double.valueOf(XML_CityPlan.s);
        XML_CityPlan.s = documentElement.getAttribute("Xmin");
        XML_CityPlan.xMin = Double.valueOf(XML_CityPlan.s);
        XML_CityPlan.s = documentElement.getAttribute("Ymin");
        XML_CityPlan.yMin = Double.valueOf(XML_CityPlan.s);
        if (XML_CityPlan.scale <= 0.0) {
            System.out.println("CityScale invalid in xml city plan");
            System.exit(0);
        }
        final NodeList elementsByTagName = documentElement.getElementsByTagName("Intersection");
        System.out.println(" ");
        System.out.println("Reading and Validating " + elementsByTagName.getLength() + " Intersection(s)");
        for (int i = 0; i < elementsByTagName.getLength(); ++i) {
            final Node item = elementsByTagName.item(i);
            final Element element = (Element) item;
            XML_CityPlan.width = getScaledValue(element, "Width");
            XML_CityPlan.length = getScaledValue(element, "Length");
            XML_CityPlan.s = element.getAttribute("Angle");
            XML_CityPlan.angle = Double.valueOf(XML_CityPlan.s);
            final TransPoint point = getPoint(findChildNodeByName(item, "EndPoint1"));
            final TransPoint point2 = getPoint(findChildNodeByName(item, "EndPoint2"));
            XML_CityPlan.length = validateLength(point, point2, XML_CityPlan.length);
            final Intersection intersection = new Intersection();
            intersection.init(point, point2, XML_CityPlan.width, XML_CityPlan.length, XML_CityPlan.angle);
            CityPlanAbstract.city.createIntersection(intersection);
        }
        final NodeList elementsByTagName2 = documentElement.getElementsByTagName("Road");
        System.out.println("Reading and Validating " + elementsByTagName2.getLength() + " Road(s)");
        for (int j = 0; j < elementsByTagName2.getLength(); ++j) {
            final Node item2 = elementsByTagName2.item(j);
            final Element element2 = (Element) item2;
            XML_CityPlan.width = getScaledValue(element2, "Width");
            XML_CityPlan.length = getScaledValue(element2, "Length");
            XML_CityPlan.s = element2.getAttribute("Angle");
            XML_CityPlan.angle = Double.valueOf(XML_CityPlan.s);
            final TransPoint point3 = getPoint(findChildNodeByName(item2, "EndPoint1"));
            final TransPoint point4 = getPoint(findChildNodeByName(item2, "EndPoint2"));
            XML_CityPlan.length = validateLength(point3, point4, XML_CityPlan.length);
            final Node childNodeByName = findChildNodeByName(item2, "MotionRules");
            if (childNodeByName == null) {
                System.out.println("XML_CityPlan: Road not properly defined. Missing MotionRules");
                System.exit(0);
            }
            final Element element3 = (Element) childNodeByName;
            XML_CityPlan.s = element3.getAttribute("EnterProb");
            XML_CityPlan.enterProb = Double.valueOf(XML_CityPlan.s);
            XML_CityPlan.s = element3.getAttribute("ExitProb");
            XML_CityPlan.exitProb = Double.valueOf(XML_CityPlan.s);
            XML_CityPlan.s = element3.getAttribute("VelGradient");
            XML_CityPlan.velGrad = Double.valueOf(XML_CityPlan.s);
            final Road road = new Road();
            road.init(point3, point4, XML_CityPlan.width, XML_CityPlan.length, XML_CityPlan.angle, XML_CityPlan.velGrad, XML_CityPlan.enterProb, XML_CityPlan.exitProb);
            CityPlanAbstract.city.createRoad(road);
        }
        final NodeList elementsByTagName3 = documentElement.getElementsByTagName("GrassyField");
        System.out.println("Reading and Validating " + elementsByTagName3.getLength() + " GrassyField(s)");
        for (int k = 0; k < elementsByTagName3.getLength(); ++k) {
            final Node item3 = elementsByTagName3.item(k);
            final Element element4 = (Element) item3;
            XML_CityPlan.width = getScaledValue(element4, "Width");
            XML_CityPlan.length = getScaledValue(element4, "Length");
            XML_CityPlan.s = element4.getAttribute("Angle");
            XML_CityPlan.angle = Double.valueOf(XML_CityPlan.s);
            final TransPoint point5 = getPoint(findChildNodeByName(item3, "EndPoint1"));
            final TransPoint point6 = getPoint(findChildNodeByName(item3, "EndPoint2"));
            XML_CityPlan.length = validateLength(point5, point6, XML_CityPlan.length);
            final Node childNodeByName2 = findChildNodeByName(item3, "MotionRules");
            if (childNodeByName2 == null) {
                System.out.println("XML_CityPlan: GrassyField not properly defined. Missing MotionRules");
                System.exit(0);
            }
            XML_CityPlan.s = ((Element) childNodeByName2).getAttribute("ExitProb");
            XML_CityPlan.exitProb = Double.valueOf(XML_CityPlan.s);
            final GrassyField grassyField = new GrassyField();
            grassyField.init(point5, point6, XML_CityPlan.angle, XML_CityPlan.width, XML_CityPlan.length, XML_CityPlan.exitProb);
            CityPlanAbstract.city.createGrassyField(grassyField);
        }
        final NodeList elementsByTagName4 = documentElement.getElementsByTagName("Building");
        System.out.print("Reading and Validating " + elementsByTagName4.getLength() + " Building(s)");
        int l;
        int n;
        for (l = elementsByTagName4.getLength(), n = 0; l >= 10; l /= 10, ++n) {
        }
        int n2 = n * 10;
        if (n2 <= 0) {
            n2 = 1;
        }
        for (int n3 = 0; n3 < elementsByTagName4.getLength(); ++n3) {
            if (n3 % n2 == 0) {
                System.out.print(".");
            }
            final Node item4 = elementsByTagName4.item(n3);
            final Element element5 = (Element) item4;
            XML_CityPlan.width = getScaledValue(element5, "Width");
            XML_CityPlan.length = getScaledValue(element5, "Length");
            XML_CityPlan.s = element5.getAttribute("Angle");
            XML_CityPlan.angle = Double.valueOf(XML_CityPlan.s);
            final TransPoint point7 = getPoint(findChildNodeByName(item4, "EndPoint1"));
            final TransPoint point8 = getPoint(findChildNodeByName(item4, "EndPoint2"));
            XML_CityPlan.length = validateLength(point7, point8, XML_CityPlan.length);
            final Node childNodeByName3 = findChildNodeByName(item4, "MotionRules");
            if (childNodeByName3 == null) {
                System.out.println("XML_CityPlan: Building not properly defined. Missing MotionRules");
                System.exit(0);
            }
            final Element element6 = (Element) childNodeByName3;
            XML_CityPlan.s = element6.getAttribute("UpProb");
            XML_CityPlan.upProb = Double.valueOf(XML_CityPlan.s);
            XML_CityPlan.s = element6.getAttribute("ExitProb");
            XML_CityPlan.exitProb = Double.valueOf(XML_CityPlan.s);
            final Building building = new Building();
            building.init(point7, point8, XML_CityPlan.angle, XML_CityPlan.width, XML_CityPlan.length, XML_CityPlan.upProb, XML_CityPlan.exitProb);
            final NodeList elementsByTagName5 = ((Element) item4).getElementsByTagName("Floor");
            building.placeColor = CityPlanAbstract.city.getColorByFloorNumber(elementsByTagName5.getLength());
            CityPlanAbstract.city.createBuilding(building);
            if (elementsByTagName5.getLength() >= 1) {
                this.createFloors(building, elementsByTagName5);
            }
        }
    }

    public void createFloors(final Building neighborDown, final NodeList list) {
        Floor neighborDown2 = null;
        for (int i = 0; i < list.getLength(); ++i) {
            final Node item = list.item(i);
            final Element element = (Element) item;
            XML_CityPlan.width = getScaledValue(element, "Width");
            XML_CityPlan.length = getScaledValue(element, "Length");
            XML_CityPlan.s = element.getAttribute("Angle");
            XML_CityPlan.angle = Double.valueOf(XML_CityPlan.s);
            XML_CityPlan.s = element.getAttribute("Altitude");
            XML_CityPlan.altitude = Integer.valueOf(XML_CityPlan.s);
            XML_CityPlan.s = element.getAttribute("FloorNum");
            XML_CityPlan.floorNum = Integer.valueOf(XML_CityPlan.s);
            final TransPoint point = getPoint(findChildNodeByName(item, "EndPoint1"));
            final TransPoint point2 = getPoint(findChildNodeByName(item, "EndPoint2"));
            XML_CityPlan.length = validateLength(point, point2, XML_CityPlan.length);
            final Node childNodeByName = findChildNodeByName(item, "MotionRules");
            if (childNodeByName == null) {
                System.out.println("XML_CityPlan: Building not properly defined. Missing MotionRules");
                System.exit(0);
            }
            final Element element2 = (Element) childNodeByName;
            XML_CityPlan.s = element2.getAttribute("UpProb");
            XML_CityPlan.upProb = Double.valueOf(XML_CityPlan.s);
            XML_CityPlan.s = element2.getAttribute("DownProb");
            XML_CityPlan.downProb = Double.valueOf(XML_CityPlan.s);
            final Floor floor = new Floor();
            floor.init(point, point2, XML_CityPlan.floorNum, XML_CityPlan.altitude, XML_CityPlan.angle, XML_CityPlan.width, XML_CityPlan.length, neighborDown, XML_CityPlan.upProb, XML_CityPlan.downProb);
            if (i == 0) {
                floor.setNeighborDown(neighborDown);
                neighborDown.setNeighborUp(floor);
            } else {
                floor.setNeighborDown(neighborDown2);
                neighborDown2.setNeighborUp(floor);
                floor.setNeighborUp(null);
            }
            if (i >= list.getLength()) {
                floor.setUpBound(-1.0);
            }
            neighborDown2 = floor;
            CityPlanAbstract.city.createFloor(floor);
        }
    }
}
