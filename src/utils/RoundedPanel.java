package utils;

import javax.swing.*;
import java.awt.*;

/**
 * Shamelessly stolen for this demo from the following link. Please upvote the poster if this helps you.
 *
 * @see <a href="https://stackoverflow.com/questions/15025092/border-with-rounded-corners-transparency">
 * Border with rounded corners transparency</a>
 */
public class RoundedPanel
        extends JPanel {

    private final int cornerRadius;

    public RoundedPanel(int radius) {
        super();
        cornerRadius = radius;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension arcRadii = new Dimension(
                cornerRadius,
                cornerRadius
        );
        int insetPixels = 0;
        Point coordinate = new Point(
                insetPixels,
                insetPixels
        );
        Dimension area = new Dimension(
                (getWidth() - 1),
                (getHeight() - 1)
        );
        Graphics2D graphics = (Graphics2D) g;

        graphics.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        // Paints the background rectangle.
        graphics.setColor(getBackground());
        graphics.fillRoundRect(
                coordinate.x, coordinate.y,
                area.width, area.height,
                arcRadii.width, arcRadii.height
        );
        // Paints the border rectangle.
        graphics.setColor(getForeground());
        graphics.drawRoundRect(
                coordinate.x, coordinate.y,
                area.width, area.height,
                arcRadii.width, arcRadii.height
        );
    }
}
