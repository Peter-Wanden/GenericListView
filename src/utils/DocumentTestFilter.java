package utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

@SuppressWarnings("ALL")
public class DocumentTestFilter
        extends DocumentFilter {

    private static final String TAG = "DocumentTestFilter" + ": ";
    private boolean isLogging = true;

    @Override
    public void remove(FilterBypass fb,
                       int offset,
                       int length)
            throws BadLocationException {

        if (isLogging) System.out.println(TAG + "remove:" +
                " filterBypass=" + fb +
                " offset=" + offset +
                " length=" + length);

        super.remove(fb, offset, length);
    }

    @Override
    public void insertString(FilterBypass fb,
                             int offset,
                             String string,
                             AttributeSet attr)
            throws BadLocationException {

        if (isLogging) System.out.println(TAG + "insertString:" +
                " filterBypass=" + fb +
                " offset=" + offset +
                " string=" + string +
                " attr=" + attr);

        super.insertString(fb, offset, string, attr);
    }

    @Override
    public void replace(FilterBypass fb,
                        int offset,
                        int length,
                        String text,
                        AttributeSet attrs)
            throws BadLocationException {

        if (isLogging) System.out.println(TAG + "replace:" +
                " filterBypass=" + fb +
                " offset=" + offset +
                " length=" + length +
                " string=" + text +
                " attrs=" + attrs);

        super.replace(fb, offset, length, text, attrs);
    }
}
