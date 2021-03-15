package ui.itemview;

import java.awt.*;

public interface ItemView {

    int getIndex();

    Object getModel();

    void bindModel(Object myModel);

    Component getView();
}
