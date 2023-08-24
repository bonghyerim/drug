package com.bonghyerim.drug.model;

import java.io.Serializable;

public class Place implements Serializable {

    public String name;
    public String vicinity;

    public Geometry geometry;

    public class Geometry implements Serializable {

        public Location location;

        public class Location implements Serializable {
            public double lat;
            public double lng;
        }
    }

}