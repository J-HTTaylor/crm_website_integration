package org.vaadin.application.views;

import org.vaadin.application.layout.MainLayout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Home")
@Route(value = "", layout = MainLayout.class)
public class HomeView extends VerticalLayout {
    public HomeView() {

         //Find your centre section
        Div findCentre = new Div();
        findCentre.addClassName("about-card");

        H3 journey = new H3("Your Recycle Journey Starts Here");
        journey.addClassName("about-card");

        H2 find = new H2("Find a Recycling Centre Near you");
        find.addClassName("about-card");

        Paragraph info = new Paragraph("Velorim manages the collection of inner Tyres, Tubes and Li-ion batteries, with over 600 collection sites across the UK. Help make cycling green and benefit the environment at the same time.");
        info.addClassName("about-card");

        Select<String> options = new Select<>();
        options.setLabel("What would you like to recycle?");
        options.setItems("All Materials", "Li-ion Batteries ","Tubes", "Tyres");
        options.setValue("All Materials");
        options.getStyle().set("margin-bottom", "1rem");

        HorizontalLayout searchArea = new HorizontalLayout();

        Select<String> distance = new Select<>();
        distance.setItems("5 Miles", "15 Miles","30 Miles", "50 Miles","80 Miles");
        distance.setValue("5 Miles");

        TextField postcodeField = new TextField();
        postcodeField.setPlaceholder("Enter your postcode");
        postcodeField.addClassName("search-input");

        // Action Button
        Button findButton = new Button("find your center", e -> UI.getCurrent().navigate(CentreView.class));
        findButton.addClassName("pill-item");

        searchArea.add(distance, postcodeField, findButton);
        findCentre.add(journey, find, info, options, searchArea);
        
        Image recycle = new Image("images/recycling.jpg", "reyclcing logo");
        recycle.setWidth("50%");
        recycle.setHeight("40rem");


        HorizontalLayout findYourCentre =  new HorizontalLayout();
        findYourCentre.add(findCentre, recycle);

        add(findYourCentre);

        H2 moreInfo = new H2("At Velorim, we believe in the power of community and the collective strength that emerges when individuals come together with a shared purpose.");
        moreInfo.addClassName("center");

        add(moreInfo);

        // Action Button
        Button moreButton = new Button("Find out more", e -> UI.getCurrent().navigate(AboutView.class));
        moreButton.addClassName("pill-item");
        moreButton.getStyle().set("font-size", "3rem").set("padding", "15px 30px 15px 30px");

        //moves button to the middle of the page
        setHorizontalComponentAlignment(Alignment.CENTER, moreButton);
        add(moreButton);

        Card contactHome = new Card();
        contactHome.setWidthFull();
        contactHome.addClassName("about-card");
        contactHome.addThemeVariants(CardVariant.AURA_HORIZONTAL);

        H3 tagline = new H3("Become a Centre");
        tagline.getStyle().set("text-align", "left").set("font-size", "1.5rem");
        tagline.addClassName("home");

        //seperate word for visual empasis
        H2 emphasis = new H2("Get Involved");
        emphasis.getStyle().set("text-align", "left").set("font-size", "3em");

        Div contactPull = new Div("Become part of the recycling revolution, make sure your own tyres and tubes (and even Li-ion batteries) are recycled properly, you can work with your customers to recycle theirs and, if you register as a public centre, you will get more people coming through and charging a levy will help toward your costs.");
        contactPull.getStyle().set("text-align", "left").set("margin-bottom", "1rem");

        // Group text components into a left-side container
        VerticalLayout textContainer = new VerticalLayout(emphasis, tagline, contactPull);
        textContainer.setPadding(false);
        textContainer.setSpacing(false);

        Button contactButton = new Button("Become a centre", e -> UI.getCurrent().navigate(ContactView.class));
        contactButton.addClassName("pill-item");
        contactButton.getStyle().set("font-size", "3rem").set("padding", "15px 30px 15px 30px");

        VerticalLayout buttonContainer = new VerticalLayout(contactButton);
        buttonContainer.setPadding(false);
        buttonContainer.setAlignItems(Alignment.CENTER);
        buttonContainer.setJustifyContentMode(JustifyContentMode.CENTER);

        // Wrap text container and button side-by-side, sharing space 50/50
        HorizontalLayout splitLayout = new HorizontalLayout(textContainer, buttonContainer);
        splitLayout.setWidthFull();
        splitLayout.setAlignItems(Alignment.CENTER);
        splitLayout.setFlexGrow(1, textContainer);
        splitLayout.setFlexGrow(0, buttonContainer);

        contactHome.add(splitLayout);
        

        add(contactHome);

        
    }
    
}
