package com.university.foodml.frontend;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("dashboard")
public class DashboardView extends VerticalLayout {

    public DashboardView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(createHeader());
        add(createMetricsRow());
        add(createQuickActions());
        add(createRecentActivity());
    }

    private H2 createHeader() {
        H2 header = new H2("Dashboard");
        header.addClassNames(LumoUtility.Margin.Bottom.LARGE);
        return header;
    }

    private HorizontalLayout createMetricsRow() {
        HorizontalLayout metricsRow = new HorizontalLayout();
        metricsRow.setWidthFull();
        metricsRow.setSpacing(true);

        metricsRow.add(createMetricCard("Total Predictions", "1,234", "📊"));
        metricsRow.add(createMetricCard("Active Models", "3", "🤖"));
        metricsRow.add(createMetricCard("Data Drift Alerts", "2", "⚠️"));
        metricsRow.add(createMetricCard("System Health", "Healthy", "✅"));

        return metricsRow;
    }

    private VerticalLayout createMetricCard(String title, String value, String icon) {
        VerticalLayout card = new VerticalLayout();
        card.addClassNames(
            LumoUtility.Padding.MEDIUM,
            LumoUtility.Border.ALL,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Background.CONTRAST_5
        );
        card.setWidth("200px");
        card.setHeight("120px");
        card.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        card.setAlignItems(FlexComponent.Alignment.CENTER);

        Span iconSpan = new Span(icon);
        iconSpan.addClassNames(LumoUtility.FontSize.XLARGE);

        Span valueSpan = new Span(value);
        valueSpan.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.BOLD);

        Span titleSpan = new Span(title);
        titleSpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        card.add(iconSpan, valueSpan, titleSpan);
        return card;
    }

    private VerticalLayout createQuickActions() {
        VerticalLayout quickActions = new VerticalLayout();
        quickActions.addClassNames(LumoUtility.Margin.Top.LARGE);

        H3 actionsHeader = new H3("Quick Actions");
        actionsHeader.addClassNames(LumoUtility.Margin.Bottom.MEDIUM);

        HorizontalLayout actionsRow = new HorizontalLayout();
        actionsRow.setSpacing(true);

        actionsRow.add(createActionButton("New Prediction", "🔮"));
        actionsRow.add(createActionButton("Search Food", "🔍"));
        actionsRow.add(createActionButton("Chat with AI", "💬"));
        actionsRow.add(createActionButton("View Models", "📋"));

        quickActions.add(actionsHeader, actionsRow);
        return quickActions;
    }

    private Div createActionButton(String text, String icon) {
        Div button = new Div();
        button.addClassNames(
            LumoUtility.Padding.MEDIUM,
            LumoUtility.Border.ALL,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Background.PRIMARY_50
        );
        button.getStyle().set("cursor", "pointer");
        button.setWidth("150px");
        button.setHeight("80px");
        button.getStyle().set("display", "flex");
        button.getStyle().set("flex-direction", "column");
        button.getStyle().set("align-items", "center");
        button.getStyle().set("justify-content", "center");

        Span iconSpan = new Span(icon);
        iconSpan.addClassNames(LumoUtility.FontSize.LARGE);

        Span textSpan = new Span(text);
        textSpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.FontWeight.MEDIUM);

        button.add(iconSpan, textSpan);
        return button;
    }

    private VerticalLayout createRecentActivity() {
        VerticalLayout recentActivity = new VerticalLayout();
        recentActivity.addClassNames(LumoUtility.Margin.Top.LARGE);

        H3 activityHeader = new H3("Recent Activity");
        activityHeader.addClassNames(LumoUtility.Margin.Bottom.MEDIUM);

        recentActivity.add(createActivityItem("Model retraining completed", "2 hours ago"));
        recentActivity.add(createActivityItem("New prediction request processed", "4 hours ago"));
        recentActivity.add(createActivityItem("Data drift detected in feature X", "1 day ago"));
        recentActivity.add(createActivityItem("System backup completed", "2 days ago"));

        return recentActivity;
    }

    private HorizontalLayout createActivityItem(String text, String time) {
        HorizontalLayout item = new HorizontalLayout();
        item.setWidthFull();
        item.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        item.addClassNames(LumoUtility.Padding.Vertical.SMALL);

        Span textSpan = new Span(text);
        Span timeSpan = new Span(time);
        timeSpan.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.SMALL);

        item.add(textSpan, timeSpan);
        return item;
    }
} 