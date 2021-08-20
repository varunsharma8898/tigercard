package com.nepu.metro.vo;

public class Zone implements Comparable {

    private String id;

    private String name;

    public Zone(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Object o) {
        return id.compareTo(((Zone) o).id);
    }
}
