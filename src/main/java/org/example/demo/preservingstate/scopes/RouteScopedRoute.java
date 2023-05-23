package org.example.demo.preservingstate.scopes;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.RouteScope;

import java.time.Instant;

@RouteScope
@Route("/route-scoped")
public class RouteScopedRoute extends VerticalLayout {

    public RouteScopedRoute(RouteScopedSubview subview, AnotherRouteScopedSubview subview2) {
        add(new H1("I'm a RouteScope:d route and I was created on " + Instant.now()));
        add(new Button("Go to UI scoped view", evt -> getUI().ifPresent(ui -> ui.navigate(UIScopedRoute.class))));
        add(subview);
        add(subview2);
    }
}
