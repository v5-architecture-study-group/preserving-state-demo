package org.example.demo.preservingstate.urlparameters;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.RouteScopeOwner;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.apache.commons.lang3.math.NumberUtils;
import org.example.demo.preservingstate.data.Contact;
import org.example.demo.preservingstate.data.ContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringComponent
@RouteScope
@RouteScopeOwner(ViewWithUrlParameters.class)
class UrlParameterViewState {

    private static final Logger log = LoggerFactory.getLogger(UrlParameterViewState.class);
    private static final String QUERY_PARAM_SELECTION = "selection";
    private static final String QUERY_PARAM_SORT = "sort";
    private static final Set<String> VALID_COLUMN_KEYS = Set.of(
            ViewWithUrlParameters.KEY_FIRST_NAME,
            ViewWithUrlParameters.KEY_LAST_NAME,
            ViewWithUrlParameters.KEY_EMAIL,
            ViewWithUrlParameters.KEY_PHONE
    );
    private final ContactRepository contactRepository;
    private UI ui;
    private Location location;
    private Set<Long> selectedContactIds = Collections.emptySet();
    private List<String> sortOrder = Collections.emptyList();
    private boolean loading = false;

    UrlParameterViewState(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    void initialize(UI ui, Location location) {
        this.ui = Objects.requireNonNull(ui, "ui must not be null");
        this.location = Objects.requireNonNull(location, "location must not be null");

        var queryParameters = location.getQueryParameters().getParameters();
        this.selectedContactIds = queryParameters.getOrDefault(QUERY_PARAM_SELECTION, Collections.emptyList())
                .stream()
                .filter(NumberUtils::isCreatable)
                .map(NumberUtils::createLong)
                .collect(Collectors.toUnmodifiableSet());
        this.sortOrder = queryParameters.getOrDefault(QUERY_PARAM_SORT, Collections.emptyList())
                .stream()
                .filter(this::isValidSortOrder)
                .toList();
        log.debug("Initialized with selected contacts {} and sort order {}", selectedContactIds, sortOrder);
    }

    private boolean isValidSortOrder(String sortOrder) {
        if (!sortOrder.startsWith("asc_") && !sortOrder.startsWith("dsc_")) {
            return false;
        }
        var key = sortOrder.substring(4);
        return VALID_COLUMN_KEYS.contains(key);
    }

    private String toSortOrderString(GridSortOrder<Contact> sortOrder) {
        if (sortOrder.getDirection() == SortDirection.ASCENDING) {
            return "asc_%s".formatted(sortOrder.getSorted().getKey());
        } else {
            return "dsc_%s".formatted(sortOrder.getSorted().getKey());
        }
    }

    private Stream<GridSortOrder<Contact>> toSortOrder(Grid<Contact> grid, String sortOrderString) {
        var asc = sortOrderString.startsWith("asc_");
        var key = sortOrderString.substring(4);
        var column = grid.getColumnByKey(key);
        return asc ? GridSortOrder.asc(column).build().stream() : GridSortOrder.desc(column).build().stream();
    }

    public void storeContactGridState(Grid<Contact> grid) {
        if (loading) {
            return;
        }
        selectedContactIds = grid.getSelectedItems().stream().map(Contact::getId).collect(Collectors.toUnmodifiableSet());
        sortOrder = grid.getSortOrder().stream().map(this::toSortOrderString).toList();

        // If you are really picky, you should also somehow store the index of the first visible row in the grid

        log.debug("Storing selected contacts {} and sort order {} as query parameters", selectedContactIds, sortOrder);

        var queryParameters = new QueryParameters(Map.of(
                QUERY_PARAM_SELECTION, selectedContactIds.stream().map(Objects::toString).toList(),
                QUERY_PARAM_SORT, sortOrder
        ));
        var location = new Location(this.location.getPath(), queryParameters);
        ui.getPage().getHistory().pushState(null, location);
    }

    public void loadContactGridState(Grid<Contact> grid) {
        if (loading) {
            return;
        }
        log.debug("Loading selected contacts {} and sort order {}", selectedContactIds, sortOrder);
        loading = true;
        try {

            if (selectedContactIds.isEmpty()) {
                grid.deselectAll();
            } else {
                grid.asMultiSelect().select(contactRepository.findAllById(selectedContactIds));
            }

            if (sortOrder.isEmpty()) {
                grid.sort(null);
            } else {
                grid.sort(sortOrder.stream().flatMap(sortOrderString -> toSortOrder(grid, sortOrderString)).toList());
            }
        } finally {
            loading = false;
        }
    }
}
