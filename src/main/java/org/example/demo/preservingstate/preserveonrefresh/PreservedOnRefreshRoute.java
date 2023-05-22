package org.example.demo.preservingstate.preserveonrefresh;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

import java.time.Instant;

@PreserveOnRefresh
@Route("/preserved-on-refresh")
public class PreservedOnRefreshRoute extends VerticalLayout {

    public PreservedOnRefreshRoute() {
        add(new H1("This route was created on " + Instant.now()));
        add(new Button("Go to second route", evt -> getUI().ifPresent(ui -> ui.navigate(AnotherPreservedOnRefreshRoute.class))));
    }
}
