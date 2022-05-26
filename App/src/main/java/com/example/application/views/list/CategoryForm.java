package com.example.application.views.list;

import com.example.application.data.entity.Kategoria;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class CategoryForm extends FormLayout {
    Binder<Kategoria> binder = new BeanValidationBinder<>(Kategoria.class);
    TextField nameOfTheCategory = new TextField("category");

    Button save = new Button("Save");
    Button close = new Button("Cancel");

    private Kategoria kategoria;

    public CategoryForm() {
        addClassName("category-form");
        binder.bindInstanceFields(this);
        add(nameOfTheCategory, createButtonsLayout());
    }

    public void setCategory(Kategoria kategoria) {
        this.kategoria = kategoria;
        binder.readBean(kategoria);

    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new CategoryForm.CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(kategoria);
            fireEvent(new CategoryForm.SaveEvent(this, kategoria));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    // Events
    public static abstract class CategoryFormEvent extends ComponentEvent<CategoryForm> {
        private Kategoria kategoria;

        protected CategoryFormEvent(CategoryForm source, Kategoria kategoria) {
            super(source, false);
            this.kategoria = kategoria;
        }

        public Kategoria getKategoria() {
            return kategoria;
        }
    }

    public static class SaveEvent extends CategoryFormEvent {
        SaveEvent(CategoryForm source, Kategoria kategoria) {
            super(source, kategoria);
        }
    }


    public static class CloseEvent extends CategoryFormEvent {
        CloseEvent(CategoryForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
