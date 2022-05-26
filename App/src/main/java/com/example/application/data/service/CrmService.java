package com.example.application.data.service;

import com.example.application.data.entity.Kategoria;
import com.example.application.data.entity.Slowko;
import com.example.application.data.repository.CategoryRepository;
import com.example.application.data.repository.WordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrmService {

    private final WordRepository wordRepository;
    private final CategoryRepository categoryRepository;

    public CrmService(WordRepository wordRepository,
                      CategoryRepository categoryRepository) {
        this.wordRepository = wordRepository;
        this.categoryRepository = categoryRepository;

    }

    public List<Slowko> findAllWords(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return wordRepository.findAll();
        } else {
            return wordRepository.search(stringFilter);
        }
    }

    public long countWords() {
        return categoryRepository.count();
    }

    public void deleteWord(Slowko slowko) {
        wordRepository.delete(slowko);
    }

    public void deleteCategory(Kategoria kategoria) {
        categoryRepository.delete(kategoria);
    }

    public void saveCategory(Kategoria kategoria) {
        if (kategoria == null) {
            System.err.println("Category is null. Are you sure you have connected your form to the application?");
            return;
        }
        categoryRepository.save(kategoria);
    }

    public void saveContact(Slowko slowko) {
        if (slowko == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        wordRepository.save(slowko);
    }

    public List<Kategoria> findAllCategories() {
        return categoryRepository.findAll();
    }
}