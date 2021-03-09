package main.itemview;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.util.ArrayList;
import java.util.List;

public class TextListener
        implements DocumentListener {

    private static final String TAG = "TextListener" + ": ";

    public interface TextChangedListener {
        void textChanged(JTextComponent source);
    }

    private final List<TextChangedListener> textChangedListeners;
    private final JTextComponent source;

    public TextListener(JTextComponent source) {
        textChangedListeners = new ArrayList<>();
        this.source = source;
        this.source.getDocument().addDocumentListener(this);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
//        System.out.println(TAG + "insertUpdate");
        notifyTextChangedListeners();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
//        System.out.println(TAG + "removeUpdate");
        notifyTextChangedListeners();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
//        System.out.println(TAG + "changedUpdate");
        notifyTextChangedListeners();
    }

    public void addTextChangedListener(TextChangedListener listener) {
        textChangedListeners.add(listener);
    }

    public void removeTextChangedListener(TextChangedListener listener) {
        textChangedListeners.remove(listener);
    }

    private void notifyTextChangedListeners() {
//        System.out.println(TAG + "textUpdated()");
        textChangedListeners.forEach(listener -> listener.textChanged(source));
    }
}
