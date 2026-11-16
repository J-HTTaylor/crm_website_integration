package org.vaadin.application.views;


import java.util.ArrayList;
import java.util.List;

import org.vaadin.application.databaseCommands;
import org.vaadin.application.layout.DashboardLayout;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Locations")
@Route(value = "hub/Locations", layout = DashboardLayout.class)

public class LocationsView extends VerticalLayout  implements AfterNavigationObserver{
    
    private final databaseCommands dbCommands;
    private final Grid<String[]> grid = new Grid<>();
    private  ListDataProvider<String[]> dataProvider = new ListDataProvider<>(new ArrayList<>());

    public LocationsView(databaseCommands dbcommands) {
        
        this.dbCommands = dbcommands;

        
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

        Grid.Column<String[]> colLocation = grid.addColumn(row -> row[0]).setHeader("Location").setSortable(true);
        Grid.Column<String[]> colProduct = grid.addColumn(row -> row[5]).setHeader("Product").setSortable(true);
        Grid.Column<String[]> colStatus = grid.addColumn(row -> row[1]).setHeader("Status").setSortable(true);
        Grid.Column<String[]> colJoin = grid.addColumn(row -> row[3]).setHeader("Join Date").setSortable(true);
        Grid.Column<String[]> colCity = grid.addColumn(row -> row[2]).setHeader("City").setSortable(true);
        Grid.Column<String[]> colPost = grid.addColumn(row -> row[4]).setHeader("Postcode").setSortable(true);
        Grid.Column<String[]> colContact = grid.addColumn(row -> row[6]).setHeader("Contact").setSortable(true);
        //db pull

        grid.setItems(dataProvider);
        grid.setPageSize(25);
        grid.setSizeFull();
        grid.setHeightFull();
        grid.addThemeVariants(GridVariant.COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.ROW_STRIPES);


        //search sections
        TextField searchField = new TextField();
        searchField.setPlaceholder("Location");

        TextField searchProduct = new TextField();
        searchProduct.setPlaceholder("Product");

        TextField searchstatus = new TextField();
        searchstatus.setPlaceholder("Status");

        TextField searchJoin = new TextField();
        searchJoin.setPlaceholder("Join Date");

        TextField searchCity = new TextField();
        searchCity.setPlaceholder("City");

        TextField searchPost = new TextField();
        searchPost.setPlaceholder("Postcode");

        TextField searchContact = new TextField();
        searchContact.setPlaceholder("Contact");

        for (TextField tf : new TextField[]{searchField, searchProduct, searchstatus, searchJoin, searchCity, searchPost, searchContact}) {
            tf.setWidthFull();
            tf.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
            tf.setValueChangeMode(ValueChangeMode.EAGER);
            tf.addValueChangeListener(e -> dataProvider.refreshAll());
        }

        HeaderRow searchHeaderRow = grid.prependHeaderRow();
        searchHeaderRow.getCell(colLocation).setComponent(searchField);
        searchHeaderRow.getCell(colProduct).setComponent(searchProduct);
        searchHeaderRow.getCell(colStatus).setComponent(searchstatus);
        searchHeaderRow.getCell(colJoin).setComponent(searchJoin);
        searchHeaderRow.getCell(colCity).setComponent(searchCity);
        searchHeaderRow.getCell(colPost).setComponent(searchPost);
        searchHeaderRow.getCell(colContact).setComponent(searchContact);

    dataProvider.setFilter(row -> {
        String locationFilter = searchField.getValue() == null ? "" : searchField.getValue().toLowerCase();
        String productFilter = searchProduct.getValue() == null ? "" : searchProduct.getValue().toLowerCase();
        String statusFilter = searchstatus.getValue() == null ? "" : searchstatus.getValue().toLowerCase();
        String dateFilter = searchJoin.getValue() == null ? "" : searchJoin.getValue().toLowerCase();
        String cityFilter = searchCity.getValue() == null ? "" : searchCity.getValue().toLowerCase();
        String postFilter = searchPost.getValue() == null ? "" : searchPost.getValue().toLowerCase();
        String contactFilter = searchContact.getValue() == null ? "" : searchContact.getValue().toLowerCase();

        boolean matchesLocation = row[0] != null && row[0].toLowerCase().contains(locationFilter);
        boolean matchesProduct = row[5] != null && row[5].toLowerCase().contains(productFilter);
        boolean matchesStatus = row[1] != null && row[1].toLowerCase().contains(statusFilter);
        boolean matchesDate = row[3] != null && row[3].toLowerCase().contains(dateFilter);
        boolean matchesCity = row[2] != null && row[2].toLowerCase().contains(cityFilter);
        boolean matchesPost = row[4] != null && row[4].toLowerCase().contains(postFilter);
        boolean matchesContact = row[6] != null && row[6].toLowerCase().contains(contactFilter);

        return matchesLocation && matchesProduct && matchesStatus && matchesDate && matchesCity && matchesPost && matchesContact;
    });

        fullView.add(grid);
        add(fullView);
    }
    

    public void afterNavigation(AfterNavigationEvent event) {
        List<String[]> dbData = dbCommands.allCentreInfo();
        dataProvider.getItems().clear();
        if (dbData != null) {
            dataProvider.getItems().addAll(dbData);
        }
        dataProvider.refreshAll();
    }
}
