package com.university.foodml.frontend;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("monitoring")
public class MonitoringView extends VerticalLayout {

    public MonitoringView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(createHeader());
        add(createSystemHealth());
        add(createPerformanceMetrics());
        add(createAlerts());
    }

    private H2 createHeader() {
        H2 header = new H2("System Monitoring");
        header.addClassNames(LumoUtility.Margin.Bottom.LARGE);
        return header;
    }

    private HorizontalLayout createSystemHealth() {
        HorizontalLayout health = new HorizontalLayout();
        health.setWidthFull();
        health.setSpacing(true);

        health.add(createHealthCard("System Status", "Healthy", "🟢", LumoUtility.TextColor.SUCCESS));
        health.add(createHealthCard("Database", "Connected", "🟢", LumoUtility.TextColor.SUCCESS));
        health.add(createHealthCard("ML Pipeline", "Running", "🟢", LumoUtility.TextColor.SUCCESS));
        health.add(createHealthCard("API Services", "Active", "🟢", LumoUtility.TextColor.SUCCESS));
        health.add(createHealthCard("Monitoring", "Online", "🟢", LumoUtility.TextColor.SUCCESS));

        return health;
    }

    private VerticalLayout createHealthCard(String name, String status, String icon, String color) {
        VerticalLayout card = new VerticalLayout();
        card.addClassNames(
            LumoUtility.Padding.MEDIUM,
            LumoUtility.Border.ALL,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Background.CONTRAST_5
        );
        card.setWidth("180px");
        card.setHeight("120px");
        card.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        card.setAlignItems(FlexComponent.Alignment.CENTER);

        Span iconSpan = new Span(icon);
        iconSpan.addClassNames(LumoUtility.FontSize.XLARGE);

        Span nameSpan = new Span(name);
        nameSpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.FontWeight.MEDIUM);

        Span statusSpan = new Span(status);
        statusSpan.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.BOLD, color);

        card.add(iconSpan, nameSpan, statusSpan);
        return card;
    }

    private VerticalLayout createPerformanceMetrics() {
        VerticalLayout metrics = new VerticalLayout();
        metrics.addClassNames(LumoUtility.Margin.Top.LARGE);

        H3 metricsHeader = new H3("Performance Metrics");
        metricsHeader.addClassNames(LumoUtility.Margin.Bottom.MEDIUM);

        HorizontalLayout metricsRow = new HorizontalLayout();
        metricsRow.setWidthFull();
        metricsRow.setSpacing(true);

        metricsRow.add(createMetricCard("Response Time", "45ms", "📊"));
        metricsRow.add(createMetricCard("Throughput", "1,234 req/s", "⚡"));
        metricsRow.add(createMetricCard("Error Rate", "0.02%", "📈"));
        metricsRow.add(createMetricCard("CPU Usage", "23%", "💻"));
        metricsRow.add(createMetricCard("Memory Usage", "67%", "🧠"));

        metrics.add(metricsHeader, metricsRow);
        return metrics;
    }

    private VerticalLayout createMetricCard(String name, String value, String icon) {
        VerticalLayout card = new VerticalLayout();
        card.addClassNames(
            LumoUtility.Padding.MEDIUM,
            LumoUtility.Border.ALL,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Background.CONTRAST_5
        );
        card.setWidth("200px");
        card.setHeight("140px");
        card.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        card.setAlignItems(FlexComponent.Alignment.CENTER);

        Span iconSpan = new Span(icon);
        iconSpan.addClassNames(LumoUtility.FontSize.LARGE);

        Span nameSpan = new Span(name);
        nameSpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        Span valueSpan = new Span(value);
        valueSpan.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.BOLD);

        card.add(iconSpan, nameSpan, valueSpan);
        return card;
    }

    private VerticalLayout createAlerts() {
        VerticalLayout alerts = new VerticalLayout();
        alerts.addClassNames(LumoUtility.Margin.Top.LARGE);

        H3 alertsHeader = new H3("Active Alerts");
        alertsHeader.addClassNames(LumoUtility.Margin.Bottom.MEDIUM);

        alerts.add(createAlertItem("Data Drift Detected", "Feature 'nutrition_score' shows 15% drift", "Warning", "⚠️"));
        alerts.add(createAlertItem("Model Performance", "Accuracy dropped below threshold", "Critical", "🚨"));
        alerts.add(createAlertItem("Training Pipeline", "Model retraining in progress", "Info", "ℹ️"));
        alerts.add(createAlertItem("System Backup", "Daily backup completed successfully", "Info", "✅"));

        return alerts;
    }

    private HorizontalLayout createAlertItem(String title, String description, String severity, String icon) {
        HorizontalLayout alert = new HorizontalLayout();
        alert.setWidthFull();
        alert.setSpacing(true);
        alert.setAlignItems(FlexComponent.Alignment.START);
        alert.addClassNames(
            LumoUtility.Padding.MEDIUM,
            LumoUtility.Border.ALL,
            LumoUtility.BorderRadius.MEDIUM,
            getAlertBackground(severity)
        );

        Span iconSpan = new Span(icon);
        iconSpan.addClassNames(LumoUtility.FontSize.MEDIUM);

        VerticalLayout content = new VerticalLayout();
        content.setSpacing(false);
        content.setPadding(false);

        Span titleSpan = new Span(title);
        titleSpan.addClassNames(LumoUtility.FontWeight.BOLD);

        Span descSpan = new Span(description);
        descSpan.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.SMALL);

        content.add(titleSpan, descSpan);

        Span severitySpan = new Span(severity);
        severitySpan.addClassNames(
            LumoUtility.FontSize.SMALL,
            LumoUtility.FontWeight.MEDIUM,
            getSeverityColor(severity)
        );

        alert.add(iconSpan, content, severitySpan);
        alert.expand(content);

        return alert;
    }

    private String getAlertBackground(String severity) {
        return switch (severity.toLowerCase()) {
            case "critical" -> LumoUtility.Background.ERROR_50;
            case "warning" -> LumoUtility.Background.ERROR_50;
            case "info" -> LumoUtility.Background.PRIMARY_50;
            default -> LumoUtility.Background.CONTRAST_5;
        };
    }

    private String getSeverityColor(String severity) {
        return switch (severity.toLowerCase()) {
            case "critical" -> LumoUtility.TextColor.ERROR;
            case "warning" -> LumoUtility.TextColor.ERROR;
            case "info" -> LumoUtility.TextColor.PRIMARY;
            default -> LumoUtility.TextColor.SECONDARY;
        };
    }
} 