package org.vaadin.application.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends VerticalLayout {
    public AboutView() {
        addClassName("about-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H2 headline = new H2("About the CRM Website Integration Project!");
        Paragraph paragraph = new Paragraph(
                "This project demonstrates the integration of a CRM system with a website using the Vaadin framework. This section describes the company and some stats about it.");

        add(headline, paragraph);
    }
}
