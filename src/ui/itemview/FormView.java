package ui.itemview;

import domain.UseCaseObservableList.FieldName;
import utils.SpringUtilities;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class FormView {

    @SuppressWarnings("unused")
    private static final String TAG = "FormView" + ": ";

    public static final int PADDING = 5;
    public static final int START_POSITION = 10;
    public static final int TEXT_LENGTH = 20;

    private final JPanel view;

    private final JLabel
            firstNameLabel,
            lastNameLabel,
            ageLabel;

    private final JTextField
            firstNameField,
            lastNameField,
            ageField;

    public FormView() {
        view = new JPanel(new SpringLayout());

        firstNameLabel = new JLabel("First name");
        firstNameField = new JTextField(TEXT_LENGTH);
        firstNameField.setName(FieldName.FIRST_NAME.name());

        lastNameLabel = new JLabel("Last name");
        lastNameField = new JTextField(TEXT_LENGTH);
        lastNameField.setName(FieldName.LAST_NAME.name());

        ageLabel = new JLabel("Age");
        ageField = new JTextField(TEXT_LENGTH);
        ageField.setName(FieldName.AGE.name());
    }

    public void createView() {

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
