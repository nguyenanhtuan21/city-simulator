package com.natuan.citysimulator.xml;

import com.natuan.citysimulator.GUI.Building;
import com.natuan.citysimulator.GUI.Intersection;
import com.natuan.citysimulator.model.Floor;
import com.natuan.citysimulator.model.Place;
import com.natuan.citysimulator.model.Road;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.serialize.BaseMarkupSerializer;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;

public class XML_CityWriter {
    private static File xml_file;
    private static Element root;
    private static Document doc;

    static {
        XML_CityWriter.root = null;
        XML_CityWriter.doc = null;
    }

    DOMParser xmlparser;

    public static synchronized void write() {
        try {
            final FileWriter fileWriter = new FileWriter(XML_CityWriter.xml_file);
            writeDocument(XML_CityWriter.doc, fileWriter);
            fileWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void writeDocument(final Document document, final Writer writer) throws IOException {
        ((BaseMarkupSerializer) new XMLSerializer(writer, new OutputFormat(document, "UTF-8", true))).serialize(document);
    }

    public void init(final String pathname) {
        try {
            XML_CityWriter.xml_file = new File(pathname);
            this.xmlparser = new DOMParser();
            XML_CityWriter.doc = (Document) new DocumentImpl();
            XML_CityWriter.root = XML_CityWriter.doc.createElement("CityPlan");
            XML_CityWriter.doc.appendChild(XML_CityWriter.root);
        } catch (Exception ex) {
            System.out.println("XML_CityWriter Error: make certain xerces.jar is in your classpath ");
            ex.getMessage();
            ex.printStackTrace();
        }
    }

    public void addPlace(final Place place) {
        if (place instanceof Road) {
            final Road road = (Road) place;
            final Element element = XML_CityWriter.doc.createElement("Road");
            final String string = Double.toString(2.0 * place.halfWidth);
            final String string2 = Double.toString(place.length);
            final String string3 = Double.toString(place.orientRads);
            final String string4 = Double.toString(road.velGradient);
            final String string5 = Double.toString(road.enterProb);
            final String string6 = Double.toString(road.exitProb);
            final String string7 = Integer.toString(place.endPoint1.x);
            final String string8 = Integer.toString(place.endPoint1.y);
            final String string9 = Integer.toString(place.endPoint2.x);
            final String string10 = Integer.toString(place.endPoint2.y);
            element.setAttribute("Width", string);
            element.setAttribute("Length", string2);
            element.setAttribute("Angle", string3);
            final Element element2 = XML_CityWriter.doc.createElement("MotionRules");
            element2.setAttribute("VelGradient", string4);
            element2.setAttribute("EnterProb", string5);
            element2.setAttribute("ExitProb", string6);
            final Element element3 = XML_CityWriter.doc.createElement("EndPoint1");
            element3.setAttribute("x", string7);
            element3.setAttribute("y", string8);
            final Element element4 = XML_CityWriter.doc.createElement("EndPoint2");
            element4.setAttribute("x", string9);
            element4.setAttribute("y", string10);
            element.appendChild(element3);
            element.appendChild(element4);
            element.appendChild(element2);
            XML_CityWriter.root.appendChild(element);
        } else if (place instanceof Intersection) {
            final Intersection intersection = (Intersection) place;
            final Element element5 = XML_CityWriter.doc.createElement("Intersection");
            final String string11 = new Double(2.0 * place.halfWidth).toString();
            final String string12 = new Double(place.length).toString();
            final String string13 = new Double(place.orientRads).toString();
            final String string14 = new Integer(place.endPoint1.x).toString();
            final String string15 = new Integer(place.endPoint1.y).toString();
            final String string16 = new Integer(place.endPoint2.x).toString();
            final String string17 = new Integer(place.endPoint2.y).toString();
            element5.setAttribute("Width", string11);
            element5.setAttribute("Length", string12);
            element5.setAttribute("Angle", string13);
            final Element element6 = XML_CityWriter.doc.createElement("EndPoint1");
            element6.setAttribute("x", string14);
            element6.setAttribute("y", string15);
            final Element element7 = XML_CityWriter.doc.createElement("EndPoint2");
            element7.setAttribute("x", string16);
            element7.setAttribute("y", string17);
            element5.appendChild(element6);
            element5.appendChild(element7);
            XML_CityWriter.root.appendChild(element5);
        }
    }

    public void addBuilding(final Building building, final Enumeration enumeration) {
        final Element element = XML_CityWriter.doc.createElement("Building");
        final String string = Double.toString(2.0 * building.halfWidth);
        final String string2 = Double.toString(building.length);
        final String string3 = Double.toString(building.orientRads);
        final String string4 = Double.toString(building.upBound);
        final String string5 = Double.toString(building.exitProb);
        final String string6 = Integer.toString(building.endPoint1.x);
        final String string7 = Integer.toString(building.endPoint1.y);
        final String string8 = Integer.toString(building.endPoint2.x);
        final String string9 = Integer.toString(building.endPoint2.y);
        element.setAttribute("Width", string);
        element.setAttribute("Length", string2);
        element.setAttribute("Angle", string3);
        final Element element2 = XML_CityWriter.doc.createElement("MotionRules");
        element2.setAttribute("UpProb", string4);
        element2.setAttribute("ExitProb", string5);
        final Element element3 = XML_CityWriter.doc.createElement("EndPoint1");
        element3.setAttribute("x", string6);
        element3.setAttribute("y", string7);
        final Element element4 = XML_CityWriter.doc.createElement("EndPoint2");
        element4.setAttribute("x", string8);
        element4.setAttribute("y", string9);
        element.appendChild(element3);
        element.appendChild(element4);
        element.appendChild(element2);
        XML_CityWriter.root.appendChild(element);
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                final Floor floor = (Floor) enumeration.nextElement();
                final Element element5 = XML_CityWriter.doc.createElement("Floor");
                final String string10 = Double.toString(2.0 * building.halfWidth);
                final String string11 = Double.toString(building.length);
                final String string12 = Double.toString(building.orientRads);
                final String string13 = Integer.toString(floor.floor);
                final String string14 = Integer.toString(floor.altitude);
                final String string15 = Double.toString(building.upBound);
                final String string16 = Double.toString(building.downBound);
                final String string17 = Integer.toString(building.endPoint1.x);
                final String string18 = Integer.toString(building.endPoint1.y);
                final String string19 = Integer.toString(building.endPoint2.x);
                final String string20 = Integer.toString(building.endPoint2.y);
                element5.setAttribute("Width", string10);
                element5.setAttribute("Length", string11);
                element5.setAttribute("Angle", string12);
                element5.setAttribute("FloorNum", string13);
                element5.setAttribute("Altitude", string14);
                final Element element6 = XML_CityWriter.doc.createElement("MotionRules");
                element6.setAttribute("UpProb", string15);
                element6.setAttribute("DownProb", string16);
                final Element element7 = XML_CityWriter.doc.createElement("EndPoint1");
                element7.setAttribute("x", string17);
                element7.setAttribute("y", string18);
                final Element element8 = XML_CityWriter.doc.createElement("EndPoint2");
                element8.setAttribute("x", string19);
                element8.setAttribute("y", string20);
                element5.appendChild(element7);
                element5.appendChild(element8);
                element5.appendChild(element6);
                element.appendChild(element5);
            }
        }
    }
}

