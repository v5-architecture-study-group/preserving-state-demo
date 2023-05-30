package org.example.demo.preservingstate.scopes;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.time.Instant;

@SpringComponent
@UIScope
public class UIScopedSubview extends VerticalLayout {

    public UIScopedSubview() {
        add(new H2("I'm a UI scoped sub view and I was created on " + Instant.now()));
    }

    public void doSomething() {
        add(new H3("Something has been done on " + Instant.now()));
    }
}
