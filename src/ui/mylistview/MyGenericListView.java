package ui.mylistview;

import genericlistview.AbstractGenericListView;
import data.MyModel;
import domain.UseCaseObservableList;
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
     * Called each time a {@link genericlistview.AbstractGenericListView.ViewHolder} is created,
     * whether for editing or displaying data.
     * @param position the index of item in the supplied data.
     * @return a {@link genericlistview.AbstractGenericListView.ViewHolder} containing the view.
     */
    @Override
    protected ViewHolder onCreateViewHolder(int position) {

        ItemController itemController = new ItemController(listViewController, position);
        itemController.getView().bindModel(
                listViewController.getUseCase().getModels().get(position)
        );

        return new MyViewHolder(
                position,
                itemController.getModel(),
                itemController.getView(),
                itemController
        );
    }

    /**
     * Called by the list model (TableModel) while rendering views.
     *
     * @param position the index of the item in the supplied data.
     * @return returns the data model representing the index.
     */
    @Override
    protected Object getValueAt(int position) {
        int size = useCase.getItemCount();

        // edge case: There are no elements in the list
        // Return an empty object.
        if (useCase.getModels().isEmpty()) {
            return new Object();
        }

        // edge case: The last element has been deleted.
        // Return the new index of the last element.
        if (position >= size) {
            position = size -1;
        }

//        System.out.println(TAG + "getValueAt: position=" + position);
        return useCase.getModels().get(position);
    }

    /**
     * Called post editing the views controls in the current ViewHolder.
     *
     * @param position the index of the item in the list model
     * @param model    the model with updated values form the
     *                 display controls.
     */
    @Override
    protected void setValueAt(int position,
                              Object model) {

        MyModel updatedModel = (MyModel) model;
        useCase.updateModel(position, updatedModel);

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

        private static final String TAG = "MyViewHolder" + ": ";

        public MyViewHolder(int position,
                            MyModel model,
                            ItemView view,
                            ItemController controller) {

            super(position, model, view.getView(), controller);
        }

        /**
         * This is where you get the updated values from the view
         * controls and components.
         *
         * @return the updated model from the view.
         */
        @Override
        public Object getModel() {
            MyModel model = ((ItemController) controller).getModel();
            System.out.println(TAG + "getModel: model=" + model);
            return model;
        }
    }
}
