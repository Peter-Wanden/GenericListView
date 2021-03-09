package main.dataview;

import main.data.MyModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class DataView {

    class TblMdl extends AbstractTableModel {

        String[] columnNames = {"First name", "Last name", "Age", "Is member"};
        List<MyModel> myModels = new ArrayList<>();

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public int getRowCount() {
            return myModels.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            MyModel myModel = myModels.get(rowIndex);
            switch (columnIndex) {
                case 0 -> {return myModel.getFirstName();}
                case 1 -> {return myModel.getLastName();}
                case 2 -> {return myModel.getAge();}
                case 3 -> {return myModel.isMember();}
                default -> {return new Object();}
            }
        }

        void setModels(List<MyModel> myModels) {
            this.myModels = myModels;
            fireTableDataChanged();
        }
    }

    JTable table = new JTable();
    JScrollPane view = new JScrollPane(table);
    public TblMdl tableModel = new TblMdl();

    public DataView() {
        table.setModel(tableModel);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setOpaque(true);
    }

    public JScrollPane getView() {
        return view;
    }

    public void setModels(List<MyModel> myModels) {
        tableModel.setModels(myModels);
    }
}
