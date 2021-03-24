package ui.mylistview;

import data.MyModel;
import domain.UseCaseObservableList;
import genericlistview.AbstractGenericListView;
import ui.itemview.ItemController;
import ui.itemview.ItemView;
import ui.mylistview.MyGenericListView.MyViewHolder;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class MyGenericListView
        extends AbstractGenericListView<MyViewHolder> {

    @SuppressWarnings("unused")
    private static final String TAG = "MyGenericListView" + ": ";

    private final boolean isLogging = true;

    protected class MyViewHolder
            extends ViewHolder {

        @SuppressWarnings("unused")
        private static final String TAG = "MyViewHolder" + ": ";

        private ItemController itemController;
        private ItemView itemView;

        public MyViewHolder(ItemController itemController,
                            ItemView itemView) {

            super(itemView.getView());

            if (isLogging) System.out.println(
                    TAG + "MyViewHolder constructor called"
            );

            this.itemController = itemController;
            this.itemView = itemView;
        }

        /**
         * When editing of the view is complete, i.e
         * when {@link JTable#editingStopped(ChangeEvent)} method is triggered,
         * it calls this method to get the edited values. So this is where you
         * get the values from your view and perform any clean up operations,
         * such as removing any listeners the editor uses.
         *
         * @return the model values in the view
         */
        @Override
        public Object getModel() {
            return itemView.getModel();
        }

        /**
         * Called just before the Editor closes the view. Now is a good time
         * to clean things up, such as removing listeners and extracting any
         * values in the ui controls, etc.
         */
        @Override
        public void prepareEditingStopped() {
            if (isLogging) System.out.println(TAG + "editingStopped: removing listeners");
//            ((ItemViewImpl) itemView).removeViewListeners();
        }
    }

    private final UseCaseObservableList useCase;
    private final MyGenericListViewController listViewController;

    public MyGenericListView(UseCaseObservableList useCase,
                             MyGenericListViewController listViewController) {

        if (isLogging) System.out.println(TAG + "constructor called" +
                " listViewController=" + listViewController);

        this.useCase = useCase;
        this.listViewController = listViewController;
        this.useCase.addModelListener(this);
    }

    /**
     * Called once to get a ViewHolder for the editor component and once to get
     * the viewer component. See abstract method declaration comments for more
     * information.
     * @param isEditor true if the ViewHolder requested should contain the
     *                 editor view.
     * @return a {@link ViewHolder} containing the view.
     */
    @Override
    protected MyViewHolder onCreateViewHolder(boolean isEditor) {
        if (isLogging) System.out.println(TAG + "onCreateViewHolder: " + " isEditor=" + isEditor +
                " listViewController=" + listViewController);

        ItemController controller = new ItemController(listViewController, useCase, 0);

        return new MyViewHolder(controller, controller.getView());
    }

    @Override
    protected void onBindViewHolder(MyViewHolder viewHolder,
                                    int index,
                                    boolean isEditor) {

        if (isLogging) System.out.println(
                TAG + "onBindViewHolder: " + "listViewController=" + listViewController
        );

        MyModel model = useCase.getModels().get(index);
        viewHolder.itemController = new ItemController(listViewController, useCase, index);
        viewHolder.itemView = viewHolder.itemController.getView();
        viewHolder.itemView.bindModel(model);


        if (isLogging) System.out.println(
                TAG + "onBindViewHolder: " + " binding model:" + model
        );

        if (isEditor) useCase.addModelListener(viewHolder.itemView);

    }

    /**
     * Called by the list model (TableModel) while rendering views.
     * @param index the index of the item in the supplied data.
     * @return returns the data model representing the index.
     */
    @Override
    protected Object getValueAt(int index) {
        if (isLogging) System.out.println(TAG + "getValueAt: " + index +
                " getItemCount called. useCase.getModels called.");
        int size = useCase.getItemCount();

        // edge case: There are no elements in the list
        // Return an empty object.
        if (useCase.getModels().isEmpty()) {
            return new Object();
        }

        // edge case: The last element has been deleted.
        // Return the new index of the last element.
        if (index >= size) {
            index = size -1;
        }

        if (isLogging) System.out.println(
                TAG + "getValueAt: index=" + index + " useCase.getModels called"
        );
        return useCase.getModels().get(index);
    }

    /**
     * Called post editing the views controls in the current ViewHolder.
     * @param index the index of the item in the list model
     * @param model    the model with updated values form the
     *                 display controls.
     */
    @Override
    protected void setValueAt(int index,
                              Object model) {

        MyModel updatedModel = (MyModel) model;
        useCase.updateModel(index, updatedModel);

        if (isLogging) System.out.println(
                TAG + "setValueAt:" + " updatedModel=" + updatedModel
        );
    }

    @Override
    protected int getItemCount() {
        return useCase.getItemCount();
    }
}
