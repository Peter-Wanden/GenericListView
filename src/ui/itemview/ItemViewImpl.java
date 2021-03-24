package ui.itemview;

import data.MyModel;
import domain.UseCaseObservableList;
import genericlistview.ModelListenerAdapter;
import utils.RoundedPanel;
import utils.TextListener;

import javax.swing.*;

import static ui.mylistview.MyGenericListViewController.ControlCommand;

public class ItemViewImpl
        extends ItemView {

    @SuppressWarnings("unused")
    private static final String TAG = "ItemViewImpl" + ": ";

    private final FormView formView;
    private final ControlsView controlsView;
    private final JPanel view;

    private final UseCaseObservableList useCase;
    private final ItemController controller;
    private final TextListener[] textListeners = new TextListener[3];
    private MyModel model;

    public ItemViewImpl(ItemController controller,
                        UseCaseObservableList useCase) {

        view = new RoundedPanel(30);
        view.setOpaque(true);
        controlsView = new ControlsView();
        formView = new FormView();

        this.useCase = useCase;
        this.controller = controller;

        addViewListeners();
        addControlCommands();
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

// region model changed listeners

    /**
     * Implements {@link ModelListenerAdapter}'. This item is listening for
     * updates to the model supplied by the domain {@link UseCaseObservableList}.
     *
     * @param index the index of the item in the data model that has changed.
     */
    @Override
    public void notifyItemUpdated(int index) {
        if (controller.getIndex() == index) {
            // if the model has changed, update it
            System.out.println(TAG + "notifyItemUpdated=" + useCase.getModels().get(index));
            bindModel(useCase.getModels().get(index));
        }
    }
// endregion model changed listeners

// region view event listeners

    /**
     * Adds listeners interested in changes to values in the
     * views components that may change values in the data
     * model.
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

// endregion view event listeners

    /**
     * Implements {@link ItemView}.
     * This is where the model is bound to the values in the view.
     *
     * @param model the model with values to be bound.
     */
    @Override
    public void bindModel(Object model) {
        MyModel newModel;
        MyModel oldModel;

        newModel = model == null ? new MyModel() : (MyModel) model;
        oldModel = this.model == null ? new MyModel() : this.model;

        if (!oldModel.equals(newModel)) {
            removeViewListeners();

            this.model = newModel;

            boolean firstNameChanged = !newModel.getFirstName().equals(oldModel.getFirstName());
            if (firstNameChanged) {
                formView.getFirstNameField().setText(newModel.getFirstName());
            }

            boolean lastNameChanged = !newModel.getLastName().equals(oldModel.getLastName());
            if (lastNameChanged) {
                formView.getFirstNameField().setText(newModel.getLastName());
            }

            boolean ageChanged = newModel.getAge() != oldModel.getAge();
            if (ageChanged) {
                formView.getAgeField().setText(String.valueOf(newModel.getAge()));
            }

            boolean isMemberChanged = oldModel.isMember() != newModel.isMember();
            if (isMemberChanged) {
                controlsView.getIsMemberCheckBox().setSelected(newModel.isMember());
            }

            addViewListeners();
            System.out.println(TAG + "bindModel:" + " model=" + model);
        }
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
}
