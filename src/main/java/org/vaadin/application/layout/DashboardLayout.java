package org.vaadin.application.layout;

import org.vaadin.application.views.*;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class DashboardLayout extends AppLayout {

    public DashboardLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Velorim CRM");
        logo.getStyle()
            .set("font-size", "var(--lumo-font-size-l)")
            .set("margin", "0");

        HorizontalLayout topNav = new HorizontalLayout(
            new RouterLink("Home", HomeView.class),
            new RouterLink("Find your centre", CentreView.class),
            new RouterLink("About us", AboutView.class),
            new RouterLink("Contact us", ContactView.class),
            new RouterLink("Dashboard", DashboardView.class)
        );
        topNav.setSpacing(true);
        topNav.setAlignItems(FlexComponent.Alignment.CENTER);

        // Includes the DrawerToggle button next to the logo
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, topNav);
        header.setWidthFull();
        header.setPadding(true);
        header.setSpacing(true);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(topNav);

        addToNavbar(header);
    }

    private void createDrawer() {

        VerticalLayout drawerNav = new VerticalLayout(
            new Button("Overview", e -> UI.getCurrent().navigate(DashboardView.class)),
            new Button("Locations", e -> UI.getCurrent().navigate(LocationsView.class)),
            new Button("Products", e -> UI.getCurrent().navigate(ProductsView.class)),
            new Button("Deliveries & collections", e -> UI.getCurrent().navigate(CollectionsView.class)),
            new Button("Profile", e -> UI.getCurrent().navigate(ProfileView.class))
        );
        drawerNav.setPadding(true);
        drawerNav.setSpacing(true);

        addToDrawer(new VerticalLayout(drawerNav));
    }
}