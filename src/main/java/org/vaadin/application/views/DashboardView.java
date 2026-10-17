package org.vaadin.application.views;

import org.vaadin.application.layout.DashboardLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Dashboard")
@Route(value = "dashboard", layout = DashboardLayout.class)
public class DashboardView extends VerticalLayout {
    public DashboardView() {
        setPadding(true);
        setSpacing(true);
        
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H2 headline = new H2("Overview");
        Paragraph paragraph = new Paragraph("This is the overview view.");

        add(headline, paragraph);
    }
}
