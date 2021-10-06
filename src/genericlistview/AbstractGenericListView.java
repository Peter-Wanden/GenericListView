package genericlistview;

import genericlistview.AbstractGenericListView.ViewHolder;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;

/**
 * <strong><a id="override">Implementation Note</a></strong>
 * Implementers are required to provide an implementation of the view holder.
 * The view holder need only return the view to be rendered, which can be
 * any class extending {@link Component}
 *
 * @param <VIEW_HOLDER> your view holder implementation of {@link ViewHolder}
 * @see ViewHolder
 */
@SuppressWarnings("unused")
public abstract class AbstractGenericListView
        <VIEW_HOLDER extends ViewHolder>
        implements
        ModelListener,
        TableModelListener,
        CellEditorListener {

    private static final String TAG = "AbstractGenericListView" + ": ";
    private final boolean isLogging = false;

// region view holder

    /**
     * Holds the view that will be displayed in a table cell. It is used by
     * both the {@link Renderer} and {@link Editor} as a generic wrapper for
     * the view.
     */
    public abstract class ViewHolder {

        private static final String TAG = "ViewHolder" + ": ";

        // the view to be rendered
        protected final Component view;

        /**
         * @param view the view to be rendered.
         */
        public ViewHolder(Component view) {
            if (view == null) {
                throw new IllegalArgumentException("item view may not be null");
            }
            this.view = view;
        }

        /**
         * Used by the table cell renderers to get the view from this view holder.
         *
         * @return the view to be rendered.
         */
        public Component getView() {
            return view;
        }

        /**
         * The model passed into the {@link ViewHolder}. If this view holder
         * has been rendered by the {@link Editor} and data within the view has
         * been edited, you should collect and return it with this method.
         *
         * @return either the model passed into the view or a model updated
         * within the view, post editing.
         */
        public abstract Object getModel();


        /**
         * Hook method called just before the Editor closes the view. Now is a
         * good time to clean things up, such as removing listeners and
         * extracting any values in the ui controls, etc.
         */
        public void prepareEditingStopped() {
            System.out.println(TAG + " prepareEditingStopped()" +
                    " tableIsEditing=" + table.isEditing()
            );
        }
    }
// endregion view holder

// region renderer

    /**
     * Renders the view returned by the {@link ViewHolder} to the cell.
     * Implementers of the abstract parent must also provide an implementation
     * of this class.
     *
     * @see AbstractGenericListView
     */
    private class Renderer
            implements TableCellRenderer {

        private static final String TAG = "Renderer" + ": ";

        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object model,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int index,
                                                       int column) {

            boolean isRecycled = false;

            VIEW_HOLDER viewHolder = null;

            if (index < recyclableViews.size()) {
                viewHolder = recyclableViews.get(index);
            }

            if (viewHolder == null) {
                viewHolder = onCreateViewHolder(index, false);
                recyclableViews.add(index, viewHolder);

                if (isLogging)
                    recyclableViews.forEach(view_holder ->
                            System.out.println(TAG + "getTableCellRendererComponent:" +
                                    " recyclable view=" +
                                    " view holder at:" +
                                    " hasModel=" + view_holder.getModel()
                            )
                    );

            } else {
                isRecycled = true;
                onBindViewHolder(index, false, viewHolder);
            }

            var view = (Component) viewHolder.view;
            setViewHeight(view);

            System.out.println(TAG + "getTableCellRenderer: rendering model=" + model +
                    " isRecycled=" + isRecycled);

            return view;
        }

        private void setViewHeight(Component view) {

            int rowHeight = table.getRowHeight();
            int requiredHeight = view.getPreferredSize().height +
                    table.getIntercellSpacing().height;

            if (rowHeight < requiredHeight) {
                table.setRowHeight(requiredHeight);
            }
        }
    }
// endregion renderer

