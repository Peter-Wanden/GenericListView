package main;

import com.formdev.flatlaf.FlatDarculaLaf;
import main.data.TestData;
import main.dataview.DataView;
import main.domain.UseCase;
import main.listview.AbstractGenericListView;
import main.listview.MyGenericListViewController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to install look and feel ");
        }
        new Main();
    }

    private static final String TAG = "Main" + ": ";

    private final AbstractGenericListView listView;
    private final MyGenericListViewController controller;

    private final JLayeredPane layeredPane;
    private final DataView dataView;
    private final JFrame leftFrame;
    private static final String addNewMember = "Add member";

    public Main() {
        UseCase useCase = new UseCase();
        controller = new MyGenericListViewController(useCase);

        leftFrame = new JFrame();
        leftFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension parentSize = leftFrame.getSize();

                // Create a new Layout class that this method delegates to and
                // child views listen to. Use the Swing ActionListener interface?
                // Find out how layout managers to it.
                //

                layeredPane.setSize(parentSize);
                listView.getView().setSize(parentSize);

                for (Component component : layeredPane.getComponents()) {

                    if (component.getName() != null)
                        if (component.getName().equals(addNewMember)) {
                            positionAddNewMemberButton(parentSize, component);
                        }

                }
                layeredPane.revalidate();
                layeredPane.repaint();
            }
        });

        JPanel basePanel = new JPanel();
        basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.PAGE_AXIS));

        layeredPane = new JLayeredPane();

        layeredPane.setOpaque(true);

        Button button = new Button("Click to add a new member");
        button.setName(addNewMember);
        dataView = new DataView();

        listView = controller.getView();

        layeredPane.add(listView.getView());
        layeredPane.moveToBack(listView.getView());
        Dimension listViewSize = listView.getView().getPreferredSize();
        listView.getView().setBounds(new Rectangle(0, 0,
                (int) listViewSize.getWidth(), (int) listViewSize.getHeight())
        );

        layeredPane.add(button);
        layeredPane.moveToFront(button);
        button.setBounds(new Rectangle(100, 100, 50, 30));

        setupRightFrame(getRightFrameLocation(setupLeftFrame()));

        useCase.setModels(TestData.myModels);
        dataView.setModels(TestData.myModels);
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

    private JFrame setupLeftFrame() {
        leftFrame.setPreferredSize(new Dimension(500, 1000));

        leftFrame.getContentPane().add(layeredPane);
        leftFrame.setUndecorated(true);
        leftFrame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        leftFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        leftFrame.setLocation(950, 0);
        leftFrame.pack();
        leftFrame.setVisible(true);
        return leftFrame;
    }

    private Point getRightFrameLocation(Frame leftFrame) {
        Point leftFrameLocation = leftFrame.getLocation();
        return new Point(
                leftFrameLocation.x + leftFrame.getWidth(),
                leftFrameLocation.y);
    }

    private void setupRightFrame(Point rightFrameLocation) {
        JFrame rightFrame = new JFrame();
        rightFrame.getContentPane().add(dataView.getView());
        rightFrame.setUndecorated(true);
        rightFrame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        rightFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rightFrame.setLocation(rightFrameLocation);
        rightFrame.setPreferredSize(leftFrame.getPreferredSize());
        rightFrame.pack();
        rightFrame.setVisible(true);
    }
}
