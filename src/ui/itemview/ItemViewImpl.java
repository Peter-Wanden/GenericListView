package ui.itemview;

import data.MyModel;
import utils.RoundedPanel;
import utils.TextListener;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import static ui.mylistview.MyGenericListViewController.ControlCommand;

public class ItemViewImpl
        extends ItemView {

    @SuppressWarnings("unused")
    private static final String TAG = "ItemViewImpl" + ": ";

    private final FormView formView;
    private final ControlsView controlsView;
    private final JPanel view;

    private final ItemController controller;
    private final TextListener[] textListeners = new TextListener[3];
    private MyModel model;

    public ItemViewImpl(ItemController controller) {

        view = new RoundedPanel(30);
        view.setOpaque(true);
        controlsView = new ControlsView();
        formView = new FormView();

        this.controller = controller;

        addControlCommands();
        addViewListeners();
    }

    public void createView() {
        controlsView.createView();
        formView.createView();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerSize(0); // turns divider off
        splitPane.setBorder(null);
        splitPane.setTopComponent(formView.getView());
        splitPane.setBottomComponent(controlsView.getView());

        view.add(splitPane);
    }

    private void addControlCommands() {
        controlsView.getAddMemberButton().setActionCommand(
                ControlCommand.ADD_AS_MEMBER_COMMAND.name()
        );
        controlsView.getRemoveMemberButton().setActionCommand(
                ControlCommand.REMOVE_MEMBER_COMMAND.name()
        );
        controlsView.getDeleteButton().setActionCommand(
                ControlCommand.DELETE_RECORD_COMMAND.name()
        );
    }

// region view event listeners

    /**
     * Adds listeners interested in changes to values in the
     * views components.
     */
    private void addViewListeners() {
        addFormViewListeners();
        addControlListeners();
    }

    public void removeViewListeners() {
        removeFormViewListeners();
        removeControlListeners();
    }

    /**
     * Adds listeners interested in changes to values triggered by events in the
     * views (mostly text based) components.
     */
    private void addFormViewListeners() {

        TextListener firstName = new TextListener(formView.getFirstNameField());
        firstName.addTextChangedListener(controller);
        textListeners[0] = firstName;

        TextListener lastName = new TextListener(formView.getLastNameField());
        lastName.addTextChangedListener(controller);
        textListeners[1] = lastName;

        TextListener age = new TextListener(formView.getAgeField());
        age.addTextChangedListener(controller);
        textListeners[2] = age;
    }

    private void removeFormViewListeners() {
        for (TextListener textListener : textListeners) {
            textListener.removeTextChangedListener(controller);
        }
    }

    /**
     * Adds listeners that are interested in events triggered by
     * the views controls.
     */
    private void addControlListeners() {
        controlsView.getAddMemberButton().addActionListener(controller);
        controlsView.getRemoveMemberButton().addActionListener(controller);
        controlsView.getDeleteButton().addActionListener(controller);
    }

    private void removeControlListeners() {
        controlsView.getAddMemberButton().removeActionListener(controller);
        controlsView.getRemoveMemberButton().removeActionListener(controller);
        controlsView.getDeleteButton().removeActionListener(controller);
    }

// endregion view event listeners

    /**
     * Implements {@link ItemView}.
     * This is where the model is bound to the values in the view.
     *
     * @param model the model with values to be bound.
     */
    public void bindModel(Object model) {
        MyModel newModel;
        MyModel oldModel;

        newModel = model == null ? new MyModel() : (MyModel) model;
        oldModel = this.model == null ? new MyModel() : this.model;

        if (!oldModel.equals(newModel)) {
            this.model = newModel;

            boolean firstNameChanged = !newModel.getFirstName().equals(oldModel.getFirstName());
            if (firstNameChanged) {
                updateTextField(
                        newModel.getFirstName(),
                        formView.getFirstNameField(),
                        textListeners[0]
                );
            }

            boolean lastNameChanged = !newModel.getLastName().equals(oldModel.getLastName());
            if (lastNameChanged) {
                updateTextField(
                        newModel.getLastName(),
                        formView.getLastNameField(),
                        textListeners[1]
                );
            }

            boolean ageChanged = newModel.getAge() != oldModel.getAge();
            if (ageChanged) {
                updateTextField(
                        String.valueOf(newModel.getAge()),
                        formView.getAgeField(),
                        textListeners[2]
                );
            }

            boolean isMemberChanged = oldModel.isMember() != newModel.isMember();
            if (isMemberChanged) {
                controlsView.getIsMemberCheckBox().setSelected(newModel.isMember());
            }

            System.out.println(TAG + "bindModel:" + " model=" + model);
        }
    }

    private void updateTextField(String newText,
                                 JTextComponent component,
                                 TextListener listener) {

        int caretPosition = Math.min(
                newText.length(), component.getCaretPosition()
        );
        Document document = component.getDocument();

        document.removeDocumentListener(listener);
        document = new PlainDocument();
        try {
            document.insertString(0, newText, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        component.setDocument(document);
        component.setCaretPosition(caretPosition);
        document.addDocumentListener(listener);
    }

    @Override
    public int getIndex() {
        return controller.getIndex();
    }

    /**
     * Implements {@link ItemView}.
     * Gets the current values from the views controls.
     *
     * @return the current values in the views controls
     */
    @Override
    public MyModel getModel() {
        return new MyModel(
                formView.getFirstNameField().getText(),
                formView.getLastNameField().getText(),

                formView.getAgeField().getText().equals("") ?
                        0 :
                        Integer.parseInt(formView.getAgeField().getText()),

                controlsView.getIsMemberCheckBox().isSelected()
        );
    }

    /**
     * Implements {@link ItemView}.
     *
     * @return the view
     */
    @Override
    public JPanel getView() {
        return view;
    }

    public ItemController getController() {
        return controller;
    }
}
