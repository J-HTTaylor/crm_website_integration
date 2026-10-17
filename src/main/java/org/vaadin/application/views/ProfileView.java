package org.vaadin.application.views;

import org.vaadin.application.layout.DashboardLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Profile")
@Route(value = "hub/Profile", layout = DashboardLayout.class)
public class ProfileView extends VerticalLayout {
    public ProfileView() {
        setPadding(true);
        setSpacing(true);
        
        addClassName("profile-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        H2 headline = new H2("Profile");
        Paragraph paragraph = new Paragraph("This is the profile view.");

        add(headline, paragraph);
    }
    
}