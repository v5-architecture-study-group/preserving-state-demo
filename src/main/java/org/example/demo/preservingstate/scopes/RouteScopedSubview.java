package org.example.demo.preservingstate.scopes;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.RouteScopeOwner;
import com.vaadin.flow.spring.annotation.SpringComponent;

import java.time.Instant;

@SpringComponent
@RouteScope
@RouteScopeOwner(RouteScopedRoute.class)
public class RouteScopedSubview extends VerticalLayout {

    public RouteScopedSubview() {
        add(new H2("I'm a route scoped sub view and I was created on " + Instant.now()));
    }

    public void doSomething() {
        add(new H3("Something has been done on " + Instant.now()));
    }
}
