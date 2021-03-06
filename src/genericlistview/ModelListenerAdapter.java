package genericlistview;

/**
 * An abstract adapter class for receiving {@link ModelListener} events.
 * The methods in this class are empty. This class exists as
 * convenience for creating listener objects.
 * <p>
 * Extend this class to create a {@link ModelListener} and override
 * the methods for the events of interest. (If you implement the
 * {@link ModelListener} interface, you have to define all of the methods in
 * it. This abstract class defines null methods for them all, so you only
 * have to define methods for events you care about). <P>
 * Create a listener object using your class and then register it with a
 * component using the component's {@code addComponentListener} method.
 * When the component's size, location, or visibility changes, the
 * relevant method in the listener object is invoked, and the
 * {@link ModelListener} is passed to it.
 */

public abstract class ModelListenerAdapter
        implements ModelListener {

    @Override
    public void notifyDatasetChanged() {}

    @Override
    public void notifyDataStructureChanged() {}

    @Override
    public void notifyItemsInserted(int firstIndex,
                                    int lastIndex) {}

    @Override
    public void notifyItemsUpdated(int firstIndex,
                                   int lastIndex) {}

    @Override
    public void notifyItemUpdated(int index) {}

    @Override
    public void notifyItemsDeleted(int firstIndex,
                                   int lastIndex) {}
}
