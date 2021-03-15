package ui.itemview;

import javax.swing.*;

public class ControlsView {

    private final JPanel view;
    private final JCheckBox isMemberCheckBox;
    private final JButton
            addMemberButton,
            removeMemberButton,
            deleteButton;

    public ControlsView() {
        view = new JPanel();
        isMemberCheckBox = new JCheckBox("Is member");
        addMemberButton = new JButton("Add");
        removeMemberButton  = new JButton("Remove");
        deleteButton = new JButton("Delete");
    }

    public void createView() {
        view.setLayout(
                new BoxLayout(view, BoxLayout.LINE_AXIS)
        );

        isMemberCheckBox.setEnabled(false);
        view.add(isMemberCheckBox);
        view.add(addMemberButton);
        view.add(removeMemberButton);
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
