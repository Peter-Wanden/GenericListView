package main.itemview;

import javax.swing.*;

public class ControlsView {

    static final int PREFERRED_HEIGHT = 50;

    private final JPanel view;
    private final JCheckBox isMemberCheckBox;
    private final JButton addMemberButton, removeMemberButton, deleteButton;

    ControlsView() {
        view = new JPanel();
        isMemberCheckBox = new JCheckBox("Is member");
        addMemberButton = new JButton("Add member");
        removeMemberButton  = new JButton("Remove member");
        deleteButton = new JButton("Delete record");
    }

    public void createView() {
        view.setOpaque(true);
        view.setLayout(
                new BoxLayout(view, BoxLayout.LINE_AXIS)
        );

        isMemberCheckBox.setEnabled(false);
        view.add(isMemberCheckBox);

        addMemberButton.setActionCommand(
                ItemController.ControlCommand.ADD_MEMBER_COMMAND.name()
        );
        view.add(addMemberButton);

        removeMemberButton.setActionCommand(
                ItemController.ControlCommand.REMOVE_MEMBER_COMMAND.name()
        );
        view.add(removeMemberButton);

        deleteButton.setActionCommand(
                ItemController.ControlCommand.DELETE_RECORD_COMMAND.name()
        );
        view.add(deleteButton);
    }

    public JPanel getView() {
        return view;
    }

    public JCheckBox getIsMemberCheckBox() {
        return isMemberCheckBox;
    }

    public JButton getAddMemberButton() {
        return addMemberButton;
    }

    public JButton getRemoveMemberButton() {
        return removeMemberButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }
}
