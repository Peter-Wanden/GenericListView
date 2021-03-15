package domain;

import domain.UseCaseObservableList.FieldName;

/**
 * This is an incoming notification that a listener to a field in the model
 * must implement and a modifier to the field must call.
 * Listeners are informed, in real time, of changes to values
 * to the fields in the model.
 */
public interface FieldChangedListener {
    /**
     * @param index     the index of the data model in the source data.
     * @param fieldName the {@link FieldName} assigned to the field.
     * @param value     the updated value of the field.
     */
    void fieldChanged(int index,
                      FieldName fieldName,
                      Object value
    );
}
