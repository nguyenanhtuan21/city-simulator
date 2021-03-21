package com.natuan.citysimulator.GUI;

import com.natuan.citysimulator.Resources.Constants;
import com.natuan.citysimulator.controller.CityWindowAdapter;
import com.natuan.citysimulator.model.City;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ResourceBundle;

public class CitySimulator extends JFrame implements Constants {
    public static boolean useGraph;
    public static ResourceBundle _citySimProps;
    static JDesktopPane desk;
    static String _citySimulatorName;
    static String _citySimulatorVersion;
    static String file_name;
    static String pop_hist_name;
    static String xml_plan;
    static String data_directory;
    static File dump_directory;
    static boolean storeData;
    static int numPeople;
    static int addMoves;
    static int finalMoves;
    static double startThreshold;
    static double fillThreshold;
    static double emptyThreshold;
    static boolean conservePop;
    static int fluidStartCycle;
    static boolean useFluid;
    static double exitLow;
    static double exitHigh;
    static double enterProb;
    static double exitProb;
    static double downHigh;
    static double downLow;
    static double upProb;
    static double downProb;
    static String graphMode;
    static LocationChartFrame peopleChart;
    private static String cityPlanClassName;
    private static double walkMax;
    private static double timeDelta;
    private static int personKeyMultiplier;
    private static int reentryMultiplier;
    private static int maxRoadVel;
    private static int mapScale;
    private static int scaleToMeters;
    private static int citySize;
    private static int mapSize;
    private static CitySimulator myself;

    static {
        CitySimulator.desk = new JDesktopPane();
        CitySimulator._citySimulatorName = null;
        CitySimulator._citySimulatorVersion = null;
        CitySimulator.file_name = "myname.dat";
        CitySimulator.pop_hist_name = " ";
        CitySimulator.xml_plan = "city_plan.xml";
        CitySimulator.data_directory = ".";
        CitySimulator.storeData = true;
        CitySimulator.numPeople = 1000;
        CitySimulator.addMoves = 2000;
        CitySimulator.finalMoves = 100;
        CitySimulator.startThreshold = 0.15;
        CitySimulator.fillThreshold = 0.09;
        CitySimulator.emptyThreshold = 0.5;
        CitySimulator.conservePop = true;
        CitySimulator.fluidStartCycle = 300;
        CitySimulator.useFluid = false;
        CitySimulator.exitLow = 0.1;
        CitySimulator.exitHigh = 0.9;
        CitySimulator.enterProb = 0.1;
        CitySimulator.exitProb = CitySimulator.exitLow;
        CitySimulator.downHigh = 0.06;
        CitySimulator.downLow = 0.03;
        CitySimulator.upProb = 0.03;
        CitySimulator.downProb = CitySimulator.downLow;
        CitySimulator.graphMode = "true";
        CitySimulator.cityPlanClassName = "com.natuan.CitySimulator.CityPlan";
        CitySimulator.walkMax = 10.0;
        CitySimulator.timeDelta = 2.0E-4;
        CitySimulator.personKeyMultiplier = 0;
        CitySimulator.reentryMultiplier = 0;
        CitySimulator.maxRoadVel = 120;
        CitySimulator.mapScale = 10;
        CitySimulator.scaleToMeters = 5;
        CitySimulator.citySize = 6300;
        CitySimulator.mapSize = 630;
        CitySimulator._citySimProps = ResourceBundle.getBundle("CitySimulator");
    }

