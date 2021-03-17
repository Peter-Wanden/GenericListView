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
    private static final String TAG = "ItemView" + ": ";

    private final FormView formView;
    private final ControlsView controlsView;
    private final JPanel view;

    private final UseCaseObservableList useCase;
    private final ItemController controller;
    private final TextListener[] textListeners = new TextListener[3];
    private final int index;
    private MyModel model;

    public ItemViewImpl(ItemController controller,
                        UseCaseObservableList useCase) {
        super();

        view = new RoundedPanel(30);
        view.setOpaque(true);
        controlsView = new ControlsView();
        formView = new FormView();

        this.useCase = useCase;
        this.controller = controller;
        this.index = controller.getIndex();

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

    // region model changed listeners
    /*
    This section is where the view listens and reacts to changes in the
    model, which in this case is the use case.
    */

    /**
     * Implements {@link ModelListenerAdapter}'. This item is listening for
     * updates to the model supplied by the domain {@link UseCaseObservableList}.
     *
     * @param index the index of the item in the data model that has changed.
     */
    @Override
    public void notifyItemUpdated(int index) {
        if (this.index == index) {
            // if the model has changed, update it
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

        controlsView.getAddMemberButton().setActionCommand(
                ControlCommand.ADD_AS_MEMBER_COMMAND.name()
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

// endregion view event listeners

    /**
     * Implements {@link ItemView}.
     * Gets the index of the model this view represents.
     *
     * @return the index of the model in the source data this view
     * represents.
     */
    @Override
    public int getIndex() {
        return index;
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
     * This is where the model is bound to the values in the view.
     *
     * @param model the model with values to be bound.
     */
    @Override
    public void bindModel(Object model) {
        if (this.model == null) this.model =  new MyModel();

        if (!this.model.equals(model)) {
            this.model = (MyModel) model;

        System.out.println(TAG + "bindModel:" + " model=" + model);

            String oldFirstName = formView.getFirstNameField().getText();
            String newFirstName = this.model.getFirstName();
            if (!oldFirstName.equals(newFirstName)) {
                formView.getFirstNameField().setText(newFirstName);
            }

            String oldLastName = formView.getLastNameField().getText();
            String newLastName = this.model.getLastName();
            if (!oldLastName.equals(newLastName)) {
                formView.getLastNameField().setText(newLastName);
            }

            String oldAge = formView.getAgeField().
                    getText().isEmpty() ? "0" :
                    formView.getAgeField().getText();
            String newAge = String.valueOf(this.model.getAge());
            if (!oldAge.equals(newAge)) {
                formView.getAgeField().setText(newAge);
            }

            boolean oldIsMember = controlsView.getIsMemberCheckBox().isSelected();
            boolean newIsMember = this.model.isMember();
//        System.out.println(TAG + "bindModel:" + " oldIsMember=" + oldIsMember + " newIsMember=" + newIsMember);
            if (!oldIsMember == newIsMember) {
//            System.out.println(TAG + "bindModel: membershipHasChanged");
                controlsView.getIsMemberCheckBox().setSelected(newIsMember);
            }
        }
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
