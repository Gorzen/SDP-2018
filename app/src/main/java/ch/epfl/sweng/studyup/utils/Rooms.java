package ch.epfl.sweng.studyup.utils;

import android.location.Location;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.studyup.map.Room;

public class Rooms {
    public final static List<String> ROOMS_AVAILABLE;

    static {
        List<String> rooms = new ArrayList<String>() {
            {
                add("CE_1_1");
                add("CE_1_2");
                add("CE_1_3");
                add("CE_1_4");
                add("CE_1_5");
                add("CE_1_6");

                add("CM_1_1");
                add("CM_1_2");
                add("CM_1_3");
                add("CM_1_4");
                add("CM_1_5");

                add("CO_1_1");
                add("CO_1_2");
                add("CO_1_3");
                add("CO_1_4");
                add("CO_1_5");
                add("CO_1_6");
            }
        };
        ROOMS_AVAILABLE = Collections.unmodifiableList(rooms);
    };

    public final static Map<String, Room> ROOMS_LOCATIONS;
    static {
        HashMap<String, Room> locations = new HashMap<String, Room>() {
            {
                put("CE_1_1", new Room(46.5206650, 6.5693740));
                put("CE_1_2", new Room(46.5204350, 6.5693790));
                put("CE_1_3", new Room(46.5206680, 6.5697490));
                put("CE_1_4", new Room(46.5204010, 6.5697540));
                put("CE_1_5", new Room(46.5203640, 6.5705050));
                put("CE_1_6", new Room(46.5204850, 6.5707300));

                put("CM_1_1", new Room(46.5206550, 6.5674980));
                put("CM_1_2", new Room(46.5203840, 6.5675020));
                put("CM_1_3", new Room(46.5203810, 6.5671270));
                put("CM_1_4", new Room(46.5204150, 6.5665410));
                put("CM_1_5", new Room(46.5204140, 6.5664010));

                put("CO_1_1", new Room(46.5199270, 6.5653150));
                put("CO_1_2", new Room(46.5199070, 6.5644510));
                put("CO_1_3", new Room(46.5199070, 6.5644510));
                put("CO_1_4", new Room(46.5201620, 6.5648470));
                put("CO_1_5", new Room(46.5201610, 6.5646430));
                put("CO_1_6", new Room(46.5201480, 6.5643660));
            }
        };
        ROOMS_LOCATIONS = Collections.unmodifiableMap(locations);
    };

    static boolean checkLocation(Location room, Location userPos) {
        return false;
    }
}
