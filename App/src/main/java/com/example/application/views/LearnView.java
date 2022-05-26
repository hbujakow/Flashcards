package com.example.application.views;

import com.example.application.data.entity.Kategoria;
import com.example.application.data.entity.Slowko;
import com.example.application.data.service.CrmService;
import com.example.application.views.list.QuizForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.util.*;


@Route(value = "learn", layout = MainLayout.class)
@PageTitle("Learn")
@PermitAll
public class LearnView extends VerticalLayout {

    private final CrmService service;
    private final HashMap<String, Set<Slowko>> map = new HashMap<>();
    private ComboBox<Kategoria> kategorie;
    private Button startButton;
    private QuizForm form;
    private RadioButtonGroup<String> radioButton;


    public LearnView(CrmService service) {
        this.service = service;

        List<Slowko> slowka = service.findAllWords(null);

        for (var slowko : slowka) {
            if (!map.containsKey(slowko.getKategoria().getNameOfTheCategory())) {
                map.put(slowko.getKategoria().getNameOfTheCategory(), new HashSet<>());
            }
            map.get(slowko.getKategoria().getNameOfTheCategory()).add(slowko);
        }

        addClassName("learn-view");

        add(getContent());

        if (form != null) {
            form.setVisible(false);
            removeClassName("editing");
        }
    }

    private Component getContent() {
        if (service.findAllWords(null).size() == 0) {
//        if (service.findAllCategories().size() == 0) {
            Html html = new Html("<div><h1>Error</h1><h2><b>Before learning, you must add some words!</b></h2></div>");
            return new VerticalLayout(html);
        }

        Component categoryChooser = chooseCategory();
        VerticalLayout categoryChooserLayout = new VerticalLayout(categoryChooser);
        categoryChooserLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.BASELINE);
        configureQuizForm();
        VerticalLayout content = new VerticalLayout(categoryChooserLayout, form);
        content.setDefaultHorizontalComponentAlignment(Alignment.BASELINE);
        content.setAlignItems(Alignment.BASELINE);
        content.setWidthFull();
        content.setFlexGrow(2, categoryChooser);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }


    private Component chooseCategory() {
        ComboBox<Kategoria> kategorie = new ComboBox<>("Categories");

        kategorie.setItems(service.findAllCategories());
        kategorie.setItemLabelGenerator(Kategoria::getNameOfTheCategory);
        kategorie.setValue(service.findAllCategories().get(0));
        this.kategorie = kategorie;

        assert kategorie.getValue() != null;

        kategorie.addValueChangeListener(e -> {
            form.setSlowka(new ArrayList<>(map.get(kategorie.getValue().getNameOfTheCategory())));
            System.out.println(kategorie.getValue().getNameOfTheCategory());
            System.out.println("category chooser:" + form.getSlowka());
            form.getWord().setValue(form.getSlowka().get(0).getWord());
            form.getWord().setLabel("Word to translate");
            form.setVisible(false);
            startButton.setVisible(true);
            String categoryName = kategorie.getValue().getNameOfTheCategory();
            form.setPolToLang(true);
            radioButton.setVisible(true);
            radioButton.setItems(categoryName + " to Polish",
                    "Polish to " + categoryName);
            radioButton.setValue("Polish to " + categoryName);
        });

        Button startButton = new Button("Start");
        this.startButton = startButton;

        radioButton = new RadioButtonGroup<>();
        String categoryName = service.findAllCategories().get(0).getNameOfTheCategory();
        radioButton.setItems(categoryName + " to Polish", "Polish to " + categoryName);
        radioButton.setValue("Polish to " + categoryName);

        radioButton.addValueChangeListener(e -> {
            if (form.isPolToLang()) {
                form.setPolToLang(false);
                form.getWord().setValue(form.getSlowka().get(0).getTranslation());
            } else {
                form.setPolToLang(true);
                form.getWord().setValue(form.getSlowka().get(0).getWord());
            }
        });

        startButton.addClickListener(e -> {
            form.setVisible(true);
            form.getTranslation().setVisible(true);
            form.getWord().setVisible(true);
            form.getConfirmButton().setVisible(true);
            if (form.getVerLayout() != null) {
                form.getVerLayout().removeAll();
            }
            startButton.setVisible(false);
            radioButton.setVisible(false);
        });

        HorizontalLayout hl = new HorizontalLayout();
        hl.add(kategorie, startButton, radioButton);
        hl.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        hl.setWidthFull();

        return hl;
    }

    private void configureQuizForm() {
        form = new QuizForm(new ArrayList<>(map.get(kategorie.getValue().getNameOfTheCategory())));
    }
}
