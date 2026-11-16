package org.vaadin.application.views;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.application.databaseCommands;
import org.vaadin.application.layout.DashboardLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;



@PageTitle("Dashboard")
@Route(value = "dashboard", layout = DashboardLayout.class)
public class DashboardView extends VerticalLayout implements AfterNavigationObserver{

    private final databaseCommands dbCommands;
    private final Grid<String[]> grid = new Grid<>();
    private  ListDataProvider<String[]> dataProvider = new ListDataProvider<>(new ArrayList<>());
    
    public DashboardView(databaseCommands dbcommands) {
        this.dbCommands = dbcommands;
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
        H2 location = new H2(dbcommands.commonArea());
        area.add(location);

        Card common = new Card();
        common.setTitle("Growth");
        String growthVal = dbcommands.growth();
        H2 growth = new H2(growthVal);
        Icon arrow;

        if(growthVal.equals("0.0%")){
                arrow = VaadinIcon.MINUS.create();
            arrow.setColor("grey");
            arrow.setSize("2rem");
        }
        
        else if(growthVal.contains("-")){
                arrow = VaadinIcon.ARROW_DOWN.create();
            arrow.setColor("red");
            arrow.setSize("2rem");
        }
        else{
            arrow = VaadinIcon.ARROW_UP.create();
            arrow.setColor("green");
            arrow.setSize("2rem");
        }

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

        List<Integer[]> collections = dbcommands.collectedProductbreakdown();

        int tyreWeight = collections.get(0)[0] * 40;
        int tubeWeight = collections.get(1)[0] * 15;
        int lionWeight = collections.get(2)[0] * 20;

        double tyreCost = collections.get(0)[0] * 1.5;
        double tubeCost = collections.get(1)[0] * 1;
        double lionCost = collections.get(2)[0] * 30;
        double totalRev = tyreCost +tubeCost + lionCost;
        double totalExpense = 100.00;
        double netChange = totalRev - totalExpense;

        Card tyresSvd = new Card();
        tyresSvd.setTitle("Tyres");
        H2 tyreAmt = new H2( String.valueOf(tyreWeight) + " Kg");
        tyresSvd.add(tyreAmt);

        
        Card tubesSvd = new Card();
        tubesSvd.setTitle("Tubes");
        H2 tubesAmt = new H2(String.valueOf(tubeWeight) + " Kg");
        tubesSvd.add(tubesAmt);

        Card lionSvd = new Card();
        lionSvd.setTitle("Batteries");
        H2 lionAmt = new H2(String.valueOf(lionWeight) + " Kg");
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
        H2 increaseAmt = new H2("£" + String.format("%.2f", totalRev));
        increaseAmt.getStyle().set("color", "green");
        increase.add(increaseAmt);

        Card deacrease = new Card();
        deacrease.setTitle("Expenses");
        H2 deacreaseAmt = new H2("£" + String.format("%.2f", totalExpense));
        deacreaseAmt.getStyle().set("color", "red");
        deacrease.add(deacreaseAmt);


        Card total = new Card();
        total.setTitle("Net Change");
        if( netChange > 0){
            H2 totalAmt = new H2( "+ £" + String.format("%.2f", netChange));
            totalAmt.getStyle().set("color", "green");
            total.add(totalAmt);
        }
        else if( netChange < 0 ){
            H2 totalAmt = new H2( "- £" + String.format("%.2f", netChange));
            totalAmt.getStyle().set("color", "red");
            total.add(totalAmt);
        }
        else{
            H2 totalAmt = new H2( "£" + String.format("%.2f", netChange));
            totalAmt.getStyle().set("color", "grey");
            total.add(totalAmt);
        }
        

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

        List<String[]> nextColls = dbCommands.upcomingCollections();
       
        futureDels.setItems(nextColls);

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

        List<String[]> prods = dbCommands.collectedProductInfo();
        products.setItems(prods);

        //futureDels.setAllRowsVisible(true);
        products.setPageSize(3);
        products.setSizeFull();
        return products;
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
