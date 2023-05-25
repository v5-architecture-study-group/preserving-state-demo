package org.example.demo.preservingstate.urlparameters;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import org.example.demo.preservingstate.data.Contact;
import org.example.demo.preservingstate.data.ContactRepository;

@Route("/view-with-url-parameters")
public class ViewWithUrlParameters extends VerticalLayout implements HasUrlParameter<String> {

    private final ContactRepository contactRepository;
    private final Grid<Contact> grid;
    private final UrlParameterViewState viewState;

    static final String KEY_FIRST_NAME = "firstName";
    static final String KEY_LAST_NAME = "lastName";
    static final String KEY_EMAIL = "email";
    static final String KEY_PHONE = "phone";

    public ViewWithUrlParameters(UrlParameterViewState viewState, ContactRepository contactRepository) {
        this.viewState = viewState;
        this.contactRepository = contactRepository;
        grid = new Grid<>();
        grid.addColumn(Contact::getFirstName).setHeader("First name").setKey(KEY_FIRST_NAME).setSortable(true);
        grid.addColumn(Contact::getLastName).setHeader("Last name").setKey(KEY_LAST_NAME).setSortable(true);
        grid.addColumn(Contact::getEmailAddress).setHeader("E-mail").setKey(KEY_EMAIL).setSortable(true);
        grid.addColumn(Contact::getPhoneNumber).setHeader("Phone").setKey(KEY_PHONE).setSortable(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setSizeFull();
        grid.addSortListener(event -> storeState());
        grid.addSelectionListener(event -> storeState());
        add(grid);
        setSizeFull();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        grid.setItems(contactRepository.findAll());
        loadState();
    }


    private void storeState() {
        viewState.storeContactGridState(grid);
    }

    private void loadState() {
        viewState.loadContactGridState(grid);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        viewState.initialize(event.getUI(), event.getLocation());
    }
}
