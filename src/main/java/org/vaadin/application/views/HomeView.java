package org.vaadin.application.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Home")
@Route(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout {
    public HomeView() {
        addClassName("home-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H2 headline = new H2("Welcome to the CRM Website Integration Project!");
        Paragraph paragraph = new Paragraph(
                "This project is a demonstration of a CRM website integration using Vaadin framework. "
                        + "You can navigate through different sections using the menu.");

        add(headline, paragraph);
    }
    
}
