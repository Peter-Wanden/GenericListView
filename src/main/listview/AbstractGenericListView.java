package main.listview;

import main.itemview.ItemView;
import main.itemview.ModelListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;

@SuppressWarnings("All")
public abstract class AbstractGenericListView
        implements
        ModelListener,
        TableModelListener {

    @SuppressWarnings("unused")
    private static final String TAG = "AbstractGenericListView" + ": ";

    private static final String COLUMN_NAME = "COLUMN_NAME";

    /**
     * Holds the view in a table cell. It is used by both the {@link Renderer}
     * and {@link Editor} as a generic wrapper for the view.
     */
    public static abstract class ViewHolder {

//        private static final String TAG = "ViewHolder" + ": ";

        // the position (index) of the model data in the model list
        protected final int position;
        // the model
        protected Object model;
        // the view to be rendered
        protected final Component view;
        // the views controller
        protected final Object controller;

        /**
         * @param view the view to be rendered
         * @param position the position (index) of the data
         */
        public ViewHolder(int position,
                          Object model,
                          Component view,
                          Object controller) {

//            System.out.println(TAG + "ViewHolder constructor initialised at position: " + position +
//                    " with model: " + model);

            this.position = position;

            if(view == null) {
                throw new IllegalArgumentException("item view may not be null");
            }
            if (model == null) {
                throw new IllegalArgumentException("model may not be null");
            }

            this.controller = controller;
            this.model = model;
            this.view = view;
        }

        // todo, implement the difference between position and index where:
        //  index - is the index in the model list
        //  position - is the position as displayed in the list.
        public int getPosition() {
            return position;
        }
        /**
         * Used by the table cell renderers to get the view from this view holder.
         * @return the view to be rendered.
         */
        public Component getView() {
            return view;
        }

        /**
         * The model passed into the {@link ViewHolder}. If this view
         * holder has been rendered by the {@link Editor} and data within
         * the view has been edited, you should collect it and return it
         * with this method.
         * @return either the model passed into the view or a model updated
         * within the view, post editing.
         */
        public abstract Object getModel();

        public Object getController() {
            return controller;
        };
    }

    /**
     * Renders the view returned by the {@link ViewHolder} to the cell.
     */
    private class Renderer
            implements TableCellRenderer {

        private static final String TAG = "Renderer" + ": ";

        private Renderer() {
            super();
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object model,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {

//            System.out.println(TAG + "getTableRendererComponent: rendering model=" + model);

            ViewHolder viewHolder = onCreateViewHolder(row);
            return viewHolder.getView();
        }
    }

    /**
     * When a cell is selected this class returns a view for editing the data.
     * When the cell looses focus; the return key pressed, or another cell
     * clicked for example; the {@link Renderer} returns a view to display
     * the data.
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

//            System.out.println(TAG + "getTableCellEditorComponent: editing model=" + value);

            viewHolder = onCreateViewHolder(row);
            return viewHolder.getView();
        }

        /**
         * When editing of the cell is complete, i.e when {@link JTable#editingStopped(ChangeEvent)}
         * method is triggered it calls this method to get the edited values.
         * So this is where you get the edited values from your view in the current instance the view holder.
         * @return the model values in the view
         */
        @Override
        public Object getCellEditorValue() {
//            System.out.println(TAG + "getCellEditorValue(), returns the viewHolders model=" + viewHolder.getModel());
            return viewHolder.getModel();
        }
    }

    // todo, table is refreshing data on every event. You read about this. Take a look at
    //  default table model to see how this is resolved.
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
            return 1;
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
         * @param model the updated model.
         * @param rowIndex the row or index of the item in the data model.
         * @param columnIndex the column of the cell.
         */
        @Override
        public void setValueAt(Object model,
                               int rowIndex,
                               int columnIndex) {

//            System.out.println(TAG + "setValueAt: edited model=" + model);
            // todo, where does it get it's value from?
            //  This is the value returned by the {@link Editor} I think.
            AbstractGenericListView.this.setValueAt(rowIndex, model);
        }
    }

    private final JTable table;
    private final AbstractTableModel genericTableModel;
    private final JScrollPane view;

    public AbstractGenericListView() {

        genericTableModel = new GenericTableModel();
        genericTableModel.addTableModelListener(this);

        table = new JTable();
        setupTable();

        view = new JScrollPane(table);
    }

    private void setupTable() {

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setIntercellSpacing(new Dimension(5, 5));
        table.setRowHeight(ItemView.CELL_HEIGHT); // todo, change to ViewHolder.getView().getPreferredSize().getHeight()
        table.setFillsViewportHeight(true);
        table.setModel(genericTableModel);
        table.setTableHeader(null);
        table.setShowGrid(false);
        table.setOpaque(true);

        TableColumn column = table.getColumn(COLUMN_NAME);
        column.setCellRenderer(new Renderer());
        column.setCellEditor(new Editor());
    }

    protected abstract ViewHolder onCreateViewHolder(int position);

    protected abstract Object getValueAt(int position);

    protected abstract int getItemCount();

    protected abstract void setValueAt(int position,
                                       Object value
    );

    protected abstract void valueChanged(int position, Object value);

    /**
     * Access to the root view of this {@link AbstractGenericListView} component
     * @return an {@link JScrollPane} containing the view.
     */
    public JScrollPane getView() {
        return view;
    }

    /**
     * Delegate method that informs the table model of a change in the source data.
     * Implements {@link ModelListener#notifyItemUpdated(int)}
     * @param index the index of the item in the source data
     */
    @Override
    public void notifyItemUpdated(int index) {
        genericTableModel.fireTableCellUpdated(index, 0);
//        System.out.println(TAG + "notifyItemUpdated=" + getValueAt(index));
    }

    /**
     * Delegate method that informs the table model of a change in the source data.
     * Implements {@link ModelListener#notifyDataSetChanged()}
     */
    @Override
    public void notifyDataSetChanged() {
//        System.out.println(TAG + "notifyDatasetChanged");
        genericTableModel.fireTableDataChanged();
    }

    /**
     * Delegate method that informs the table model of a change in the source data.
     * Implements {@link ModelListener#notifyItemDeleted(int)}
     * @param index the index of the item in the source data
     */
    @Override
    public void notifyItemDeleted(int index) {
//        System.out.println(TAG + "notifyItemDeleted: model=" + genericTableModel.getValueAt(index, 0));
        genericTableModel.fireTableRowsDeleted(index, index);
    }

    /**
     * Delegate method that informs the table model of a change in the source data.
     * Implements {@link ModelListener#notifyItemInserted(int)}
     * @param index the index of the item in the source data
     */
    @Override
    public void notifyItemInserted(int index) {
//        System.out.println(TAG + "notifyItemInserted");
        genericTableModel.fireTableRowsInserted(index, index);
    }

    /**
     * Implements {@link TableModelListener}. Triggered after a table cell has
     * been edited.
     * @param e the event, containing the location of the changed model.
     */
    @Override
    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = 0; // we only have one column
        Object model = genericTableModel.getValueAt(row, column);
//        System.out.println(TAG + "tableChanged. Implements TableModelListener: Model=" + model);
        valueChanged(row, model);
    }
}
