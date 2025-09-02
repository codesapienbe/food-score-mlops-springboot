package com.university.foodml.frontend;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("chat")
public class ChatView extends VerticalLayout {

    private TextField messageField;
    private VerticalLayout chatContainer;

    public ChatView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(createHeader());
        add(createChatInterface());
        add(createQuickQuestions());
    }

    private H2 createHeader() {
        H2 header = new H2("AI Chat Assistant");
        header.addClassNames(LumoUtility.Margin.Bottom.LARGE);
        return header;
    }

    private VerticalLayout createChatInterface() {
        VerticalLayout chatInterface = new VerticalLayout();
        chatInterface.addClassNames(
            LumoUtility.Padding.MEDIUM,
            LumoUtility.Border.ALL,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Background.CONTRAST_5
        );
        chatInterface.setWidthFull();
        chatInterface.setHeight("500px");
        chatInterface.setSpacing(true);

        H3 chatHeader = new H3("Chat with AI");
        chatHeader.addClassNames(LumoUtility.Margin.Bottom.MEDIUM);

        chatContainer = new VerticalLayout();
        chatContainer.setWidthFull();
        chatContainer.setHeight("350px");
        chatContainer.setSpacing(true);
        chatContainer.addClassNames(
            LumoUtility.Padding.MEDIUM,
            LumoUtility.Border.ALL,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Background.CONTRAST_10
        );

        // Add welcome message
        addMessage("AI Assistant", "Hello! I'm your AI assistant for food quality and MLOps. How can I help you today?", true);

        HorizontalLayout inputRow = new HorizontalLayout();
        inputRow.setWidthFull();
        inputRow.setSpacing(true);
        inputRow.setAlignItems(FlexComponent.Alignment.END);

        messageField = new TextField("Type your message");
        messageField.setPlaceholder("Ask about food quality, MLOps, or anything else...");
        messageField.setWidth("400px");

        Button sendButton = new Button("Send");
        sendButton.addClassNames(LumoUtility.Background.PRIMARY);
        sendButton.addClickListener(e -> sendMessage());

        inputRow.add(messageField, sendButton);
        inputRow.expand(messageField);

        chatInterface.add(chatHeader, chatContainer, inputRow);
        return chatInterface;
    }

    private void addMessage(String sender, String message, boolean isAI) {
        HorizontalLayout messageLayout = new HorizontalLayout();
        messageLayout.setWidthFull();
        messageLayout.setSpacing(true);
        messageLayout.setAlignItems(FlexComponent.Alignment.START);

        String icon = isAI ? "🤖" : "👤";
        Span iconSpan = new Span(icon);
        iconSpan.addClassNames(LumoUtility.FontSize.MEDIUM);

        VerticalLayout messageContent = new VerticalLayout();
        messageContent.setSpacing(false);
        messageContent.setPadding(false);

        Span senderSpan = new Span(sender);
        senderSpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.FontWeight.BOLD);

        Span messageSpan = new Span(message);
        messageSpan.addClassNames(LumoUtility.Margin.Top.SMALL);

        messageContent.add(senderSpan, messageSpan);

        messageLayout.add(iconSpan, messageContent);
        messageLayout.expand(messageContent);

        chatContainer.add(messageLayout);
    }

    private void sendMessage() {
        String message = messageField.getValue();
        if (message != null && !message.trim().isEmpty()) {
            addMessage("You", message, false);
            messageField.setValue("");

            // Simulate AI response
            String response = generateAIResponse(message);
            addMessage("AI Assistant", response, true);
        }
    }

    private String generateAIResponse(String message) {
        String lowerMessage = message.toLowerCase();
        
        if (lowerMessage.contains("food quality") || lowerMessage.contains("score")) {
            return "Food quality scoring is based on multiple factors including nutritional value, processing methods, ingredient quality, and safety standards. Our ML models analyze these factors to provide accurate predictions.";
        } else if (lowerMessage.contains("mlops") || lowerMessage.contains("pipeline")) {
            return "Our MLOps pipeline includes automated model training, continuous monitoring, data drift detection, and automated retraining. This ensures our models stay accurate and up-to-date.";
        } else if (lowerMessage.contains("model") || lowerMessage.contains("accuracy")) {
            return "Our current production model has 95.2% accuracy. We continuously monitor performance and retrain models when accuracy drops below acceptable thresholds.";
        } else if (lowerMessage.contains("data") || lowerMessage.contains("drift")) {
            return "Data drift detection monitors changes in input data distribution. When significant drift is detected, we trigger model retraining to maintain accuracy.";
        } else {
            return "I can help you with questions about food quality scoring, MLOps processes, model performance, and data management. What specific aspect would you like to know more about?";
        }
    }

    private VerticalLayout createQuickQuestions() {
        VerticalLayout quickQuestions = new VerticalLayout();
        quickQuestions.addClassNames(LumoUtility.Margin.Top.LARGE);

        H3 questionsHeader = new H3("Quick Questions");
        questionsHeader.addClassNames(LumoUtility.Margin.Bottom.MEDIUM);

        HorizontalLayout questionsRow = new HorizontalLayout();
        questionsRow.setSpacing(true);

        questionsRow.add(createQuickQuestionButton("How does food scoring work?"));
        questionsRow.add(createQuickQuestionButton("What is MLOps?"));
        questionsRow.add(createQuickQuestionButton("How accurate are the models?"));
        questionsRow.add(createQuickQuestionButton("What is data drift?"));

        quickQuestions.add(questionsHeader, questionsRow);
        return quickQuestions;
    }

    private Button createQuickQuestionButton(String question) {
        Button button = new Button(question);
        button.addClassNames(
            LumoUtility.Background.PRIMARY_50,
            LumoUtility.Border.ALL,
            LumoUtility.BorderRadius.MEDIUM
        );
        button.addClickListener(e -> {
            messageField.setValue(question);
            sendMessage();
        });
        return button;
    }
} 