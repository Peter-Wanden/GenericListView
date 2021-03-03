package main;

import utils.RoundedPanel;
import utils.SpringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestView
        implements ActionListener {

    public static class FormView {

        public static final int PREFERRED_HEIGHT = 100;
        public static final int START_POSITION = 10;
        public static final int PADDING = 5;

        private final JPanel view;

        protected final JTextField
                firstNameField = new JTextField(),
                lastNameField = new JTextField(),
                ageField = new JTextField();

        public FormView() {
            view = new JPanel(new SpringLayout());
            view.setOpaque(true);

            JLabel firstNameLabel = new JLabel("First name");
            JLabel lastNameLabel = new JLabel("Last name: ");
            JLabel ageLabel = new JLabel("Age: ");
            JLabel[] labels = {
                    firstNameLabel, lastNameLabel, ageLabel
            };

            int numberOfPairs = labels.length;
            for (int i = 0; i < numberOfPairs; i++) {
                labels[i].setHorizontalTextPosition(JLabel.TRAILING);
                JTextField[] fields = {
                        firstNameField, lastNameField, ageField
                };
                labels[i].setLabelFor(fields[i]);

                view.add(labels[i]);
                view.add(fields[i]);
            }

            // Layout the panel
            int numberOfColumns = 2;
            SpringUtilities.makeCompactGrid(
                    view, // the container
                    numberOfPairs, // no of rows
                    numberOfColumns, // no of columns
                    START_POSITION, // x position
                    START_POSITION, // y position
                    PADDING, // x padding
                    PADDING // y padding
            );
        }

        public JPanel getView() {
            return view;
        }
    }

    public static class ControlsView {

        public static final int PREFERRED_HEIGHT = 50;

        public static final String ADD_MEMBER_COMMAND = "ADD";
        public static final String REMOVE_MEMBER_COMMAND = "REMOVE";

        private final JPanel view;
        private final JCheckBox isMemberCheckBox;
        private final JButton addMemberButton, removeMemberButton;

        public ControlsView() {

            view = new JPanel();
            view.setOpaque(true);

            view.setLayout(
                    new BoxLayout(view, BoxLayout.LINE_AXIS)
            );

            isMemberCheckBox = new JCheckBox("Is member");
            isMemberCheckBox.setEnabled(false);
            view.add(isMemberCheckBox);

            addMemberButton = new JButton("Add member");
            addMemberButton.setActionCommand(ADD_MEMBER_COMMAND);
            view.add(addMemberButton);

            removeMemberButton = new JButton("Remove member");
            removeMemberButton.setActionCommand(REMOVE_MEMBER_COMMAND);
            view.add(removeMemberButton);
        }

        public JPanel getView() {
            return view;
        }

        public JCheckBox getIsMemberCheckBox() {
            return isMemberCheckBox;
        }
    }

    public static final int PREFERRED_HEIGHT =
            FormView.PREFERRED_HEIGHT + ControlsView.PREFERRED_HEIGHT;

    private static final int PREFERRED_WIDTH = 200;

    private final JPanel view;
    private final FormView formView;
    private final ControlsView controlsView;

    public TestView() {
        formView = new FormView();
        controlsView = new ControlsView();
        controlsView.addMemberButton.addActionListener(this);
        controlsView.removeMemberButton.addActionListener(this);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerSize(0); // turns divider off
        splitPane.setBorder(null);
        splitPane.setOpaque(true);
        splitPane.setTopComponent(formView.view);
        splitPane.setBottomComponent(controlsView.view);

        view = new RoundedPanel(30);
        view.setPreferredSize(new Dimension(
                PREFERRED_WIDTH,
                PREFERRED_HEIGHT
        ));

        view.add(splitPane);
        view.setOpaque(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (ControlsView.ADD_MEMBER_COMMAND.equals(actionCommand)) {
            controlsView.getIsMemberCheckBox().setSelected(true);
        }
        if (ControlsView.REMOVE_MEMBER_COMMAND.equals(actionCommand)) {
            controlsView.getIsMemberCheckBox().setSelected(false);
        }
    }

    public FormView getFormView() {
        return formView;
    }

    public ControlsView getControlsView() {
        return controlsView;
    }

    public JPanel getView() {
        return view;
    }
}
