package org.vaadin.application.views;

import org.vaadin.application.layout.DashboardLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Products")
@Route(value = "hub/Products", layout = DashboardLayout.class)
public class ProductsView extends VerticalLayout {
    public ProductsView() {
        setPadding(true);
        setSpacing(true);
        
        addClassName("products-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H2 headline = new H2("Products");
        Paragraph paragraph = new Paragraph("This is the products view.");

        add(headline, paragraph);
    }
    
}