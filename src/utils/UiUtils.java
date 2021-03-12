package utils;

import java.awt.*;

public class UiUtils {

    public static Rectangle getMaximumScreenBounds() {
        int minx=0, miny=0, maxx=0, maxy=0;
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (GraphicsDevice device : environment.getScreenDevices()) {
            Rectangle bounds = device.getDefaultConfiguration().getBounds();
            minx = Math.min(minx, bounds.x);
            miny = Math.min(miny, bounds.y);
            maxx = Math.max(maxx,  bounds.x+bounds.width);
            maxy = Math.max(maxy, bounds.y+bounds.height);
        }
        return new Rectangle(minx, miny, maxx-minx, maxy-miny);
    }

    /**
     * getScreenInsets, This returns the insets of the screen, which are defined by any task bars
     * that have been set up by the user. This function accounts for multi-monitor setups. If a
     * window is supplied, then the the monitor that contains the window will be used. If a window
     * is not supplied, then the primary monitor will be used.
     */
    static public Insets getScreenInsets(Window windowOrNull) {
        Insets insets;
        if (windowOrNull == null) {
            insets = Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment
                    .getLocalGraphicsEnvironment().getDefaultScreenDevice()
                    .getDefaultConfiguration());
        } else {
            insets = windowOrNull.getToolkit().getScreenInsets(
                    windowOrNull.getGraphicsConfiguration());
        }
        return insets;
    }

    /**
     * getScreenWorkingArea, This returns the working area of the screen. (The working area excludes
     * any task bars.) This function accounts for multi-monitor setups. If a window is supplied,
     * then the the monitor that contains the window will be used. If a window is not supplied, then
     * the primary monitor will be used.
     */
    static public Rectangle getScreenWorkingArea(Window windowOrNull) {
        Insets insets;
        Rectangle bounds;
        if (windowOrNull == null) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            insets = Toolkit.getDefaultToolkit().getScreenInsets(ge.getDefaultScreenDevice()
                    .getDefaultConfiguration());
            bounds = ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        } else {
            GraphicsConfiguration gc = windowOrNull.getGraphicsConfiguration();
            insets = windowOrNull.getToolkit().getScreenInsets(gc);
            bounds = gc.getBounds();
        }
        bounds.x += insets.left;
        bounds.y += insets.top;
        bounds.width -= (insets.left + insets.right);
        bounds.height -= (insets.top + insets.bottom);
        return bounds;
    }

    /**
     * getScreenTotalArea, This returns the total area of the screen. (The total area includes any
     * task bars.) This function accounts for multi-monitor setups. If a window is supplied, then
     * the the monitor that contains the window will be used. If a window is not supplied, then the
     * primary monitor will be used.
     */
    static public Rectangle getScreenTotalArea(Window windowOrNull) {
        Rectangle bounds;
        if (windowOrNull == null) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            bounds = ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        } else {
            GraphicsConfiguration gc = windowOrNull.getGraphicsConfiguration();
            bounds = gc.getBounds();
        }
        return bounds;
    }
}
