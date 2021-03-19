package ui.dataview;

import data.MyModel;
import domain.UseCaseObservableList;
import genericlistview.ModelListener;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

public class DataView
        implements ModelListener {

    @SuppressWarnings("unused")
    private static final String TAG = "DataView";

    static final String[] columnNames = {
            "First name",
            "Last name",
            "Age",
            "Is member"
    };

    class DataViewTableModel
            extends AbstractTableModel {

        private static final String TAG = "DataViewTableModel" + ": ";

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public int getRowCount() {
            System.out.println(TAG + "getRowCount: calling useCase.getItemCount");
            return useCase.getItemCount();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            System.out.println(TAG + "getValueAt: " + "useCase: getModels called.");
            MyModel myModel = useCase.getModels().get(rowIndex);
            switch (columnIndex) {
                case 0 -> {return myModel.getFirstName();}
                case 1 -> {return myModel.getLastName();}
                case 2 -> {return myModel.getAge();}
                case 3 -> {return myModel.isMember();}
                default -> {return new Object();}
            }
        }
    }

    JTable table = new JTable();
    JScrollPane view = new JScrollPane(table);
    DataViewTableModel tableModel = new DataViewTableModel();
    UseCaseObservableList useCase;

    public DataView(UseCaseObservableList useCase) {
        this.useCase = useCase;
        this.useCase.addModelListener(this);

        table.setModel(tableModel);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setOpaque(true);
    }

    @Override
    public void notifyDatasetChanged() {
        tableModel.fireTableDataChanged();
    }

    @Override
    public void notifyDataStructureChanged() {
        tableModel.fireTableStructureChanged();
    }

    @Override
    public void notifyItemsInserted(int firstIndex, int lastIndex) {
        tableModel.fireTableRowsInserted(firstIndex, lastIndex);
    }

    @Override
    public void notifyItemsUpdated(int firstIndex, int lastIndex) {
        tableModel.fireTableRowsUpdated(firstIndex, lastIndex);
    }

    @Override
    public void notifyItemUpdated(int index) {
        tableModel.fireTableRowsUpdated(index, index);
    }

    @Override
    public void notifyItemsDeleted(int firstIndex, int lastIndex) {
        tableModel.fireTableRowsDeleted(firstIndex, lastIndex);
    }

    public JScrollPane getView() {
        return view;
    }
}
