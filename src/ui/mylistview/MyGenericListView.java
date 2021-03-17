package ui.mylistview;

import data.MyModel;
import domain.UseCaseObservableList;
import genericlistview.AbstractGenericListView;
import genericlistview.AbstractGenericListView.ViewHolder;
import ui.itemview.ItemController;
import ui.itemview.ItemView;

public class MyGenericListView
        extends AbstractGenericListView {

    private static final String TAG = "MyGenericListView" + ": ";

    private static class MyViewHolder
            extends
            AbstractGenericListView.ViewHolder {

        @SuppressWarnings("unused")
        private static final String TAG = "MyViewHolder" + ": ";

        private final ItemController controller;
        private final ItemView itemView;

        public MyViewHolder(final int index,
                            final ItemController controller,
                            final ItemView itemView) {

            super(index, itemView.getView());

            this.controller = controller;
            this.itemView = itemView;
        }

        @Override
        public Object getModel() {
            return itemView.getModel();
        }
    }

    private final UseCaseObservableList useCase;
    private final MyGenericListViewController listViewController;

    public MyGenericListView(MyGenericListViewController listViewController,
                             UseCaseObservableList useCase) {

        this.listViewController = listViewController;
        this.useCase = useCase;
        useCase.addModelListener(this);
    }

    /**
     * Called each time a {@link ViewHolder} is created to
     * display a view.
     * @param index the index of item in the supplied data.
     * @return a {@link ViewHolder} containing the view.
     */
    @Override
    protected ViewHolder onCreateViewHolder(final int index) {

        MyModel model = useCase.getModels().get(index);

        ItemController controller = new ItemController(
                listViewController, useCase, index
        );
        ItemView view = controller.getView();
        view.bindModel(model);

        return new MyViewHolder(
                index, controller, view
        );
    }

    /**
     * Called each time an {@link ViewHolder} is created to
     * edit a view.
     * @param index the index of the data model in the supplied data.
     * @return a {@link ViewHolder} containing the view.
     */
    @Override
    protected ViewHolder onCreateEditorViewHolder(int index) {

        MyModel model = useCase.getModels().get(index);

        ItemController controller = new ItemController(
                listViewController, useCase, index
        );

        ItemView view = controller.getView();
        view.bindModel(model);

        useCase.addModelListener(view);

        return new MyViewHolder(
                index, controller, view
        );
    }

    /**
     * Called by the list model (TableModel) while rendering views.
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

        // edge case: The last element has been deleted.
        // Return the new index of the last element.
        if (index >= size) {
            index = size -1;
        }

//        System.out.println(TAG + "getValueAt: position=" + position);
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

        System.out.println(
                TAG + "setValueAt:" + " updatedModel=" + updatedModel
        );
    }

    @Override
    protected int getItemCount() {
        return useCase.getItemCount();
    }
}