    public CitySimulator(final boolean useGraph, final String file_name, final String data_directory, final int numPeople, final int addMoves, final int finalMoves, final double startThreshold, final double fillThreshold, final double emptyThreshold, final boolean useFluid, final int fluidStartCycle) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, FileNotFoundException, IOException {
        CitySimulator.useGraph = useGraph;
        CitySimulator.file_name = file_name;
        CitySimulator.data_directory = data_directory;
        CitySimulator.numPeople = numPeople;
        CitySimulator.addMoves = addMoves;
        CitySimulator.finalMoves = finalMoves;
        CitySimulator.startThreshold = startThreshold;
        CitySimulator.fillThreshold = fillThreshold;
        CitySimulator.emptyThreshold = emptyThreshold;
        CitySimulator.useFluid = useFluid;
        CitySimulator.fluidStartCycle = fluidStartCycle;
        CitySimulator.myself = this;
        if (CitySimulator.useGraph) {
            final Console comp = new Console("Console:     Runtime Data", null, null);
            comp.setLocation(10, 480);
            CitySimulator.desk.add(comp, 4);
            comp.setVisible(true);
            UIManager.put("InternalFrame.titleFont", new Font("Dialog", 1, 9));
            final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            this.setBounds(0, 0, screenSize.width - 100, screenSize.height - 20);
            this.setContentPane(CitySimulator.desk);
            this.setFont(new Font("Helvetica", 0, 6));
            this.addWindowListener(new CityWindowAdapter());
            this.setJMenuBar(this.createMenuBar());
            try {
                this.setIconImage(new ImageIcon().getImage());
            } catch (Exception ex) {
            }
            this.setVisible(true);
            final Input_data input_data = new Input_data();
            final JFrame initialize = input_data.Initialize(CitySimulator.myself);
            try {
                initialize.setIconImage(new ImageIcon().getImage());
            } catch (Exception ex2) {
            }
            while (!input_data.start) {
                try {
                    Thread.sleep(500L);
                } catch (Exception ex3) {
                }
            }
            this.InitComponents(input_data);
            input_data.winClose(initialize);
        }
        System.out.println(" ");
        setAllDirectories();
        RunSimulation runSimulation;
        if (CitySimulator.useGraph) {
            CitySimulator.peopleChart = new LocationChartFrame("Floor Population Histogram", 700, 300, "Number (People/Floors) vs Floor index", "Floor Index", "Num People", "Num Floors", "People ", "Floors ", 15, CitySimulator.numPeople, 80);
            this.InitPeopleChart();
            final City city = new City(CitySimulator.desk, CitySimulator.useGraph, CitySimulator.peopleChart, CitySimulator.myself);
            city.build();
            System.gc();
            runSimulation = new RunSimulation(CitySimulator.desk, CitySimulator.useGraph, CitySimulator.peopleChart, CitySimulator.myself, city);
        } else {
            final City city2 = new City(CitySimulator.desk, CitySimulator.myself);
            city2.build();
            System.gc();
            runSimulation = new RunSimulation(CitySimulator.desk, CitySimulator.useGraph, CitySimulator.myself, city2);
        }
        runSimulation.runSim();
        System.out.println("All done");
        if (!CitySimulator.useGraph) {
            System.exit(0);
        }
    }

    public static CitySimulator getMyself() {
        return CitySimulator.myself;
    }

