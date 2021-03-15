package genericlistview;

import ui.itemview.ItemView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public abstract class AbstractGenericListView
        implements
        ModelListener,
        TableModelListener {

    @SuppressWarnings("unused")
    private static final String TAG = "AbstractGenericListView" + ": ";

    /**
     * Holds the view in a table cell. It is used by both the {@link Renderer}
     * and {@link Editor} as a generic wrapper for the view.
     */
    public static abstract class ViewHolder
            implements ItemView {

        @SuppressWarnings("unused")
        private static final String TAG = "ViewHolder" + ": ";

        // the index of the model data in the model list
        protected final int index;
        // the model
        protected Object model;
        // the view to be rendered
        protected final Component view;
        // the (optional) controller;
        protected final Object controller;

        /**
         * @param view     the view to be rendered.
         * @param index the index of the model in the source data.
         */
        public ViewHolder(int index,
                          Object model,
                          Component view,
                          Object controller) {

            this.index = index;

            if (view == null) {
                throw new IllegalArgumentException("item view may not be null");
            }
            if (model == null) {
                throw new IllegalArgumentException("model may not be null");
            }
            this.model = model;
            this.view = view;
            this.controller = controller;
        }

        // TODO: implement the difference between position and index where:
        //  index - is the index in the model list
        //  position - is the position as displayed in the list.
        @Override
        public int getIndex() {
            return index;
        }

        /**
         * Used by the table cell renderers to get the view from this view holder.
         *
         * @return the view to be rendered.
         */
        @Override
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
        @Override
        public abstract Object getModel();

        public Object getController() {
            return controller;
        }
    }

    /**
     * Renders the view returned by the {@link ViewHolder} to the cell.
     */
    private class Renderer
            implements TableCellRenderer {

        private static final String TAG = "Renderer" + ": ";

        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object model,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {

            ViewHolder viewHolder = onCreateViewHolder(row);
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

        private ViewHolder viewHolder;

        @Override
        public Component getTableCellEditorComponent(JTable table,
                                                     Object value,
                                                     boolean isSelected,
                                                     int row,
                                                     int column) {

            viewHolder = onCreateViewHolder(row);
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
    }

    private class GenericTableModel
            extends AbstractTableModel {

        private static final String TAG = "GenericTableModel" + ": ";

        @Override
        public String getColumnName(int column) {
            return COLUMN_NAME;
        }

        @Override
        public int getRowCount() {
            return getItemCount();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NUMBER + 1;
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
            return ViewHolder.class;
        }

        /**
         * Implements {@link AbstractTableModel#setValueAt(Object, int, int)} method.
         * Called when the cell being edited looses focus. When this happens the
         * {@link Editor#getCellEditorValue()} method is called, which in turn calls
         * the {@link ViewHolder#getModel()} method, delegating the task of getting
         * the current views values and placing them in a model. The model
         * is then passed to the implementer for processing.
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

    public AbstractGenericListView() {

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

    /**
     * Implementers should place their view into the ViewHolder.
     * The view holder is used by both the renderer and editor
     * for displaying views.
     * @param index the index of the item model in the source data.
     * @return your ViewHolder concrete class with your view.
     */
    protected abstract ViewHolder onCreateViewHolder(final int index);

    /**
     * Called by the list model (TableModel) while rendering views.
     * @param index the index of the item in the supplied data.
     * @return returns the data model representing the index.
     */
    protected abstract Object getValueAt(int index);

    /**
     * Informs the table model of the amount of items in the data
     * @return the number of data models in your list.
     */
    protected abstract int getItemCount();

    /**
     * Called when editing stops. Passes the value of the object
     * collected by the {@link Editor#getCellEditorValue}. This
     * is generally an updated data model
     * @param index the index of the data model being edited.
     * @param value the edited value of the data model as provided by
     *              {@link Editor#getCellEditorValue()}.
     */
    protected abstract void setValueAt(int index,
                                       Object value
    );

    /**
     * Access to the root view of this {@link AbstractGenericListView} component.
     * @return an {@link JScrollPane} containing the view.
     */
    public JScrollPane getView() {
        return view;
    }

    /**
     * Delegate method that informs the table model of a change in the source data.
     * Implements {@link ModelListener#notifyDataSetChanged()}
     */
    @Override
    public void notifyDataSetChanged() {
        tableModel.fireTableDataChanged();
    }

    /**
     * Delegate method that informs the table model the table structure has changed.
     */
    @Override
    public void notifyDataStructureChanged() {
        tableModel.fireTableStructureChanged();
    }

    /**
     * Delegate method that informs the table model of a change in the source data.
     * Implements {@link ModelListener#notifyItemsInserted(int, int)}
     *
     * @param firstRow the inclusive index of the first row of data inserted.
     * @param lastRow  the inclusive index of the last row of data inserted.
     */
    @Override
    public void notifyItemsInserted(int firstRow, int lastRow) {
        tableModel.fireTableRowsInserted(firstRow, lastRow);
    }

    /**
     * Delegate method that informs the table model of a change in the source data.
     * Implements {@link ModelListener#notifyItemsUpdated(int, int)}
     *
     * @param firstRow the inclusive index of the first item in the source data to be updated.
     * @param lastRow  the inclusive index of the last item in the source data to be updated.
     */
    @Override
    public void notifyItemsUpdated(int firstRow,
                                   int lastRow) {
        tableModel.fireTableCellUpdated(firstRow, lastRow);
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
     * @param firstRow the inclusive index of the first row in the source data to be deleted.
     * @param lastRow  the inclusive index of the last row in the source data to be deleted
     */
    @Override
    public void notifyItemsDeleted(int firstRow, int lastRow) {
        tableModel.fireTableRowsDeleted(firstRow, lastRow);

    }

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
}
