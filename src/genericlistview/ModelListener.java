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

    void notifyItemsInserted(int firstRow,
                             int lastRow);

    void notifyItemsUpdated(int firstRow,
                            int lastRow);

    void notifyItemUpdated(int row);

    void notifyItemsDeleted(int firstRow,
                            int lastRow);
}
