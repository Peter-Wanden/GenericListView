package main.itemview;

import utils.SpringUtilities;

import javax.swing.*;

public class FormView {

    static final int PADDING = 5;
    static final int START_POSITION = 10;
    static final int PREFERRED_HEIGHT = 100;

    private final JPanel view;

    private final JLabel
            firstNameLabel,
            lastNameLabel,
            ageLabel;

    private final JTextField
            firstNameField,
            lastNameField,
            ageField;

    FormView() {
        view = new JPanel(new SpringLayout());

        firstNameLabel = new JLabel("First name");
        lastNameLabel = new JLabel("Last name");
        ageLabel = new JLabel("Age");

        firstNameField = new JTextField();
        lastNameField = new JTextField();
        ageField = new JTextField();
    }

    public void createView() {
        view.setOpaque(true);

        JLabel[] labels = {
                firstNameLabel,
                lastNameLabel,
                ageLabel
        };

        JTextField[] fields = {
                firstNameField,
                lastNameField,
                ageField
        };

        int numberOfPairs = labels.length;
        for (int i = 0; i < numberOfPairs; i++) {
            labels[i].setHorizontalTextPosition(JLabel.TRAILING);
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

    public JTextField getFirstNameField() {
        return firstNameField;
    }

    public JTextField getLastNameField() {
        return lastNameField;
    }

    public JTextField getAgeField() {
        return ageField;
    }

    public JPanel getView() {
        return view;
    }
}
