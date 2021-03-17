package domain;

import data.MyModel;
import genericlistview.ModelListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single source of truth for the data models.
 */
@SuppressWarnings("HardCodedStringLiteral")
public class UseCaseObservableList
        implements
        ModelListener,
        FieldChangedListener {

    private static final String TAG = "UseCaseObservableList" + ": ";

    /**
     * Used by components to identify data fields/components etc. in the
     * model that this use case can handle.
     */
    public enum FieldName {
        FIRST_NAME,
        LAST_NAME,
        AGE
    }

    // a list of listeners interested to changes to the models
    private final List<ModelListener> modelListeners;
    // the source data
    private final List<MyModel> models = new ArrayList<>();

    public UseCaseObservableList() {
        modelListeners = new ArrayList<>();
    }

    //region update model

    /**
     * Implements {@link FieldChangedListener}. An incoming change to a
     * to a field in a model has occurred.
     *
     * @param index     the index of the data model in the source data.
     * @param fieldName the {@link FieldName} assigned to the field.
     * @param value     the updated value of the field.
     */
    @Override
    public void fieldChanged(int index,
                             FieldName fieldName,
                             Object value) {

        switch (fieldName) {
            case FIRST_NAME -> firstNameFieldChanged(index, (String) value);
            case LAST_NAME -> lastNameChanged(index, (String) value);
            case AGE -> ageChanged(index, (String) value);
        }
    }

    private void firstNameFieldChanged(int index,
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

    private void lastNameChanged(int index,
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

    private void ageChanged(int index,
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
        System.out.println(TAG + "addMembership: " + " for model:" + oldModel);
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

        System.out.println(TAG + "removeMembership: " + " for model:" + oldModel);

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

    /**
     * <p>
     * This method is here to be called when 'whole model' editing
     * is preferred. Whole model editing is where the model is edited
     * externally, and when complete, the model in it's entirety with
     * all edits is then set to the use case.
     * </P>
     * <p>
     * This is in stark contrast to field editing, where a change to
     * any field in the model is reported and a new model updated and
     * issued to the observers. Field editing methods are:<br>
     * {@link #firstNameFieldChanged(int, String)}<br>
     * {@link #lastNameChanged(int, String)}<br>
     * {@link #ageChanged(int, String)}<br>
     * {@link #addMembership(int)}<br>
     * {@link #removeMembership(int)}
     * etc.
     * </P>
     * <p>
     * Although both methods of editing can be used in unison
     * it isn't recommended as it may, if setup incorrectly, cause an
     * infinite loop.
     * </P>
     *
     * @param index        the index of the model to be updated.
     * @param updatedModel the new model to replace the existing model.
     */
    public void updateModel(int index,
                            MyModel updatedModel) {

        // edge case index is out of range
        if (getItemCount() > index) {

            MyModel oldModel = models.get(index);
            boolean isChanged = !oldModel.equals(updatedModel);

            /*
             isChanged is here to prevent infinite loops.
             For this to work, it is essential for the model to
             implement equals and hashcode correctly.
            */
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

    public void notifyItemsUpdated(int firstIndex,
                                   int lastIndex) {

        System.out.println(TAG + "notifyItemsUpdated:=" +
                " from:" + models.get(firstIndex) +
                " to:" + lastIndex +
                " there are currently:" + modelListeners.size() + " model listeners to update.");

        modelListeners.forEach(listener ->
                listener.notifyItemsUpdated(firstIndex, lastIndex)
        );
    }

    /**
     * Implements {@link ModelListener}. Convenience method for
     * {@link #notifyItemsUpdated(int, int)}
     */
    public void notifyItemUpdated(int index) {
        System.out.println(TAG + "notifyItemUpdated: there are: " + modelListeners.size() + " to update");
        modelListeners.forEach(listener -> listener.notifyItemUpdated(index));
    }
    // endregion update model

    // region insert member
    public void addNewMember() {
        int index = models.size();

        System.out.println(TAG + "addNewMember: index=" + index);

        models.add(new MyModel());
        notifyItemsInserted(index, index);
    }

    private void insertModels(List<MyModel> models,
                              int index) {

        System.out.println(TAG + "insertModels: " +
                " inserting models=" + models +
                " at index=" + index);

        this.models.addAll(index, models);
        notifyItemsInserted(index, index + models.size() - 1);
    }

    /**
     * Implements {@link ModelListener}
     */
    public void notifyItemsInserted(int firstIndex,
                                    int lastIndex) {
        System.out.println(TAG + "notifyItemsInserted:" +
                " from index:" + firstIndex +
                " to index:" + lastIndex +
                " sending to " + modelListeners.size() + " listeners");

        modelListeners.forEach(listener ->
                listener.notifyItemsInserted(firstIndex, lastIndex)
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
    public void notifyItemsDeleted(int firstIndex,
                                   int lastIndex) {
        modelListeners.forEach(listener ->
                listener.notifyItemsDeleted(firstIndex, lastIndex)
        );
    }
    // endregion delete member

    /**
     * Modifiable operations are blocked to the requester as, if an external
     * source performs any mutation, observers will not be notified. Although this
     * function returns an unmodifiable list, it will mutate, as this class
     * performs actions on the underlying observable list. This is because
     * {@link Collections#unmodifiableList} returns a 'window' to the underlying
     * list and blocks any modifiable operations.
     *
     * @return An unmodifiable set is returned to keep the ability to mutate the list
     * within this class.
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
    @Override
    public void notifyDataSetChanged() {
        modelListeners.forEach(ModelListener::notifyDataSetChanged);
    }

    /**
     * Implements {@link ModelListener}
     */
    @Override
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
