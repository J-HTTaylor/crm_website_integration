package org.vaadin.application.views;

import org.vaadin.application.layout.MainLayout;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@PageTitle("Contact")
@Route(value = "contact", layout = MainLayout.class)
public class ContactView extends VerticalLayout {
    public ContactView() {
        addClassName("contact-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H2 headline = new H2("Contact Us");
        Paragraph paragraph = new Paragraph("This is the contact view.");

        add(headline, paragraph);
    }
}
