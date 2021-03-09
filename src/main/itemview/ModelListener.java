package main.itemview;

/**
 * This interface is implemented by {@link main.listview.AbstractGenericListView}.
 * These methods allow your concrete implementation to be informed when the
 * underlying data changes. This is particularly useful when listening to external
 * data sources.
 */
public interface ModelListener {

    void notifyItemUpdated(int index);

    void notifyDataSetChanged();

    void notifyItemDeleted(int index);

    void notifyItemInserted(int index);
}
