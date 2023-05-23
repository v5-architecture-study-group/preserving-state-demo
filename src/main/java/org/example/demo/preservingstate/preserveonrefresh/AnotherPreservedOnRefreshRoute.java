package org.example.demo.preservingstate.preserveonrefresh;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import java.time.Instant;

@PreserveOnRefresh
@Route("/another-preserved-on-refresh")
@UIScope
public class AnotherPreservedOnRefreshRoute extends VerticalLayout {

    public AnotherPreservedOnRefreshRoute() {
        add(new H1("This other route was created on " + Instant.now()));
        add(new Button("Go to first route", evt -> getUI().ifPresent(ui -> ui.navigate(PreservedOnRefreshRoute.class))));
    }
}
