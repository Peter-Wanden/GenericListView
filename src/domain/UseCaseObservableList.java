package domain;

import data.MyModel;
import listview.ModelListener;
import org.w3c.dom.Document;

import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single source of truth for the data models.
 */
public class UseCaseObservableList {

    private static final String TAG = "UseCaseObservableList" + ": ";

    private final List<ModelListener> modelListeners;
    private final List<MyModel> models = new ArrayList<>();

    public UseCaseObservableList() {
        modelListeners = new ArrayList<>();
    }

    //region update member
    public void firstNameFieldChanged(int index,
                                      String firstName) {

        MyModel oldModel = models.get(index);
        boolean firstNameChanged = !oldModel.getFirstName().equals(firstName);

        if (firstNameChanged) {
            MyModel updatedModel = new MyModel(
                    firstName,
                    oldModel.getLastName(),
                    oldModel.getAge(),
                    oldModel.isMember()
            );
            models.set(index, updatedModel);
            notifyItemUpdated(index);
        }
    }

    public void lastNameChanged(int index,
                                String lastName) {

        MyModel oldModel = models.get(index);
        boolean lastNameChanged = !oldModel.getLastName().equals(lastName);

        if (lastNameChanged) {
            MyModel updatedModel = new MyModel(
                    oldModel.getFirstName(),
                    lastName,
                    oldModel.getAge(),
                    oldModel.isMember()
            );
            models.set(index, updatedModel);
            notifyItemUpdated(index);
        }
    }

    public void ageChanged(int index,
                           String age) {

        MyModel oldModel = models.get(index);
        String oldAge = String.valueOf(oldModel.getAge());
        boolean ageChanged = !oldAge.equals(age);

        if (ageChanged) {
            MyModel updatedModel = new MyModel(
                    oldModel.getFirstName(),
                    oldModel.getLastName(),
                    age.isEmpty() ? 0 : Integer.parseInt(age),
                    oldModel.isMember()
            );
            models.set(index, updatedModel);
            notifyItemUpdated(index);
        }
    }

    public void addMembership(int index) {

        MyModel oldModel = models.get(index);
        System.out.println(TAG + "addMember: " + " for model:" + oldModel);
        boolean addMember = !oldModel.isMember();

        if (addMember) {
            MyModel updatedModel = new MyModel(
                    oldModel.getFirstName(),
                    oldModel.getLastName(),
                    oldModel.getAge(),
                    true
            );
            models.set(index, updatedModel);
            notifyItemUpdated(index);
        }
    }

    public void removeMembership(int index) {

        MyModel oldModel = models.get(index);
        boolean removeMember = oldModel.isMember();

        if (removeMember) {
            MyModel updatedModel = new MyModel(
                    oldModel.getFirstName(),
                    oldModel.getLastName(),
                    oldModel.getAge(),
                    false
            );
            models.set(index, updatedModel);
            notifyItemUpdated(index);
        }
    }

    public void updateModel(int index,
                            MyModel updatedModel) {

        // edge case index is out of range
        if (getItemCount() > index) {

            MyModel oldModel = models.get(index);
            boolean isChanged = !oldModel.equals(updatedModel);

            if (isChanged) {
                System.out.println(TAG + " updateModel: " +
                        " Model has changed. " + "\n" +
                        " Old model=" + oldModel + "\n" +
                        " New model=" + updatedModel);
                System.out.println(TAG + " updateModel: is currently turned off.");
//            models.set(index, updatedModel);
//            notifyItemUpdated(index);
            } else {
                System.out.println(TAG + "updateModel: " +
                        " Model has not changed." + "\n" +
                        " Old model=" + oldModel + "\n" +
                        " New model=" + updatedModel);
            }
        }
    }

    public void notifyItemsUpdated(int firstRow,
                                   int lastRow) {

        System.out.println(TAG + "notifyItemsUpdated:=" +
                " from:" + models.get(firstRow) +
                " to:" + lastRow);
        modelListeners.forEach(listener ->
                listener.notifyItemsUpdated(firstRow, lastRow)
        );
    }

    /**
     * Implements {@link ModelListener}
     */
    public void notifyItemUpdated(int row) {
        System.out.println(TAG + "notifyItemUpdated:=" + models.get(row));
        modelListeners.forEach(modelListener ->
                modelListener.notifyItemUpdated(row));
    }
    // endregion update member

    // region insert member
    public void addNewMember(MyModel model) {
        int index = models.size();

        System.out.println(TAG + "addNewMember: index=" + index +
                "model=" + model);

        models.add(
                model == null ? new MyModel() : model
        );
        notifyItemsInserted(index, index);
    }

    private void insertModels(List<MyModel> models,
                              int index) {

        System.out.println(TAG + "insertModels: " +
                " inserting models=" + models +
                " at index=" + index);

        this.models.addAll(index, models);
        notifyItemsInserted(index, index + models.size() -1);
    }

    /**
     * Implements {@link ModelListener}
     */
    public void notifyItemsInserted(int firstRow,
                                    int lastRow) {
        System.out.println(TAG + "notifyItemsInserted:" +
                " from index:" + firstRow +
                " to index:" + lastRow);

        modelListeners.forEach(listener ->
                listener.notifyItemsInserted(firstRow, lastRow)
        );
    }
    // endregion insert member

    // region delete member
    public void deleteModel(int index) {
        System.out.println(TAG + "deleteModel:" +
                " at index=" + index);

        System.out.println(TAG + "deleteModel: modelsSize=" + getItemCount() + " index to delete=" + index);
        models.remove(index);
        notifyItemsDeleted(index, index);
    }

    /**
     * Implements {@link ModelListener}
     */
    public void notifyItemsDeleted(int firstRow, int lastRow) {
        modelListeners.forEach(listener -> listener.notifyItemsDeleted(firstRow, lastRow));
    }
    // endregion delete member

    /**
     * Modifiable operations are blocked to the requester as, if an external
     * source performs any mutation, observers will not be notified. Although this
     * function returns an unmodifiable list, it will mutate, as this class
     * performs actions on the underlying observable list. This is because
     * {@link Collections#unmodifiableList} returns a 'window' to the underlying
     * list and blocks any modifiable operations.
     * @return An unmodifiable set is returned to keep the ability to mutate the list
     *         within this class.
     */
    public List<MyModel> getModels() {
        return Collections.unmodifiableList(models);
    }

    public void setModels(List<MyModel> models) {
        this.models.clear();
        this.models.addAll(models);
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return models.size();
    }

    /**
     * Implements {@link ModelListener}
     */
    public void notifyDataSetChanged() {
        modelListeners.forEach(ModelListener::notifyDataSetChanged);
    }

    /**
     * Implements {@link ModelListener}
     */
    public void notifyDataStructureChanged() {
        modelListeners.forEach(ModelListener::notifyDataStructureChanged);
    }

    public void addModelListener(ModelListener listener) {
        modelListeners.add(listener);
    }
    public void removeModelListener(ModelListener listener) {
        modelListeners.remove(listener);
    }
}
