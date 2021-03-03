package main;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class ListView<
        VIEW_HOLDER extends ListView.ViewHolder<MODEL>,
        MODEL extends ListView.ViewHolder.Model
        > {

    public abstract static class ViewHolder<
            MODEL extends ViewHolder.Model
            > {

        interface Model {}

        protected MODEL model;

        public ViewHolder(MODEL model) {
            this.model = model;
        }

        public abstract Component getView();

        public abstract MODEL getModel();

        public abstract void bindModel(MODEL model);
    }

    public static class Adapter<
            MODEL extends ViewHolder.Model
            >
            extends AbstractTableModel {

        private static final String TAG = "Adapter" + ": ";

        public static final String COLUMN_NAME = "COLUMN_NAME";
        private final List<MODEL> models = new ArrayList<>();

        @Override
        public String getColumnName(int column) {
            return COLUMN_NAME;
        }

        @Override
        public int getRowCount() {
            return models.size();
        }

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return models.get(rowIndex);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            System.out.println(TAG + "getColumnClass()"); // fixme, not being called!
            return ViewHolder.class;
        }

        @Override
        public void setValueAt(Object model, int rowIndex, int columnIndex) {
            System.out.println(TAG + "setValueAt:" + (MODEL) model);
            models.set(rowIndex, (MODEL) model);
        }

        public void setModels(List<MODEL> models) {
            this.models.clear();
            this.models.addAll(models);
            fireTableDataChanged();
        }
    }

    private class Renderer
            extends
            AbstractCellEditor
            implements
            TableCellRenderer,
            TableCellEditor {

        private static final String TAG = "Renderer" + ": ";

        private final VIEW_HOLDER viewHolder;
        private Object editedModel;

        public Renderer(VIEW_HOLDER viewHolder) {
            super();
            this.viewHolder = viewHolder;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table,
                                                     Object model,
                                                     boolean isSelected,
                                                     int row,
                                                     int column) {
            System.out.println(TAG + "getTableCellEditorComponent");
            //noinspection unchecked
            return getView(table, (MODEL) model);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object model,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            System.out.println(TAG + "getTableCellRendererComponent");
            //noinspection unchecked
            return getView(table, (MODEL) model);
        }

        public Component getView(JTable table, MODEL model) {
            viewHolder.bindModel(model);

            if (table == null) return viewHolder.getView();
            if (model == null) return viewHolder.getView();

            viewHolder.getView().setEnabled(table.isEnabled());

            return viewHolder.getView();
        }

        @Override
        public Object getCellEditorValue() {
            System.out.println(TAG + "getCellEditorValue");
            return editedModel;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            if (e instanceof MouseEvent) {
                return ((MouseEvent) e).getClickCount() >= 1;
            }
            return true;
        }

        @Override
        public boolean stopCellEditing() {
            editedModel = viewHolder.getModel();
            fireEditingStopped();
            return true;
        }
    }

    private final Adapter<MODEL> adapter;
    private final JScrollPane view;

    public ListView(VIEW_HOLDER viewHolder) {

        JTable table = new JTable();
        table.setModel(adapter = new Adapter<>());
        table.setFillsViewportHeight(true);
        table.setShowGrid(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setIntercellSpacing(new Dimension(5,5));
        table.setRowHeight(viewHolder.getView().getPreferredSize().height);

        TableColumn column = table.getColumn(Adapter.COLUMN_NAME);
        Renderer renderer = new Renderer(viewHolder);
        column.setCellRenderer(renderer);
        column.setCellEditor(renderer);

        view = new JScrollPane();
        view.setViewportView(table);
        view.setOpaque(true);
    }

    public Adapter<MODEL> getAdapter() {
        return adapter;
    }

    public JScrollPane getView() {
        return view;
    }
}
