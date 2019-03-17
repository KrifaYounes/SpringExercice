package com.exercice.spring.demo.enumeration;

public enum CivilityEnum {

    MR("Monsieur"), MRS("Madame");

    private String label;

    CivilityEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
