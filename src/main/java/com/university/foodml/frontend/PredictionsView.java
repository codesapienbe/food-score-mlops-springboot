package com.university.foodml.frontend;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("predictions")
public class PredictionsView extends VerticalLayout {

    private TextField foodNameField;
    private TextArea descriptionField;
    private Select<String> categorySelect;
    private VerticalLayout predictionsContainer;

    public PredictionsView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(createHeader());
        add(createPredictionForm());
        add(createPredictionsHistory());
    }

    private H2 createHeader() {
        H2 header = new H2("Food Quality Predictions");
        header.addClassNames(LumoUtility.Margin.Bottom.LARGE);
        return header;
    }

    private VerticalLayout createPredictionForm() {
        VerticalLayout form = new VerticalLayout();
        form.addClassNames(
            LumoUtility.Padding.MEDIUM,
            LumoUtility.Border.ALL,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Background.CONTRAST_5
        );
        form.setWidthFull();
        form.setSpacing(true);

        H3 formHeader = new H3("Make New Prediction");
        formHeader.addClassNames(LumoUtility.Margin.Bottom.MEDIUM);

        foodNameField = new TextField("Food Name");
        foodNameField.setPlaceholder("e.g., Organic Bananas");
        foodNameField.setWidth("400px");

        descriptionField = new TextArea("Description");
        descriptionField.setPlaceholder("Describe the food product, ingredients, processing method, etc.");
        descriptionField.setWidth("400px");
        descriptionField.setHeight("100px");

        categorySelect = new Select<>();
        categorySelect.setLabel("Food Category");
        categorySelect.setItems("Fruits & Vegetables", "Grains & Cereals", "Dairy & Eggs", 
                              "Meat & Fish", "Processed Foods", "Beverages", "Snacks", "Other");
        categorySelect.setWidth("400px");

        HorizontalLayout buttonRow = new HorizontalLayout();
        buttonRow.setSpacing(true);

        Button predictButton = new Button("Predict Quality");
        predictButton.addClassNames(LumoUtility.Background.PRIMARY);
        predictButton.addClickListener(e -> makePrediction());

        Button clearButton = new Button("Clear Form");
        clearButton.addClassNames(LumoUtility.Background.CONTRAST);
        clearButton.addClickListener(e -> clearForm());

        buttonRow.add(predictButton, clearButton);

        form.add(formHeader, foodNameField, descriptionField, categorySelect, buttonRow);
        return form;
    }

    private VerticalLayout createPredictionsHistory() {
        VerticalLayout history = new VerticalLayout();
        history.addClassNames(LumoUtility.Margin.Top.LARGE);

        H3 historyHeader = new H3("Prediction History");
        historyHeader.addClassNames(LumoUtility.Margin.Bottom.MEDIUM);

        predictionsContainer = new VerticalLayout();
        predictionsContainer.setSpacing(true);

        // Add sample predictions
        addSamplePredictions();

        history.add(historyHeader, predictionsContainer);
        return history;
    }

    private void addSamplePredictions() {
        predictionsContainer.add(createPredictionCard(
            "Organic Strawberries",
            "Fresh organic strawberries from local farm",
            "Low",
            "95%",
            "2 hours ago"
        ));

        predictionsContainer.add(createPredictionCard(
            "Whole Wheat Pasta",
            "100% whole wheat pasta with natural ingredients",
            "Medium",
            "87%",
            "1 day ago"
        ));

        predictionsContainer.add(createPredictionCard(
            "Processed Cheese Slices",
            "Processed cheese with artificial preservatives",
            "High",
            "92%",
            "3 days ago"
        ));
    }

    private HorizontalLayout createPredictionCard(String name, String description, String score, String confidence, String timestamp) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassNames(
            LumoUtility.Padding.MEDIUM,
            LumoUtility.Border.ALL,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Background.CONTRAST_5
        );
        card.setWidthFull();
        card.setSpacing(true);
        card.setAlignItems(FlexComponent.Alignment.START);

        VerticalLayout content = new VerticalLayout();
        content.setSpacing(false);
        content.setPadding(false);

        H3 nameHeader = new H3(name);
        nameHeader.addClassNames(LumoUtility.Margin.Bottom.SMALL);

        Span descSpan = new Span(description);
        descSpan.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.Margin.Bottom.SMALL);

        Span timestampSpan = new Span(timestamp);
        timestampSpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        content.add(nameHeader, descSpan, timestampSpan);

        VerticalLayout scoreSection = new VerticalLayout();
        scoreSection.setAlignItems(FlexComponent.Alignment.END);
        scoreSection.setSpacing(false);
        scoreSection.setPadding(false);

        Span scoreLabel = new Span("Quality Score:");
        scoreLabel.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        Span scoreValue = new Span(score);
        scoreValue.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.FontWeight.BOLD,
            getScoreColor(score)
        );

        Span confidenceLabel = new Span("Confidence:");
        confidenceLabel.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        Span confidenceValue = new Span(confidence);
        confidenceValue.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.MEDIUM);

        scoreSection.add(scoreLabel, scoreValue, confidenceLabel, confidenceValue);

        card.add(content, scoreSection);
        card.expand(content);

        return card;
    }

    private String getScoreColor(String score) {
        return switch (score.toLowerCase()) {
            case "low" -> LumoUtility.TextColor.SUCCESS;
            case "medium" -> LumoUtility.TextColor.ERROR;
            case "high" -> LumoUtility.TextColor.ERROR;
            default -> LumoUtility.TextColor.PRIMARY;
        };
    }

    private void makePrediction() {
        String foodName = foodNameField.getValue();
        String description = descriptionField.getValue();
        String category = categorySelect.getValue();

        if (foodName != null && !foodName.trim().isEmpty()) {
            // TODO: Implement actual prediction logic
            System.out.println("Making prediction for: " + foodName);
            System.out.println("Description: " + description);
            System.out.println("Category: " + category);
        }
    }

    private void clearForm() {
        foodNameField.setValue("");
        descriptionField.setValue("");
        categorySelect.setValue(null);
    }
} 