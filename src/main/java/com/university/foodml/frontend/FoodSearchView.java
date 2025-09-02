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

@Route("food-search")
public class FoodSearchView extends VerticalLayout {

    private TextField searchField;
    private VerticalLayout resultsContainer;

    public FoodSearchView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(createHeader());
        add(createSearchForm());
        add(createResultsContainer());
    }

    private H2 createHeader() {
        H2 header = new H2("Food Search");
        header.addClassNames(LumoUtility.Margin.Bottom.LARGE);
        return header;
    }

    private HorizontalLayout createSearchForm() {
        HorizontalLayout searchForm = new HorizontalLayout();
        searchForm.setWidthFull();
        searchForm.setSpacing(true);
        searchForm.setAlignItems(FlexComponent.Alignment.END);

        searchField = new TextField("Search for food products");
        searchField.setPlaceholder("e.g., apple, bread, yogurt");
        searchField.setWidth("400px");

        Button searchButton = new Button("Search");
        searchButton.addClassNames(LumoUtility.Background.PRIMARY);
        searchButton.addClickListener(e -> performSearch());

        Button clearButton = new Button("Clear");
        clearButton.addClassNames(LumoUtility.Background.CONTRAST);

        searchForm.add(searchField, searchButton, clearButton);
        searchForm.expand(searchField);

        return searchForm;
    }

    private VerticalLayout createResultsContainer() {
        resultsContainer = new VerticalLayout();
        resultsContainer.setWidthFull();
        resultsContainer.setSpacing(true);
        resultsContainer.addClassNames(LumoUtility.Margin.Top.LARGE);

        // Add some sample results for demonstration
        addSampleResults();

        return resultsContainer;
    }

    private void addSampleResults() {
        resultsContainer.add(createFoodResultCard(
            "Organic Apples",
            "Fresh organic apples from local farms",
            "Low",
            "High fiber, vitamin C",
            "🍎"
        ));

        resultsContainer.add(createFoodResultCard(
            "Whole Grain Bread",
            "100% whole grain bread with seeds",
            "Medium",
            "Good source of fiber and protein",
            "🍞"
        ));

        resultsContainer.add(createFoodResultCard(
            "Greek Yogurt",
            "Plain Greek yogurt with live cultures",
            "Low",
            "High protein, probiotics",
            "🥛"
        ));
    }

    private HorizontalLayout createFoodResultCard(String name, String description, String score, String benefits, String emoji) {
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

        Span emojiSpan = new Span(emoji);
        emojiSpan.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.Margin.Right.MEDIUM);

        VerticalLayout content = new VerticalLayout();
        content.setSpacing(false);
        content.setPadding(false);

        H3 nameHeader = new H3(name);
        nameHeader.addClassNames(LumoUtility.Margin.Bottom.SMALL);

        Span descSpan = new Span(description);
        descSpan.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.Margin.Bottom.SMALL);

        Span benefitsSpan = new Span(benefits);
        benefitsSpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.Margin.Bottom.SMALL);

        content.add(nameHeader, descSpan, benefitsSpan);

        VerticalLayout scoreSection = new VerticalLayout();
        scoreSection.setAlignItems(FlexComponent.Alignment.END);
        scoreSection.setSpacing(false);
        scoreSection.setPadding(false);

        Span scoreLabel = new Span("Food Score:");
        scoreLabel.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        Span scoreValue = new Span(score);
        scoreValue.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.FontWeight.BOLD,
            getScoreColor(score)
        );

        scoreSection.add(scoreLabel, scoreValue);

        card.add(emojiSpan, content, scoreSection);
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

    private void performSearch() {
        String query = searchField.getValue();
        if (query != null && !query.trim().isEmpty()) {
            // TODO: Implement actual search logic
            // For now, just log the search
            System.out.println("Searching for: " + query);
        }
    }
} 