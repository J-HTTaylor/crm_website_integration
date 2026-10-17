package org.vaadin.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    // Record structure to cleanly hold menu item data
    private record MenuItem(String label, Class<? extends Component> viewClass) {}

    public MainLayout() {
        createHeader();
    }

    private void createHeader() {
        H1 logo = new H1("Velorim CRM");
        logo.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

        //page links at the top of page
        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.setPadding(false);
        menuLayout.setSpacing(true);
        menuLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        //links to other main pages
        menuLayout.add(
            new RouterLink("Home", HomeView.class),
            new RouterLink("Find your centre", CentreView.class),
            new RouterLink("About us", AboutView.class),
            new RouterLink("Contact us", ContactView.class),
            new RouterLink("Dashboard", DashboardView.class)
        );

        // Place logo and menu side-by-side in header
        HorizontalLayout header = new HorizontalLayout(logo, menuLayout);
        header.setWidthFull();
        header.setPadding(true);
        header.setSpacing(true);
        header.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, logo, menuLayout);
        
        // Push menu items across or align properly
        header.expand(menuLayout); 

        // Add header bar to the navbar zone at the top
        addToNavbar(header);
    }
}
