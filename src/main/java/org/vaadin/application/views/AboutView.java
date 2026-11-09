package org.vaadin.application.views;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.application.layout.MainLayout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)

public class AboutView extends VerticalLayout {
    public AboutView() {

        //segment 1 details

        Card overview = new Card();
        overview.setWidthFull();
        overview.addClassName("about-card");
        overview.addThemeVariants(CardVariant.HORIZONTAL);

        H2 tagline = new H2("Velorim is making cycling");
        tagline.getStyle().set("text-align", "right").set("margin-right", "5rem");
        //seperate word for visual empasis
        H2 emphasis = new H2("green");
        emphasis.getStyle().set("color", "#00e699").set("font-size", "3.5rem").set("text-align", "right").set("margin-right", "5rem");

        Image recycling = new Image("images/bikeRide.jpg", "Recycling Image");

        //set image dimensions and make it circular
        recycling.setWidth("250px");
        recycling.setHeight("250px");
        recycling.getStyle().set("border-radius", "50%");
        recycling.getStyle().set("margin-left", "10rem");

        overview.setMedia(recycling);
        overview.add(tagline, emphasis);

        add(overview);

        //segment 2 details

        Card mission = new Card();
        mission.setWidthFull();
        mission.addClassName("about-card");

        Div missionHeader = new Div();
        H2 missionTitle = new H2("Our Mission");
        Div missionStatement = new Div("At Velorim, our mission is mitigate the final black mark against cycling – the disposal of bicycle tyres and inner tubes.");

        H2 valuesHeader = new H2("Our Values");
        Div valuesStatement = new Div("At Velorim, we are passionate about cycling and its potential for a healthier planet.");

        missionHeader.add(missionTitle, missionStatement, valuesHeader, valuesStatement);
        mission.setHeader(missionHeader);

        addClassName("about-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        Card productsStats = new Card();
        productsStats.setWidthFull();
        productsStats.addClassName("about-card");

        H2 productsTitle = new H2("We Recycle");
        productsTitle.getStyle().set("font-size", "1.5rem");

        Div productsStatement = new Div("Bicycle tyres\ninner tubes\nLi-ion batteries");
        productsStatement.getStyle().set("text-align", "left").set("white-space", "pre-line").set("margin-left", "1.5rem").set("line-height", "2rem");


        VerticalLayout productsCol = new VerticalLayout(productsTitle, productsStatement);
        productsCol.setPadding(false);
        productsCol.setSpacing(true);

        H2 centersTitle = new H2("600");
        centersTitle.getStyle().set("margin-right", "4rem");
        //TODO update this with real world vals
        Div centersStatement = new Div("Centres across the UK");
        centersStatement.getStyle().set("text-align", "center");

        VerticalLayout centersCol = new VerticalLayout(centersTitle, centersStatement);
        centersCol.setPadding(false);
        centersCol.setSpacing(true);
        centersCol.setHorizontalComponentAlignment(FlexComponent.Alignment.END, centersTitle, centersStatement);

        HorizontalLayout productsHeader = new HorizontalLayout(productsCol, centersCol);
        productsHeader.setWidthFull();
        productsHeader.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        productsStats.setHeader(productsHeader);

        mission.getStyle().set("flex", "2 1 0%");
        productsStats.getStyle().set("flex", "1 1 0%"); 

        HorizontalLayout sideBySideStats = new HorizontalLayout(mission, productsStats);
        sideBySideStats.setWidthFull();
        sideBySideStats.setSpacing(true);

        //View styling
        addClassName("about-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);
        
        add(sideBySideStats);

        //segment 3 details
        
        Div whatWeDo = new Div();
        whatWeDo.setWidthFull();
        whatWeDo.addClassName("about-card");
        whatWeDo.getStyle().set("background-color", "var(--aura-background-color-dark)");

        Div header = new Div();
        H2 title = new H2("We share your passion for cycling and its environmental benefits.");
        title.getStyle().set("text-align", "center");

        Div subtitle = new Div("What We Do");
        subtitle.getStyle().set("font-size", "1.5rem").set("text-transform", "uppercase").set("text-align", "center");;

        header.add(subtitle, title);

        Div description = new Div("At Velorim, we make it easy to recycle your old bicycle tyres, tubes, and Li-ion batteries.");
        description.getStyle().set("text-align", "center");

        whatWeDo.add(header, description);

        addClassName("about-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(whatWeDo);

        //Side by side cards
        Card howItWorks = new Card();
        howItWorks.setWidthFull();
        howItWorks.addClassName("about-card");

        howItWorks.setHeader(new Div(new H2("How It Works"))); 

        //card for dynamic prices
        //TODO make prices change whe nupdated
        Card tyrePrice = new Card();
        tyrePrice.setWidthFull();
        tyrePrice.addClassName("about-card");

        H2 tyrePriceVal = new H2("£1.50");
        tyrePrice.getStyle().set("text-align", "center");

        Div tyre = new Div("Tyre");
        tyre.getStyle().set("text-align", "center");

        tyrePrice.setHeader(tyrePriceVal);
        tyrePrice.add(tyre);

        Card tubePrice = new Card();
        tubePrice.setWidthFull();
        tubePrice.addClassName("about-card");

        H2 tubePriceVal = new H2("50p");
        tubePrice.getStyle().set("text-align", "center");

        Div tube = new Div("Inner Tube");
        tube.getStyle().set("text-align", "center");

        tubePrice.setHeader(tubePriceVal);
        tubePrice.add(tube);
        
        Card lionPrice = new Card();
        lionPrice.setWidthFull();
        lionPrice.addClassName("about-card");

        H2 lionPriceVal = new H2("£30.00");
        lionPrice.getStyle().set("text-align", "center");

        Div lion = new Div("Li-ion Battery");
        lion.getStyle().set("text-align", "center");

        lionPrice.setHeader(lionPriceVal);
        lionPrice.add(lion);
        HorizontalLayout currentPrices = new HorizontalLayout(tyrePrice, tubePrice, lionPrice);
        currentPrices.setWidthFull();
        currentPrices.getStyle().set("margin-top", "3rem").set("margin-bottom", "3rem");
        //list of what to do when giving the items
        OrderedList steps = new OrderedList(
            new ListItem(
                new Span("Find A Centre"),
                new Div("Find your closest Velorim Centre: Our network of Velorim Centers makes it easy for you to locate the nearest one using our Find a Recycling Centre feature.")
            ),
            new ListItem(
                new Span("Drop Off"), 
                new Div("Visit your nearest Velorim Centre, with all your tyres, tubes, and Li-ion batteries you want to recycle. Check your garage, shed, or storage to ensure you don’t miss any.")
            ),
            new ListItem(
                new Span("Support The Process"),
                new Div("Help support the recycling efforts by paying the following levies:"),currentPrices
                
            ),
            new ListItem(
                new Span("Leave with a Warm Feeling"),
                new Div("While you’re at the Velorim Centre, take a moment to explore the shop. You will find the additional tyres, tubes, and accessories you need for your bike. After completing the recycling process and supporting sustainable practices, enjoy that feeling of a job well done.")
            )
        );


        steps.addClassName("lists");

        
        Div whiteLine = new Div();
        whiteLine.addClassName("line");

        Div note = new Div("Note: Please do not send tyres, tubes, or Li-ion batteries to the administration offices. We cannot accept them at those locations. Kindly take them to a Velorim Centre as explained above.");
        note.addClassName("about-card");


        howItWorks.add(steps, whiteLine, note);

        //Find your centre section
        Div findCentre = new Div();
        findCentre.addClassName("about-card");

        H3 journey = new H3("Your Recycle Journey Starts Here");
        journey.addClassName("about-card");

        H2 find = new H2("Find a Recycling Centre Near you");
        find.addClassName("about-card");

        Paragraph info = new Paragraph("Looking to recycle your bicycle tyres, inner tubes, and even used bike batteries? Find a centre close to you.");
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
        // Container to hold the two additional cards side-by-side and set their proportions
        findCentre.getStyle().set("flex", "3 1 0%");
        findCentre.setHeight("75%");
        howItWorks.getStyle().set("flex", "2 1 0%"); 
        
        HorizontalLayout sideBySide = new HorizontalLayout(findCentre, howItWorks);
        sideBySide.setWidthFull();
        sideBySide.setSpacing(true);

        //View styling
        addClassName("about-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setSpacing(true);
        setPadding(true);

        add(sideBySide);

        //history segment
        
        Image waste = new Image("images/waste tyres.jpg", "waste tyres Image");

        //set image dimensions and make it circular
        waste.setWidth("95%");
        waste.setHeight("150px");
        waste.getStyle().set("margin-left", "3rem").set("margin-right", "3rem").set("object-fit", "cover").set("border-radius", "20%");

        Div history = new Div();
        history.setWidthFull();
        history.addClassName("about-card");
        history.getStyle().set("background-color", "var(--aura-background-color-dark)");

        Div issue = new Div();
        H2 issueStatment = new H2("The Waste Issue");
        issueStatment.getStyle().set("text-align", "left");

        Div issueDesc = new Div("Cycling is celebrated for its health and environmental benefits. However, a dark secret lurks beneath the surface: in the UK alone, over 9,300 tonnes of used bicycle tires and inner tubes end up in landfills every year. This poses a significant environmental issue. The good news is, the industry is not standing idle. Retailers, service providers, and organizations are joining forces to tackle this problem head-on. This is where Velorim steps in.");

        issue.add(issueStatment, issueDesc);
        
        Div whiteLine2 = new Div();
        whiteLine2.addClassName("line");


        H2 ourHistory = new H2("Corporate History");
        ourHistory.getStyle().set("text-align", "left");

        Paragraph corpHist = new Paragraph("The founding Directors were previously Trustees of a Bicycle Recycling Charity, providing training and work experience for long-term unemployed to get them back into the job market. The work involved renovating/refurbishing donated second-hand bikes, so they could be sold at an affordable price to those in the community who needed them. However, about ½ of the bikes donated were too deteriorated to be economically refurbished, so were stripped for any usable parts. \n\n\nAt the end of this process, the charity was left with piles of steel, aluminum, plastic, and rubber. The local scrap dealers were more than happy to take the first three piles, but no one wanted the rubber (some even quoted £9.00 per tyre). At the time the Trustees were working closely with other cycle shops across the UK redefining the Cycle Mechanic Apprenticeship and in discussing what to do with waste rubber, everyone involved said they had the same issue.  Velorim launched the UK National Bicycle Waste Recycling Scheme in September 2020 and it now services over 650 locations across the UK. Velorim have secured their own site to enable it to bring reprocessing, with machinery it has specifically developed for that purpose, and expects to have its new site in South Staffordshire operational before the end of the second quarter 2025.");
        
        corpHist.getStyle().set("white-space", "pre-line");

        Div whiteLine3 = new Div();
        whiteLine3.addClassName("line");

        H2 teamMembers = new H2("Our Team");
        teamMembers.getStyle().set("text-align", "left");

        HorizontalLayout membeGrid = new HorizontalLayout();

        H2 russ = new H2("Russ Taylor");
        H2 rich = new H2("Richard Lawrence");
        H3 role1 = new H3("Co-Founder");
        H3 role2 = new H3("Co-Founder");

        russ.getStyle().set("font-size", "2rem");
        rich.getStyle().set("font-size", "2rem");

        role1.getStyle().set("font-size", "1.5rem").set("color", "#9CA3AF");
        role2.getStyle().set("font-size", "1.5rem").set("color", "#9CA3AF");
        Image russHead = new Image("images/Russ-Head.jpg", "Russ headshot");

        //set image dimensions and make it circular
        russHead.setWidth("250px");
        russHead.setHeight("250px");
        russHead.getStyle().set("border-radius", "50%").set("margin-left", "10rem");

        Image richHead = new Image("images/Rich-Head.jpg", "Rich headshot");

        //set image dimensions and make it circular
        richHead.setWidth("250px");
        richHead.setHeight("250px");
        richHead.getStyle().set("border-radius", "50%").set("margin-left", "10rem");

        Card russCard = new Card();
        russCard.setWidthFull();
        russCard.addClassName("about-card");

        Card richCard = new Card();
        richCard.setWidthFull();
        richCard.addClassName("about-card");

        russCard.add(russHead,russ,role1);
        richCard.add(richHead,rich,role2);
        membeGrid.add(russCard, richCard);

        history.add(waste, issue,whiteLine2, ourHistory, corpHist, whiteLine3, teamMembers, membeGrid);

        add(history);

        //contact segment
        

        Card contactUs = new Card();
        contactUs.setWidthFull();
        contactUs.addClassName("about-card");
        contactUs.addThemeVariants(CardVariant.AURA_HORIZONTAL);
        contactUs.addThemeVariants(CardVariant.AURA_COVER_MEDIA);

        H3 involve = new H3("Become a Centre");
        H2 contact = new H2("Get Involved");

        Paragraph context = new Paragraph("Become part of the recycling revolution, make sure your own tyres and tubes (and even Li-ion batteries) are recycled properly, you can work with your customers to recycle theirs and, if you register as a public centre, you will get more people coming through and charging a levy will help toward your costs.");

        Button contactButton = new Button("Contact Us", e -> UI.getCurrent().navigate(ContactView.class));
        contactButton.addClassName("pill-item");

        involve.getStyle().set("font-size", "1.5rem").set("color", "#9CA3AF").set("text-align", "right");
        contact.getStyle().set("text-align", "right");
        context.getStyle().set("text-align", "right").set("margin-right", "1rem");
        context.setWidth("50%");

        contactButton.getStyle().set("margin-right", "1rem").set("font-size", "1.2rem").set("padding", "2rem");

        Image betterView = new Image("images/better-Horizon.jpg", "looking forward");

        contactUs.setMedia(betterView);

        VerticalLayout contactInfo = new VerticalLayout(involve, contact, context, contactButton);
        contactInfo.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.END);
        contactUs.add(contactInfo);

        add(contactUs);
    }
}
