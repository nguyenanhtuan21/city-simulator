package com.natuan.citysimulator.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CityWindowAdapter extends WindowAdapter {
    public void windowClosing(final WindowEvent windowEvent) {
        System.exit(0);
    }
}
