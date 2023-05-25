package org.example.demo.preservingstate.state;

import com.vaadin.flow.component.grid.Grid;
import org.example.demo.preservingstate.data.Contact;

public interface ViewState {

    void storeContactGridState(Grid<Contact> grid);

    void loadContactGridState(Grid<Contact> grid);
}
