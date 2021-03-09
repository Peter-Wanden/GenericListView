package main.itemview;

import main.data.MyModel;
import utils.RoundedPanel;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ItemView
        implements
        ActionListener,
        TextListener.TextChangedListener {

    private static final String TAG = "ItemView" + ": ";

    public static final int CELL_HEIGHT =
            FormView.PREFERRED_HEIGHT + ControlsView.PREFERRED_HEIGHT;

    private final FormView formView;
    private final ControlsView controlsView;
    private final JPanel view;

    private final ItemController controller;

    public ItemView(ItemController controller) {

        this.controller = controller;
        formView = new FormView();
        controlsView = new ControlsView();
        view = new RoundedPanel(30);

        addListeners();
    }

    public void createView() {
        controlsView.createView();
        formView.createView();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerSize(0); // turns divider off
        splitPane.setBorder(null);
        splitPane.setTopComponent(formView.getView());
        splitPane.setBottomComponent(controlsView.getView());

        view.setOpaque(false);
        view.add(splitPane);
    }

    private void addListeners() {
        TextListener firstName = new TextListener(
                formView.getFirstNameField()
        );
        firstName.addTextChangedListener(this);

        TextListener lastName = new TextListener(
                formView.getLastNameField()
        );
        lastName.addTextChangedListener(this);

        TextListener age = new TextListener(
                formView.getAgeField()
        );
        age.addTextChangedListener(this);

        controlsView.getAddMemberButton().addActionListener(this);
        controlsView.getRemoveMemberButton().addActionListener(this);
        controlsView.getDeleteButton().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        controller.actionPerformed(actionCommand);
    }

    @Override
    public void textChanged(JTextComponent source) {
        String changedText = source.getText();

        if (formView.getFirstNameField() == source) {
            controller.firstNameChanged(changedText);
        }
        else if (formView.getLastNameField() == source) {
            controller.lastNameChanged(changedText);//
        }
        else if (formView.getAgeField() == source) {
            controller.ageChanged(changedText);
        }
        else {
            throw new UnsupportedOperationException(
                    "Source: " + source + " unknown for text: " + changedText
            );
        }
    }

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
        formView.getFirstNameField().setText(myModel.getFirstName());
        formView.getLastNameField().setText(myModel.getLastName());
        formView.getAgeField().setText(String.valueOf(myModel.getAge()));
        controlsView.getIsMemberCheckBox().setSelected(myModel.isMember());
    }

    public JPanel getView() {
        return view;
    }
}
