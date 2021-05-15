package com.natuan.citysimulator.GUI;

import javax.swing.*;
import java.awt.*;

class SingleChartPanel extends JPanel {
    private int maxy;
    private int maxx;
    private int[] datay;

    public SingleChartPanel(final int maxy, final int maxx) {
        this.maxy = maxy;
        this.maxx = maxx;
    }

    public void paint(final Graphics graphics) {
        final Graphics2D graphics2D = (Graphics2D) graphics;
        final int height = this.getSize().height - 1;
        final int width = this.getSize().width - 1;
        final int n = 5;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        graphics2D.setColor(Color.white);
        graphics2D.fillRect(0, 0, width, height);
        graphics2D.setColor(Color.black);
        graphics2D.drawRect(0, 0, width, height);
        for (int i = 1; i < 5; ++i) {
            final int n2 = i * width / 5;
            graphics2D.drawLine(n2, height - n, n2, height);
        }
        for (int j = 1; j < 4; ++j) {
            final int n3 = j * height / 4;
            graphics2D.drawLine(0, n3, n, n3);
        }
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (this.datay == null) {
            return;
        }
        graphics2D.setColor(LocationChartFrame.COLOR_LEFT);
        for (int k = 1; k < this.datay.length; ++k) {
            final int n4 = (k - 1) * width / this.maxx;
            final int n5 = k * width / this.maxx;
            final int n6 = height - this.datay[k - 1] * height / this.maxy;
            final int n7 = height - this.datay[k] * height / this.maxy;
            if (this.datay[k] >= 0 && this.datay[k - 1] >= 0) {
                graphics2D.drawLine(n4, n6, n5, n7);
            }
        }
    }

    public void update(final int maxy, final int[] datay) {
        this.maxy = maxy;
        this.datay = datay;
        this.repaint();
    }
}