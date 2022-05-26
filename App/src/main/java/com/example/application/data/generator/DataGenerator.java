package com.example.application.data.generator;

import com.example.application.data.entity.Kategoria;
import com.example.application.data.entity.Slowko;
import com.example.application.data.repository.CategoryRepository;
import com.example.application.data.repository.WordRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(WordRepository wordRepository, CategoryRepository categoryRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (wordRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }

            logger.info("Generating demo data");

            Kategoria kategoria = new Kategoria();
            kategoria.setNameOfTheCategory("English");

            Slowko slowko = new Slowko();
            slowko.setWord("pies");
            slowko.setTranslation("dog");
            slowko.setKategoria(kategoria);

            Slowko slowko2 = new Slowko();
            slowko2.setWord("kot");
            slowko2.setTranslation("cat");
            dodajSlowka(wordRepository, categoryRepository, kategoria, slowko, slowko2);

            Kategoria kategoria2 = new Kategoria();
            kategoria2.setNameOfTheCategory("Espanol");

            Slowko slowko3 = new Slowko();
            slowko3.setWord("dom");
            slowko3.setTranslation("casa");
            slowko3.setKategoria(kategoria2);

            Slowko slowko4 = new Slowko();
            slowko4.setWord("krzesło");
            slowko4.setTranslation("silla");
            dodajSlowka(wordRepository, categoryRepository, kategoria2, slowko3, slowko4);

            logger.info("Generated demo data");
        };
    }

    private void dodajSlowka(WordRepository wordRepository, CategoryRepository categoryRepository, Kategoria kategoria2, Slowko slowko3, Slowko slowko4) {
        slowko4.setKategoria(kategoria2);

        kategoria2.getSlowka().add(slowko3);
        kategoria2.getSlowka().add(slowko4);

        categoryRepository.save(kategoria2);


        List<Slowko> slowkos2 = new ArrayList<>();
        slowkos2.add(slowko3);
        slowkos2.add(slowko4);

        wordRepository.saveAll(slowkos2);
    }

}
