package domain.usecase;

import data.MyModel;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.SwingPropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.Serializable;

public class UseCaseItem
        implements Serializable {

    // Property names (only needed for bound or constrained properties).
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String AGE = "age";
    public static final String IS_MEMBER = "isMember";

    private MyModel delegate;

    // Manages all property change listeners.
    protected SwingPropertyChangeSupport propertyChangeListeners =
            new SwingPropertyChangeSupport(this);
    // Manages all vetoable property change listeners.
    protected VetoableChangeSupport vetoChangeListeners =
            new VetoableChangeSupport(this);

    // Only one change event is needed since the event's only state  is the
    // source property. The source of events generated is always "this". You'll
    // see this in lots of Swing source.
    protected transient ChangeEvent changeEvent = null;

    // This can manage all types of listeners, as long as we set up the fireXX
    // methods to correctly look through this list. This makes you appreciate the
    // XXSupport classes.
    protected EventListenerList changeListeners =
            new EventListenerList();

    // Must have a no arg constructor. We'll set the default values here.
    public UseCaseItem() {
        delegate = new MyModel.Builder().build();
    }

    public String getFirstName() {
        return delegate.getFirstName();
    }

    public void setFirstName(String firstName)
            throws PropertyVetoException {

        // check if changed...
        if (!delegate.getFirstName().equals(firstName)) {

            String oldValue = delegate.getFirstName();

            // Notify all VetoableChangeListeners before making a change.
            // Exception will be thrown if a listener veto's.
            vetoChangeListeners.fireVetoableChange(
                    FIRST_NAME, oldValue, firstName
            );
            // If not vetoed, we can continue to make the change.
            delegate = new MyModel.Builder()
                    .basedOn(delegate)
                    .setFirstName(firstName)
                    .build();

            // Let the listeners know of the change.
            propertyChangeListeners.firePropertyChange(
                    FIRST_NAME, oldValue, delegate.getFirstName()
            );
            fireStateChanged();
        }
    }

    public String getLastName() {
        return delegate.getLastName();
    }

    public void setLastName(String lastName)
            throws PropertyVetoException {

        if (!delegate.getLastName().equals(lastName)) {

            String oldValue = delegate.getLastName();

            vetoChangeListeners.fireVetoableChange(
                    LAST_NAME, oldValue, lastName
            );

            delegate = new MyModel.Builder()
                    .basedOn(delegate)
                    .setLastName(lastName)
                    .build();

            propertyChangeListeners.firePropertyChange(
                    LAST_NAME, oldValue, lastName
            );
            fireStateChanged();
        }
    }

    public int getAge() {
        return delegate.getAge();
    }

    public void setAge(int age)
            throws PropertyVetoException {

        if (delegate.getAge() != age) {

            int oldAge = delegate.getAge();

            vetoChangeListeners.fireVetoableChange(
                    AGE, oldAge, age
            );

            delegate = new MyModel.Builder()
                    .basedOn(delegate)
                    .setAge(age)
                    .build();

            propertyChangeListeners.firePropertyChange(
                    AGE, oldAge, age
            );
            fireStateChanged();
        }
    }

    public boolean isMember() {
        return delegate.isMember();
    }

    public void setMember(boolean isMember)
            throws PropertyVetoException {

        if (delegate.isMember() != isMember) {

            boolean oldIsMember = delegate.isMember();

            vetoChangeListeners.fireVetoableChange(
                    IS_MEMBER, oldIsMember, isMember
            );

            delegate = new MyModel.Builder()
                    .basedOn(delegate)
                    .setMember(isMember)
                    .build();

            propertyChangeListeners.firePropertyChange(
                    IS_MEMBER, oldIsMember, isMember
            );
            fireStateChanged();
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeListeners.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeListeners.removePropertyChangeListener(listener);
    }

    public void addVetoableChangeListener(VetoableChangeListener listener) {
        vetoChangeListeners.addVetoableChangeListener(listener);
    }

    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        vetoChangeListeners.removeVetoableChangeListener(listener);
    }

    // EventListenerList is an array of key/value pairs:
    //    key = XXListener class reference.
    //  value = XXListener instance.
    public void addChangeListener(ChangeListener listener) {
        changeListeners.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        changeListeners.remove(ChangeListener.class, listener);
    }

    // This is typical EventListenerList dispatching code. You'll see this in
    // lots of Swing source.
    protected void fireStateChanged() {
        Object[] listeners = changeListeners.getListenerList();
        // Process teh listeners last to first, notifying those that are
        // interested in this event.
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                    ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
                }
            }
        }
    }
}
