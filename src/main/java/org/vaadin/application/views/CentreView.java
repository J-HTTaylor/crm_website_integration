package org.vaadin.application.views;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.application.layout.MainLayout;

import com.vaadin.flow.component.badge.Badge;
import com.vaadin.flow.component.badge.BadgeVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin.Minus.Vertical;


@PageTitle("Centre")
@Route(value = "centre", layout = MainLayout.class)
    
public class CentreView extends VerticalLayout {
        
    Card nearestLocation = new Card();
    List<Integer> locationDet = new ArrayList<>(List.of(0));
    int currentPos = 0;
    boolean findPressed = false;

    public CentreView() {

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        HorizontalLayout centreMain = new HorizontalLayout();
        
        centreMain.setWidthFull();
        centreMain.setHeight("80%");
        centreMain.setAlignItems(Alignment.CENTER);
        centreMain.setSpacing(true);
        centreMain.addClassName("centreView");
        //centreMain.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        centreMain.setPadding(true);
        
        // search area
        Card search = new Card();
        search.setWidth("35%");
        search.setHeightFull();

        
        H2 find = new H2("Find a Recycling Centre Near you");
        find.addClassName("about-card");

        Select<String> options = new Select<>();
        options.setLabel("What would you like to recycle?");
        options.setItems("All Materials", "Li-ion Batteries ","Tubes", "Tyres");
        options.setValue("All Materials");
        options.getStyle().set("margin-bottom", "0.2rem");


        Select<String> distance = new Select<>();
        distance.setItems("5 Miles", "15 Miles","30 Miles", "50 Miles","80 Miles");
        distance.setValue("5 Miles");
        distance.getStyle().set("margin-bottom", "0.2rem");

        TextField postcodeField = new TextField();
        postcodeField.setPlaceholder("Enter your postcode");
        postcodeField.addClassName("search-input");
        postcodeField.getStyle().set("margin-bottom", "0.2rem");

        //result screen
        HorizontalLayout result = new HorizontalLayout();
        result.setWidthFull();
        result.setHeightFull();
        result.setAlignItems(Alignment.CENTER);
        result.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        result.setSpacing(true);
        result.setFlexGrow(0, nearestLocation);
        
        nearestLocation.setHeightFull();;
        nearestLocation.getStyle().set("display", "flex");
        nearestLocation.getStyle().set("flex-direction", "column");
        nearestLocation.getStyle().set("flex-shrink", "0");
        nearestLocation.getStyle().set("flex-grow", "0");
        nearestLocation.getStyle().set("box-sizing", "border-box");
        nearestLocation.getStyle().set("overflow", "hidden");

        // Action Button
        Button findButton = new Button("find your center", e ->{findPressed = true; locationDet = getCenters(); currentPos = 0;  Card hold = updateCard(currentPos); result.replace(nearestLocation, hold); nearestLocation = hold;});
        findButton.addClassName("pill-item");

        VerticalLayout searchLayout = new VerticalLayout(find, options, distance, postcodeField, findButton);
        searchLayout.setPadding(false);
        searchLayout.setMargin(false);
        searchLayout.setAlignItems(Alignment.CENTER);

        search.add(searchLayout);

        // next and previous location

        Icon forwardArrow = VaadinIcon.ARROW_FORWARD.create();
        forwardArrow.setSize("3rem");

        Button nextButton = new Button(forwardArrow, e -> {if(findPressed){increaseTick(); Card hold = updateCard(currentPos); result.replace(nearestLocation, hold); nearestLocation = hold;}});
        nextButton.addClassName("pill-item");

        Icon backArrow = VaadinIcon.ARROW_BACKWARD.create();
        backArrow.setSize("3rem");

        Button previousButton = new Button(backArrow, e ->{if(findPressed){decreaseTick(); Card hold = updateCard(currentPos); result.replace(nearestLocation, hold); nearestLocation = hold;}});
        previousButton.addClassName("pill-item");

        result.setVerticalComponentAlignment(Alignment.CENTER, previousButton);
        result.setVerticalComponentAlignment(Alignment.CENTER, nextButton);
        result.setSpacing("2rem");

        //sets base image
        Image defaultImage = new Image("images/velorimlogo.webp", "Velorim Logo");
        defaultImage.getStyle().set("max-width", "100%");
        defaultImage.getStyle().set("max-height", "100%");
        defaultImage.getStyle().set("object-fit", "contain");

        VerticalLayout imageContainer = new VerticalLayout(defaultImage);
        imageContainer.setHeight("40%");
        imageContainer.setWidthFull();
        imageContainer.setAlignItems(Alignment.CENTER);
        imageContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        imageContainer.setPadding(false);
        imageContainer.setSpacing(false);
        imageContainer.getStyle().set("border-bottom", "1px solid rgba(255, 255, 255, 0.15)");

        H2 centreTitle = new H2("Velorim");
        H3 locTitle = new H3("Head Office");
        locTitle.getStyle().set("color", "#FFFFFF");

        VerticalLayout titleAndSubtitle = new VerticalLayout(centreTitle, locTitle);
        titleAndSubtitle.setSpacing(false);
        titleAndSubtitle.setPadding(false);
        titleAndSubtitle.addClassName("centreView");

        //seting card header
        HorizontalLayout acceptedItems = new HorizontalLayout();
        acceptedItems.setSpacing(false);
        acceptedItems.setPadding(false);
        acceptedItems.setAlignItems(Alignment.END);

        Badge tyreBadge = new Badge("Tyres");
        tyreBadge.addThemeVariants(BadgeVariant.SUCCESS);

        Badge tubeBadge = new Badge("Tubes");
        tubeBadge.addThemeVariants(BadgeVariant.SUCCESS);

        Badge lionBadge = new Badge("Batteries");
        lionBadge.addThemeVariants(BadgeVariant.SUCCESS);
        acceptedItems.add(tyreBadge, tubeBadge, lionBadge);

        for (Badge b : new Badge[]{tyreBadge, tubeBadge, lionBadge}) {
            b.getStyle().set("margin", "0.1rem");
        }

        VerticalLayout nameAndBadgesRow = new VerticalLayout(titleAndSubtitle, acceptedItems);
        nameAndBadgesRow.setWidthFull();
        nameAndBadgesRow.getStyle().set("padding", "10px");

        //seting base info
        Paragraph adr1 = new Paragraph("1910 W University Dr");
        Paragraph adr2 = new Paragraph();
        Paragraph city = new Paragraph("Boise");
        Paragraph postcode = new Paragraph("83725");

        for (Paragraph p : new Paragraph[]{adr1, adr2, city, postcode}) {
            p.getStyle().set("margin", "0");
        }

        Div locationSpec = new Div(adr1, adr2, city, postcode);
        
        locationSpec.addClassName("centreView");
        locationSpec.getStyle().set("margin-top", "0.1rem");
        locationSpec.getStyle().set("text-align", "left");

        //contact info
        H3 websiteCard = new H3("Website: ");
        Anchor webAddr = new Anchor("https://www.boisestate.edu", "https://www.boisestate.edu");
        Span website = new Span(websiteCard, webAddr);

        H3 phoneCard = new H3("Phone: ");
        Anchor phoneNum = new Anchor("tel:+12084264636", "(208) 426-4636");
        Span phone = new Span(phoneCard, phoneNum);

        H3 emailCard = new H3("Email: ");
        Anchor emailAddr = new Anchor("mailto:helpdesk@boisestate.edu", "helpdesk@boisestate.edu");
        Span email = new Span(emailCard, emailAddr);

        Div contactSpec = new Div(website, phone, email);

        contactSpec.addClassName("advancedView");

        String fuladdr = "1910 W University Dr, Boise, 83725";
        Button mapDirButton = new Button("View on maps", VaadinIcon.MAP_MARKER.create(), e ->{mapButton(fuladdr);});
        mapDirButton.addClassName("pill-item");
        mapDirButton.getStyle().set("margin-top", "1rem");

        VerticalLayout basiContact = new VerticalLayout(nameAndBadgesRow, locationSpec);
        basiContact.setSpacing(false);
        basiContact.setPadding(true);
        basiContact.getStyle().set("margin-right", "2rem");
        basiContact.setWidth("50%");
        basiContact.setHeightFull();

        VerticalLayout advancedContact = new VerticalLayout(contactSpec, mapDirButton);
        advancedContact.setSpacing(false);
        advancedContact.setPadding(false);
        advancedContact.setWidth("50%");
        advancedContact.getStyle().set("margin-top", "3rem");
        advancedContact.setHeightFull();
        advancedContact.getStyle().set("margin-right", "2rem");
        advancedContact.getStyle().set("overflow", "hidden");

        HorizontalLayout bottomContent = new HorizontalLayout(basiContact, advancedContact);
        bottomContent.setWidthFull();
        bottomContent.setHeightFull();
        bottomContent.setSpacing(false);
        bottomContent.setAlignItems(Alignment.CENTER);
        bottomContent.getStyle().set("box-sizing", "border-box");
        bottomContent.getStyle().set("flex-grow", "1");
        bottomContent.setFlexGrow(1, basiContact, advancedContact);

        nearestLocation.removeAll();
        nearestLocation.add(imageContainer, bottomContent);
        
        //add items to view
        result.add(previousButton, nearestLocation, nextButton);
        centreMain.add(search, result);
        add(centreMain);
    }

