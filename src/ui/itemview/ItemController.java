package ui.itemview;

import data.MyModel;
import ui.mylistview.MyGenericListViewController;

import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * ItemController reports on all changes within the view.
 * ItemController could perform input validation etc.
 * ItemController reports changes to ListController
 */
public class ItemController
        implements
        ActionListener,
        TextListener.TextChangedListener {

    private static final String TAG = "ItemController" + ": ";

    public enum ControlCommand {
        ADD_MEMBER_COMMAND,
        REMOVE_MEMBER_COMMAND,
        DELETE_RECORD_COMMAND
    }

    public interface FieldChangedListener {
        void fieldChanged(int index,
                          FormView.FieldName fieldName,
                          String value
        );
    }

    public interface ControlActionListener {
        void addMembership(int index);

        void removeMembership(int index);

        void deleteModel(int index);
    }

    private final List<FieldChangedListener> fieldChangedListeners;
    private final List<ControlActionListener> controlActionListeners;

    private final MyGenericListViewController parentController;
    private final ItemView view;
    private final int index;

    public ItemController(MyGenericListViewController parentController,
                          final int index) {

        fieldChangedListeners = new ArrayList<>();
        controlActionListeners = new ArrayList<>();

        this.parentController = parentController;
        this.index = index;

        view = new ItemView(this);
        view.createView();

        addFieldChangedListener(parentController);
        addControlActionListener(parentController);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        notifyControlActionListeners(e.getActionCommand());
    }

    @Override
    public void textChanged(JTextComponent source) {

        String newText = source.getText();
        String componentName = source.getName();

        if (FormView.FieldName.FIRST_NAME.name().equals(componentName)) {
            notifyFieldChangedListeners(FormView.FieldName.FIRST_NAME, newText);
        } else if (FormView.FieldName.LAST_NAME.name().equals(componentName)) {
            notifyFieldChangedListeners(FormView.FieldName.LAST_NAME, newText);
        } else if (FormView.FieldName.AGE.name().equals(componentName)) {
            notifyFieldChangedListeners(FormView.FieldName.AGE, newText);
        } else {
            throw new UnsupportedOperationException(
                    "Source: " + source + " unknown for text: " + newText
            );
        }
    }

    public void addFieldChangedListener(FieldChangedListener listener) {
        fieldChangedListeners.add(listener);
    }

    public void removeFieldChangedListener(FieldChangedListener listener) {
        fieldChangedListeners.remove(listener);
    }

    private void notifyFieldChangedListeners(FormView.FieldName fieldName,
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
            controlActionListeners.forEach(listener -> listener.addMembership(index));
        } else if (ControlCommand.REMOVE_MEMBER_COMMAND.name().equals(actionCommand)) {
            controlActionListeners.forEach(listener -> listener.removeMembership(index));
        } else if (ControlCommand.DELETE_RECORD_COMMAND.name().equals(actionCommand)) {
            controlActionListeners.forEach(listener -> listener.deleteModel(index));
        }
    }

    public ItemView getView() {
        return view;
    }

    /**
     * Delegates the collection of the views current components states to
     * the view.
     *
     * @return the model who's values are the current values in the view.
     */
    public MyModel getModel() {
        return view.getModel();
    }

    public int getIndex() {
        return index;
    }
}
