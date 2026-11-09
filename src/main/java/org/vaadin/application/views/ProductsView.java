package org.vaadin.application.views;

import java.util.List;

import org.vaadin.application.layout.DashboardLayout;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Products")
@Route(value = "hub/Products", layout = DashboardLayout.class)
public class ProductsView extends VerticalLayout {
    public ProductsView() {
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
        grid.addColumn(row -> row[0]).setHeader("Name").setSortable(true).setHeader("Name");
        grid.addColumn(row -> row[1]).setHeader("Price").setSortable(true).setHeader("Price");
        grid.addColumn(row -> row[2]).setHeader("SKU").setSortable(true).setHeader("SKU");
        grid.addColumn(row -> row[3]).setHeader("Collected").setSortable(true).setHeader("collected");
        grid.addColumn(row -> row[4]).setHeader("Waiting").setSortable(true).setHeader("Waiting");


        // Create a data provider instance with a configurable filter, allowing
        // the filter value to be set programmatically via setFilter().
        ListDataProvider<String[]> dataProvider = new ListDataProvider<>(List.of(
            new String[]{"Tyres", "£1.50", "T150", "10000", "282"},
            new String[]{"Tubes", "£1.00", "T100", "1234", "1212"},
            new String[]{"Li-ion Battery", "£30.00" ,"L3000", "125", "25"}
        ));

        grid.setItems(dataProvider);
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.ROW_STRIPES);

        TextField searchField = new TextField();
        searchField.setWidth("50%");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> {
        String filterText = event.getValue().toLowerCase();
        dataProvider.setFilter(row -> row[0].toLowerCase().contains(filterText) || row[1].toLowerCase().contains(filterText) || row[2].toLowerCase().contains(filterText) || row[3].toLowerCase().contains(filterText));
        });

        fullView.add(searchField, grid);
        add(fullView);
    }
    
}