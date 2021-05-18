package com.natuan.citysimulator.GUI;

import com.natuan.citysimulator.Resources.Constants;
import com.natuan.citysimulator.model.City;
import com.natuan.citysimulator.model.Place;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class matrixDisplay extends JPanel implements Constants {
    private static boolean mouse_event;
    private static int numColors;
    private static Color[] colorList;
    private static Color[] color;

    static {
        matrixDisplay.mouse_event = false;
        matrixDisplay.numColors = 10;
        matrixDisplay.colorList = new Color[]{Color.black, Color.white, Color.red, Color.blue, Color.yellow, new Color(45, 45, 76), new Color(0, 80, 0), Color.magenta, Color.orange, Color.pink, Color.lightGray, Color.cyan};
        matrixDisplay.color = new Color[127];
    }

    Image image;
    byte[][] Matrix;
    private int maxmap;
    private int xstart;
    private int ystart;
    private int x0;
    private int y0;
    private int limit;
    private int ix;
    private int iy;
    private int pixlSize;
    private City city;
    private Place selectedPlace;
    private Point Mouseloc;
    private boolean createImage;

    public matrixDisplay(final byte[][] array, final int n, final int n2, final int n3, final int n4, final boolean b, final JDesktopPane desktopPane, final City city) {
        this.image = null;
        this.x0 = 10;
        this.y0 = 30;
        this.Mouseloc = new Point(0, 0);
        this.createImage = false;
        this.city = city;
        this.setColors();
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(final MouseEvent mouseEvent) {
                matrixDisplay.this.Mouseloc.x = mouseEvent.getX();
                matrixDisplay.this.Mouseloc.y = mouseEvent.getY();
                matrixDisplay.this.ix = matrixDisplay.this.Mouseloc.x / n4;
                matrixDisplay.this.iy = matrixDisplay.this.Mouseloc.y / n4;
                if (matrixDisplay.this.ix >= n3) {
                    matrixDisplay.this.ix = n3 - 1;
                }
                if (matrixDisplay.this.iy >= n3) {
                    matrixDisplay.this.iy = n3 - 1;
                }
                matrixDisplay.this.selectedPlace = matrixDisplay.this.city.getSelectedPlacebyMouseClick(matrixDisplay.this.ix, matrixDisplay.this.iy);
                if (matrixDisplay.this.selectedPlace != null) {
                    matrixDisplay.this.selectedPlace.showOptionsFrame();
                    matrixDisplay.mouse_event = true;
                }
            }
        });
        this.refresh(array, n, n2, n3, n4, b);
        this.setPreferredSize(new Dimension(n4 * n3 + this.x0, n4 * 3 * this.maxmap + this.y0));
    }

    public void paint(final Graphics graphics) {
        this.paintCanvas(graphics);
    }

    public void update(final Graphics graphics) {
        this.paint(graphics);
    }

    public void refresh(final byte[][] matrix, final int xstart, final int ystart, final int limit, final int pixlSize, final boolean createImage) {
        this.Matrix = matrix;
        this.xstart = xstart;
        this.ystart = ystart;
        this.limit = limit;
        this.pixlSize = pixlSize;
        this.createImage = createImage;
    }

    public boolean getMouseEvent() {
        final boolean mouse_event = matrixDisplay.mouse_event;
        matrixDisplay.mouse_event = false;
        return mouse_event;
    }

    public Place getSelectedPlace() {
        return this.selectedPlace;
    }

    private void setColors() {
        int n = 0;
        for (int i = 0; i < 127; ++i) {
            if (i < 10) {
                matrixDisplay.color[i] = matrixDisplay.colorList[n];
                ++n;
            } else {
                final int n2 = i + 54;
                matrixDisplay.color[i] = new Color(n2, n2, n2);
            }
        }
    }

    private void paintCanvas(final Graphics graphics) {
        int ystart = this.ystart;
        int n = this.xstart;
        for (int i = 0; i < this.limit; ++i) {
            for (int j = 0; j < this.limit; ++j) {
                int n2 = this.Matrix[j][i];
                if (n2 < 0) {
                    n2 = 0;
                }
                if (n2 >= 127) {
                    n2 = 126;
                }
                graphics.setColor(matrixDisplay.color[n2]);
                graphics.fillRect(n, ystart, this.pixlSize, this.pixlSize);
                n += this.pixlSize;
            }
            ystart += this.pixlSize;
            n = this.xstart;
        }
    }
}

