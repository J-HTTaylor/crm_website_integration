package org.vaadin.application.views;

import org.vaadin.application.layout.MainLayout;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Centre")
@Route(value = "centre", layout = MainLayout.class)

public class CentreView extends VerticalLayout {
    public CentreView() {
        addClassName("centre-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H2 headline = new H2("Centre");
        Paragraph paragraph = new Paragraph("This is where you will be able to find your local centres.");

        add(headline, paragraph);

        //drop down combo box
        ComboBox<String> sqlTest = new ComboBox<>("Select a Country");
        sqlTest.setPlaceholder("United Kingdom");

        add(sqlTest);
    }
}
