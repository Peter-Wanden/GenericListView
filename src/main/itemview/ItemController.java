package main.itemview;

import main.data.MyModel;
import main.listview.MyGenericListViewController;

import java.util.ArrayList;
import java.util.List;

/**
 * ItemController reports on all changes within the view.
 * ItemController could perform input validation etc.
 * ItemController reports changes to ListController
 */
public class ItemController {

    private static final String TAG = "ItemController" + ": ";

    public enum FieldName {
        FIRST_NAME,
        LAST_NAME,
        AGE
    }

    public enum ControlCommand {
        ADD_MEMBER_COMMAND,
        REMOVE_MEMBER_COMMAND,
        DELETE_RECORD_COMMAND
    }

    public interface FieldChangedListener {
        void fieldChanged(int index,
                          FieldName fieldName,
                          String value
        );
    }

    public interface ControlActionListener {
        void addMember(int index);

        void removeMember(int index);

        void deleteRecord(int index);
    }

    private final List<FieldChangedListener> fieldChangedListeners;
    private final List<ControlActionListener> controlActionListeners;

    private final ItemView view;
    private final int index;

    public ItemController(MyGenericListViewController controller,
                          final int index) {

        fieldChangedListeners = new ArrayList<>();
        controlActionListeners = new ArrayList<>();

        this.index = index;

        view = new ItemView(this);
        view.createView();

        addFieldChangedListener(controller);
        addControlActionListener(controller);
    }

    public void firstNameChanged(String firstName) {
        notifyFieldChangedListeners(FieldName.FIRST_NAME, firstName);
    }

    public void lastNameChanged(String lastName) {
        notifyFieldChangedListeners(FieldName.LAST_NAME, lastName);
    }

    public void ageChanged(String age) {
        notifyFieldChangedListeners(FieldName.AGE, age);
    }

    public void actionPerformed(String actionCommand) {
        notifyControlActionListeners(actionCommand);
    }

    public ItemView getView() {
        return view;
    }

    /**
     * Delegates the collection of the views current components states to
     * the view.
     * @return the model who's values are the current values in the view.
     */
    public MyModel getModel() {
        return view.getModel();
    }

    public void addFieldChangedListener(FieldChangedListener listener) {
        fieldChangedListeners.add(listener);
    }

    public void removeFieldChangedListener(FieldChangedListener listener) {
        fieldChangedListeners.remove(listener);
    }

    private void notifyFieldChangedListeners(FieldName fieldName,
                                             String value) {

        fieldChangedListeners.forEach(listener ->
                listener.fieldChanged(index, fieldName, value)
        );
    }

    public void addControlActionListener(ControlActionListener listener) {
        controlActionListeners.add(listener);
    }

    public void removeControlActionListener(ControlActionListener listener) {
        controlActionListeners.remove(listener);
    }

    private void notifyControlActionListeners(String actionCommand) {
        if (ControlCommand.ADD_MEMBER_COMMAND.name().equals(actionCommand)) {
            controlActionListeners.forEach(listener -> listener.addMember(index));
        }
        else if (ControlCommand.REMOVE_MEMBER_COMMAND.name().equals(actionCommand)) {
            controlActionListeners.forEach(listener -> listener.removeMember(index));
        }
        else if (ControlCommand.DELETE_RECORD_COMMAND.name().equals(actionCommand)) {
            controlActionListeners.forEach(listener -> listener.deleteRecord(index));
        }
    }
}
