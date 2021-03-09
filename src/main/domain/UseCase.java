package main.domain;

import main.data.MyModel;
import main.itemview.ModelListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single source of truth for the data models.
 */
public class UseCase {

    private static final String TAG = "UseCase" + ": ";

    private final List<ModelListener> modelListeners;
    private List<MyModel> models = new ArrayList<>();

    public UseCase() {
        modelListeners = new ArrayList<>();
    }

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
            notifyModelUpdated(index);
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
            notifyModelUpdated(index);
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
                    Integer.parseInt(age),
                    oldModel.isMember()
            );
            models.set(index, updatedModel);
            notifyModelUpdated(index);
        }
    }

    public void addMember(int index) {
        MyModel oldModel = models.get(index);
        boolean addMember = !oldModel.isMember();
        if (addMember) {
            MyModel updatedModel = new MyModel(
                    oldModel.getFirstName(),
                    oldModel.getLastName(),
                    oldModel.getAge(),
                    true
            );
            models.set(index, updatedModel);
            notifyModelUpdated(index);
        }
    }

    public void removeMember(int index) {
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
            notifyModelUpdated(index);
        }
    }

    public void insertModel(MyModel model) {
        int index = models.size();

        models.add(
                model == null ? new MyModel() : model
        );

        notifyModelInserted(index);
    }

    public int getItemCount() {
        return models.size();
    }

    // Model updates
    public void updateModel(int index,
                            MyModel updatedModel) {
        boolean isUpdated = false;
        MyModel oldModel = models.get(index);
        if (!oldModel.equals(updatedModel)) {
            models.set(index, updatedModel);
            isUpdated = true;
            notifyModelUpdated(index);
        }
        System.out.println(TAG + "updateModel=" + isUpdated);
    }

    public void deleteModel(int index) {
        System.out.println(TAG + "modelsSize=" + getItemCount() + " index to delete=" + index);
        models.remove(index);
        notifyModelDeleted(index);
    }

    public List<MyModel> getModels() {
        return models;
    }

    public void setModels(List<MyModel> models) {
        this.models.clear();
        this.models.addAll(models);
        notifyDataSetChanged();
    }

    public void addModelListener(ModelListener listener) {
        modelListeners.add(listener);
    }

    public void removeModelListener(ModelListener listener) {
        modelListeners.remove(listener);
    }

    private void notifyModelUpdated(int index) {
        System.out.println(TAG + "notifyModelUpdated:=" + models.get(index));
        modelListeners.forEach(listener -> listener.notifyItemUpdated(index));
    }

    private void notifyModelDeleted(int index) {
        modelListeners.forEach(listener -> listener.notifyItemDeleted(index));
    }

    private void notifyModelInserted(int index) {
        modelListeners.forEach(listener -> listener.notifyItemInserted(index));
    }

    private void notifyDataSetChanged() {
        modelListeners.forEach(ModelListener::notifyDataSetChanged);
    }
}
