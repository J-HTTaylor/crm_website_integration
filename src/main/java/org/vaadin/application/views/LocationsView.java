package org.vaadin.application.views;

import org.vaadin.application.layout.DashboardLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Locations")
@Route(value = "hub/Locations", layout = DashboardLayout.class)
public class LocationsView extends VerticalLayout {
    public LocationsView() {
        setPadding(true);
        setSpacing(true);
        
        addClassName("locations-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H2 headline = new H2("Locations");
        Paragraph paragraph = new Paragraph("This is the locations view.");

        add(headline, paragraph);
    }
    
}