// region editor

    /**
     * When a cell is selected this class renders the view for editing the data
     * model.
     */
    private class Editor
            extends AbstractCellEditor
            implements TableCellEditor {

        private static final String TAG = "Editor" + ": ";

        private VIEW_HOLDER viewHolder;

        @Override
        public Component getTableCellEditorComponent(JTable table,
                                                     Object model,
                                                     boolean isSelected,
                                                     int index,
                                                     int column) {

            System.out.println(TAG + "getTableCellEditor: rendering model:=" + model);

            viewHolder = null;

            if (index < recyclableViews.size()) {
                viewHolder = recyclableViews.get(index);
            }
            if (viewHolder == null) {
                viewHolder = onCreateViewHolder(index, true);
                recyclableViews.add(index, viewHolder);

                recyclableViews.forEach(viewHolder ->
                        System.out.println(TAG + "getTableCellEditorComponent: recyclable view=" +
                                " view holder at:" +
                                " hasModel=" + viewHolder.getModel()
                        )
                );

            } else {
                onBindViewHolder(index, true, viewHolder);
            }

            return viewHolder.getView();
        }

        /**
         * When editing of the cell is complete, i.e when
         * {@link JTable#editingStopped(ChangeEvent)} method is triggered, it
         * calls this method to get the edited values. So this is where you get
         * the edited values from your view in the current instance the
         * {@link ViewHolder}.
         *
         * @return the model values in the view
         */
        @Override
        public Object getCellEditorValue() {
            System.out.println(TAG + "getCellEditorValue: calling viewHolder.getModel()");
            return viewHolder.getModel();
        }

        @Override
        public boolean stopCellEditing() {
            System.out.println(TAG
                    + "stopCellEditing: table.isEditing=" + table.isEditing()
                    + " calling: viewHolder.prepareEditingStopped()"
            );
            viewHolder.prepareEditingStopped();
            return super.stopCellEditing();
        }
    }
// endregion editor

// region model

    /**
     * An implementation of {@link AbstractTableModel} that points to an
     * external data source.
     */
    private class GenericTableModel
            extends AbstractTableModel {

        private static final String TAG = "GenericTableModel" + ": ";

        @Override
        public String getColumnName(int column) {
            return AbstractGenericListView.COLUMN_NAME;
        }

        @Override
        public int getRowCount() {
            return AbstractGenericListView.this.getItemCount();
        }

        @Override
        public int getColumnCount() {
            return AbstractGenericListView.COLUMN_NUMBER + 1;
        }

        @Override
        public Object getValueAt(int rowIndex,
                                 int columnIndex) {

            return AbstractGenericListView.this.getValueAt(rowIndex);
        }

        @Override
        public boolean isCellEditable(int rowIndex,
                                      int columnIndex) {
            return true;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return AbstractGenericListView.ViewHolder.class;
        }

        /**
         * Implements {@link AbstractTableModel#setValueAt(Object, int, int)}
         * method. Called when the cell being edited looses focus. When this
         * happens the {@link Editor#getCellEditorValue()} method is called,
         * which in turn calls the {@link ViewHolder#getModel()} method,
         * delegating the task of getting the current views values and placing
         * them in a model. The model is then passed to the implementer for
         * processing.
         *
         * @param model       the updated model.
         * @param rowIndex    the row or index of the item in the data model.
         * @param columnIndex the column of the cell.
         */
        @Override
        public void setValueAt(Object model,
                               int rowIndex,
                               int columnIndex) {

            AbstractGenericListView.this.setValueAt(rowIndex, model);
        }
    }
// endregion model

    private static final String COLUMN_NAME = "COLUMN_NAME";
    private static final int COLUMN_NUMBER = 0;

    private final JTable table;
    private final AbstractTableModel tableModel;
    private final JScrollPane view;
    protected final ArrayList<VIEW_HOLDER> recyclableViews;
    private final Editor editor;

    protected AbstractGenericListView() {

        tableModel = new GenericTableModel();
        table = new JTable();
        view = new JScrollPane(table);
        recyclableViews = new ArrayList<>();
        editor = new Editor();
        editor.addCellEditorListener(this);

        initialiseTable();
        addListeners();
    }

    private void initialiseTable() {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setIntercellSpacing(new Dimension(5, 5));
        table.setModel(tableModel);
        table.setFillsViewportHeight(true);
        table.setTableHeader(null);
        table.setShowGrid(false);
        table.setOpaque(true);

        table.setDefaultRenderer(ViewHolder.class, new Renderer());
        table.setDefaultEditor(ViewHolder.class, editor);
    }

    private void addListeners() {
        tableModel.addTableModelListener(this);
    }

