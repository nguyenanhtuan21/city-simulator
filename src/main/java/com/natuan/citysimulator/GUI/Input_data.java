package com.natuan.citysimulator.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Input_data extends JPanel implements ActionListener {
    private static int FILESCALEFACTOR;
    private static char sep1;
    private static char sep2;

    static {
        Input_data.FILESCALEFACTOR = 61;
        Input_data.sep1 = '/';
        Input_data.sep2 = '\\';
    }

    public boolean start;
    public boolean useFluid;
    private Canvas c;
    private String[] fieldNames;
    private String[] afieldNames;
    private String[] a2fieldNames;
    private JTextField[] values;
    private JTextField[] avalues;
    private JTextField[] a2values;
    private JTextField filename;
    private JLabel[] labels;
    private JLabel[] alabels;
    private JLabel[] a2labels;
    private JCheckBox fluidCheck;
    private JButton start_button;
    private JButton advanced_button;
    private JButton interactions_button;
    private JButton ok_button;
    private String dataDirectory;
    private String defaultName;
    private String finalName;
    private int fluidStartCycle;
    private String finalDirectory;
    private int[] numbers;
    private double[] anumbers;
    private double[] a2numbers;

    public Input_data() {
        this.start = false;
        this.useFluid = false;
        this.fieldNames = new String[]{"Filename:  ", "Data Directory:  ", "NumPeople:   ", "Max Relax.: ", "Final Moves:   "};
        this.afieldNames = new String[]{"Fluid Model Start Cycle:  ", "Start Threshold:  ", "Fill Threshold:   ", "Empty Threshold: "};
        this.a2fieldNames = new String[]{"Max Exit Prob:  ", "Min Exit Prob:  ", "Max Down Prob:   ", "Min Down Prob: ", "Enter Prob:  ", "Up Prob:  "};
        this.values = new JTextField[6];
        this.avalues = new JTextField[5];
        this.a2values = new JTextField[6];
        this.labels = new JLabel[6];
        this.alabels = new JLabel[5];
        this.a2labels = new JLabel[6];
        this.dataDirectory = ".";
        this.defaultName = "file1.txt";
        this.finalName = " ";
        this.fluidStartCycle = 200;
        this.finalDirectory = " ";
        this.numbers = new int[]{0, 0, 100, 2, 10000};
        this.anumbers = new double[]{100.0, 0.15, 0.09, 0.5};
        this.a2numbers = new double[]{0.1, 0.1, 0.1, 0.1, 120.0, 5.0};
    }

    public JFrame Initialize(final CitySimulator citySimulator) {
        this.defaultName = citySimulator.getFileName();
        this.dataDirectory = citySimulator.getDataDirectory();
        this.numbers[2] = citySimulator.getNumPeople();
        this.numbers[3] = citySimulator.getAddMoves();
        this.numbers[4] = citySimulator.getFinalMoves();
        this.useFluid = citySimulator.getUseFluid();
        this.fluidStartCycle = citySimulator.getFluidStartCycle();
        this.anumbers[1] = citySimulator.getStartThreshold();
        this.anumbers[2] = citySimulator.getFillThreshold();
        this.anumbers[3] = citySimulator.getEmptyThreshold();
        this.a2numbers[0] = citySimulator.getExitHighProb();
        this.a2numbers[1] = citySimulator.getExitLowProb();
        this.a2numbers[2] = citySimulator.getDownHighProb();
        this.a2numbers[3] = citySimulator.getDownLowProb();
        this.a2numbers[4] = citySimulator.getEnterProb();
        this.a2numbers[5] = citySimulator.getUpProb();
        this.finalName = this.defaultName;
        this.finalDirectory = this.dataDirectory;
        this.fluidStartCycle = 200;
        final JFrame frame = new JFrame("Input Run Parameters");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        this.setLayout(new BorderLayout());
        final JPanel comp = new JPanel();
        comp.setLayout(new GridLayout(0, 1));
        for (int i = 0; i < 5; ++i) {
            if (i == 0) {
                comp.add(this.labels[0] = new JLabel(this.fieldNames[0]));
                this.values[0] = new JTextField(this.defaultName, 8);
            } else if (i == 1) {
                comp.add(this.labels[1] = new JLabel(this.fieldNames[1]));
                this.values[1] = new JTextField(this.dataDirectory, 8);
            } else {
                comp.add(this.labels[i] = new JLabel(this.fieldNames[i]));
                this.values[i] = new JTextField(this.numbers[i] + "", 8);
            }
            comp.add(this.values[i]);
        }
        comp.add(this.labels[5] = new JLabel(" "));
        final JButton comp2 = new JButton("Press to Estimate File Size");
        comp2.setForeground(Color.darkGray);
        comp2.setBackground(Color.lightGray);
        comp.add(comp2);
        comp2.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                try {
                    Input_data.this.values[5].setText(Input_data.FILESCALEFACTOR * Integer.valueOf(Input_data.this.values[2].getText()) * Integer.valueOf(Input_data.this.values[4].getText()) / 1000 + " KB");
                } catch (NumberFormatException ex) {
                    Input_data.this.start = false;
                    System.err.println("String entered is not a number");
                }
            }
        });
        comp.add(this.values[5] = new JTextField(Input_data.FILESCALEFACTOR * this.numbers[2] * this.numbers[4] / 1000 + " KB", 18));
        final JPanel comp3 = new JPanel(new FlowLayout());
        final JButton comp4 = new JButton("Start");
        comp4.setForeground(Color.black);
        comp4.setBackground(Color.lightGray);
        comp3.add(comp4);
        comp4.addActionListener(this);
        final JButton comp5 = new JButton("adv. options");
        comp5.setForeground(Color.darkGray);
        comp5.setBackground(Color.lightGray);
        comp3.add("North", comp5);
        comp5.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                Input_data.this.advancedJButtonClick(frame);
            }
        });
        final JButton comp6 = new JButton("interactions");
        comp6.setForeground(Color.darkGray);
        comp6.setBackground(Color.lightGray);
        comp3.add("South", comp6);
        comp6.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                Input_data.this.interactionsJButtonClick(frame);
            }
        });
        frame.getContentPane().add(comp, "North");
        frame.getContentPane().add(comp3, "South");
        frame.setSize(300, 550);
        frame.setLocation(50, 10);
        frame.pack();
        frame.show();
        return frame;
    }

    public void advancedJButtonClick(final JFrame owner) {
        final JDialog dialog = new JDialog(owner, "Advance Settings", true);
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent windowEvent) {
                dialog.dispose();
            }
        });
        this.setLayout(new BorderLayout());
        final JPanel comp = new JPanel();
        comp.setLayout(new GridLayout(0, 1));
        for (int i = 0; i < 4; ++i) {
            if (i == 0) {
                comp.add(this.fluidCheck = new JCheckBox("Check to enable traffic fluid model:", this.useFluid));
                this.alabels[0] = new JLabel(this.afieldNames[0]);
                (this.avalues[0] = new JTextField(this.fluidStartCycle + "", 8)).setForeground(Color.black);
                if (!this.useFluid) {
                    this.avalues[0].setBackground(Color.lightGray);
                }
                this.avalues[0].setEditable(this.useFluid);
                this.fluidCheck.addItemListener(new ItemListener() {
                    public void itemStateChanged(final ItemEvent itemEvent) {
                        Input_data.this.avalues[0].setEditable(Input_data.this.fluidCheck.isSelected());
                        Input_data.this.avalues[0].setBackground(Input_data.this.fluidCheck.isSelected() ? Color.white : Color.lightGray);
                        Input_data.this.useFluid = Input_data.this.fluidCheck.isSelected();
                    }
                });
                comp.add(this.alabels[0]);
                comp.add(this.avalues[0]);
            } else {
                comp.add(this.alabels[i] = new JLabel(this.afieldNames[i]));
                comp.add(this.avalues[i] = new JTextField(this.anumbers[i] + "", 8));
            }
        }
        final JPanel comp2 = new JPanel(new FlowLayout());
        final JButton comp3 = new JButton("ok");
        comp3.setForeground(Color.blue);
        comp3.setBackground(Color.lightGray);
        comp2.add(comp3);
        comp3.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                Input_data.this.okJButtonClick(actionEvent, dialog);
            }
        });
        dialog.getContentPane().add(comp, "North");
        dialog.getContentPane().add(comp2, "South");
        dialog.setSize(300, 350);
        dialog.setLocation(150, 110);
        dialog.show();
    }

    public void interactionsJButtonClick(final JFrame owner) {
        final JDialog dialog = new JDialog(owner, "Set Interactions", true);
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent windowEvent) {
                dialog.dispose();
            }
        });
        this.setLayout(new BorderLayout());
        final JPanel comp = new JPanel();
        comp.setLayout(new GridLayout(0, 1));
        for (int i = 0; i <= 5; ++i) {
            comp.add(this.a2labels[i] = new JLabel(this.a2fieldNames[i]));
            comp.add(this.a2values[i] = new JTextField(this.a2numbers[i] + "", 8));
        }
        final JPanel comp2 = new JPanel(new FlowLayout());
        final JButton comp3 = new JButton("ok");
        comp3.setForeground(Color.blue);
        comp3.setBackground(Color.lightGray);
        comp2.add(comp3);
        comp3.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                Input_data.this.ok2JButtonClick(actionEvent, dialog);
            }
        });
        dialog.getContentPane().add(comp, "North");
        dialog.getContentPane().add(comp2, "South");
        dialog.setSize(300, 350);
        dialog.setLocation(150, 110);
        dialog.show();
    }

    public void ok2JButtonClick(final ActionEvent actionEvent, final JDialog dialog) {
        for (int i = 0; i <= 5; ++i) {
            try {
                this.a2numbers[i] = Double.valueOf(this.a2values[i].getText());
            } catch (NumberFormatException ex) {
                this.start = false;
                this.a2numbers[i] = 0.01;
                this.a2values[i].setText("0.01");
                System.err.println("String entered is not a float");
                return;
            }
            if (this.a2numbers[i] < 0.0 || this.a2numbers[i] > 0.999) {
                this.start = false;
                this.a2numbers[i] = 0.01;
                this.a2values[i].setText("0.01");
                System.err.println("Value out of range 0.0 - 0.99");
                return;
            }
        }
        dialog.dispose();
    }

    public void okJButtonClick(final ActionEvent actionEvent, final JDialog dialog) {
        try {
            this.fluidStartCycle = Integer.valueOf(this.avalues[0].getText());
        } catch (NumberFormatException ex) {
            this.start = false;
            this.fluidStartCycle = 100;
            this.avalues[0].setText("100");
            System.err.println("String entered is not an int");
            return;
        }
        for (int i = 1; i <= 3; ++i) {
            try {
                this.anumbers[i] = Double.valueOf(this.avalues[i].getText());
            } catch (NumberFormatException ex2) {
                this.start = false;
                this.anumbers[i] = 0.01;
                this.avalues[i].setText("0.01");
                System.err.println("String entered is not a float");
                return;
            }
            if (this.anumbers[i] < 0.0 || this.anumbers[i] > 0.999) {
                this.start = false;
                this.anumbers[i] = 0.01;
                this.avalues[i].setText("0.01");
                System.err.println("Value out of range 0.0 - 0.99");
                return;
            }
        }
        dialog.dispose();
    }

    public void actionPerformed(final ActionEvent actionEvent) {
        try {
            (this.finalName = this.values[0].getText()).replace(Input_data.sep1, File.separatorChar);
            this.finalName.replace(Input_data.sep2, File.separatorChar);
        } catch (Exception ex) {
            this.start = false;
            this.finalName = "not valid name";
            this.values[0].setText("file name error");
            System.err.println("String entered is not a valid filename");
            ex.getMessage();
            return;
        }
        this.defaultName = this.finalName;
        try {
            this.finalDirectory = this.values[1].getText();
            if (!this.finalDirectory.endsWith("\\") && !this.finalDirectory.endsWith("/") && !this.finalDirectory.endsWith(File.separator)) {
                this.finalDirectory += File.separator;
            }
            this.finalDirectory.replace(Input_data.sep1, File.separatorChar);
            this.finalDirectory.replace(Input_data.sep2, File.separatorChar);
        } catch (Exception ex2) {
            this.start = false;
            this.finalDirectory = "." + File.separator;
            this.values[1].setText("directory name error");
            System.err.println("String entered is not a valid directory name");
            ex2.getMessage();
            return;
        }
        this.dataDirectory = this.finalDirectory;
        final File file = new File(this.finalDirectory);
        if (!file.exists() || !file.isDirectory()) {
            this.start = false;
            this.finalDirectory = "." + File.separator;
            this.values[1].setText(this.finalDirectory);
            System.err.println("String entered is not a valid directory");
            return;
        }
        for (int i = 2; i <= 4; ++i) {
            try {
                this.numbers[i] = Integer.valueOf(this.values[i].getText());
            } catch (NumberFormatException ex3) {
                this.start = false;
                this.numbers[i] = 0;
                this.values[i].setText("0");
                System.err.println("String entered is not a number");
                return;
            }
            if (this.numbers[i] < 0 || this.numbers[i] > 1999999) {
                this.start = false;
                this.numbers[i] = 0;
                this.values[i].setText("0");
                System.err.println("Number entered is not in range 0 - 1999999");
                return;
            }
        }
        this.start = true;
    }

    public String getFilename() {
        return this.finalName;
    }

    public String getDataDirectory() {
        return this.finalDirectory;
    }

    public int getNumPeople() {
        return this.numbers[2];
    }

    public int getAddMoves() {
        return this.numbers[3];
    }

    public int getFinalMoves() {
        return this.numbers[4];
    }

    public boolean getUseFluid() {
        return this.useFluid;
    }

    public int getFluidStartCycle() {
        return this.fluidStartCycle;
    }

    public double getStartThresh() {
        return this.anumbers[1];
    }

    public double getFillThresh() {
        return this.anumbers[2];
    }

    public double getEmptyThresh() {
        return this.anumbers[3];
    }

    public double getMaxExitProb() {
        return this.a2numbers[0];
    }

    public double getMinExitProb() {
        return this.a2numbers[1];
    }

    public double getMaxDownProb() {
        return this.a2numbers[2];
    }

    public double getMinDownProb() {
        return this.a2numbers[3];
    }

    public double getEnterProb() {
        return this.a2numbers[4];
    }

    public double getUpProb() {
        return this.a2numbers[5];
    }

    public void winClose(final JFrame frame) {
        frame.setVisible(false);
    }
}

