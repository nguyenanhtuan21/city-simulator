package com.natuan.citysimulator.GUI;

import javax.swing.*;
import java.awt.*;

class ChartPanel extends JPanel {
    private int maxy1;
    private int maxy2;
    private int maxx;
    private int[] datay1;
    private int[] datay2;

    public ChartPanel(final int maxy2, final int maxx) {
        this.maxy2 = maxy2;
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
        for (int k = 1; k < 4; ++k) {
            final int n4 = k * height / 4;
            graphics2D.drawLine(width - n, n4, width, n4);
        }
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(LocationChartFrame.COLOR_RIGHT);
        if (this.datay2 == null || this.datay1 == null) {
            return;
        }
        for (int l = 1; l < this.datay2.length; ++l) {
            graphics2D.drawLine((l - 1) * width / this.maxx, height - this.datay2[l - 1] * height / this.maxy2, l * width / this.maxx, height - this.datay2[l] * height / this.maxy2);
        }
        graphics2D.setColor(LocationChartFrame.COLOR_LEFT);
        for (int n5 = 1; n5 < this.datay1.length; ++n5) {
            graphics2D.drawLine((n5 - 1) * width / this.maxx, height - this.datay1[n5 - 1] * height / this.maxy1, n5 * width / this.maxx, height - this.datay1[n5] * height / this.maxy1);
        }
    }

    public void update(final int maxy1, final int[] datay1, final int[] datay2) {
        this.maxy1 = maxy1;
        this.datay1 = datay1;
        this.datay2 = datay2;
        this.repaint();
    }
}

