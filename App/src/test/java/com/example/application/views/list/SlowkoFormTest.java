package com.example.application.views.list;

import com.example.application.data.entity.Kategoria;
import com.example.application.data.entity.Slowko;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SlowkoFormTest {
    private List<Kategoria> kategorie;
    private Slowko marcUsher;
    private Kategoria kategoria1;
    private Kategoria kategoria2;


    @Before 
    public void setupData() {
        kategorie = new ArrayList<>();
        kategoria1 = new Kategoria();
        kategoria1.setNameOfTheCategory("Vaadin Ltd");
        kategoria2 = new Kategoria();
        kategoria2.setNameOfTheCategory("IT Mill");
        kategorie.add(kategoria1);
        kategorie.add(kategoria2);


        marcUsher = new Slowko();
        marcUsher.setWord("Marc");
        marcUsher.setTranslation("Usher");
        marcUsher.setKategoria(kategoria2);
    }

    @Test
    public void formFieldsPopulated() {
        WordForm form = new WordForm(kategorie);
        form.setWord(marcUsher);
        Assert.assertEquals("Marc", form.word.getValue());
        Assert.assertEquals("Usher", form.translation.getValue());
        Assert.assertEquals(kategoria2, form.kategoria.getValue());
    }

    @Test
    public void saveEventHasCorrectValues() {
        WordForm form = new WordForm(kategorie);
        Slowko slowko = new Slowko();
        form.setWord(slowko);
        form.word.setValue("John");
        form.translation.setValue("Doe");
        form.kategoria.setValue(kategoria1);

        AtomicReference<Slowko> savedContactRef = new AtomicReference<>(null);
        form.addListener(WordForm.SaveEvent.class, e -> {
            savedContactRef.set(e.getWord());
        });
        form.save.click();
        Slowko savedSlowko = savedContactRef.get();

        Assert.assertEquals("John", savedSlowko.getWord());
        Assert.assertEquals("Doe", savedSlowko.getTranslation());
        Assert.assertEquals(kategoria1, savedSlowko.getKategoria());
    }
}