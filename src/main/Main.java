package main;

import com.formdev.flatlaf.FlatDarculaLaf;
import data.TestData;
import domain.usecase.UseCaseObservableList;
import ui.mylistview.ListViewParent;
import ui.mylistview.MyGenericListViewController;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to install look and feel ");
        }
        SwingUtilities.invokeLater(Main::new);
    }

    @SuppressWarnings("unused")
    private static final String TAG = "Main" + ": ";

//    private final DataView dataView;

    public Main() {
        var useCase = new UseCaseObservableList();
        var controller = new MyGenericListViewController(useCase);
        var parentView = new ListViewParent(controller);

//        dataView = new DataView(useCase);
//        setupRightFrame(parentView.getView());

        useCase.setModels(TestData.myModels);
    }

//    private void setupRightFrame(Component leftFrame) {
//        Point leftFrameLocation = leftFrame.getLocation();
//        Point rightFrameLocation = new Point(
//                (leftFrameLocation.x + leftFrame.getWidth()),
//                leftFrameLocation.y
//        );
//
//        JFrame rightFrame = new JFrame();
//        rightFrame.getContentPane().add(dataView.getView());
//        rightFrame.setUndecorated(true);
//        rightFrame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
//        rightFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        rightFrame.setLocation(rightFrameLocation);
//        rightFrame.setPreferredSize(leftFrame.getPreferredSize());
//        rightFrame.pack();
//        rightFrame.setVisible(true);
//    }
}
