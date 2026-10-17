package org.vaadin.application.views;

import org.vaadin.application.layout.DashboardLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Deliveries & Collections")
@Route(value = "hub/DeliveriesAndCollections", layout = DashboardLayout.class)
public class CollectionsView extends VerticalLayout {
    public CollectionsView() {
        setPadding(true);
        setSpacing(true);
        
        addClassName("collections-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H2 headline = new H2("Deliveries & Collections");
        Paragraph paragraph = new Paragraph("This is the deliveries and collections view.");

        add(headline, paragraph);
    }
    
}