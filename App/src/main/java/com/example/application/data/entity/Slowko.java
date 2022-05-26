package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Slowko extends AbstractEntity {

    @NotEmpty
    private String word = "";

    @NotEmpty
    private String translation = "";

    @ManyToOne
    @JoinColumn(name = "kategoria_id")
    @NotNull
    @JsonIgnoreProperties({"slowka"})
    private Kategoria kategoria;


    @Override
    public String toString() {
        return word + " " + translation;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public Kategoria getKategoria() {
        return kategoria;
    }

    public void setKategoria(Kategoria kategoria) {
        this.kategoria = kategoria;
    }
}
