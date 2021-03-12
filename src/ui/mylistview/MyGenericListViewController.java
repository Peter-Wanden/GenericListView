package ui.mylistview;

import domain.UseCaseObservableList;
import genericlistview.AbstractGenericListView;
import data.MyModel;
import ui.itemview.FormView;
import ui.itemview.ItemController;

public class MyGenericListViewController
        implements
        ItemController.ControlActionListener,
        ItemController.FieldChangedListener {

    private static final String TAG = "MyGenericListViewController" + ": ";

    private final UseCaseObservableList useCase;
    private final AbstractGenericListView listView;

    public MyGenericListViewController(UseCaseObservableList useCase) {

        this.useCase = useCase;
        listView = new MyGenericListView(this, useCase);
    }

    /**
     * Provides character by character fine granularity feedback of the values
     * in the views fields as they change. Implements
     * {@link ItemController.FieldChangedListener}. Called whenever there
     * is a change to one of the fields values.
     * @param index the index of the {@link MyModel} in the list.
     * @param fieldName an {@link FormView.FieldName} enum value describing the field.
     * @param value the current value of the field.
     */
    @Override
    public void fieldChanged(int index,
                             FormView.FieldName fieldName,
                             String value) {

        switch (fieldName) {
            case FIRST_NAME -> useCase.firstNameFieldChanged(index, value);
            case LAST_NAME -> useCase.lastNameChanged(index, value);
            case AGE -> useCase.ageChanged(index, value);
        }
    }

    /**
     * Implements {@link ItemController.ControlActionListener}.
     * @param index the index of the object in the model
     */
    @Override
    public void addMembership(int index) {
        useCase.addMembership(index);
    }

    /**
     * Implements {@link ItemController.ControlActionListener}.
     * @param index the index of the object in the model
     */
    @Override
    public void removeMembership(int index) {
        useCase.removeMembership(index);
    }

    /**
     * Implements {@link ItemController.ControlActionListener}.
     * @param index the index of the object in the model
     */
    @Override
    public void deleteModel(int index) {
        useCase.deleteModel(index);
    }

    public UseCaseObservableList getUseCase() {
        return useCase;
    }

    public AbstractGenericListView getView() {
        return listView;
    }
  }
