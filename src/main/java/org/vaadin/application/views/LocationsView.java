package org.vaadin.application.views;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.application.layout.DashboardLayout;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Locations")
@Route(value = "hub/Locations", layout = DashboardLayout.class)
public class LocationsView extends VerticalLayout {
    public LocationsView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        addClassName("dashboard");
        
        VerticalLayout fullView = new VerticalLayout();
        fullView.setSizeFull();
        fullView.setWidthFull();
        fullView.setHeightFull();
        fullView.setSpacing(true);
        fullView.setPadding(false);


        Grid<String[]> grid = new Grid<>();
        grid.addColumn(row -> row[0]).setHeader("Location").setSortable(true).setHeader("Location");
        grid.addColumn(row -> row[1]).setHeader("Product").setSortable(true).setHeader("Product");
        grid.addColumn(row -> row[2]).setHeader("Status").setSortable(true).setHeader("Status");
        grid.addColumn(row -> row[3]).setHeader("Join Date").setSortable(true).setHeader("Join Date");
        grid.addColumn(row -> row[4]).setHeader("City").setSortable(true).setHeader("City");
        grid.addColumn(row -> row[5]).setHeader("Postcode").setSortable(true).setHeader("Postcode");
        grid.addColumn(row -> row[6]).setHeader("Country").setSortable(true).setHeader("Country");

        // Create a data provider instance with a configurable filter, allowing
        // the filter value to be set programmatically via setFilter().
        List<String[]> csvData = loadCsvData("locations.csv");
        ListDataProvider<String[]> dataProvider = new ListDataProvider<>(csvData);

        grid.setItems(dataProvider);
        grid.setPageSize(25);
        grid.setSizeFull();
        grid.setHeightFull();
        grid.addThemeVariants(GridVariant.COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.ROW_STRIPES);



        TextField searchField = new TextField();
        searchField.setWidth("50%");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> {
        String filterText = event.getValue().toLowerCase();
        dataProvider.setFilter(row -> row[0].toLowerCase().contains(filterText) || row[1].toLowerCase().contains(filterText) || row[2].toLowerCase().contains(filterText) || row[3].toLowerCase().contains(filterText) || row[4].toLowerCase().contains(filterText) || row[5].toLowerCase().contains(filterText) || row[6].toLowerCase().contains(filterText));
        });

        fullView.add(searchField, grid);
        add(fullView);
    }
    

    private List<String[]> loadCsvData(String fileName) {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
            getClass().getClassLoader().getResourceAsStream(fileName), StandardCharsets.UTF_8))) {
            
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] values = line.split(",");
                records.add(values);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }
}
