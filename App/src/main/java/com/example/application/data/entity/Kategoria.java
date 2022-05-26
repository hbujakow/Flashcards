package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import org.hibernate.annotations.Formula;

import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

@Entity
public class Kategoria extends AbstractEntity {
    @NotBlank
    private String nameOfTheCategory;

    @OneToMany(mappedBy = "kategoria")
    @Nullable
    private List<Slowko> slowka = new LinkedList<>();

    @Formula("(select count(c.id) from Slowko c where c.kategoria_id = id)")
    private int iloscSlowek;

    public String getNameOfTheCategory() {
        return nameOfTheCategory;
    }

    public void setNameOfTheCategory(String name) {
        this.nameOfTheCategory = name;
    }

    @Nullable
    public List<Slowko> getSlowka() {
        return slowka;
    }

    public void setSlowka(@Nullable List<Slowko> slowka) {
        this.slowka = slowka;
    }

    public int getIloscSlowek() {
        return iloscSlowek;
    }

    public void setIloscSlowek(int iloscSlowek) {
        this.iloscSlowek = iloscSlowek;
    }
}
