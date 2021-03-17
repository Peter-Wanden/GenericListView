package ui.mylistview;

import data.MyModel;
import domain.FieldChangedListener;
import domain.UseCaseObservableList;
import domain.UseCaseObservableList.FieldName;
import genericlistview.AbstractGenericListView;
import genericlistview.ControlActionListener;
import ui.itemview.ItemController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyGenericListViewController
        implements
        ControlActionListener,
        FieldChangedListener,
        ActionListener {

    @SuppressWarnings("unused")
    private static final String TAG = "MyGenericListViewController" + ": ";

    /**
     * These are the commands this controller can process. These commands are designed
     * to be attached to UI components, especially controls, using the following format:
     * <code>button.addActionCommand(ControlCommand.ADD_MEMBER_COMMAND.name())</code>
     * This controller can then be added as an {@link ActionListener} to the UI control
     * like so: <code>button.addActionListener(controller)</code>. Thereafter , each
     * time an {@link ActionEvent} occurs in the view the
     * {@link ItemController#actionPerformed(ActionEvent)} method is called and the
     * {@link ControlCommand} is delivered within its arguments.
     */
    public enum ControlCommand {
        ADD_AS_MEMBER_COMMAND,
        REMOVE_MEMBER_COMMAND,
        DELETE_RECORD_COMMAND,
        ADD_NEW_RECORD_COMMAND
    }

    private final UseCaseObservableList useCase;
    private final AbstractGenericListView listView;

    public MyGenericListViewController(UseCaseObservableList useCase) {

        this.useCase = useCase;
        listView = new MyGenericListView(this, useCase);
    }

    /**
     * Provides character by character fine granularity feedback of the values
     * in the views fields as they change. Implements
     * {@link FieldChangedListener}. Called whenever there
     * is a change to one of the fields values.
     * @param index the index of the {@link MyModel} in the list.
     * @param fieldName an {@link UseCaseObservableList.FieldName} enum value describing the field.
     * @param value the current value of the field.
     */
    @Override
    public void fieldChanged(int index,
                             FieldName fieldName,
                             Object value) {
        /*
         This is a good place to insert text validation etc.
         As it is not required in this toy app we'll just forward the
         event to the use case.
        */
        useCase.fieldChanged(
                index, fieldName, value
        );
    }

    /**
     * Implements {@link ActionListener}.
     * @param e the event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (ControlCommand.ADD_NEW_RECORD_COMMAND.name().equals(e.getActionCommand())) {
            useCase.addNewMember();
        }
    }

    /**
     * Implements {@link ControlActionListener}.
     * @param index the index of the object in the model
     */
    @Override
    public void addMembership(int index) {
        System.out.println(TAG + "addMembership: for index:" + index);
        useCase.addMembership(index);
    }

    /**
     * Implements {@link ControlActionListener}.
     * @param index the index of the object in the model
     */
    @Override
    public void removeMembership(int index) {
        useCase.removeMembership(index);
    }

    /**
     * Implements {@link ControlActionListener}.
     * @param index the index of the object in the model
     */
    @Override
    public void deleteMember(int index) {
        useCase.deleteModel(index);
    }

    public AbstractGenericListView getView() {
        return listView;
    }
}
