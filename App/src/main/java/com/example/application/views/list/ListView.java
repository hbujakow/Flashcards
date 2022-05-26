package com.example.application.views.list;

import com.example.application.data.entity.Kategoria;
import com.example.application.data.entity.Slowko;
import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.context.annotation.Scope;

import javax.annotation.security.PermitAll;
import java.util.List;

@org.springframework.stereotype.Component
@Scope("prototype")
@Route(value = "", layout = MainLayout.class)
@PageTitle("Fiszki")
@PermitAll
public class ListView extends VerticalLayout {
    Grid<Slowko> grid = new Grid<>(Slowko.class);
    TextField filterText = new TextField();
    WordForm form;
    CategoryForm catForm;
    private CrmService service;

    public ListView(CrmService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureWordForm();
        configureCategoryForm();
        add(getToolbar(), getContent());

        updateList();
        closeEditor();

        //catForm close editor
        catForm.setCategory(null);
        catForm.setVisible(false);
        removeClassName("editing");
    }

    private void closeEditor() {
        form.setWord(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void closeEditor2() {
        catForm.setCategory(null);
        catForm.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findAllWords(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form, catForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setFlexGrow(1, catForm);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureWordForm() {
        form = new WordForm(service.findAllCategories());
        form.setWidth("25em");

        form.addListener(WordForm.SaveEvent.class, this::saveContact);
        form.addListener(WordForm.DeleteEvent.class, this::deleteContact);
        form.addListener(WordForm.CloseEvent.class, e -> closeEditor());
    }

    private void configureCategoryForm() {
        catForm = new CategoryForm();
        catForm.setWidth("25em");

        catForm.addListener(CategoryForm.SaveEvent.class, this::saveCategory);
        catForm.addListener(CategoryForm.CloseEvent.class, e -> closeEditor2());
    }


    private void deleteContact(WordForm.DeleteEvent event) {
        service.deleteWord(event.getWord());
        updateList();
        closeEditor();
    }

    private void saveCategory(CategoryForm.SaveEvent event) {
        service.saveCategory(event.getKategoria());
        updateList();

        catForm.setCategory(null);
        catForm.setVisible(false);
        removeClassName("editing");
    }

    private void saveContact(WordForm.SaveEvent event) {
        service.saveContact(event.getWord());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("word", "translation");
        grid.addColumn(word -> word.getKategoria().getNameOfTheCategory()).setHeader("Category")
                .setSortable(true).setComparator(word -> word.getKategoria().getNameOfTheCategory());

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(e -> editWord(e.getValue()));

    }

    private void editWord(Slowko word) {
        if (word == null) {
            closeEditor();
        } else {
            form.setWord(word);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void editCategory(Kategoria kategoria) {
        if (kategoria == null) {
            catForm.setCategory(null);
            catForm.setVisible(false);
            removeClassName("editing");
        } else {
            catForm.setCategory(kategoria);
            catForm.setVisible(true);
            addClassName("editing");
        }
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by category...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addWordButton = new Button("Add word", VaadinIcon.PLUS_CIRCLE.create());
        addWordButton.addClickListener(e -> addWord());

        Button addCategoryButton = new Button("Add category", VaadinIcon.PLUS_CIRCLE.create());
        addCategoryButton.addClickListener(e -> addCategory());

        Button showAllCategoriesButton = new Button("Show all categories", VaadinIcon.EYE.create());
        showAllCategoriesButton.addClickListener(e -> showAllCategories());


        Button toggleButton = new Button("Toggle dark theme", VaadinIcon.FLASH.create(), click -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList(); // (1)

            if (themeList.contains(Lumo.DARK)) { // (2)
                themeList.remove(Lumo.DARK);
            } else {
                themeList.add(Lumo.DARK);
            }
        });
        toggleButton.addClassName("buttonik");

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addWordButton,
                addCategoryButton,
                showAllCategoriesButton,
                toggleButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void showAllCategories() {
        List<Kategoria> categories = service.findAllCategories();

        if (categories == null) {
            Notification.show("Add some categories", 5000, Notification.Position.MIDDLE);
        } else {
            StringBuilder res = new StringBuilder();
            for (Kategoria category : categories) {
                res.append(category.getNameOfTheCategory()).append("\n");
            }
            Notification.show("All categories: \n" + res, 5000, Notification.Position.MIDDLE);
        }
    }

    private void addCategory() {
        grid.asSingleSelect().clear();
        editCategory(new Kategoria());
    }

    private void addWord() {
        form.refreshCategories(service.findAllCategories());

        grid.asSingleSelect().clear();
        editWord(new Slowko());
    }
}