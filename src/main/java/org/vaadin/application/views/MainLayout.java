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
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Velorim CRM");
        logo.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.setPadding(true);
        addToNavbar(header);
    }

    private void createDrawer() {
        VerticalLayout menuLayout = new VerticalLayout();
        menuLayout.setPadding(true);
        menuLayout.setSpacing(true);

        // add links to the other pages
        menuLayout.add(new RouterLink("Home", HomeView.class));
        menuLayout.add(new RouterLink("Find your centre", CentreView.class));
        menuLayout.add(new RouterLink("About us", AboutView.class));
        menuLayout.add(new RouterLink("Contact us", ContactView.class));
        menuLayout.add(new RouterLink("Dashboard", DashboardView.class));

        addToDrawer(menuLayout);
    }
}
