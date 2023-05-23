package org.example.demo.preservingstate.scopes;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.RouteScopeOwner;
import com.vaadin.flow.spring.annotation.SpringComponent;

import java.time.Instant;

@SpringComponent
@RouteScope
@RouteScopeOwner(RouteScopedRoute.class)
public class AnotherRouteScopedSubview extends VerticalLayout {

    public AnotherRouteScopedSubview(RouteScopedSubview routeScopedSubview) {
        add(new H2("I'm another route scoped sub view and I was created on " + Instant.now()));
        add(new Button("Do something in subview", evt -> routeScopedSubview.doSomething()));
    }
}
