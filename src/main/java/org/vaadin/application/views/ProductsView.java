package org.vaadin.application.views;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vaadin.application.databaseCommands;
import org.vaadin.application.layout.DashboardLayout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Products")
@Route(value = "hub/Products", layout = DashboardLayout.class)

public class ProductsView extends VerticalLayout  implements AfterNavigationObserver{

    private final databaseCommands dbCommands;
    private final Grid<String[]> grid = new Grid<>();
    private  ListDataProvider<String[]> dataProvider = new ListDataProvider<>(new ArrayList<>());
    
    public ProductsView(databaseCommands dbcommands) {
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

        Grid.Column<String[]> colName = grid.addColumn(row -> row[0]).setHeader("Name").setSortable(true);
        Grid.Column<String[]> colPrice = grid.addColumn(row -> row[1]).setHeader("Price").setSortable(true);
        Grid.Column<String[]> colSku = grid.addColumn(row -> row[2]).setHeader("SKU").setSortable(true);
        Grid.Column<String[]> colColl = grid.addColumn(row -> row[3]).setHeader("Collected").setSortable(true);
        Grid.Column<String[]> colWait = grid.addColumn(row -> row[4]).setHeader("Waiting").setSortable(true);
        
        GridContextMenu<String[]> menu = grid.addContextMenu();
        menu.addItem("Add", event -> {
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle("New Product");

            TextField productNameField = new TextField("Product name");
            TextField priceNameField = new TextField("Price");
            TextField skuNameField = new TextField("SKU");
        

            FormLayout dialogLayout = new FormLayout(productNameField, priceNameField, skuNameField);
            dialogLayout.setAutoResponsive(true);
            dialogLayout.setColumnWidth("18rem");
            dialogLayout.setExpandFields(true);

            dialog.add(dialogLayout);
            
            
            Button saveButton = new Button("Add", e -> {
                try {
                    dbcommands.addProduct(productNameField.getValue(), priceNameField.getValue(), skuNameField.getValue());
                    dialog.close();
                    dataProvider.refreshAll();

                } 
                catch (org.springframework.dao.DataIntegrityViolationException nameException) {
                    Notification.show("A product with this name already exists or has existed.", 3000, Notification.Position.MIDDLE);
                }
                catch (java.lang.NumberFormatException numbException) {
                    Notification.show("A product's price must be a monetary value from 0.00 to 9999999999.99 .", 3000, Notification.Position.MIDDLE);
                }
            });

            Button cancelButton = new Button("Cancel", e -> dialog.close());

            dialog.getFooter().add(cancelButton);
            dialog.getFooter().add(saveButton);
            
            dialog.open();
            dataProvider.refreshAll();
        });
        menu.addItem("Edit", event -> {
            event.getItem().ifPresent(row -> {
                String name = row[0];
                String price = row[1];
                String sku = row[2];
            
                Dialog dialog = new Dialog();
                dialog.setHeaderTitle("Update Product");

                TextField productNameField = new TextField("Product name");
                productNameField.setValue(name);
                TextField priceNameField = new TextField("Price");
                priceNameField.setValue(price);
                TextField skuNameField = new TextField("SKU");
                skuNameField.setValue(sku);
            

                FormLayout dialogLayout = new FormLayout(productNameField, priceNameField, skuNameField);
                dialogLayout.setAutoResponsive(true);
                dialogLayout.setColumnWidth("18rem");
                dialogLayout.setExpandFields(true);

                dialog.add(dialogLayout);
                
                
                Button saveButton = new Button("Update", e -> {
                    try {
                        dbcommands.updateProduct(name, productNameField.getValue(), priceNameField.getValue(), skuNameField.getValue());
                        dialog.close();
                        refreshGridData();

                    } 
                    catch (org.springframework.dao.DataIntegrityViolationException nameException) {
                        Notification.show("A product with this name already exists or has existed.", 3000, Notification.Position.MIDDLE);
                    }
                    catch (java.lang.NumberFormatException numbException) {
                        Notification.show("A product's price must be a monetary value from 0.00 to 9999999999.99 .", 3000, Notification.Position.MIDDLE);
                    }
                });

                Button cancelButton = new Button("Cancel", e -> dialog.close());

                dialog.getFooter().add(cancelButton);
                dialog.getFooter().add(saveButton);
                
                dialog.open();
                });
        });
        menu.addItem("Delete", event -> {
            event.getItem().ifPresent(row -> {
                dbcommands.deleteProduct(row[0]);
                dataProvider.getItems().remove(row);
                refreshGridData();
            });
        });

        //db pull

        grid.setItems(dataProvider);
        grid.setPageSize(25);
        grid.setSizeFull();
        grid.setHeightFull();
        grid.addThemeVariants(GridVariant.COLUMN_BORDERS);
        grid.addThemeVariants(GridVariant.ROW_STRIPES);


        //search sections
        TextField searchName = new TextField();
        searchName.setPlaceholder("Name");

        TextField searchPrice = new TextField();
        searchPrice.setPlaceholder("Price");

        TextField searchSku = new TextField();
        searchSku.setPlaceholder("SKU");

        TextField searchColl = new TextField();
        searchColl.setPlaceholder("Collected");

        TextField searchWait = new TextField();
        searchWait.setPlaceholder("Waiting");


        for (TextField tf : new TextField[]{searchName, searchPrice, searchSku, searchColl, searchWait}) {
            tf.setWidthFull();
            tf.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
            tf.setValueChangeMode(ValueChangeMode.EAGER);
            tf.addValueChangeListener(e -> dataProvider.refreshAll());
        }

        HeaderRow searchHeaderRow = grid.prependHeaderRow();
        searchHeaderRow.getCell(colName).setComponent(searchName);
        searchHeaderRow.getCell(colPrice).setComponent(searchPrice);
        searchHeaderRow.getCell(colSku).setComponent(searchSku);
        searchHeaderRow.getCell(colColl).setComponent(searchColl);
        searchHeaderRow.getCell(colWait).setComponent(searchWait);

    dataProvider.setFilter(row -> {
        String locationFilter = searchName.getValue() == null ? "" : searchName.getValue().toLowerCase();
        String productFilter = searchPrice.getValue() == null ? "" : searchPrice.getValue().toLowerCase();
        String statusFilter = searchSku.getValue() == null ? "" : searchSku.getValue().toLowerCase();
        String dateFilter = searchColl.getValue() == null ? "" : searchColl.getValue().toLowerCase();
        String cityFilter = searchWait.getValue() == null ? "" : searchWait.getValue().toLowerCase();

        boolean matchesLocation = row[0] != null && row[0].toLowerCase().contains(locationFilter);
        boolean matchesProduct = row[1] != null && row[1].toLowerCase().contains(productFilter);
        boolean matchesStatus = row[2] != null && row[2].toLowerCase().contains(statusFilter);
        boolean matchesDate = row[3] != null && row[3].toLowerCase().contains(dateFilter);
        boolean matchesCity = row[4] != null && row[3].toLowerCase().contains(cityFilter);


        return matchesLocation && matchesProduct && matchesStatus && matchesDate && matchesCity;
    });

        fullView.add(grid);
        add(fullView);
    }
    

    public void afterNavigation(AfterNavigationEvent event) {
        refreshGridData();
    }

    private void refreshGridData() {
    List<String[]> dbData = dbCommands.allProductInfo();
    dataProvider.getItems().clear();
    if (dbData != null) {
        dataProvider.getItems().addAll(dbData);
    }
    dataProvider.refreshAll();
}
    
}