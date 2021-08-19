package com.nepu.metro.vo;

import java.time.LocalDateTime;

public class Journey {

    private LocalDateTime datetime;

    private Zone fromZone;

    private Zone toZone;

    public Journey(LocalDateTime datetime, Zone fromZone, Zone toZone) {
        this.datetime = datetime;
        this.fromZone = fromZone;
        this.toZone = toZone;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public Zone getFromZone() {
        return fromZone;
    }

    public void setFromZone(Zone fromZone) {
        this.fromZone = fromZone;
    }

    public Zone getToZone() {
        return toZone;
    }

    public void setToZone(Zone toZone) {
        this.toZone = toZone;
    }
}
