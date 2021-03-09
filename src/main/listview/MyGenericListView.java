package main.listview;

import main.data.MyModel;
import main.domain.UseCase;
import main.itemview.ItemController;
import main.itemview.ItemView;

public class MyGenericListView
        extends AbstractGenericListView {

    private static final String TAG = "MyGenericListView" + ": ";

    private final UseCase useCase;
    private final MyGenericListViewController listViewController;

    public MyGenericListView(MyGenericListViewController listViewController,
                             UseCase useCase) {

        this.listViewController = listViewController;
        this.useCase = useCase;
        useCase.addModelListener(this);
    }

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
//        System.out.println(TAG + "getValueAt: position=" + position);
        return useCase.getModels().size() == 0 ?
                new Object() :
                useCase.getModels().get(position);
    }

    /**
     * Called post editing the controls in the ViewHolder.
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
        System.out.println(TAG + updatedModel);
    }

    protected void valueChanged(int index, Object model) {
        System.out.println(TAG + "valueChanged:" + model);
        useCase.updateModel(index, (MyModel) model);
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
