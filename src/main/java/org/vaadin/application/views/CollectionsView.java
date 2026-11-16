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

@PageTitle("Deliveries & Collections")
@Route(value = "hub/DeliveriesAndCollections", layout = DashboardLayout.class)
public class CollectionsView extends VerticalLayout  implements AfterNavigationObserver{

    private final databaseCommands dbCommands;
    private final Grid<String[]> grid = new Grid<>();
    private  ListDataProvider<String[]> dataProvider = new ListDataProvider<>(new ArrayList<>());

    public CollectionsView(databaseCommands dbcommands) {
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

        Grid.Column<String[]> colProduct = grid.addColumn(row -> row[0]).setHeader("Product").setSortable(true);
        Grid.Column<String[]> colLocation = grid.addColumn(row -> row[1]).setHeader("Location").setSortable(true);
        Grid.Column<String[]> colDue = grid.addColumn(row -> row[2]).setHeader("Due Date").setSortable(true);
        Grid.Column<String[]> colStatus = grid.addColumn(row -> row[3]).setHeader("Status").setSortable(true);
        Grid.Column<String[]> colService = grid.addColumn(row -> row[4]).setHeader("Service").setSortable(true);
        //db pull

        grid.setItems(dataProvider);
        grid.setPageSize(25);
        grid.setSizeFull();
        grid.setHeightFull();
        grid.addThemeVariants(GridVariant.COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.ROW_STRIPES);


        //search sections
        TextField searchProduct = new TextField();
        searchProduct.setPlaceholder("Product");

        TextField searchLocation = new TextField();
        searchLocation.setPlaceholder("Location");

        TextField searchDue = new TextField();
        searchDue.setPlaceholder("Due Date");

        TextField searchStatus = new TextField();
        searchStatus.setPlaceholder("Status");

        TextField searchService = new TextField();
        searchService.setPlaceholder("Service");


        for (TextField tf : new TextField[]{searchProduct, searchLocation, searchDue, searchStatus, searchService}) {
            tf.setWidthFull();
            tf.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
            tf.setValueChangeMode(ValueChangeMode.EAGER);
            tf.addValueChangeListener(e -> dataProvider.refreshAll());
        }

        HeaderRow searchHeaderRow = grid.prependHeaderRow();
        searchHeaderRow.getCell(colProduct).setComponent(searchProduct);
        searchHeaderRow.getCell(colLocation).setComponent(searchLocation);
        searchHeaderRow.getCell(colDue).setComponent(searchDue);
        searchHeaderRow.getCell(colStatus).setComponent(searchStatus);
        searchHeaderRow.getCell(colService).setComponent(searchService);

    dataProvider.setFilter(row -> {
        String productFilter = searchProduct.getValue() == null ? "" : searchProduct.getValue().toLowerCase();
        String locationFilter = searchLocation.getValue() == null ? "" : searchLocation.getValue().toLowerCase();
        String dueFilter = searchDue.getValue() == null ? "" : searchDue.getValue().toLowerCase();
        String statusFilter = searchStatus.getValue() == null ? "" : searchStatus.getValue().toLowerCase();
        String serviceFilter = searchService.getValue() == null ? "" : searchService.getValue().toLowerCase();

        boolean matchesProduct = row[0] != null && row[0].toLowerCase().contains(productFilter);
        boolean matchesLocation = row[1] != null && row[1].toLowerCase().contains(locationFilter);
        boolean matchesDue = row[2] != null && row[2].toLowerCase().contains(dueFilter);
        boolean matchesStatus = row[3] != null && row[3].toLowerCase().contains(statusFilter);
        boolean matchesService = row[4] != null && row[3].toLowerCase().contains(serviceFilter);


        return matchesLocation && matchesProduct && matchesStatus && matchesDue && matchesService;
    });

        fullView.add(grid);
        add(fullView);
    }
    

    public void afterNavigation(AfterNavigationEvent event) {
        List<String[]> dbData = dbCommands.allCollectionInfo();
        dataProvider.getItems().clear();
        if (dbData != null) {
            dataProvider.getItems().addAll(dbData);
        }
        dataProvider.refreshAll();
    }
    
}