    public static void main(final String[] array) {
        boolean b = true;
        boolean b2 = CitySimulator.useFluid;
        int n = CitySimulator.fluidStartCycle;
        readPropertiesFile();
        if (array.length > 0) {
            CitySimulator.graphMode = array[0];
            if (CitySimulator.graphMode.equalsIgnoreCase("true")) {
                b = true;
            }
            if (CitySimulator.graphMode.equalsIgnoreCase("false")) {
                b = false;
            }
            if (CitySimulator.graphMode.equalsIgnoreCase("none")) {
                b = false;
            }
        }
        if (array.length > 1) {
            CitySimulator.file_name = array[1];
        }
        if (array.length > 2) {
            CitySimulator.data_directory = array[2];
        }
        if (array.length > 3) {
            CitySimulator.numPeople = Integer.parseInt(array[3]);
        }
        if (array.length > 4) {
            CitySimulator.addMoves = Integer.parseInt(array[4]);
        }
        if (array.length > 5) {
            CitySimulator.finalMoves = Integer.parseInt(array[5]);
        }
        if (array.length > 6) {
            CitySimulator.startThreshold = Double.parseDouble(array[6]);
        }
        if (array.length > 7) {
            CitySimulator.fillThreshold = Double.parseDouble(array[7]);
        }
        if (array.length > 8) {
            CitySimulator.emptyThreshold = Double.parseDouble(array[8]);
        }
        if (array.length > 10) {
            b2 = array[9].equalsIgnoreCase("true");
            if (b2) {
                n = Integer.parseInt(array[10]);
            }
        }
        try {
            final CitySimulator citySimulator = new CitySimulator(b, CitySimulator.file_name, CitySimulator.data_directory, CitySimulator.numPeople, CitySimulator.addMoves, CitySimulator.finalMoves, CitySimulator.startThreshold, CitySimulator.fillThreshold, CitySimulator.emptyThreshold, b2, n);
        } catch (Exception ex) {
            System.out.println("CitySimulator Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void readPropertiesFile() {
        try {
            CitySimulator._citySimulatorName = CitySimulator._citySimProps.getString("name");
            CitySimulator._citySimulatorVersion = CitySimulator._citySimProps.getString("version");
            System.out.println(" " + CitySimulator._citySimulatorName + " Verson:" + CitySimulator._citySimulatorVersion);
            CitySimulator.numPeople = Integer.parseInt(CitySimulator._citySimProps.getString("numPeople"));
            CitySimulator.conservePop = Boolean.parseBoolean(CitySimulator._citySimProps.getString("conservePopulation"));
            CitySimulator.addMoves = Integer.parseInt(CitySimulator._citySimProps.getString("relaxMoves"));
            CitySimulator.finalMoves = Integer.parseInt(CitySimulator._citySimProps.getString("finalMoves"));
            CitySimulator.file_name = CitySimulator._citySimProps.getString("fileName");
            CitySimulator.data_directory = CitySimulator._citySimProps.getString("dataDirectory");
            CitySimulator.xml_plan = CitySimulator._citySimProps.getString("xmlPlanFile");
            CitySimulator.startThreshold = Double.parseDouble(CitySimulator._citySimProps.getString("startThreshold"));
            CitySimulator.fillThreshold = Double.parseDouble(CitySimulator._citySimProps.getString("fillThreshold"));
            CitySimulator.emptyThreshold = Double.parseDouble(CitySimulator._citySimProps.getString("emptyThreshold"));
            CitySimulator.useFluid = Boolean.parseBoolean(CitySimulator._citySimProps.getString("fluidCheck"));
            if (CitySimulator.useFluid) {
                CitySimulator.fluidStartCycle = Integer.parseInt(CitySimulator._citySimProps.getString("fluidStartCycle"));
            }
            CitySimulator.exitHigh = Double.parseDouble(CitySimulator._citySimProps.getString("exitHigh"));
            CitySimulator.exitLow = Double.parseDouble(CitySimulator._citySimProps.getString("exitLow"));
            CitySimulator.downHigh = Double.parseDouble(CitySimulator._citySimProps.getString("downHigh"));
            CitySimulator.downLow = Double.parseDouble(CitySimulator._citySimProps.getString("downLow"));
            CitySimulator.enterProb = Double.parseDouble(CitySimulator._citySimProps.getString("enterProb"));
            CitySimulator.upProb = Double.parseDouble(CitySimulator._citySimProps.getString("upProb"));
            CitySimulator.exitProb = CitySimulator.exitLow;
            CitySimulator.downProb = CitySimulator.downLow;
            CitySimulator.cityPlanClassName = CitySimulator._citySimProps.getString("className");
            CitySimulator.storeData = Boolean.valueOf(CitySimulator._citySimProps.getString("storeData"));
            CitySimulator.walkMax = Double.valueOf(CitySimulator._citySimProps.getString("walkMax"));
            CitySimulator.timeDelta = Double.valueOf(CitySimulator._citySimProps.getString("timeDelta"));
            CitySimulator.personKeyMultiplier = Integer.parseInt(CitySimulator._citySimProps.getString("PERSONKEYMULTIPLIER"));
            CitySimulator.reentryMultiplier = Integer.parseInt(CitySimulator._citySimProps.getString("REENTRYMULTIPLIER"));
            CitySimulator.maxRoadVel = Integer.parseInt(CitySimulator._citySimProps.getString("MAXROADVEL"));
            CitySimulator.mapScale = Integer.parseInt(CitySimulator._citySimProps.getString("MAPSCALE"));
            CitySimulator.scaleToMeters = Integer.parseInt(CitySimulator._citySimProps.getString("SCALETOMETERS"));
            CitySimulator.citySize = Integer.parseInt(CitySimulator._citySimProps.getString("CITYSIZE"));
            CitySimulator.mapSize = Integer.parseInt(CitySimulator._citySimProps.getString("MAPSIZE"));
        } catch (Exception ex) {
            System.out.println("CitySimulator.properities: file corrupt or missing property ");
            ex.getMessage();
            System.exit(0);
        }
    }

    public static void setAllDirectories() {
        final char oldChar = '/';
        final char oldChar2 = '\\';
        CitySimulator.data_directory.length();
        if (!CitySimulator.data_directory.endsWith("\\") && !CitySimulator.data_directory.endsWith("/")) {
            CitySimulator.data_directory += "\\";
        }
        CitySimulator.file_name.replace(oldChar, File.separatorChar);
        CitySimulator.file_name.replace(oldChar2, File.separatorChar);
        CitySimulator.data_directory.replace(oldChar, File.separatorChar);
        CitySimulator.data_directory.replace(oldChar2, File.separatorChar);
        CitySimulator.xml_plan.replace(oldChar, File.separatorChar);
        CitySimulator.xml_plan.replace(oldChar2, File.separatorChar);
        CitySimulator.dump_directory = new File(CitySimulator.data_directory);
        if (!CitySimulator.dump_directory.isDirectory()) {
            System.out.println("Data Directory " + CitySimulator.data_directory + " is invalid !!!!");
            System.out.println("Exiting...");
            System.exit(0);
        }
        CitySimulator.pop_hist_name = CitySimulator.data_directory + "pop_" + CitySimulator.file_name;
        CitySimulator.file_name = CitySimulator.data_directory + CitySimulator.file_name;
    }

    public JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        final JMenu c = new JMenu("Document");
        c.setMnemonic(68);
        final JMenuItem menuItem = new JMenuItem("New");
        menuItem.setMnemonic(78);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
            }
        });
        c.add(menuItem);
        menuBar.add(c);
        return menuBar;
    }

    public void InitComponents(final Input_data input_data) {
        CitySimulator.numPeople = input_data.getNumPeople();
        CitySimulator.addMoves = input_data.getAddMoves();
        CitySimulator.finalMoves = input_data.getFinalMoves();
        CitySimulator.file_name = input_data.getFilename();
        CitySimulator.data_directory = input_data.getDataDirectory();
        CitySimulator.startThreshold = input_data.getStartThresh();
        CitySimulator.fillThreshold = input_data.getFillThresh();
        CitySimulator.emptyThreshold = input_data.getEmptyThresh();
        CitySimulator.useFluid = input_data.getUseFluid();
        if (CitySimulator.useFluid) {
            CitySimulator.fluidStartCycle = input_data.getFluidStartCycle();
        }
        CitySimulator.exitHigh = input_data.getMaxExitProb();
        CitySimulator.exitLow = input_data.getMinExitProb();
        CitySimulator.downHigh = input_data.getMaxDownProb();
        CitySimulator.downLow = input_data.getMinDownProb();
        CitySimulator.enterProb = input_data.getEnterProb();
        CitySimulator.upProb = input_data.getUpProb();
        CitySimulator.exitProb = CitySimulator.exitLow;
        CitySimulator.downProb = CitySimulator.downLow;
    }

    public int getAddMoves() {
        return CitySimulator.addMoves;
    }

    public String getCityPlanClassName() {
        return CitySimulator.cityPlanClassName;
    }

    public int getCitySize() {
        return CitySimulator.citySize;
    }

    public boolean getConservePop() {
        return CitySimulator.conservePop;
    }

    public String getDataDirectory() {
        return CitySimulator.data_directory;
    }

    public double getDownProb() {
        return CitySimulator.downProb;
    }

    public double getDownHighProb() {
        return CitySimulator.downHigh;
    }

    public double getDownLowProb() {
        return CitySimulator.downLow;
    }

    public double getEmptyThreshold() {
        return CitySimulator.emptyThreshold;
    }

    public double getEnterProb() {
        return CitySimulator.enterProb;
    }

    public double getExitLowProb() {
        return CitySimulator.exitLow;
    }

    public double getExitHighProb() {
        return CitySimulator.exitHigh;
    }

    public double getExitProb() {
        return CitySimulator.exitProb;
    }

    public String getFileName() {
        return CitySimulator.file_name;
    }

    public double getFillThreshold() {
        return CitySimulator.fillThreshold;
    }

    public int getFinalMoves() {
        return CitySimulator.finalMoves;
    }

    public int getFluidStartCycle() {
        return CitySimulator.fluidStartCycle;
    }

    public int getMapScale() {
        return CitySimulator.mapScale;
    }

    public int getMapSize() {
        return CitySimulator.mapSize;
    }

    public int getMaxRoadVel() {
        return CitySimulator.maxRoadVel;
    }

    public int getNumPeople() {
        return CitySimulator.numPeople;
    }

    public int getPersonKeyMultiplier() {
        return CitySimulator.personKeyMultiplier;
    }

    public String getPopFileName() {
        return CitySimulator.pop_hist_name;
    }

    public int getReentryMultiplier() {
        return CitySimulator.reentryMultiplier;
    }

    public int getScaleToMeters() {
        return CitySimulator.scaleToMeters;
    }

    public double getStartThreshold() {
        return CitySimulator.startThreshold;
    }

    public boolean getStoreData() {
        return CitySimulator.storeData;
    }

    public double getUpProb() {
        return CitySimulator.upProb;
    }

    public boolean getUseFluid() {
        return CitySimulator.useFluid;
    }

    public double getTimeDelta() {
        return CitySimulator.timeDelta;
    }

    public double getWalkMax() {
        return CitySimulator.walkMax;
    }

    public String getXMLPlanFile() {
        return CitySimulator.xml_plan;
    }

    public void InitPeopleChart() {
        CitySimulator.peopleChart.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 300, 0);
        CitySimulator.desk.add(CitySimulator.peopleChart, 4);
        CitySimulator.peopleChart.setVisible(true);
    }
}

