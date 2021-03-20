package com.natuan.citysimulator.abstracts;

import java.awt.*;

public abstract class GridBagHelper {
    public static void add(final Container container, final Component component, final GridBagConstraints constraints) {
        container.add(component);
        ((GridBagLayout) container.getLayout()).setConstraints(component, constraints);
    }

    public static GridBagConstraints labelLike(final int gridx, final int gridy) {
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = gridx;
        gridBagConstraints.gridy = gridy;
        final GridBagConstraints gridBagConstraints2 = gridBagConstraints;
        final GridBagConstraints gridBagConstraints3 = gridBagConstraints;
        final double n = 0.0;
        gridBagConstraints3.weighty = n;
        gridBagConstraints2.weightx = n;
        gridBagConstraints.fill = 0;
        return centered(gridBagConstraints);
    }

    public static GridBagConstraints labelLike(final int n, final int n2, final int gridwidth) {
        final GridBagConstraints labelLike = labelLike(n, n2);
        labelLike.gridwidth = gridwidth;
        return labelLike;
    }

    public static GridBagConstraints labelLike(final int n, final int n2, final int gridwidth, final int anchor) {
        final GridBagConstraints labelLike = labelLike(n, n2);
        labelLike.gridwidth = gridwidth;
        labelLike.anchor = anchor;
        return labelLike;
    }

    public static GridBagConstraints fieldLike(final int gridx, final int gridy) {
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = gridx;
        gridBagConstraints.gridy = gridy;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.anchor = 18;
        gridBagConstraints.fill = 2;
        return gridBagConstraints;
    }

    public static GridBagConstraints fieldLike(final int n, final int n2, final int gridwidth) {
        final GridBagConstraints fieldLike = fieldLike(n, n2);
        fieldLike.gridwidth = gridwidth;
        return fieldLike;
    }

    public static GridBagConstraints areaLike(final int gridx, final int gridy) {
        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = gridx;
        gridBagConstraints.gridy = gridy;
        final GridBagConstraints gridBagConstraints2 = gridBagConstraints;
        final GridBagConstraints gridBagConstraints3 = gridBagConstraints;
        final double n = 1.0;
        gridBagConstraints3.weighty = n;
        gridBagConstraints2.weightx = n;
        gridBagConstraints.anchor = 18;
        gridBagConstraints.fill = 1;
        return gridBagConstraints;
    }

    public static GridBagConstraints areaLike(final int n, final int n2, final int gridwidth) {
        final GridBagConstraints areaLike = areaLike(n, n2);
        areaLike.gridwidth = gridwidth;
        return areaLike;
    }

    public static GridBagConstraints rightJustify(final GridBagConstraints gridBagConstraints) {
        gridBagConstraints.anchor = 12;
        return gridBagConstraints;
    }

    public static GridBagConstraints centered(final GridBagConstraints gridBagConstraints) {
        gridBagConstraints.anchor = 10;
        return gridBagConstraints;
    }
}