    public List<String> fetchCountriesFromDatabase() {
        List<String> countryNames = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/velorim_crm";
        String user = System.getenv("DB_VAADIN_USERNAME");
        String password = System.getenv("DB_VAADIN_PASSWORD");

        try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT full_name FROM countries")) {

            while (resultSet.next()) {
                countryNames.add(resultSet.getString("full_name"));
            }
        } catch (Exception e) {
            System.err.println("CRITICAL DATABASE ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        return countryNames;
    }

    //TODO update for real world pulling
    public List<Integer> getCenters() {
        List<Integer> retVal = new ArrayList<>();

        String url = "jdbc:mysql://localhost:3306/velorim_crm";
        String user = System.getenv("DB_VAADIN_USERNAME");
        String password = System.getenv("DB_VAADIN_PASSWORD");

        String sql = "SELECT id FROM centers LIMIT 5";

        try (Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(sql)) {
        
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    retVal.add(resultSet.getInt("id"));
                }
            }
        }
        
        catch (Exception e) {
            System.err.println("CRITICAL DATABASE ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        
        return retVal;
    }


    //gets location and stored products
    public List<String> centreInfo(int id){
        List<String> info = new  ArrayList<>();

        String url = "jdbc:mysql://localhost:3306/velorim_crm";
        String user = System.getenv("DB_VAADIN_USERNAME");
        String password = System.getenv("DB_VAADIN_PASSWORD");

        String sql = "SELECT * from centers where id = ? and status = 1";
        String productsSql =  "select p.name from products p inner join center_products c on p.id = c.product_id where c.center_id = ?";

        try (Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(sql)) {
        
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    info.add(resultSet.getString("name"));
                    info.add(resultSet.getString("address_line_1"));
                    info.add(resultSet.getString("address_line_2"));
                    info.add(resultSet.getString("city"));
                    info.add(resultSet.getString("postcode"));
                    info.add(resultSet.getString("image"));
                }
            }
        }
        
        
        catch (Exception e) {
            System.err.println("CRITICAL DATABASE ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(productsSql)) {
        
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    info.add(resultSet.getString("name"));
                }
            }
        }
        
        
        catch (Exception e) {
            System.err.println("CRITICAL DATABASE ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return info;
    }

    //TODO in cp 5 add checks and real world pulls
    public Card updateCard(int cardId){
        Card updatedCard = new Card();
        
        //sets base image
        Image defaultImage = new Image("images/velorimlogo.webp", "Velorim Logo");
        defaultImage.getStyle().set("max-width", "100%");
        defaultImage.getStyle().set("max-height", "100%");
        defaultImage.getStyle().set("object-fit", "contain");

        VerticalLayout imageContainer = new VerticalLayout(defaultImage);
        imageContainer.setHeight("40%");
        imageContainer.setWidthFull();
        imageContainer.setAlignItems(Alignment.CENTER);
        imageContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        imageContainer.setPadding(false);
        imageContainer.setSpacing(false);
        imageContainer.getStyle().set("border-bottom", "1px solid rgba(255, 255, 255, 0.15)");

        H2 centreTitle = new H2("Velorim" + cardId);
        H3 locTitle = new H3("Head Office");
        locTitle.getStyle().set("color", "#FFFFFF");

        VerticalLayout titleAndSubtitle = new VerticalLayout(centreTitle, locTitle);
        titleAndSubtitle.setSpacing(false);
        titleAndSubtitle.setPadding(false);
        titleAndSubtitle.addClassName("centreView");

        //seting card header
        HorizontalLayout acceptedItems = new HorizontalLayout();
        acceptedItems.setSpacing(false);
        acceptedItems.setPadding(false);
        acceptedItems.setAlignItems(Alignment.END);

        Badge tyreBadge = new Badge("Tyres");
        tyreBadge.addThemeVariants(BadgeVariant.SUCCESS);

        Badge tubeBadge = new Badge("Tubes");
        tubeBadge.addThemeVariants(BadgeVariant.SUCCESS);

        Badge lionBadge = new Badge("Batteries");
        lionBadge.addThemeVariants(BadgeVariant.SUCCESS);
        acceptedItems.add(tyreBadge, tubeBadge, lionBadge);

        for (Badge b : new Badge[]{tyreBadge, tubeBadge, lionBadge}) {
            b.getStyle().set("margin", "0.1rem");
        }

        VerticalLayout nameAndBadgesRow = new VerticalLayout(titleAndSubtitle, acceptedItems);
        nameAndBadgesRow.setWidthFull();
        nameAndBadgesRow.getStyle().set("padding", "10px");

        //seting base info
        Paragraph adr1 = new Paragraph("1910 W University Dr");
        Paragraph adr2 = new Paragraph();
        Paragraph city = new Paragraph("Boise");
        Paragraph postcode = new Paragraph("83725");

        for (Paragraph p : new Paragraph[]{adr1, adr2, city, postcode}) {
            p.getStyle().set("margin", "0");
        }

        Div locationSpec = new Div(adr1, adr2, city, postcode);
        
        locationSpec.addClassName("centreView");
        locationSpec.getStyle().set("margin-top", "0.1rem");
        locationSpec.getStyle().set("text-align", "left");

        //contact info
        H3 websiteCard = new H3("Website: ");
        Anchor webAddr = new Anchor("https://www.boisestate.edu", "https://www.boisestate.edu");
        Span website = new Span(websiteCard, webAddr);

        H3 phoneCard = new H3("Phone: ");
        Anchor phoneNum = new Anchor("tel:+12084264636", "(208) 426-4636");
        Span phone = new Span(phoneCard, phoneNum);

        H3 emailCard = new H3("Email: ");
        Anchor emailAddr = new Anchor("mailto:helpdesk@boisestate.edu", "helpdesk@boisestate.edu");
        Span email = new Span(emailCard, emailAddr);

        Div contactSpec = new Div(website, phone, email);

        contactSpec.addClassName("advancedView");

        String fuladdr = "1910 W University Dr, Boise, 83725";
        Button mapDirButton = new Button("View on maps", VaadinIcon.MAP_MARKER.create(), e ->{mapButton(fuladdr);});
        mapDirButton.addClassName("pill-item");
        mapDirButton.getStyle().set("margin-top", "1rem");

        VerticalLayout basiContact = new VerticalLayout(nameAndBadgesRow, locationSpec);
        basiContact.setSpacing(false);
        basiContact.setPadding(true);
        basiContact.getStyle().set("margin-right", "2rem");
        basiContact.setWidth("50%");
        basiContact.setHeightFull();

        VerticalLayout advancedContact = new VerticalLayout(contactSpec, mapDirButton);
        advancedContact.setSpacing(false);
        advancedContact.setPadding(false);
        advancedContact.setWidth("50%");
        advancedContact.getStyle().set("margin-top", "3rem");
        advancedContact.setHeightFull();
        advancedContact.getStyle().set("margin-right", "2rem");
        advancedContact.getStyle().set("overflow", "hidden");

        HorizontalLayout bottomContent = new HorizontalLayout(basiContact, advancedContact);
        bottomContent.setWidthFull();
        bottomContent.setHeightFull();
        bottomContent.setSpacing(false);
        bottomContent.setAlignItems(Alignment.CENTER);
        bottomContent.getStyle().set("box-sizing", "border-box");
        bottomContent.getStyle().set("flex-grow", "1");
        bottomContent.setFlexGrow(1, basiContact, advancedContact);

        updatedCard.add(imageContainer, bottomContent);
        return  updatedCard;
    }

    public void increaseTick(){
        if(currentPos == locationDet.size() - 1){
            currentPos = 0;
        }
        else{
            currentPos++;
        }
    }

    public void decreaseTick(){
        if(currentPos == 0){
            currentPos = locationDet.size() -1;
        }
        else{
            currentPos--;
        }
    }

    public void mapButton(String address){
        try {
            //URL encode the address
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            String mapsUrl = "https://google.com/maps/dir/" + encodedAddress;

            getUI().ifPresent(ui -> ui.getPage().open(mapsUrl, "_blank"));

        } 
        catch (Exception e) {
            Notification.show("Could not resolve address formatting.");
        }

    }
}
