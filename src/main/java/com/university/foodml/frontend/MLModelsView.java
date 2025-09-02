package com.university.foodml.frontend;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("ml-models")
public class MLModelsView extends VerticalLayout {

    public MLModelsView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(createHeader());
        add(createModelsOverview());
        add(createModelDetails());
    }

    private H2 createHeader() {
        H2 header = new H2("Machine Learning Models");
        header.addClassNames(LumoUtility.Margin.Bottom.LARGE);
        return header;
    }

    private HorizontalLayout createModelsOverview() {
        HorizontalLayout overview = new HorizontalLayout();
        overview.setWidthFull();
        overview.setSpacing(true);

        overview.add(createModelCard("Production Model", "v2.1.0", "Active", "95.2%", "🟢"));
        overview.add(createModelCard("Staging Model", "v2.2.0", "Testing", "94.8%", "🟡"));
        overview.add(createModelCard("Training Model", "v2.3.0", "Training", "N/A", "🔄"));
        overview.add(createModelCard("Legacy Model", "v1.5.0", "Deprecated", "89.1%", "🔴"));

        return overview;
    }

    private VerticalLayout createModelCard(String name, String version, String status, String accuracy, String statusIcon) {
        VerticalLayout card = new VerticalLayout();
        card.addClassNames(
            LumoUtility.Padding.MEDIUM,
            LumoUtility.Border.ALL,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Background.CONTRAST_5
        );
        card.setWidth("250px");
        card.setHeight("180px");
        card.setSpacing(false);
        card.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        VerticalLayout header = new VerticalLayout();
        header.setSpacing(false);
        header.setPadding(false);

        H3 nameHeader = new H3(name);
        nameHeader.addClassNames(LumoUtility.Margin.Bottom.SMALL);

        Span versionSpan = new Span("Version: " + version);
        versionSpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        header.add(nameHeader, versionSpan);

        VerticalLayout footer = new VerticalLayout();
        footer.setSpacing(false);
        footer.setPadding(false);
        footer.setAlignItems(FlexComponent.Alignment.END);

        HorizontalLayout statusRow = new HorizontalLayout();
        statusRow.setSpacing(true);
        statusRow.setAlignItems(FlexComponent.Alignment.CENTER);

        Span statusIconSpan = new Span(statusIcon);
        statusIconSpan.addClassNames(LumoUtility.FontSize.LARGE);

        Span statusSpan = new Span(status);
        statusSpan.addClassNames(LumoUtility.FontSize.SMALL, getStatusColor(status));

        statusRow.add(statusIconSpan, statusSpan);

        Span accuracySpan = new Span("Accuracy: " + accuracy);
        accuracySpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.FontWeight.MEDIUM);

        footer.add(statusRow, accuracySpan);

        card.add(header, footer);
        return card;
    }

    private String getStatusColor(String status) {
        return switch (status.toLowerCase()) {
            case "active" -> LumoUtility.TextColor.SUCCESS;
            case "testing" -> LumoUtility.TextColor.ERROR;
            case "training" -> LumoUtility.TextColor.PRIMARY;
            case "deprecated" -> LumoUtility.TextColor.ERROR;
            default -> LumoUtility.TextColor.SECONDARY;
        };
    }

    private VerticalLayout createModelDetails() {
        VerticalLayout details = new VerticalLayout();
        details.addClassNames(LumoUtility.Margin.Top.LARGE);

        H3 detailsHeader = new H3("Model Performance Details");
        detailsHeader.addClassNames(LumoUtility.Margin.Bottom.MEDIUM);

        details.add(createPerformanceTable());
        details.add(createTrainingHistory());

        return details;
    }

    private VerticalLayout createPerformanceTable() {
        VerticalLayout table = new VerticalLayout();
        table.addClassNames(
            LumoUtility.Padding.MEDIUM,
            LumoUtility.Border.ALL,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Background.CONTRAST_5
        );
        table.setWidthFull();
        table.setSpacing(false);

        // Table header
        HorizontalLayout header = new HorizontalLayout();
        header.addClassNames(
            LumoUtility.Padding.MEDIUM,
            LumoUtility.FontWeight.BOLD
        );
        header.getStyle().set("border-bottom", "1px solid #e0e0e0");
        header.setWidthFull();

        header.add(createTableCell("Metric", true));
        header.add(createTableCell("Production", true));
        header.add(createTableCell("Staging", true));
        header.add(createTableCell("Change", true));

        table.add(header);

        // Table rows
        table.add(createTableRow("Accuracy", "95.2%", "94.8%", "-0.4%"));
        table.add(createTableRow("Precision", "93.8%", "93.5%", "-0.3%"));
        table.add(createTableRow("Recall", "96.1%", "95.9%", "-0.2%"));
        table.add(createTableRow("F1-Score", "94.9%", "94.7%", "-0.2%"));

        return table;
    }

    private HorizontalLayout createTableRow(String metric, String production, String staging, String change) {
        HorizontalLayout row = new HorizontalLayout();
        row.addClassNames(LumoUtility.Padding.MEDIUM);
        row.getStyle().set("border-bottom", "1px solid #e0e0e0");
        row.setWidthFull();

        row.add(createTableCell(metric, false));
        row.add(createTableCell(production, false));
        row.add(createTableCell(staging, false));
        row.add(createTableCell(change, false));

        return row;
    }

    private Span createTableCell(String text, boolean isHeader) {
        Span cell = new Span(text);
        cell.setWidth("150px");
        if (isHeader) {
            cell.addClassNames(LumoUtility.FontWeight.BOLD);
        }
        return cell;
    }

    private VerticalLayout createTrainingHistory() {
        VerticalLayout history = new VerticalLayout();
        history.addClassNames(LumoUtility.Margin.Top.LARGE);

        H3 historyHeader = new H3("Training History");
        historyHeader.addClassNames(LumoUtility.Margin.Bottom.MEDIUM);

        history.add(createTrainingEvent("Model v2.1.0 deployed to production", "2 days ago", "✅"));
        history.add(createTrainingEvent("Model v2.2.0 training completed", "1 week ago", "🔄"));
        history.add(createTrainingEvent("Model v2.0.0 deprecated", "2 weeks ago", "🔴"));
        history.add(createTrainingEvent("Model v1.5.0 performance degraded", "1 month ago", "⚠️"));

        return history;
    }

    private HorizontalLayout createTrainingEvent(String description, String timestamp, String icon) {
        HorizontalLayout event = new HorizontalLayout();
        event.setWidthFull();
        event.setSpacing(true);
        event.setAlignItems(FlexComponent.Alignment.CENTER);
        event.addClassNames(LumoUtility.Padding.Vertical.SMALL);

        Span iconSpan = new Span(icon);
        iconSpan.addClassNames(LumoUtility.FontSize.MEDIUM);

        Span descSpan = new Span(description);
        descSpan.addClassNames(LumoUtility.FontWeight.MEDIUM);

        Span timeSpan = new Span(timestamp);
        timeSpan.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontSize.SMALL);

        event.add(iconSpan, descSpan, timeSpan);
        event.expand(descSpan);

        return event;
    }
} 