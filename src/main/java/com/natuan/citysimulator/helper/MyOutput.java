package com.natuan.citysimulator.helper;

import com.natuan.citysimulator.Resources.Constants;
import com.natuan.citysimulator.model.Person;
import com.natuan.citysimulator.model.Place;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

public class MyOutput implements Constants {
    private static long seconds0;
    private static double fx;
    private static double fy;
    private static double fz;
    private static int ix;
    private static int iy;
    private static int iz;
    private static String placeName;
    private long seconds;
    private String Sdata;
    private String file_name;
    private String file_convert_name;
    private String pop_file_name;

    public MyOutput() {
        MyOutput.seconds0 = System.currentTimeMillis();

//        file_convert_name = getFileConvertName();
    }

    public MyOutput(final String file_name, final String pop_file_name) {
        MyOutput.seconds0 = System.currentTimeMillis();
        this.file_name = file_name;
        this.pop_file_name = pop_file_name;
        this.file_convert_name = getFileConvertName();
    }

    public void showTime() {
        this.seconds = System.currentTimeMillis();
        System.out.print("cycle time: milli-seconds = " + (this.seconds - MyOutput.seconds0) + "   ");
        MyOutput.seconds0 = this.seconds;
    }

    public void writeFileData(final Vector vector, final int n, final int i) {
        try {
            final PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(this.file_name, true)));
            final PrintWriter printWriter2 = new PrintWriter(new BufferedWriter(new FileWriter(this.pop_file_name, true)));
            for (int size = vector.size(), j = 0; j < size; ++j) {
                final Person person = (Person) vector.elementAt(j);
                final String string = new Integer(person.personKey).toString();
                MyOutput.ix = new Integer(person.xpos);
                MyOutput.iy = new Integer(person.ypos);
                MyOutput.iz = new Integer(person.zpos);
                MyOutput.ix *= 100;
                MyOutput.iy *= 100;
                MyOutput.iz *= 100;
                MyOutput.ix /= 5;
                MyOutput.iy /= 5;
                MyOutput.iz /= (int) 3.0;
                MyOutput.fx = MyOutput.ix;
                MyOutput.fy = MyOutput.iy;
                MyOutput.fz = MyOutput.iz;
                MyOutput.fx *= 0.01;
                MyOutput.fy *= 0.01;
                MyOutput.fz *= 0.01;
                //place
                MyOutput.placeName =  person.place.placeName;
                int flo = person.place.floor;
                String floSting = flo == 0? "" : "Floor " + flo + ". ";
                //place
                final String string2 = new Double(MyOutput.fx).toString();
                final String string3 = new Double(MyOutput.fy).toString();
                final String string4 = new Double(MyOutput.fz).toString();
                final String string5 = new Double(person.time).toString();
                int index = string5.indexOf(".");
                index += 7;
                final String substring = string5.substring(0, Math.min(index, string5.length()));
                int index2 = string2.indexOf(".");
                index2 += 2;
                final String substring2 = string2.substring(0, index2);
                int index3 = string3.indexOf(".");
                index3 += 2;
                final String substring3 = string3.substring(0, index3);
                int index4 = string4.indexOf(".");
                index4 += 2;
                printWriter.println(this.Sdata = "" + string + ", " + substring + ", " + substring2 + "," + substring3 + "," + string4.substring(0, index4) + "," + n +","+ floSting + MyOutput.placeName);
            }
            printWriter.flush();
            printWriter.close();
            printWriter2.println("" + n + ", " + i);
            printWriter2.flush();
            printWriter2.close();
        } catch (FileNotFoundException ex2) {
            System.err.println("Can't open output file");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void writeFileHeader(final int i, final int j, final int k, final long lng) {
        try {
            final PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(this.file_name)));
            printWriter.println(this.Sdata = "# Num People  = " + i + " moves between additions = " + j + " final Moves = " + k + "   output filename: = " + this.file_name);
            printWriter.println(this.Sdata = "# random number seed = " + lng);
            printWriter.println(this.Sdata = "# data format is: ");
            printWriter.println(this.Sdata = "# index, time,  x,y,z , cycle, place \n");
            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException ex2) {
            System.err.println("Can't open output file");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void writeFileDataConvert(final int pNum, final int cNum, final int i, final int j, final int k, final long lng){
        final int numData = pNum * cNum;
        String data[] = new String[numData];

        try {
            int header = 0;
            FileInputStream fileInputStream = new FileInputStream(file_name);
            Scanner scanner = new Scanner(fileInputStream);
            final PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(this.file_convert_name)));
            printWriter.println(this.Sdata = "# Convert File");
            printWriter.println(this.Sdata = "# Num People  = " + i + " moves between additions = " + j + " final Moves = " + k + "   output filename: = " + file_convert_name);
            printWriter.println(this.Sdata = "# random number seed = " + lng);
            printWriter.println(this.Sdata = "# data format is: ");
            printWriter.println(this.Sdata = "# index, time,  x,y,z , cycle, place\n");
            printWriter.flush();

            while (scanner.hasNextLine() && header < 5) {
                scanner.nextLine();
                header++;
            }
            int index = 0;
            while (scanner.hasNextLine() && index < numData) {
                data[index] = scanner.nextLine();
                index++;
            }

            for (int pe = 0; pe < pNum; pe++) {
                for(int ce = 0; ce < cNum; ce++){
                    printWriter.println(data[pe + ce * pNum]);
                }
            }


            printWriter.flush();
            printWriter.close();
            scanner.close();
            fileInputStream.close();



        } catch (FileNotFoundException ex2) {
            System.err.println("Can't open output file");
            System.out.println("Please try again");
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    private String getFileConvertName(){
        int index=0;
        for(int i=0;i<this.file_name.length();i++){
            if(this.file_name.charAt(i) == '.') index = i;
        }
        return new String(this.file_name.substring(0,index) + "Convert" + this.file_name.substring(index, this.file_name.length()));
    }
}

