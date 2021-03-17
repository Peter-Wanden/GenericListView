package ui.itemview;

import genericlistview.ModelListenerAdapter;

import java.awt.*;

public abstract class ItemView
        extends ModelListenerAdapter {

    public abstract int getIndex();

    public abstract Object getModel();

    public abstract void bindModel(Object myModel);

    public abstract Component getView();
}
