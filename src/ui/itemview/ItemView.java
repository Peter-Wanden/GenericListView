package ui.itemview;

import data.MyModel;
import ui.itemview.ItemController.ControlCommand;
import utils.RoundedPanel;

import javax.swing.*;

public class ItemView {

    private static final String TAG = "ItemView" + ": ";

    public static final int CELL_HEIGHT =
            FormView.PREFERRED_HEIGHT + ControlsView.PREFERRED_HEIGHT;

    private final FormView formView;
    private final ControlsView controlsView;
    private final JPanel view;

    private final ItemController controller;
    private final TextListener[] textListeners = new TextListener[3];

    public ItemView(ItemController controller) {

        view = new RoundedPanel(30);
        view.setOpaque(true);
        controlsView = new ControlsView();
        formView = new FormView();

        this.controller = controller;

        addViewControlListeners();
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


    private void addViewControlListeners() {
        addFormViewListeners();
        addControlViewComponentListeners();
    }

    private void removeViewControlListeners() {
        removeFormViewListeners();
        removeControlListeners();
    }

    // TODO - DON'T USE DOCUMENT LISTENERS, USE DOCUMENT FILTERS!
    //  SEE: https://stackoverflow.com/questions/7439455/document-model-in-java-gui
    //
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

    private void addControlViewComponentListeners() {
        controlsView.getAddMemberButton().setActionCommand(
                ControlCommand.ADD_MEMBER_COMMAND.name()
        );
        controlsView.getAddMemberButton().addActionListener(
                controller
        );

        controlsView.getRemoveMemberButton().setActionCommand(
                ControlCommand.REMOVE_MEMBER_COMMAND.name()
        );
        controlsView.getRemoveMemberButton().addActionListener(
                controller
        );

        controlsView.getDeleteButton().setActionCommand(
                ControlCommand.DELETE_RECORD_COMMAND.name()
        );
        controlsView.getDeleteButton().addActionListener(
                controller
        );
    }

    private void removeControlListeners() {
        controlsView.getAddMemberButton().removeActionListener(controller);
        controlsView.getRemoveMemberButton().removeActionListener(controller);
        controlsView.getDeleteButton().removeActionListener(controller);
    }

    /**
     * Gets the current values from the views controls.
     *
     * @return the current values in the views controls
     */
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

    public void bindModel(MyModel myModel) {
        removeViewControlListeners();
        formView.getFirstNameField().setText(myModel.getFirstName());
        formView.getLastNameField().setText(myModel.getLastName());
        formView.getAgeField().setText(String.valueOf(myModel.getAge()));
        controlsView.getIsMemberCheckBox().setSelected(myModel.isMember());
        addViewControlListeners();
    }

    public JPanel getView() {
        return view;
    }
}
