package utils;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.util.ArrayList;
import java.util.List;

/**
 * A listener whose purpose is to be attached to any {@link JTextComponent}.
 * Once attached any registered listeners are informed of changes to
 * the underlying JTextComponents {@link Document} object.
 */
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
        notifyTextChangedListeners();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        notifyTextChangedListeners();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        notifyTextChangedListeners();
    }

    public void addTextChangedListener(TextChangedListener listener) {
        textChangedListeners.add(listener);
    }

    public void removeTextChangedListener(TextChangedListener listener) {
        textChangedListeners.remove(listener);
    }

    private void notifyTextChangedListeners() {
        textChangedListeners.forEach(listener -> listener.textChanged(source));
    }
}
