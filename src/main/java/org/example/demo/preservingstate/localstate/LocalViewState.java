package org.example.demo.preservingstate.localstate;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import org.apache.commons.lang3.math.NumberUtils;
import org.example.demo.preservingstate.data.Contact;
import org.example.demo.preservingstate.data.ContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.firitin.util.WebStorage;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class LocalViewState {

    private static final Logger log = LoggerFactory.getLogger(LocalViewState.class);
    private static final String STORAGE_ITEM_SELECTION = "selection";
    private static final String STORAGE_ITEM_SORT = "sort";
    private static final Set<String> VALID_COLUMN_KEYS = Set.of(
            ViewWithLocalState.KEY_FIRST_NAME,
            ViewWithLocalState.KEY_LAST_NAME,
            ViewWithLocalState.KEY_EMAIL,
            ViewWithLocalState.KEY_PHONE
    );
    private final ContactRepository contactRepository;
    private Set<Long> selectedContactIds = Collections.emptySet();
    private List<String> sortOrder = Collections.emptyList();
    private boolean loadingSelection = false;
    private boolean loadingSortOrder = false;

    LocalViewState(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
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
        if (loadingSelection || loadingSortOrder) {
            return;
        }
        selectedContactIds = grid.getSelectedItems().stream().map(Contact::getId).collect(Collectors.toUnmodifiableSet());
        sortOrder = grid.getSortOrder().stream().map(this::toSortOrderString).toList();

        log.debug("Storing selected contacts {} and sort order {} in localStorage", selectedContactIds, sortOrder);

        WebStorage.setItem(WebStorage.Storage.localStorage, STORAGE_ITEM_SELECTION, selectedContactIds.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(",")));

        WebStorage.setItem(WebStorage.Storage.localStorage, STORAGE_ITEM_SORT, String.join(",", sortOrder));
    }

    public void loadContactGridState(Grid<Contact> grid) {
        if (loadingSelection || loadingSortOrder) {
            return;
        }
        loadingSelection = true;
        WebStorage.getItem(WebStorage.Storage.localStorage, STORAGE_ITEM_SELECTION, value -> {
            try {
                if (value == null) {
                    selectedContactIds = Collections.emptySet();
                } else {
                    selectedContactIds = Stream.of(value.split(","))
                            .filter(NumberUtils::isCreatable)
                            .map(NumberUtils::createLong)
                            .collect(Collectors.toUnmodifiableSet());
                }
                log.debug("Loaded selected contacts {} from localStorage", selectedContactIds);
                if (selectedContactIds.isEmpty()) {
                    grid.deselectAll();
                } else {
                    grid.asMultiSelect().select(contactRepository.findAllById(selectedContactIds));
                }
            } finally {
                loadingSelection = false;
            }
        });

        loadingSortOrder = true;
        WebStorage.getItem(WebStorage.Storage.localStorage, STORAGE_ITEM_SORT, value -> {
            try {
                if (value == null) {
                    sortOrder = Collections.emptyList();
                } else {
                    sortOrder = Stream.of(value.split(",")).filter(this::isValidSortOrder).toList();
                }
                log.debug("Loaded sort order {} from localStorage", sortOrder);
                if (sortOrder.isEmpty()) {
                    grid.sort(null);
                } else {
                    grid.sort(sortOrder.stream().flatMap(sortOrderString -> toSortOrder(grid, sortOrderString)).toList());
                }
            } finally {
                loadingSortOrder = false;
            }
        });
    }
}
