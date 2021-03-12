package ui.mylistview;

import genericlistview.AbstractGenericListView;
import utils.UiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ListViewParent {

    private static final String TAG = "ListViewParent" + ": ";

    public static final String ADD_NEW_MEMBER_COMMAND = "ADD_NEW_MEMBER_COMMAND";
    public static final String ADD_NEW_MEMBER_BUTTON_NAME = "ADD_NEW_MEMBER_BUTTON_NAME";

    private final JFrame frame;
    private final JLayeredPane layeredPane;
    private final AbstractGenericListView listView;
    private final MyGenericListViewController listViewController;

    public ListViewParent(MyGenericListViewController listViewController) {
        this.listViewController = listViewController;
        listView = listViewController.getView();

        layeredPane = new JLayeredPane();
        layeredPane.setOpaque(true);
        layeredPane.add(listView.getView());
        layeredPane.moveToBack(listView.getView());

        Dimension listViewSize = listView.getView().getPreferredSize();
        listView.getView().setBounds(new Rectangle(0, 0,
                listViewSize.width, listViewSize.height)
        );


        JPanel basePanel = new JPanel();
        basePanel.setLayout(
                new BoxLayout(
                        basePanel, BoxLayout.PAGE_AXIS)
        );

        JButton addNewMemberButton = new JButton("+");
        addNewMemberButton.setActionCommand(ADD_NEW_MEMBER_COMMAND);
        addNewMemberButton.setName(ADD_NEW_MEMBER_BUTTON_NAME);

        layeredPane.add(addNewMemberButton);
        layeredPane.moveToFront(addNewMemberButton);
        addNewMemberButton.setBounds(new Rectangle(0, 0, 50, 30));

        frame = new JFrame();
        addListeners();
        setupFrame();
    }

    private void setupFrame() {
        Rectangle availableSize = UiUtils.getScreenWorkingArea(null);
        Dimension frameSize = new Dimension(
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
                Dimension parentSize = frame.getSize();
                layeredPane.setSize(parentSize);
                listView.getView().setSize(parentSize);

                for (Component component : layeredPane.getComponents()) {

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

    private void positionAddNewMemberButton(Dimension parentSize,
                                            Component component) {
        int padding = 10;
        Point bottomRightOfPanel = new Point(
                parentSize.width - padding,
                parentSize.height - padding
        );

        Dimension componentSize = component.getSize();

        int w = componentSize.width;
        int h = componentSize.height;
        int x = bottomRightOfPanel.x - w;
        int y = bottomRightOfPanel.y - h * 2;

        Rectangle positionAndSize = new Rectangle(x, y, w, h);

        component.setBounds(positionAndSize);
    }

    public JFrame getView() {
        return frame;
    }
 }
