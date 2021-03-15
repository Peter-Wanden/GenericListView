package ui.itemview;

import data.MyModel;
import domain.UseCaseObservableList;
import genericlistview.ModelListenerAdapter;
import utils.RoundedPanel;
import utils.TextListener;

import javax.swing.*;

import static ui.mylistview.MyGenericListViewController.*;

public class ItemViewImpl
        extends ModelListenerAdapter
        implements ItemView {

    private static final String TAG = "ItemView" + ": ";

    private final FormView formView;
    private final ControlsView controlsView;
    private final JPanel view;

    private final UseCaseObservableList useCase;
    private final ItemController controller;
    private final TextListener[] textListeners = new TextListener[3];
    private final int index;

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
        addModelListeners();
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
     * Adds listeners interested in changes to the data model.
     */
    private void addModelListeners() {
        useCase.addModelListener(this);
    }

    /**
     * Implements {@link ModelListenerAdapter}'. This item is listening for
     * updates to the model supplied by the domain {@link UseCaseObservableList}.
     * @param index the index of the item in the data model that has changed.
     */
    @Override
    public void notifyItemUpdated(int index) {
        System.out.println(TAG + "notifyItemUpdated:" + " with index=" + index +
                " with model=" + useCase.getModels().get(index));
        if (this.index == index) {
            // if the model has changed, update it
            System.out.println(TAG + "notifyItemUpdated. This model with index=" + index + " has changed");
            System.out.println(TAG + "notifyItemUpdated. pulling model from useCase=" + useCase.getModels().get(index));
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

    private void removeViewListeners() {
        removeFormViewListeners();
        removeControlListeners();
    }

    // TODO - DON'T USE DOCUMENT LISTENERS, USE DOCUMENT FILTERS!
    //  SEE: https://stackoverflow.com/questions/7439455/document-model-in-java-gui
    //


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
     * @param model the model with values to be bound.
     */
    @Override
    public void bindModel(Object model) {
        MyModel myModel = (MyModel) model;
        System.out.println(TAG + "bindModel:" + " model=" + myModel);
        removeFormViewListeners();
        formView.getFirstNameField().setText(myModel.getFirstName());
        formView.getLastNameField().setText(myModel.getLastName());
        formView.getAgeField().setText(String.valueOf(myModel.getAge()));
        controlsView.getIsMemberCheckBox().setSelected(myModel.isMember());
        addFormViewListeners();
    }

    /**
     * Implements {@link ItemView}.
     * @return the view
     */
    @Override
    public JPanel getView() {
        return view;
    }
}
