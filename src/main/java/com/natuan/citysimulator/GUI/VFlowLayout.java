package com.natuan.citysimulator.GUI;

import java.awt.*;

public class VFlowLayout implements LayoutManager {
    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;
    private int align;
    private int hgap;
    private int vgap;

    public VFlowLayout() {
        this(1, 5, 5);
    }

    public VFlowLayout(final int n) {
        this(n, 5, 5);
    }

    public VFlowLayout(final int align, final int hgap, final int vgap) {
        this.align = align;
        this.hgap = hgap;
        this.vgap = vgap;
    }

    public int getAlignment() {
        return this.align;
    }

    public void setAlignment(final int align) {
        this.align = align;
    }

    public int getHgap() {
        return this.hgap;
    }

    public void setHgap(final int hgap) {
        this.hgap = hgap;
    }

    public int getVgap() {
        return this.vgap;
    }

    public void setVgap(final int vgap) {
        this.vgap = vgap;
    }

    public void addLayoutComponent(final String s, final Component component) {
    }

    public void removeLayoutComponent(final Component component) {
    }

    public Dimension preferredLayoutSize(final Container container) {
        synchronized (container.getTreeLock()) {
            return this.findLayoutSize(container, true);
        }
    }

    public Dimension minimumLayoutSize(final Container container) {
        synchronized (container.getTreeLock()) {
            return this.findLayoutSize(container, false);
        }
    }

    private Dimension findLayoutSize(final Container container, final boolean b) {
        final Dimension dimension = new Dimension(0, 0);
        final Component[] components = container.getComponents();
        for (int i = 0; i < components.length; ++i) {
            if (components[i].isVisible()) {
                final Dimension dimension2 = b ? components[i].getPreferredSize() : components[i].getMinimumSize();
                dimension.width = Math.max(dimension.width, dimension2.width);
                final Dimension dimension3 = dimension;
                dimension3.height += this.vgap + dimension2.height;
            }
        }
        final Insets insets = container.getInsets();
        final Dimension dimension4 = dimension;
        dimension4.width += insets.left + insets.right + 2 * this.hgap;
        final Dimension dimension5 = dimension;
        dimension5.height += insets.top + insets.bottom + this.vgap;
        return dimension;
    }

    public void layoutContainer(final Container container) {
        synchronized (container.getTreeLock()) {
            final Dimension layoutSize = this.findLayoutSize(container, true);
            final Insets insets = container.getInsets();
            final int n = layoutSize.width - (insets.left + insets.right + 2 * this.hgap);
            final int x = insets.left + this.hgap;
            int y = insets.top + this.vgap;
            final Component[] components = container.getComponents();
            for (int i = 0; i < components.length; ++i) {
                if (components[i].isVisible()) {
                    final Dimension preferredSize = components[i].getPreferredSize();
                    switch (this.align) {
                        case 0: {
                            components[i].setBounds(x, y, n, preferredSize.height);
                            break;
                        }
                        case 1: {
                            components[i].setBounds(x + (n - preferredSize.width) / 2, y, preferredSize.width, preferredSize.height);
                            break;
                        }
                        case 2: {
                            components[i].setBounds(x + (n - preferredSize.width) / 2, y, n, preferredSize.height);
                            break;
                        }
                    }
                    y += preferredSize.height + this.vgap;
                }
            }
            container.setSize(layoutSize);
        }
    }

    public String toString() {
        final String string = this.getClass().getName() + "[hgap=" + this.hgap + ",vgap=" + this.vgap;
        switch (this.align) {
            case 0: {
                return string + ",align=left]";
            }
            case 1: {
                return string + ",align=center]";
            }
            case 2: {
                return string + ",align=right]";
            }
            default: {
                return null;
            }
        }
    }
}

