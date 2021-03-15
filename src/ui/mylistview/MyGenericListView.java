package ui.mylistview;

import data.MyModel;
import domain.UseCaseObservableList;
import genericlistview.AbstractGenericListView;
import ui.itemview.ItemController;
import ui.itemview.ItemView;

public class MyGenericListView
        extends AbstractGenericListView {

    private static final String TAG = "MyGenericListView" + ": ";

    private final UseCaseObservableList useCase;
    private final MyGenericListViewController listViewController;

    public MyGenericListView(MyGenericListViewController listViewController,
                             UseCaseObservableList useCase) {

        this.listViewController = listViewController;
        this.useCase = useCase;
        useCase.addModelListener(this);
    }

    /**
     * Called each time a {@link ViewHolder} is created,
     * whether for editing or displaying data.
     * @param index the index of item in the supplied data.
     * @return a {@link ViewHolder} containing the view.
     */
    @Override
    protected ViewHolder onCreateViewHolder(final int index) {

        MyModel model = useCase.getModels().get(index);

        ItemController itemController = new ItemController(
                listViewController, useCase, index
        );
        itemController.getView().bindModel(model);

        return new MyViewHolder(
                index,
                itemController.getModel(),
                itemController.getView(),
                itemController
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

    private static class MyViewHolder
            extends
            AbstractGenericListView.ViewHolder {

        @SuppressWarnings("unused")
        private static final String TAG = "MyViewHolder" + ": ";

        public MyViewHolder(int position,
                            MyModel model,
                            ItemView view,
                            ItemController controller) {

            super(position, model, view.getView(), controller);
        }

        @Override
        public Object getModel() {
            return ((ItemController)controller).getView().getModel();
        }

        @Override
        public void bindModel(Object model) {
            ((ItemView) view).bindModel(model);
        }
    }
}
