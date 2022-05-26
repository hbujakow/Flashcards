package com.example.application.views.list;

import com.example.application.data.entity.Slowko;
import com.example.application.views.DashboardView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class QuizForm extends FormLayout {
    TextArea word = new TextArea("Word to translate");
    TextField translation = new TextField("Translation");

    Button confirmButton = new Button("Confirm answer");
    VerticalLayout verLayout;
    Integer index = 0;
    List<Slowko> slowka;

    boolean PolToLang = true;

    private HashMap<String, Integer> poprawneNiepoprawne;

    public QuizForm(List<Slowko> slowka) {
        this.slowka = slowka;

        this.poprawneNiepoprawne = new HashMap<>();
        this.poprawneNiepoprawne.put("poprawne", 0);
        this.poprawneNiepoprawne.put("niepoprawne", 0);

        addClassName("contact-form");
        if (PolToLang) {
            word.setValue(slowka.get(index).getWord());
        } else {
            word.setValue(slowka.get(index).getTranslation());
        }

        word.setReadOnly(true);

        word.setWidth("75%");
        translation.setWidth("75%");

        VerticalLayout vl = new VerticalLayout(word, translation, createButtonsLayout());
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.BASELINE);
        vl.setWidthFull();
        add(vl);

        confirmButton.addClickListener(event -> {

            if (PolToLang) {
                if (this.slowka.get(index).getTranslation().equals(translation.getValue())) {
                    System.out.println("poprawna odpowiedz");
                    this.poprawneNiepoprawne.put("poprawne", this.getPoprawneNiepoprawne().get("poprawne") + 1);
                } else {
                    System.out.println("niepoprawna odpowiedz");
                    this.poprawneNiepoprawne.put("niepoprawne", this.getPoprawneNiepoprawne().get("niepoprawne") + 1);
                }
            } else {
                if (this.slowka.get(index).getWord().equals(translation.getValue())) {
                    System.out.println("poprawna odpowiedz");
                    this.poprawneNiepoprawne.put("poprawne", this.getPoprawneNiepoprawne().get("poprawne") + 1);
                } else {
                    System.out.println("niepoprawna odpowiedz");
                    this.poprawneNiepoprawne.put("niepoprawne", this.getPoprawneNiepoprawne().get("niepoprawne") + 1);
                }
            }

            index += 1;
            if (index >= this.slowka.size()) {
                System.out.println("koniec slowek");
                translation.setVisible(false);
                translation.setValue("");
                confirmButton.setVisible(false);
                word.setLabel(null);
                word.setValue("Total number of words: " +
                        (poprawneNiepoprawne.get("poprawne") + poprawneNiepoprawne.get("niepoprawne"))
                        + "\n"
                        + "Number of correct answers: "
                        + poprawneNiepoprawne.get("poprawne")
                        + "\n"
                        + "Number of incorrect answers: "
                        + poprawneNiepoprawne.get("niepoprawne"));
                index = 0;
                word.setSizeFull();

                // podsumowanie - wykres
                Html ht = new Html("<div><h2>Summary</h2></div>");
                Chart chart = chartPie();
                if (UI.getCurrent().getElement().getThemeList().contains(Lumo.DARK)) {
                    chart.getConfiguration().getChart().setBackgroundColor(new SolidColor("#233348"));
                }
                this.verLayout = new VerticalLayout(ht, chart);
                verLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
                vl.add(verLayout);

                this.poprawneNiepoprawne.put("poprawne", 0);
                this.poprawneNiepoprawne.put("niepoprawne", 0);

            } else {
                System.out.println("kolejne słówko");
                System.out.println(this.slowka.get(index).getWord());
                if (PolToLang) {
                    word.setValue(this.slowka.get(index).getWord());
                } else {
                    word.setValue(this.slowka.get(index).getTranslation());
                }

                translation.setValue("");
            }
        });
    }

    public void setPolToLang(boolean polToLang) {
        PolToLang = polToLang;
    }

    public boolean isPolToLang() {
        return PolToLang;
    }

    public VerticalLayout getVerLayout() {
        return verLayout;
    }

    private Chart chartPie() {
        Chart chart = new Chart(ChartType.PIE);
        DataSeries dataSeries = new DataSeries();

        DashboardView.getPlot(chart);

        dataSeries.add(new DataSeriesItem("incorrect", this.getPoprawneNiepoprawne().get("niepoprawne")));
        dataSeries.add(new DataSeriesItem("correct", this.getPoprawneNiepoprawne().get("poprawne")));
        chart.getConfiguration().setSeries(dataSeries);

        return chart;
    }

    private Component createButtonsLayout() {
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        confirmButton.addClickShortcut(Key.ENTER);

        HorizontalLayout hl = new HorizontalLayout(confirmButton);
        hl.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        return hl;
    }

    public TextField getTranslation() {
        return translation;
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    public TextArea getWord() {
        return word;
    }

    public void setWord(TextArea word) {
        this.word = word;
    }

    public List<Slowko> getSlowka() {
        return slowka;
    }

    public void setSlowka(List<Slowko> slowka) {
        this.slowka = slowka;
        Collections.shuffle(this.slowka);
    }

    public HashMap<String, Integer> getPoprawneNiepoprawne() {
        return poprawneNiepoprawne;
    }
}