// region abstract methods

    /**
     * When a view is created with this method it is stored in the
     * <code>recyclableViews</code> hash table instance variable with the
     * models index as the key. When the Editor or Renderer is invoked to
     * provide a view, if the view does not exist this method is called. If the
     * view exists the {@link #onBindViewHolder} method is called.
     *
     * @param index    the index of the model in the underlying data.
     * @param isEditor if the view to return is about to be edited.
     * @return the ViewHolder containing the view.
     * @see Editor
     * @see Renderer
     * @see ViewHolder
     */
    protected abstract VIEW_HOLDER onCreateViewHolder(final int index,
                                                      final boolean isEditor
    );

    /**
     * Called by the underlying table to display the data at the specified
     * index. This method should update the contents of the
     * {@link ViewHolder#view} to reflect the item at the given index.
     *
     * @param index    The index of the item within the data set.
     * @param isEditor Tells the implementer if the view returned should
     *                 be editable.
     */
    protected abstract void onBindViewHolder(final int index,
                                             final boolean isEditor,
                                             final VIEW_HOLDER viewHolder
    );

    /**
     * Called by the list model while rendering views.
     *
     * @param index the index of the item in the supplied data.
     * @return returns the data model representing the index.
     */
    protected abstract Object getValueAt(int index);

    /**
     * Informs model of the amount of items in the data
     *
     * @return the number of data models in your list.
     */
    protected abstract int getItemCount();

    /**
     * Called when editing stops. Passes the value of the object collected by
     * the {@link Editor#getCellEditorValue}. This is generally the data model
     * just updated in the Editors view.
     *
     * @param index the index of the data model being edited.
     * @param value the edited value of the data model as provided by
     *              {@link Editor#getCellEditorValue()}.
     */
    protected abstract void setValueAt(int index,
                                       Object value
    );
// endregion abstract methods

// region implements CellEditorListener

    @Override
    public void editingStopped(ChangeEvent e) {
        System.out.println(TAG + "CellEditorListener: editingStopped: ChangeEvent=" + e);
    }

    @Override
    public void editingCanceled(ChangeEvent e) {
        System.out.println(TAG + "CellEditorListener: editingStopped: ChangeEvent=" + e);
    }

// endregion implements CellEditorListener

// region implements ModelListener
    /*
    The following methods implement the ModelListener interface. This
    interface's contract is designed to be enforced by an external data source
    who calls these methods as required to keep the view in sync with the
    underlying data source.
     */

    /**
     * Delegate method that informs the table model of a change in the source
     * data.
     *
     * @see ModelListener
     */
    @Override
    public void notifyDatasetChanged() {
        System.out.println(TAG + "notifyDatasetChanged: ");
        tableModel.fireTableDataChanged();
    }

    /**
     * Delegate method that informs the table model the table structure has
     * changed.
     *
     * @see ModelListener
     */
    @Override
    public void notifyDataStructureChanged() {
        tableModel.fireTableStructureChanged();
    }

    /**
     * Delegate method that informs the table model of a change in the source
     * data.
     *
     * @param firstIndex the inclusive index of the first row of data inserted.
     * @param lastIndex  the inclusive index of the last row of data inserted.
     * @see ModelListener
     */
    @Override
    public void notifyItemsInserted(int firstIndex, int lastIndex) {
        tableModel.fireTableRowsInserted(firstIndex, lastIndex);
    }

    /**
     * Delegate method that informs the table model of a change in the source
     * data.
     *
     * @param firstIndex the inclusive index of the first item in the source
     *                   data to be updated.
     * @param lastIndex  the inclusive index of the last item in the source
     *                   data to be updated.
     * @see ModelListener
     */
    @Override
    public void notifyItemsUpdated(int firstIndex,
                                   int lastIndex) {
        tableModel.fireTableCellUpdated(firstIndex, lastIndex);
    }

    /**
     * Delegate method that informs the table model of a single item has been
     * updated in the source data.
     *
     * @param index the index of the updated item in the source data
     * @see ModelListener
     */
    @Override
    public void notifyItemUpdated(int index) {
        tableModel.fireTableCellUpdated(index, COLUMN_NUMBER);
    }

    /**
     * Delegate method that informs the table model of a change in the source data.
     *
     * @param firstIndex the inclusive index of the first row in the source data to be deleted.
     * @param lastIndex  the inclusive index of the last row in the source data to be deleted
     * @see ModelListener
     */
    @Override
    public void notifyItemsDeleted(final int firstIndex,
                                   final int lastIndex) {

        System.out.println(TAG + "notifyItemsDeleted: " +
                " firstIndex=" + firstIndex +
                " lastIndex=" + lastIndex);

        tableModel.fireTableRowsDeleted(firstIndex, lastIndex);
    }
