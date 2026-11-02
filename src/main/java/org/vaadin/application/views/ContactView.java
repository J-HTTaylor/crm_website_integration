package org.vaadin.application.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.vaadin.application.layout.MainLayout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;



@PageTitle("Contact")
@Route(value = "contact", layout = MainLayout.class)

public class ContactView extends VerticalLayout {
        //declaration for email to work
    private final JavaMailSender mailSender;
    
    @Value("${EMAIL_SENDER}")
    private String teamEmail;

   @Autowired
    public ContactView(JavaMailSender mailSender, @Value("${EMAIL_SENDER}") String teamEmail) {
        this.mailSender = mailSender;
        this.teamEmail = teamEmail;
        HorizontalLayout contactForms = new HorizontalLayout();
        VerticalLayout contactSide = new VerticalLayout();

        contactForms.getStyle().set("margin", "auto");

        RadioButtonGroup<String> contactType = new RadioButtonGroup<>();
        contactType.addThemeVariants(RadioGroupVariant.AURA_HORIZONTAL);
        contactType.setItems("Make a Comment", "Nominate Your Local Bike shop", "Become a Centre");
        contactType.setValue("Make a Comment");
        contactType.getElement().getThemeList().add("horizontal");
        contactType.getStyle().set("--vaadin-radio-group-spacing", "var(--lumo-space-m)");

        TextField firstName = new TextField("First name");
        firstName.setRequiredIndicatorVisible(true);
        TextField lastName = new TextField("Last name");
        lastName.setRequiredIndicatorVisible(true);
        EmailField email = new EmailField("Email address");
        email.setRequiredIndicatorVisible(true);


        TextField phoneField = new TextField("Phone number");
        phoneField.setAllowedCharPattern("[0-9()+\\- ]");
        phoneField.setPattern("^[+]?[\\(]?[0-9]{3}[\\)]?[\\- ]?[0-9]{3}[\\- ]?[0-9]{4,6}$");
        phoneField.setMinLength(5);
        phoneField.setMaxLength(18);


        TextArea textArea = new TextArea();
        textArea.setWidthFull();
        textArea.setMinHeight("100px");
        textArea.setMaxHeight("150px");
        textArea.setLabel("Feedback");
        textArea.setPlaceholder("Enter message content here");
        textArea.setMaxLength(400);
        textArea.setValueChangeMode(ValueChangeMode.EAGER);
        textArea.addValueChangeListener(e -> {e.getSource().setHelperText(e.getValue().length() + "/" + 400);});



    //submites the message to the team and an automated resposne
        Button submitButton = new Button("Send a Message", e -> {

        String subject = contactType.getValue();
        String responseSubject = "Velorim Team Message recieved";
        String body = "Name: ";
        String responseBody = "Dear, \n";
        String from = firstName.getValue() + " " + lastName.getValue()  + "\n";
        String emailReplay = "Email: " + email.getValue() + "\n";

        //checks for extra info
        if(!(phoneField.getValue().equals(""))){
            emailReplay += "Phone: " + phoneField.getValue() + "\n";
        }

        String messageBody = "Message: " + textArea.getValue();
        String signOff = "Thank you for your message we will respond to you shortly. \n Sincerly, \nThe Velorim Team";

        body += from + emailReplay + messageBody;
        responseBody += from + signOff;

        try {
            
            SimpleMailMessage teamMessage = new SimpleMailMessage();
            teamMessage.setFrom(teamEmail);
            teamMessage.setTo(teamEmail);
            teamMessage.setSubject(subject);
            teamMessage.setText(body);
            teamMessage.setReplyTo(email.getValue());
            mailSender.send(teamMessage);

            SimpleMailMessage autoResponse = new SimpleMailMessage();
            autoResponse.setFrom(teamEmail);
            autoResponse.setTo(email.getValue());
            autoResponse.setSubject(responseSubject);
            autoResponse.setText(responseBody);
            mailSender.send(autoResponse);

            Notification.show("Message sent successfully!");
            
        } catch (Exception ex) {
            ex.printStackTrace();
            Notification.show("Failed to send message.");
        }
    });
        submitButton.addClassName("pill-item");
        submitButton.getStyle().set("font-size", "2rem").set("padding", "15px 30px 15px 30px");

        FormLayout formLayout = new FormLayout();
        formLayout.setAutoResponsive(true);
        formLayout.addFormRow(firstName, lastName);
        formLayout.addFormRow(email, phoneField);
        formLayout.addFormRow(textArea);
        formLayout.addFormRow(submitButton);
        
        formLayout.setColspan(textArea, 2);
        formLayout.setWidthFull();
        formLayout.setExpandColumns(true);
        formLayout.setExpandFields(true);
        


        contactSide.add(contactType, formLayout);

        Card directCont = new Card();
        directCont.addClassName("about-card");

        VerticalLayout phoneAndEmail = new VerticalLayout();
        phoneAndEmail.setSpacing(true);
        phoneAndEmail.getStyle().set("gap", "2rem").set("padding", "1rem").set("margin", "3rem 0.5rem  0.5rem  0.5rem ");

        Card emailCard = new Card();
        emailCard.addThemeVariants(CardVariant.AURA_HORIZONTAL);
        emailCard.addThemeVariants(CardVariant.AURA_ELEVATED);
        emailCard.addThemeVariants(CardVariant.AURA_OUTLINED);
        emailCard.setWidthFull();

        

        H3 emailWords = new H3("Email");
        Div emailAddr = new Div("jamestaylor314@u.boisestate.edu");
        Icon emailIcon =  VaadinIcon.ENVELOPES.create();

        emailWords.addClassName("contactus");
        emailAddr.addClassName("contactus");
        
        emailIcon.addClassName("custom-icon");
        emailIcon.getStyle().set("--vaadin-icon-color", "#17a89e");
        Anchor emailLink = new Anchor("mailto:jamestaylor314@u.boisestate.edu", emailIcon);

        emailCard.setTitle(emailWords);
        emailCard.add(emailAddr);
        emailCard.setHeaderSuffix(emailLink);

        Card phoneCard = new Card();
        phoneCard.addThemeVariants(CardVariant.AURA_HORIZONTAL);
        phoneCard.addThemeVariants(CardVariant.AURA_ELEVATED);
        phoneCard.addThemeVariants(CardVariant.AURA_OUTLINED);
        phoneCard.setWidthFull();

        H3 phoneWords = new H3("Phone");
        Div phoneNum = new Div("(208)760-5530");
        Icon phoneIcon =  VaadinIcon.PHONE.create();
        
        phoneWords.addClassName("contactus");
        phoneWords.getStyle().set("color" , "#17a89e");
        phoneNum.addClassName("contactus");

        phoneIcon.addClassName("custom-icon");
        phoneIcon.getStyle().set("--vaadin-icon-color", "#17a89e");
        Anchor phoneLink = new Anchor("tel:2087605530", phoneIcon);
        phoneCard.setTitle(phoneWords);
        phoneCard.add(phoneNum);
        phoneCard.setHeaderSuffix(phoneLink);

        phoneAndEmail.add(emailCard, phoneCard);
        directCont.add(phoneAndEmail);


        contactForms.add(contactSide, directCont);
        
        add(contactForms);
    }
}

