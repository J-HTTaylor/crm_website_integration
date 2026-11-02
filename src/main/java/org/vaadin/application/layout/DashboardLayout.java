package org.vaadin.application.layout;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.application.views.*;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public class DashboardLayout extends AppLayout implements BeforeEnterObserver {

    private final Map<Button, Class<? extends Component>> navButtons = new HashMap<>();

    public DashboardLayout() {
        // Page alignment direction
        getElement().setAttribute("dir", "ltr");
        createHeader();
        createDrawer();
        this.addClassName("dashboard_layout");
    }

    private void createHeader() {
        Image logo = new Image("images/velorimlogo.webp", "Velorim Logo");

        Button homeBtn = createNavButton("Home", HomeView.class);
        homeBtn.getStyle().set("margin-left", "68px");

        HorizontalLayout topNav = new HorizontalLayout(
            homeBtn,
            createNavButton("Find your centre", CentreView.class),
            createNavButton("About us", AboutView.class),
            createNavButton("Contact us", ContactView.class),
            createNavButton("Dashboard", DashboardView.class)
        );

        // Set button location
        topNav.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        topNav.setSpacing("50px");
        topNav.setAlignItems(FlexComponent.Alignment.CENTER);
        
        // Add your pill wrapper class if you use one here too
        topNav.addClassName("segmented-pill-nav");

        // Includes the DrawerToggle button next to the logo
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, topNav);
        header.setPadding(true);
        header.setSpacing(true);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(topNav);

        addToNavbar(header);
    }

    private Button createNavButton(String text, Class<? extends Component> viewClass) {
        Button btn = new Button(text, e -> UI.getCurrent().navigate(viewClass));
        btn.addClassName("pill-item");

        // Checks active page target against viewClass to highlight correct button
        boolean isActive = UI.getCurrent().getActiveRouterTargetsChain().stream().anyMatch(target -> target.getClass().equals(viewClass));
        
        if (isActive) {
            btn.addClassName("active");
        }

        navButtons.put(btn, viewClass);
        
        return btn;
    }

    private void createDrawer() {
        SideNav nav = new SideNav();
        nav.setWidthFull();

        SideNavItem overview = new SideNavItem("Overview", DashboardView.class, VaadinIcon.HOME.create());
        SideNavItem locations = new SideNavItem("Locations", LocationsView.class, VaadinIcon.MAP_MARKER.create());
        SideNavItem products = new SideNavItem("Products", ProductsView.class, VaadinIcon.TAGS.create());
        SideNavItem collections = new SideNavItem("Deliveries & collections", CollectionsView.class, VaadinIcon.TRUCK.create());
        
        nav.addItem(overview, locations, products, collections);
        addToDrawer(nav);
    }

    public void beforeEnter(BeforeEnterEvent event) {
        Class<?> targetView = event.getNavigationTarget();

        navButtons.forEach((btn, viewClass) -> {
            boolean isActive = false;

            // 1. Normal behavior for public pages (Home, Centre, About, Contact)
            if (viewClass.equals(targetView)) {
                isActive = true;
            } 
            // 2. HARD SET: If the user is on ANY dashboard sub-page, always light up the Dashboard button
            else if (btn.getText().equals("Dashboard") && (
                    targetView.equals(DashboardView.class) ||
                    targetView.equals(LocationsView.class) ||
                    targetView.equals(ProductsView.class) ||
                    targetView.equals(CollectionsView.class))) {
                isActive = true;
            }

            if (isActive) {
                btn.addClassName("active");
            } else {
                btn.removeClassName("active");
            }
        });
    }
}