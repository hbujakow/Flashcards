package com.example.application.views;

import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.charts.model.style.Style;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;

import javax.annotation.security.PermitAll;


@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard")
@PermitAll
public class DashboardView extends VerticalLayout {

    private final CrmService service;

    public DashboardView(CrmService service) {
        this.service = service;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(getContactStats(), getCompaniesChart());
    }

    private Component getContactStats() {
        Span stats = new Span("Categories: " + service.countWords());
        stats.addClassNames("text-xl", "mt-m", "napisik");
        return stats;
    }

    private Chart getCompaniesChart() {
        Chart chart = new Chart(ChartType.PIE);

        getPlot(chart);

        DataSeries dataSeries = new DataSeries();
        service.findAllCategories().forEach(company ->
                dataSeries.add(new DataSeriesItem(company.getNameOfTheCategory(), company.getIloscSlowek())));
        chart.getConfiguration().setSeries(dataSeries);
        return chart;
    }

    public static void getPlot(Chart chart) {
        PlotOptionsPie options = new PlotOptionsPie();
        Style style = new Style();
        style.setFontSize("20px");
        DataLabels datalab = new DataLabels();
        datalab.setStyle(style);
        options.setDataLabels(datalab);
        chart.getConfiguration().setPlotOptions(options);

        if (UI.getCurrent().getElement().getThemeList().contains(Lumo.DARK)) {
            chart.getConfiguration().getChart().setBackgroundColor(new SolidColor("#233348"));
        } else {
            chart.getConfiguration().getChart().setBackgroundColor(new SolidColor("#ffffff"));
        }
    }
}
