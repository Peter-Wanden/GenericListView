package ui.mylistview;

import listview.AbstractGenericListView;
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
     * Called each time a {@link listview.AbstractGenericListView.ViewHolder} is created,
     * whether for editing or displaying data.
     * @param position of the data in the models provided list.
     * @return a {@link listview.AbstractGenericListView.ViewHolder} containing the view.
     */
    @Override
    protected ViewHolder onCreateViewHolder(int position) {

        // TODO - ADD MODEL LISTENERS HERE TO ENSURE THE MODEL IS BEING LISTENED TO
        //  WHILE IT IS BEING EDITED!
//
//        System.out.println(TAG + "onCreateViewHolder:" + " position:" + position +
//                " currently working with model at index=" + lastEditedIndex);

        ItemController itemController = new ItemController(listViewController, position);
        itemController.getView().bindModel(
                listViewController.getUseCase().getModels().get(position)
        );
        ItemView view = itemController.getView();
        // register model listeners

        System.out.println(TAG + "onCreateViewHolder");


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

        // edge case: there are no elements in the list
        if (useCase.getModels().isEmpty()) {
            return new Object();
        }

        // edge case: the last element has been deleted.
        // Return the new index of the last element.
        if (position >= size) {
            position = size -1;
        }

        System.out.println(TAG + "getValueAt: position=" + position);
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
        System.out.println(TAG + "setValueAt:" +
                " updatedModel=" + updatedModel);
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
