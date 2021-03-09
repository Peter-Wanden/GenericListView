package main.listview;

import main.data.MyModel;
import main.domain.UseCase;
import main.itemview.ItemController;
import main.itemview.ItemController.FieldName;

public class MyGenericListViewController
        implements
        ItemController.ControlActionListener,
        ItemController.FieldChangedListener {

    private static final String TAG = "MyGenericListViewController" + ": ";

    private final UseCase useCase;
    private final AbstractGenericListView listView;

    public MyGenericListViewController(UseCase useCase) {

        this.useCase = useCase;
        listView = new MyGenericListView(this, useCase);
    }

    /**
     * Provides character by character fine granularity feedback of the values
     * in the views fields as they change. Implements
     * {@link ItemController.FieldChangedListener}. Called whenever there
     * is a change to one of the fields values.
     * @param index the index of the {@link MyModel} in the list.
     * @param fieldName an {@link FieldName} enum value describing the field.
     * @param value the current value of the field.
     */
    @Override
    public void fieldChanged(int index,
                             ItemController.FieldName fieldName,
                             String value) {

        switch (fieldName) {
            case FIRST_NAME -> useCase.firstNameFieldChanged(index, value);
            case LAST_NAME -> useCase.lastNameChanged(index, value);
            case AGE -> useCase.ageChanged(index, value);
        }
    }

    @Override
    public void addMember(int index) {
        useCase.addMember(index);
    }

    @Override
    public void removeMember(int index) {
        useCase.removeMember(index);
    }

    @Override
    public void deleteRecord(int index) {
        useCase.deleteModel(index);
    }

    public void createNewRecord() {
        useCase.insertModel(new MyModel());
    }

    public UseCase getUseCase() {
        return useCase;
    }

    public AbstractGenericListView getView() {
        return listView;
    }
}
