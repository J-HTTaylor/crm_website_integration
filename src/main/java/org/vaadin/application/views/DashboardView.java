package org.vaadin.application.views;

import org.vaadin.application.layout.DashboardLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;



@PageTitle("Dashboard")
@Route(value = "dashboard", layout = DashboardLayout.class)
public class DashboardView extends VerticalLayout {
    public DashboardView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        addClassName("dashboard");
        
        
        HorizontalLayout topView = new HorizontalLayout();
        topView.setWidthFull();
        topView.setHeight("60%");
        topView.setSpacing(true);
        

        HorizontalLayout bottomView = new HorizontalLayout();
        bottomView.setWidthFull();
        bottomView.setHeight("40%");
        bottomView.setSpacing(true);

        VerticalLayout fullView = new VerticalLayout(topView, bottomView);
        fullView.setSizeFull();
        fullView.setSpacing(true);
        fullView.setPadding(false);


        //topcard info

        VerticalLayout leftTV = new VerticalLayout();
        leftTV.setSizeFull();
        leftTV.setPadding(false);

        VerticalLayout rightTV = new VerticalLayout();
        rightTV.setSizeFull();
        rightTV.setPadding(false);

        Card productsCollected = new Card();
        productsCollected.setTitle("products");
        productsCollected.setSizeFull();

        HorizontalLayout deepStats = new HorizontalLayout();
        deepStats.setWidthFull();
        deepStats.setHeightFull();
        deepStats.setSizeFull();
        

        Card area = new Card();
        area.setTitle("Most active area");
        H2 location = new H2("Staffordshire");
        area.add(location);

        Card common = new Card();
        common.setTitle("Growth");
        H2 growth = new H2("25.5%");
        Icon arrow = VaadinIcon.ARROW_UP.create();
        arrow.setColor("green");
        arrow.setSize("2rem");

        HorizontalLayout changes = new HorizontalLayout(growth, arrow);
        common.add(changes);

        deepStats.add(area, common);
        deepStats.setFlexGrow(1, area, common);

        Grid<String[]> prod = products();
        prod.setHeightFull();
        prod.setWidthFull();

        VerticalLayout productBreak = new VerticalLayout(prod, deepStats);
        productBreak.setFlexGrow(1, prod, deepStats);
        productBreak.setSizeFull();
        

        productsCollected.add(productBreak);


        Card weightSaved = new Card();
        weightSaved.setTitle("weight Saved");
        weightSaved.setSizeFull();

        Card tyresSvd = new Card();
        tyresSvd.setTitle("Tyres");
        H2 tyreAmt = new H2("5,000 Kg");
        tyresSvd.add(tyreAmt);

        Card tubesSvd = new Card();
        tubesSvd.setTitle("Tubes");
        H2 tubesAmt = new H2("3458 Kg");
        tubesSvd.add(tubesAmt);

        Card lionSvd = new Card();
        lionSvd.setTitle("Batteries");
        H2 lionAmt = new H2("123 Kg");
        lionSvd.add(lionAmt);

        HorizontalLayout  allSvd = new HorizontalLayout(tyresSvd, tubesSvd, lionSvd);
        allSvd.setSizeFull();
        allSvd.setFlexGrow(1, tyresSvd, tubesSvd, lionSvd);

        weightSaved.add(allSvd);

        Card cost = new Card();
        cost.setTitle("cost");
        cost.setSizeFull();

        Card increase = new Card();
        increase.setTitle("Revenue");
        H2 increaseAmt = new H2("£385.26");
        increaseAmt.getStyle().set("color", "green");
        increase.add(increaseAmt);

        Card deacrease = new Card();
        deacrease.setTitle("Expenses");
        H2 deacreaseAmt = new H2("£100.00");
        deacreaseAmt.getStyle().set("color", "red");
        deacrease.add(deacreaseAmt);

        Card total = new Card();
        total.setTitle("Net Change");
        H2 totalAmt = new H2("+ £285.26");
        totalAmt.getStyle().set("color", "green");
        total.add(totalAmt);

        HorizontalLayout  totalCost = new HorizontalLayout(increase, deacrease, total);
        totalCost .setSizeFull();
        totalCost .setFlexGrow(1, increase, deacrease, total);
        cost.add(totalCost);

