package com.example.application.views;

import com.example.application.security.SecurityService;
import com.example.application.views.list.ListView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;


public class MainLayout extends AppLayout {

    private SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Fiszki");
        logo.addClassNames("text-l", "m-m", "text-xl");

        Button logout = new Button("Logout", VaadinIcon.CHECK.create(), e -> securityService.logout());
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink listView = new RouterLink("List", ListView.class);
//        listView.setHighlightCondition(HighlightConditions.sameLocation());

        Div menu = new Div();
        menu.add(listView);
        menu.addClassNames("linki");

        Div menu2 = new Div();
        RouterLink dashboardView = new RouterLink("Dashboard", DashboardView.class);
        menu2.add(dashboardView);
        menu2.addClassNames("linki");

        Div menu3 = new Div();
        RouterLink learnView = new RouterLink("Learn", LearnView.class);
        menu3.add(learnView);
        menu3.addClassNames("linki");

        addToDrawer(new VerticalLayout(menu, menu2, menu3));
    }

}
