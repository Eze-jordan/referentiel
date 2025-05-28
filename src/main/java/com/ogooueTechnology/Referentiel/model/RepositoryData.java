package com.ogooueTechnology.Referentiel.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "tbrepository")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refID;

    @Column(length = 30, nullable = false, unique = true)
    private String keyValue;

    @Column(length = 30, nullable = false)
    private String value1;

    @Column(length = 30)
    private String value2;

    @Column(length = 30)
    private String value3;

    @Column(length = 30)
    private String value4;

    @Column(length = 3, nullable = false)
    private String refCategory;

    public Long getRefID() {
        return refID;
    }

    public void setRefID(Long refID) {
        this.refID = refID;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        return value3;
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public String getValue4() {
        return value4;
    }

    public void setValue4(String value4) {
        this.value4 = value4;
    }

    public String getRefCategory() {
        return refCategory;
    }

    public void setRefCategory(String refCategory) {
        this.refCategory = refCategory;
    }
}

