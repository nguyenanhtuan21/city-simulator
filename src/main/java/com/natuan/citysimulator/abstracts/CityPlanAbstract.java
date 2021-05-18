package com.natuan.citysimulator.abstracts;

import com.natuan.citysimulator.GUI.Building;
import com.natuan.citysimulator.GUI.Intersection;
import com.natuan.citysimulator.Resources.Constants;
import com.natuan.citysimulator.interfaces.CityPlanInterface;
import com.natuan.citysimulator.model.City;
import com.natuan.citysimulator.model.Floor;
import com.natuan.citysimulator.model.Road;

public abstract class CityPlanAbstract implements CityPlanInterface, Constants {
    public static Intersection[] intersection;
    public static Road road;
    public static Building building;
    public static Floor floor;
    public static City city;

    static {
        CityPlanAbstract.intersection = new Intersection[6];
    }

    public abstract void buildPlan(final City p0);
}
