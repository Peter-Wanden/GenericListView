package genericlistview;

/**
 * This interface is implemented by {@link AbstractGenericListView}.
 * These methods allow your concrete implementation to be informed when the
 * underlying data changes. This is particularly useful when listening to external
 * data sources.
 */
public interface ModelListener {

    void notifyDataSetChanged();

    void notifyDataStructureChanged();

    void notifyItemsInserted(int firstIndex,
                             int lastIndex);

    void notifyItemsUpdated(int firstIndex,
                            int lastIndex);

    void notifyItemUpdated(int index);

    void notifyItemsDeleted(int firstIndex,
                            int lastIndex);
}
