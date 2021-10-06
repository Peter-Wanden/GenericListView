package ui.mylistview;

import ui.mylistview.MyGenericListViewController.ControlCommand;
import utils.UiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ListViewParent {

    @SuppressWarnings("unused")
    private static final String TAG = "ListViewParent" + ": ";

    private static final String ADD_NEW_MEMBER_BUTTON_NAME = "ADD_NEW_MEMBER_BUTTON_NAME";

    private final JFrame frame;
    private final JLayeredPane layeredPane;
    private final MyGenericListView listView;
    private final MyGenericListViewController listViewController;

    public ListViewParent(MyGenericListViewController listViewController) {
        this.listViewController = listViewController;
        listView = listViewController.getView();
        layeredPane = new JLayeredPane();
        frame = new JFrame();

        setupLayeredPane();
        setupAddNewMemberButton();

        addListeners();
        setupFrame();
    }

    private void setupLayeredPane() {
        layeredPane.setOpaque(true);
        layeredPane.setPreferredSize(listView.getView().getPreferredSize());
        layeredPane.add(listView.getView());
        layeredPane.moveToBack(listView.getView());
    }

    private void setupAddNewMemberButton() {
        var addNewMemberButton = new JButton("+");
        addNewMemberButton.setActionCommand(ControlCommand.ADD_NEW_RECORD_COMMAND.name());
        addNewMemberButton.setName(ADD_NEW_MEMBER_BUTTON_NAME);
        addNewMemberButton.addActionListener(listViewController);

        layeredPane.add(addNewMemberButton);
        layeredPane.moveToFront(addNewMemberButton);
        addNewMemberButton.setBounds(new Rectangle(0, 0, 50, 30));
    }

    private void setupFrame() {
        var availableSize = (Rectangle) UiUtils.getScreenWorkingArea(null);
        var frameSize = new Dimension(
                (availableSize.width / 4),
                availableSize.height
        );

        frame.setPreferredSize(frameSize);
        frame.getContentPane().add(layeredPane);
        frame.setUndecorated(true);
        frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(frameSize.width * 2, 0);
        frame.pack();
        frame.setVisible(true);
    }

    private void addListeners() {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                var parentSize = (Dimension) frame.getSize();
                // synchronises the layered pane size with the parent frame
                layeredPane.setSize(parentSize);

                // fixme, viewport height is too big.
                var listViewSize = new Dimension(
                        parentSize.width, parentSize.height - 200);
                listView.getView().setSize(listViewSize);

                for (var component : layeredPane.getComponents()) {
                    if (component.getName() != null)
                        if (component.getName().equals(ADD_NEW_MEMBER_BUTTON_NAME)) {
                            positionAddNewMemberButton(parentSize, component);
                        }

                }
                layeredPane.revalidate();
                layeredPane.repaint();
            }
        });
    }

    // dynamically positions the button in the bottom right of supplied component.
    private void positionAddNewMemberButton(Dimension parentSize,
                                            Component component) {
        int padding = 10;
        var bottomRightOfPanel = new Point(
                parentSize.width - padding,
                parentSize.height - padding
        );

        var componentSize = (Dimension) component.getSize();

        int w = componentSize.width;
        int h = componentSize.height;
        int x = bottomRightOfPanel.x - w;
        int y = bottomRightOfPanel.y - h * 2;

        var positionAndSize = new Rectangle(
                x, y, w, h
        );

        component.setBounds(positionAndSize);
    }

    public JFrame getView() {
        return frame;
    }
}
