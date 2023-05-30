package org.example.demo.preservingstate.scopes;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import java.time.Instant;

@UIScope
@Route("/ui-scoped")
public class UIScopedRoute extends VerticalLayout {

    public UIScopedRoute(UIScopedSubview subview) {
        add(new H1("I'm a UIScope:d route and I was created on " + Instant.now()));
        add(subview);
        add(new Button("Go to route scoped view", evt -> getUI().ifPresent(ui -> ui.navigate(RouteScopedRoute.class))));
    }
}
