package genericlistview;

import genericlistview.AbstractGenericListView.ViewHolder;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

@SuppressWarnings("unused")
public abstract class AbstractGenericListView<
        VIEW_HOLDER extends ViewHolder
        >
        implements
        ModelListener,
        TableModelListener {

    private static final String TAG = "AbstractGenericListView" + ": ";

    /**
     * Holds the view in a table cell. It is used by both the {@link Renderer}
     * and {@link Editor} as a generic wrapper for the view.
     */
    public abstract class ViewHolder {

        private static final String TAG = "ViewHolder" + ": ";

        // the view to be rendered
        protected final Component view;

        /**
         * @param view  the view to be rendered.
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
         * The model passed into the {@link ViewHolder}. If this view
         * holder has been rendered by the {@link Editor} and data within
         * the view has been edited, you should collect and return it
         * with this method.
         *
         * @return either the model passed into the view or a model updated
         * within the view, post editing.
         */
        public abstract Object getModel();
        

        /**
         * Called just before the Editor closes the view. Now is a good time to
         * clean things up, such as removing listeners and extracting any values
         * in the ui controls, etc.
         */
        public abstract void prepareEditingStopped();
    }

    static int rendererConstructorCalls;

    /**
     * Renders the view returned by the {@link ViewHolder} to the cell.
     */
    private class Renderer
            implements TableCellRenderer {

        private static final String TAG = "Renderer" + ": ";

        private VIEW_HOLDER viewHolder;

        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object model,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {

            if (viewHolder == null) {
                viewHolder = onCreateViewHolder(false);
            }

            onBindViewHolder(viewHolder, row, false);
            Component view = viewHolder.view;
            setViewHeight(view);

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
                                                     Object value,
                                                     boolean isSelected,
                                                     int row,
                                                     int column) {
            if (viewHolder == null) {
                viewHolder = onCreateViewHolder(true);
            }

            onBindViewHolder(viewHolder, row, true);

            return viewHolder.getView();
        }

        /**
         * When editing of the cell is complete, i.e when {@link JTable#editingStopped(ChangeEvent)}
         * method is triggered, it calls this method to get the edited values.
         * So this is where you get the edited values from your view in the current instance
         * the {@link ViewHolder}.
         *
         * @return the model values in the view
         */
        @Override
        public Object getCellEditorValue() {
            return viewHolder.getModel();
        }

        @Override
        public boolean stopCellEditing() {
            viewHolder.prepareEditingStopped();
            System.out.println(TAG + "AbstractEditor: isEditing=" + table.isEditing());
            return super.stopCellEditing();
        }
    }

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
         * Implements {@link AbstractTableModel#setValueAt(Object, int, int)} method.
         * Called when the cell being edited looses focus. When this happens the
         * {@link Editor#getCellEditorValue()} method is called, which in turn calls
         * the {@link ViewHolder#getModel()} method, delegating the task of getting
         * the current views values and placing them in a model. The model
         * is then passed to the implementer for processing.         *
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

    private static final String COLUMN_NAME = "COLUMN_NAME";
    private static final int COLUMN_NUMBER = 0;

    private final JTable table;
    private final AbstractTableModel tableModel;
    private final JScrollPane view;

    protected AbstractGenericListView() {
        System.out.println(TAG + "constructor called.");

        tableModel = new GenericTableModel();
        table = new JTable();
        view = new JScrollPane(table);

        setupTable();
        addListeners();
    }

    private void setupTable() {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setIntercellSpacing(new Dimension(5, 5));
        table.setModel(tableModel);
        table.setFillsViewportHeight(true);
        table.setTableHeader(null);
        table.setShowGrid(false);
        table.setOpaque(true);

        table.setDefaultRenderer(ViewHolder.class, new Renderer());
        table.setDefaultEditor(ViewHolder.class, new Editor());
    }

    private void addListeners() {
        tableModel.addTableModelListener(this);
    }

// region abstract methods

    /**
     * When the table is creating its {@link Renderer} and {@link Editor},
     * which it does only once for each, it calls this method so that the
     * implementer may provide a ViewHolder. Implementers should place their
     * view into the ViewHolder. The <code>isEditor</code> boolean tells the
     * implementer if the {@link ViewHolder}'s view should return the editor
     * view or the renderer view. Both views can be the same type.
     *
     * @param isEditor true if the view holder should contain the editor
     *                 {@link Editor} view.
     * @return your ViewHolder concrete class with your view.
     */
    protected abstract VIEW_HOLDER onCreateViewHolder(final boolean isEditor);

    /**
     * Called by the underlying table to display the data at the specified
     * index. This method should update the contents of the
     * {@link ViewHolder#view} to reflect the item at the given index.
     * <p>
     *
     * @param viewHolder The ViewHolder which should be updated to represent
     *                   the contents of the item at the given index in the
     *                   data set.
     * @param index      The index of the item within the data set.
     */
    protected abstract void onBindViewHolder(final VIEW_HOLDER viewHolder,
                                             final int index,
                                             final boolean isEditor
    );

    /**
     * Called by the list model (TableModel) while rendering views.
     *
     * @param index the index of the item in the supplied data.
     * @return returns the data model representing the index.
     */
    protected abstract Object getValueAt(int index);

    /**
     * Informs the table model of the amount of items in the data
     *
     * @return the number of data models in your list.
     */
    protected abstract int getItemCount();

    /**
     * Called when editing stops. Passes the value of the object
     * collected by the {@link Editor#getCellEditorValue}. This
     * is generally an updated data model
     *
     * @param index the index of the data model being edited.
     * @param value the edited value of the data model as provided by
     *              {@link Editor#getCellEditorValue()}.
     */
    protected abstract void setValueAt(int index,
                                       Object value
    );
// endregion abstract methods

// region data change notifications

    /**
     * Delegate method that informs the table model of a change in the source data.
     * Implements {@link ModelListener#notifyDatasetChanged()}
     */
    @Override
    public void notifyDatasetChanged() {
        tableModel.fireTableDataChanged();
    }

    /**
     * Delegate method that informs the table model the table structure has changed.
     * Implements {@link ModelListener#notifyDataStructureChanged()}.
     */
    @Override
    public void notifyDataStructureChanged() {
        tableModel.fireTableStructureChanged();
    }

    /**
     * Delegate method that informs the table model of a change in the source data.
     * Implements {@link ModelListener#notifyItemsInserted(int, int)}
     *
     * @param firstIndex the inclusive index of the first row of data inserted.
     * @param lastIndex  the inclusive index of the last row of data inserted.
     */
    @Override
    public void notifyItemsInserted(int firstIndex, int lastIndex) {
        tableModel.fireTableRowsInserted(firstIndex, lastIndex);
    }

    /**
     * Delegate method that informs the table model of a change in the source data.
     * Implements {@link ModelListener#notifyItemsUpdated(int, int)}
     *
     * @param firstIndex the inclusive index of the first item in the source data to be updated.
     * @param lastIndex  the inclusive index of the last item in the source data to be updated.
     */
    @Override
    public void notifyItemsUpdated(int firstIndex,
                                   int lastIndex) {
        // todo, if model updated is model being edited?
        //  When ViewHolder is being created, if it is being created for the editor
        //  then attach a model changed listener to the view
        tableModel.fireTableCellUpdated(firstIndex, lastIndex);
    }

    /**
     * Delegate method that informs the table model of a single item has been
     * updated in the source data.
     * Implements {@link ModelListener#notifyItemsUpdated(int, int)}
     *
     * @param index the index of the updated item in the source data
     */
    @Override
    public void notifyItemUpdated(int index) {
        tableModel.fireTableCellUpdated(index, COLUMN_NUMBER);
    }

    /**
     * Delegate method that informs the table model of a change in the source data.
     * Implements {@link ModelListener#notifyItemsDeleted(int, int)}
     *
     * @param firstIndex the inclusive index of the first row in the source data to be deleted.
     * @param lastIndex  the inclusive index of the last row in the source data to be deleted
     */
    @Override
    public void notifyItemsDeleted(int firstIndex, int lastIndex) {
        tableModel.fireTableRowsDeleted(firstIndex, lastIndex);

    }
// endregion data change notifications.

    /**
     * Implements {@link TableModelListener}. This fine grain notification tells listeners
     * the exact range of cells, rows, or columns that changed.
     *
     * @param e the event, containing the location of the changed model.
     */
    @Override
    public void tableChanged(TableModelEvent e) {

        if (TableModelEvent.DELETE == e.getType()) {
            // If the cell or cells being edited are within the range of the cells that have
            // been been changed, as declared in the table event, then editing must either
            // be cancelled or stopped.
            if (table.isEditing()) {
                TableCellEditor editor = table.getDefaultEditor(ViewHolder.class);
                if (editor != null) {
                    // the coordinate of the cell being edited.
                    int editingColumn = table.getEditingColumn();
                    int editingRow = table.getEditingRow();

                    // the inclusive coordinates of the cells that have changed.
                    int changedColumn = e.getColumn();
                    int firstRowChanged = e.getFirstRow();
                    int lastRowChanged = e.getLastRow();

                    // returns true if the cell being edited is in the range of cells changed
                    boolean editingCellInRangeOfChangedCells =
                            (TableModelEvent.ALL_COLUMNS == changedColumn ||
                                    changedColumn == editingColumn) &&
                                    editingRow >= firstRowChanged &&
                                    editingRow <= lastRowChanged;

                    if (editingCellInRangeOfChangedCells) {

                        editor.cancelCellEditing();
                    }
                }
            }
        }
    }

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
