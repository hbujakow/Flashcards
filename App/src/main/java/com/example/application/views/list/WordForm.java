package com.example.application.views.list;

import com.example.application.data.entity.Kategoria;
import com.example.application.data.entity.Slowko;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class WordForm extends FormLayout {
    Binder<Slowko> binder = new BeanValidationBinder<>(Slowko.class);

    TextField word = new TextField("word");
    TextField translation = new TextField("translation");
    ComboBox<Kategoria> kategoria = new ComboBox<>("category");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    private Slowko slowko;

    public WordForm(List<Kategoria> kategorie) {
        addClassName("contact-form");
        binder.bindInstanceFields(this);

        kategoria.setItems(kategorie);
        kategoria.setItemLabelGenerator(Kategoria::getNameOfTheCategory);

        add(word, translation, kategoria,
                createButtonsLayout());
    }

    public void refreshCategories(List<Kategoria> kategorie) {
        kategoria.setItems(kategorie);
    }

    public void setWord(Slowko slowko) {
        this.slowko = slowko;
        binder.readBean(slowko);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, slowko)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(slowko);
            fireEvent(new SaveEvent(this, slowko));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class WordFormEvent extends ComponentEvent<WordForm> {
        private Slowko slowko;

        protected WordFormEvent(WordForm source, Slowko slowko) {
            super(source, false);
            this.slowko = slowko;
        }

        public Slowko getWord() {
            return slowko;
        }
    }

    public static class SaveEvent extends WordFormEvent {
        SaveEvent(WordForm source, Slowko slowko) {
            super(source, slowko);
        }
    }

    public static class DeleteEvent extends WordFormEvent {
        DeleteEvent(WordForm source, Slowko slowko) {
            super(source, slowko);
        }
    }

    public static class CloseEvent extends WordFormEvent {
        CloseEvent(WordForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
