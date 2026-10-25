package org.vaadin.application.views;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.application.layout.MainLayout;

import com.vaadin.flow.component.combobox.ComboBox;
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

        // get country names from the DB
        List<String> countryNames = fetchCountriesFromDatabase();

        //drop down combo box
        ComboBox<String> sqlTest = new ComboBox<>("Select a Country");
        sqlTest.setPlaceholder("United Kingdom");
        sqlTest.setItems(countryNames);
        

        add(sqlTest);
    }

    public List<String> fetchCountriesFromDatabase() {
        List<String> countryNames = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/velorim_crm";
        String user = "vaadin";
        String password = "Vaadin_Full_Stack!";

        try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT full_name FROM countries")) {

            while (resultSet.next()) {
                countryNames.add(resultSet.getString("full_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return countryNames;
    }
}
