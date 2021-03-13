package ui.itemview;

import data.MyModel;

import javax.swing.*;

public interface ItemView {
    MyModel getModel();

    void bindModel(MyModel myModel);

    JPanel getView();
}
