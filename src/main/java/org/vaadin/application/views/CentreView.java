package org.vaadin.application.views;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

import org.vaadin.application.databaseCommands;
import org.vaadin.application.layout.MainLayout;

import com.vaadin.flow.component.badge.Badge;
import com.vaadin.flow.component.badge.BadgeVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
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


@PageTitle("Centre")
@Route(value = "centre", layout = MainLayout.class)
    
public class CentreView extends VerticalLayout {
    
    private final databaseCommands dbCommands;
    Card nearestLocation = new Card();
    List<Integer> locationDet;
    int currentPos = 0;
    boolean findPressed = false;

    public CentreView(databaseCommands dbcommands) {

        this.dbCommands = dbcommands;
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
        distance.setItems("5 Miles", "15 Miles","All");
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
        nearestLocation.setWidth("60%");
        nearestLocation.setHeightFull();;

        // Action Button

        Button findButton = new Button("find your center", e ->{
            if(postcodeField.getValue().length() > 8 || postcodeField.getValue().length() < 6  ){
                Notification emptyInput = Notification.show("Invalid postcode format used");
            }
            else{
                findPressed = true;
                locationDet = dbCommands.totalActiveLocation(postcodeSelector(distance.getValue(), postcodeField.getValue()));
                locationDet = refine(options.getValue(), locationDet);
                if(locationDet.size() == 0){
                    Notification emptyInput = Notification.show("No centers found within given restrictions");
                    findPressed = false;
                    return;
                }
                currentPos = 0; 
                Card hold = updateCard(dbcommands.centreInfo(locationDet.get(currentPos))); 
                result.replace(nearestLocation, hold); 
                nearestLocation = hold;
                System.out.print(locationDet.toString());
            }
        });

        findButton.addClassName("pill-item");

        VerticalLayout searchLayout = new VerticalLayout(find, options, distance, postcodeField, findButton);
        searchLayout.setPadding(false);
        searchLayout.setMargin(false);
        searchLayout.setAlignItems(Alignment.CENTER);

        search.add(searchLayout);

        // next and previous location

        Icon forwardArrow = VaadinIcon.ARROW_FORWARD.create();
        forwardArrow.setSize("3rem");

        Button nextButton = new Button(forwardArrow, e -> {
            if(findPressed){
                increaseTick(); 
                Card hold = updateCard(dbcommands.centreInfo(locationDet.get(currentPos))); 
                result.replace(nearestLocation, hold); 
                nearestLocation = hold;
            }
        });
        nextButton.addClassName("pill-item");

        Icon backArrow = VaadinIcon.ARROW_BACKWARD.create();
        backArrow.setSize("3rem");

        Button previousButton = new Button(backArrow, e ->{
            if(findPressed){
                decreaseTick(); 
                Card hold = updateCard(dbcommands.centreInfo(locationDet.get(currentPos))); 
                result.replace(nearestLocation, hold); 
                nearestLocation = hold;
            }
        });
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

    //TODO in cp 5 add checks and real world pulls
    public Card updateCard(List<String> cardinfo){
        Card updatedCard = new Card();
        updatedCard.setWidth("60%");
        updatedCard.setHeightFull();
        
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

        H2 centreTitle = new H2(cardinfo.get(0));
        VerticalLayout titleAndSubtitle = new VerticalLayout(centreTitle);
        titleAndSubtitle.setSpacing(false);
        titleAndSubtitle.setPadding(false);
        titleAndSubtitle.addClassName("centreView");

        //seting card header
        HorizontalLayout acceptedItems = new HorizontalLayout();
        acceptedItems.setSpacing(false);
        acceptedItems.setPadding(false);
        acceptedItems.setAlignItems(Alignment.END);

        //seting base info
        Paragraph adr1 = new Paragraph(cardinfo.get(1));
        Paragraph adr2 = new Paragraph(cardinfo.get(2));
        Paragraph city = new Paragraph(cardinfo.get(3));
        Paragraph postcode = new Paragraph(cardinfo.get(4));

        for (Paragraph p : new Paragraph[]{adr1, adr2, city, postcode}) {
            p.getStyle().set("margin", "0");
        }

        //badge checks
        if(cardinfo.get(6).contains("Tyres")){
            Badge tyreBadge = new Badge("Tyres");
            tyreBadge.addThemeVariants(BadgeVariant.SUCCESS);
            tyreBadge.getStyle().set("margin", "0.1rem");
            acceptedItems.add(tyreBadge);
        }

        if(cardinfo.get(6).contains("Tubes")){
            Badge tubeBadge = new Badge("Tubes");
            tubeBadge.addThemeVariants(BadgeVariant.SUCCESS);
            tubeBadge.getStyle().set("margin", "0.1rem");
            acceptedItems.add(tubeBadge);
        }

        if(cardinfo.get(6).contains("Li-ion Battery")){
            Badge lionBadge = new Badge("Batteries");
            lionBadge.addThemeVariants(BadgeVariant.SUCCESS);
            acceptedItems.add(lionBadge);
            lionBadge.getStyle().set("margin", "0.1rem");
            acceptedItems.add(lionBadge);
        }

        VerticalLayout nameAndBadgesRow = new VerticalLayout(titleAndSubtitle, acceptedItems);
        nameAndBadgesRow.setWidthFull();
        nameAndBadgesRow.getStyle().set("padding", "10px");

        Div locationSpec = new Div(adr1, adr2, city, postcode);
        
        locationSpec.addClassName("centreView");
        locationSpec.getStyle().set("margin-top", "0.1rem");
        locationSpec.getStyle().set("text-align", "left");

        //contact info
        H3 websiteCard = new H3("Website: ");
        Anchor webAddr = new Anchor("https://www.boisestate.edu", cardinfo.get(7));
        Span website = new Span(websiteCard, webAddr);

        H3 phoneCard = new H3("Phone: ");
        Anchor phoneNum = new Anchor(cardinfo.get(9), cardinfo.get(9));
        Span phone = new Span(phoneCard, phoneNum);

        H3 emailCard = new H3("Email: ");
        Anchor emailAddr = new Anchor("mailto:" + cardinfo.get(8), cardinfo.get(8));
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
        bottomContent.getStyle().set("flex-grow", "0");
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
            currentPos = locationDet.size() - 1;
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
    
    public String postcodeSelector(String option, String postcode) {

        String clean = postcode.trim().toUpperCase().replaceAll("\\s+", "");

        switch (option) {
            case "5 Miles":
                return clean.length() > 3 ? clean.substring(0, clean.length() - 3) + " " : clean;

            case "15 Miles":
                int i = 0;
                while (i < clean.length() && Character.isLetter(clean.charAt(i))) {
                    i++;
                }
                return clean.substring(0, i);

            case "All":
            default:
                return "";
        }
    }
   
    public List<Integer> refine(String searchPeram, List<Integer> checkAgainst){
        if(searchPeram.equals("All Materials")){
            return checkAgainst;
        }
        List<Integer> retList = checkAgainst;
        Iterator<Integer> listIterator = retList.iterator();
        while (listIterator.hasNext()) {
            Integer item = listIterator.next();
            if(!dbCommands.centreInfo(item).get(6).contains(searchPeram)) {
                listIterator.remove();
            }
        }
        return retList;
    }
}
