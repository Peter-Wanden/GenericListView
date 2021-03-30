package ui.mylistview;

import data.MyModel;
import domain.UseCaseObservableList;
import genericlistview.AbstractGenericListView;
import ui.itemview.ItemController;
import ui.itemview.ItemView;
import ui.itemview.ItemViewImpl;
import ui.mylistview.MyGenericListView.MyViewHolder;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class MyGenericListView
        extends AbstractGenericListView<MyViewHolder> {

    @SuppressWarnings("unused")
    private static final String TAG = "MyGenericListView" + ": ";

    private final boolean isLogging = false;

    protected class MyViewHolder
            extends ViewHolder {

        @SuppressWarnings("unused")
        private static final String TAG = "MyViewHolder" + ": ";

        private final ItemView itemView;

        public MyViewHolder(ItemController itemController) {
            super(itemController.getView().getView());
            this.itemView = itemController.getView();
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
            if (isLogging)
                System.out.println(TAG + "editingStopped: removing listeners");
//            ((ItemViewImpl) itemView).removeViewListeners();
        }
    }

    private final UseCaseObservableList useCase;
    private final MyGenericListViewController listViewController;

    public MyGenericListView(UseCaseObservableList useCase,
                             MyGenericListViewController listViewController) {

        this.useCase = useCase;
        this.listViewController = listViewController;
    }

    @Override
    protected MyViewHolder onCreateViewHolder(int index,
                                              boolean isEditor) {

        if (isLogging)
            System.out.println(TAG + "onCreateViewHolder: " + "for index: " + index + " isEditor=" + isEditor);

        MyModel model = useCase.getModels().get(index);
        ItemController controller = new ItemController(
                listViewController, useCase, index
        );
        ItemViewImpl view = (ItemViewImpl) controller.getView();
        view.bindModel(model);

        return new MyViewHolder(controller);
    }

    /**
     * Called when a view is being reused for a specific index in the data.
     * Check the model has not changed since the view was last used, if it has
     * update the model form the source data and rebind to the view. If the
     * model has not changed, just return the <code>ViewHolder</code> as is.
     *
     * @param index      The index of the item within the data set.
     * @param isEditor   Tells the implementer if the view returned should
     * @param viewHolder A recycled <code>ViewHolder</code>
     */
    @Override
    protected void onBindViewHolder(int index,
                                    boolean isEditor,
                                    MyViewHolder viewHolder) {

        if (isLogging)
            System.out.println(TAG + "onBindViewHolder: " + "for index: " + index + " isEditor=" + isEditor);

        MyModel newModel = useCase.getModels().get(index);
        MyModel oldModel = (MyModel) viewHolder.getModel();

        if (!newModel.equals(oldModel)) {
            // todo don't forget to update the values in the controller!!!
            ItemViewImpl view = (ItemViewImpl)viewHolder.itemView;
            view.removeViewListeners();
            view.bindModel(newModel);

            ItemController controller = view.getController();
            controller.setIndex(index);
        }

        if (isLogging)
            recyclableViews.forEach(vh ->
                System.out.println(TAG + "onBindViewHolder:" +
                        " recyclable view=" +
                        " view holder at:" +
                        " hasModel=" + vh.getModel()
                )
        );
    }

    /**
     * Called by the list model (TableModel) while rendering views. The model
     * returned by this method is passed into the renderer for the cell. The
     * renderer will then look for a <code>ViewHolder</code> that shares the
     * same index in its <code>HashTable</code> of recyclable views. If the
     * view for this index has been rendered before its <code>ViewHolder</code>
     * will be retrieved and passed to
     * {@link #onBindViewHolder(int, boolean, MyViewHolder)} for reprocessing.
     * If the view has not been rendered before the model will be passed to
     * {@link #onCreateViewHolder(int, boolean)} so that a new
     * <code>ViewHolder</code> can be constructed.
     *
     * @param index the index of the item in the supplied data.
     * @return returns the data model representing the index.
     */
    @Override
    protected Object getValueAt(int index) {
        int size = useCase.getItemCount();

        // edge case: There are no elements in the list
        // Return an empty object.
        if (useCase.getModels().isEmpty()) {
            return new Object();
        }

        // edge case: The last element is not available, it may have been
        // deleted.
        // Return the new index of the last element.
        if (index >= size) {
            index = size - 1;
        }

//        if (isLogging) System.out.println(
//                TAG + "getValueAt: index=" + index + " useCase.getModels called. Models size=" + useCase.getModels().size()
//        );
        return useCase.getModels().get(index);
    }

    /**
     * Called post editing the views in the current ViewHolder.
     *
     * @param index the index of the item in the list model
     * @param model the model with updated values form the display controls.
     *              This is the model as returned by
     *              {@link ViewHolder#getModel()}
     */
    @Override
    protected void setValueAt(int index,
                              Object model) {

        MyModel updatedModel = (MyModel) model;
        useCase.updateModel(index, updatedModel);

        if (isLogging)
            System.out.println(
                TAG + "setValueAt:" + " updatedModel=" + updatedModel
        );
    }

    @Override
    protected int getItemCount() {
        return useCase.getItemCount();
    }
}
