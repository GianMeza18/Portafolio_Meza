package com.gianmeza.portafolio.model;

import java.util.List;

public record Evidence(
        int week,
        String title,
        String description,
        String status,
        String image,
        List<EvidenceFile> files) {

    public String weekLabel() {
        return String.format("Semana %02d", week);
    }

    public int getWeek() {
        return week;
    }

    public record EvidenceFile(String name, String type, String url, String icon, boolean external) {
        public EvidenceFile(String name, String type, String url, String icon) {
            this(name, type, url, icon, false);
        }
    }
}