// endregion implements ModelListener

// region implements TableModelListener

    /**
     * This fine grain notification tells listeners the exact range of cells,
     * rows, or columns that changed.
     *
     * @param e the event, containing the location of the changed model.
     * @see TableModelListener
     */
    @Override
    public void tableChanged(TableModelEvent e) {
        int eventType = e.getType();

        if (TableModelEvent.DELETE == eventType) {
            System.out.println(TAG + "tableChanged: event=DELETE");
            // If the cell being edited is within the range of the cells that
            // have been deleted then editing must be either cancelled and the
            // view holder set to null or recycled.
            if (table.isEditing()) {
//                TableCellEditor editor = table.getDefaultEditor(ViewHolder.class);
                TableCellEditor editor = table.getCellEditor();
                if (editor != null) {
                    // The coordinate of the cell being edited.
                    int editingColumn = table.getEditingColumn();
                    int editingRow = table.getEditingRow();

                    // The inclusive coordinates of the cells that have changed.
                    int changedColumn = e.getColumn();
                    int firstRowChanged = e.getFirstRow();
                    int lastRowChanged = e.getLastRow();

                    // Returns true if the cell being edited is in the range of
                    // cells deleted.
                    boolean editingCellInRangeOfChangedCells =
                            (TableModelEvent.ALL_COLUMNS == changedColumn ||
                                    changedColumn == editingColumn) &&
                                    editingRow >= firstRowChanged &&
                                    editingRow <= lastRowChanged;


                    if (editingCellInRangeOfChangedCells) {
                        // Clean up view and view holder
                        editor.cancelCellEditing();
                        if (table.getCellEditor() != null) {
                            // Cell editor did not concede control, forcefully
                            // remove it.
                            table.removeEditor();
                        }
                        VIEW_HOLDER viewHolder = recyclableViews.remove(editingRow);

                        // remove the last element in the recyclable views or delete the range


                        System.out.println(TAG + "tableChanged: removed viewHolder=" + viewHolder.getModel());
                        viewHolder = null;
                        System.out.println(TAG + "tableChanged: viewHolder=" + viewHolder);

                        System.out.println(TAG + "tableChanged:" +
                                " editingCellInRangeOfDeletedCells=" + editingCellInRangeOfChangedCells +
                                " editing has been cancelled. ViewHolder has been removed from recyclable views");

                        recyclableViews.forEach(vh ->
                                System.out.println(TAG + "tableChanged: " +
                                        "view holder at:" +
                                        " hasModel=" + vh.getModel()
                                )
                        );

                    }
                }
            }
        } else if (TableModelEvent.UPDATE == eventType) {
            // The inclusive coordinates of the cells that have changed.
            int changedColumn = e.getColumn();
            int firstRowChanged = e.getFirstRow();
            int lastRowChanged = e.getLastRow();
            recyclableViews.ensureCapacity(getItemCount());

            System.out.println(TAG + "tableChanged: event=UPDATE" +
                    " firstRowChanged:" + firstRowChanged +
                    " lastRowChanged:" + lastRowChanged
            );

        } else if (TableModelEvent.INSERT == eventType) {
            // The inclusive coordinates of the cells that have changed.
            int changedColumn = e.getColumn();
            int firstRowChanged = e.getFirstRow();
            int lastRowChanged = e.getLastRow();

            System.out.println(TAG + "tableChanged: event=INSERT" +
                    " firstRowChanged:" + firstRowChanged +
                    " lastRowChanged:" + lastRowChanged
            );
        } else {
            System.out.println(TAG + "tableChanged: event=" + eventType);
        }
    }
// endregion implements TableModelListener

// region getters and setters

    /**
     * Access to the root view of this {@link AbstractGenericListView} component.
     *
     * @return an {@link JScrollPane} containing the view.
     */
    public JScrollPane getView() {
        return view;
    }

// endregion getters and setters
}
