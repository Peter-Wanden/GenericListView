package ui.itemview;

import data.MyModel;
import domain.FieldChangedListener;
import domain.UseCaseObservableList;
import domain.UseCaseObservableList.FieldName;
import ui.mylistview.MyGenericListViewController;
import utils.TextListener;

import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static ui.mylistview.MyGenericListViewController.ControlCommand;

/**
 * ItemController reports on all changes within the view.
 * ItemController could perform input validation etc.
 * ItemController reports changes to ListController
 */
public class ItemController
        implements
        ActionListener,
        TextListener.TextChangedListener {

    @SuppressWarnings("unused")
    private static final String TAG = "ItemController" + ": ";

    private final List<FieldChangedListener> fieldChangedListeners;

    private final UseCaseObservableList useCase;
    private final MyGenericListViewController listViewController;
    private final ItemView view;
    private final int index;

    public ItemController(MyGenericListViewController listViewController,
                          UseCaseObservableList useCase,
                          int index) {

        fieldChangedListeners = new ArrayList<>();

        this.listViewController = listViewController;
        this.index = index;
        this.useCase = useCase;

        view = new ItemViewImpl(this, useCase);
        ((ItemViewImpl) view).createView();

        addFieldChangedListener(listViewController);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (ControlCommand.ADD_AS_MEMBER_COMMAND.name().equals(command)) {
            listViewController.addMembership(index);
        } else if (ControlCommand.REMOVE_MEMBER_COMMAND.name().equals(command)) {
            listViewController.removeMembership(index);
        } else if (ControlCommand.DELETE_RECORD_COMMAND.name().equals(command)) {
            ((ItemViewImpl) view).removeViewListeners();
            removeFieldChangedListener(listViewController);
            listViewController.deleteMember(index);
        }
    }

    @Override
    public void textChanged(JTextComponent source) {
        MyModel newValue = useCase.getModels().get(index);

        String newText = source.getText();
        String componentName = source.getName();

        if (FieldName.FIRST_NAME.name().equals(componentName) &&
                !newValue.getFirstName().equals(newText)) {
            notifyFieldChangedListeners(FieldName.FIRST_NAME, newText);

        } else if (FieldName.LAST_NAME.name().equals(componentName) &&
                !newValue.getLastName().equals(newText)) {
            notifyFieldChangedListeners(FieldName.LAST_NAME, newText);

        } else if (FieldName.AGE.name().equals(componentName) &&
                !String.valueOf(newValue.getAge()).equals(newText)) {
            notifyFieldChangedListeners(FieldName.AGE, newText);
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

    private void notifyFieldChangedListeners(FieldName fieldName,
                                             String value) {

        fieldChangedListeners.forEach(listener ->
                listener.fieldChanged(index, fieldName, value)
        );
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
        return (MyModel) view.getModel();
    }

    public int getIndex() {
        return index;
    }
}