        leftTV.add(productsCollected);

        rightTV.setFlexGrow(1, weightSaved);
        rightTV.setFlexGrow(1, cost);

        topView.add(leftTV, rightTV);
        topView.setFlexGrow(1, leftTV);
        topView.setFlexGrow(1, rightTV);

        rightTV.add(weightSaved, cost);

        //bottom view
        Card upcoming = new Card();
        upcoming.setTitle("Upcoming Deliveries");
        upcoming.setSizeFull();

        upcoming.add(upcomingTable());

        Card money = new Card();
        money.setTitle("Monthly Finances");
        money.setSizeFull();

        money.add(upcomingDebt());

        bottomView.add(upcoming, money);
        bottomView.setFlexGrow(1, upcoming);
        bottomView.setFlexGrow(1, money);

        add(fullView);
    }

    public Grid<String[]> upcomingTable() {
        Grid<String[]> futureDels = new Grid<>(String[].class, false);
        futureDels.addThemeVariants(GridVariant.COLUMN_BORDERS);
        futureDels.addThemeVariants(GridVariant.ROW_STRIPES);
        
        futureDels.addColumn(row -> row[0]).setHeader("Product");
        futureDels.addColumn(row -> row[1]).setHeader("Location");
        futureDels.addColumn(row -> row[2]).setHeader("Due Date");
        futureDels.addColumn(row -> row[3]).setHeader("Link");

        futureDels.setItems(
            new String[]{"Solar Panel X1", "Warehouse A", "2026-06-15", "View Details"},
            new String[]{"Inverter Pro", "Warehouse B", "2026-06-18", "View Details"},
            new String[]{"Battery Pack 10kWh", "Warehouse A", "2026-06-20", "View Details"},
            new String[]{"Mounting Brackets", "Warehouse C", "2026-06-22", "View Details"},
            new String[]{"DC Cable Roll", "Warehouse B", "2026-06-25", "View Details"}
        );

        //futureDels.setAllRowsVisible(true);
        futureDels.setPageSize(3);
        futureDels.setSizeFull();
        return futureDels;
    }

    public Grid<String[]> upcomingDebt() {
        Grid<String[]> futureDebt = new Grid<>(String[].class, false);
        futureDebt.addThemeVariants(GridVariant.COLUMN_BORDERS);
        futureDebt.addThemeVariants(GridVariant.ROW_STRIPES);
        

        futureDebt.addColumn(row -> row[0]).setHeader("Creditor/Debeteer");
        futureDebt.addColumn(row -> row[1]).setHeader("Flow Type");
        futureDebt.addColumn(row -> row[2]).setHeader("Amount Due");
        futureDebt.addColumn(row -> row[3]).setHeader("Reason");
        futureDebt.addColumn(row -> row[4]).setHeader("Due Date");

        futureDebt.setItems(
            new String[]{"HSBC", "Outgoing","£123", "Disposal fees", "23-11-26"},
            new String[]{"NationWide", "Outgoing", "£56,354", "Machine Repair", "1-12-26"},
            new String[]{"Chase", "Incoming", "£12,082.43", "Tax return", "05-12-26"}

        );

        //futureDels.setAllRowsVisible(true);
        futureDebt.setPageSize(3);
        futureDebt.setSizeFull();
        return futureDebt;
    }

        public Grid<String[]> products() {
        Grid<String[]> products = new Grid<>(String[].class, false);
        products.addThemeVariants(GridVariant.COLUMN_BORDERS);
        products.addThemeVariants(GridVariant.ROW_STRIPES);
        

        products.addColumn(row -> row[0]).setHeader("Name");
        products.addColumn(row -> row[1]).setHeader("Monthly");
        products.addColumn(row -> row[2]).setHeader("Year");
        products.addColumn(row -> row[3]).setHeader("Total");

        products.setItems(
            new String[]{"Tyres", "154", "1000", "10000"},
            new String[]{"Tubes", "60", "849", "1234"},
            new String[]{"Li-ion Battery", "4", "70", "125"}

        );

        //futureDels.setAllRowsVisible(true);
        products.setPageSize(3);
        products.setSizeFull();
        return products;
    }
}
