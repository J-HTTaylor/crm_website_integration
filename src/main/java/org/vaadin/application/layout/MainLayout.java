package org.vaadin.application.layout;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.application.views.AboutView;
import org.vaadin.application.views.CentreView;
import org.vaadin.application.views.ContactView;
import org.vaadin.application.views.DashboardView;
import org.vaadin.application.views.HomeView;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public class MainLayout extends AppLayout implements BeforeEnterObserver{

    private final Map<Button, Class<? extends Component>> navButtons = new HashMap<>();

    public MainLayout() {
        createHeader();
        this.addClassName("main_layout");
    }

    private void createHeader() {
        Image logo = new Image("images/velorimlogo.webp", "Velorim Logo");

        // Links to other main pages (passing 2 arguments each)
        HorizontalLayout topNav = new HorizontalLayout(
            createNavButton("Home", HomeView.class), 
            createNavButton("Find your centre", CentreView.class),
            createNavButton("About us", AboutView.class),
            createNavButton("Contact us", ContactView.class),
            createNavButton("Dashboard", DashboardView.class)
        );
        
        // Layout positioning and spacing settings
        topNav.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        topNav.setSpacing(false); // Handled by CSS gap
        topNav.setAlignItems(FlexComponent.Alignment.CENTER);

        // Attach class for the dark pill capsule wrapper
        topNav.addClassName("segmented-pill-nav");

        // Place logo and menu side-by-side in header
        HorizontalLayout header = new HorizontalLayout(logo, topNav);
        header.setPadding(true);
        header.setSpacing(true);
        header.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, logo, topNav);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        
        // Gap between logo and topNav
        header.getStyle().set("gap", "5rem");
        header.getStyle().set("padding-left", "4.75rem");
            
        // Push menu items across top
        header.expand(topNav);

        // Add header bar to the navbar zone at the top
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

    public void beforeEnter(BeforeEnterEvent event) {
        String rawPath = event.getLocation().getPath();
        
        // Use an effectively final variable for the path to satisfy Java's lambda rules
        final String currentPath = rawPath.isEmpty() ? "home" : rawPath;

        // Dynamically toggle the "active" class using a clean switch statement
        navButtons.forEach((btn, viewClass) -> {
            boolean isActive = false;

            switch (currentPath) {
                case "home": isActive = viewClass.equals(HomeView.class);
                    break;
                case "centre": isActive = viewClass.equals(CentreView.class);
                    break;
                case "about": isActive = viewClass.equals(AboutView.class);
                    break;
                case "contact": isActive = viewClass.equals(ContactView.class);
                    break;
                case "dashboard": isActive = viewClass.equals(DashboardView.class);
                    break;
                default:
                    isActive = false;
                    break;
            }

            if (isActive) {
                btn.addClassName("active");
            } else {
                btn.removeClassName("active");
            }
        });
    }
}