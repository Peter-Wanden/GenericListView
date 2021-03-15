package genericlistview;

/**
 * This is an incoming notification from a control in a view.
 * Listeners are informed of the specific action performed and
 * the index of the data model in the supplied source data.
 */
public interface ControlActionListener {
    /**
     * Informs the listener that membership should be added.
     *
     * @param index the index of the data model in the source data.
     */
    void addMembership(int index);

    /**
     * Informs the listener that membership should be removed.
     *
     * @param index the index of the data model in the source data.
     */
    void removeMembership(int index);

    /**
     * Informs the listener that the record should be deleted.
     *
     * @param index the index of the data model in the source data.
     */
    void deleteModel(int index);
}
