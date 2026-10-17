package org.vaadin.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {
    public DashboardView() {
        setPadding(true);
        setSpacing(true);
        
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H2 headline = new H2("Dashboard");
        Paragraph paragraph = new Paragraph("This is the dashboard view.");

        add(headline, paragraph);

        HorizontalLayout subMenuLayout = new HorizontalLayout();
        subMenuLayout.setSpacing(true);

        subMenuLayout.add(
        new RouterLink("locations", LocationsView.class),
        new RouterLink("Products", ProductsView.class),
        new RouterLink("deliveries & collections", CollectionsView.class),
        new RouterLink("Profile", ProfileView.class));

        add(subMenuLayout);
    }
}
