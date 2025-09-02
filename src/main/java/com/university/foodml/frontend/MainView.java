package com.university.foodml.frontend;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("")
public class MainView extends AppLayout {

    public MainView() {
        createHeader();
        createDrawer();
        createContent();
    }

    private void createHeader() {
        H1 logo = new H1("🍎 Food Score MLOps");
        logo.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.Margin.MEDIUM);

        String username = "User"; // TODO: Get from security context
        Span user = new Span(username);
        user.addClassNames(
            LumoUtility.FontSize.MEDIUM,
            LumoUtility.Margin.MEDIUM);

        HorizontalLayout header = new HorizontalLayout(
            new DrawerToggle(),
            logo,
            user
        );
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
            LumoUtility.Padding.Vertical.XSMALL,
            LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink dashboardLink = new RouterLink("Dashboard", DashboardView.class);
        RouterLink foodSearchLink = new RouterLink("Food Search", FoodSearchView.class);
        RouterLink predictionsLink = new RouterLink("Predictions", PredictionsView.class);
        RouterLink mlModelsLink = new RouterLink("ML Models", MLModelsView.class);
        RouterLink chatLink = new RouterLink("AI Chat", ChatView.class);
        RouterLink monitoringLink = new RouterLink("Monitoring", MonitoringView.class);

        addToDrawer(new VerticalLayout(
            dashboardLink,
            foodSearchLink,
            predictionsLink,
            mlModelsLink,
            chatLink,
            monitoringLink
        ));
    }

    private void createContent() {
        // Default content - will be replaced by router
        VerticalLayout content = new VerticalLayout();
        content.add(new H2("Welcome to Food Score MLOps"));
        content.add(new Span("Select a section from the menu to get started."));
        content.setSizeFull();
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        
        setContent(content);
    }
} 