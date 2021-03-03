package main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ParentView::new);
    }

    /**
     * Represents the model for the view. Models passed to
     * the <code>ViewHolder</code> must either implement or
     * be wrapped using the {@link ListView.ViewHolder.Model}
     * interface. A the view holder model interface is a
     * tagging interface there are no methods to implement.
     */
    static class ModelWrapper
            implements ListView.ViewHolder.Model {

        TestData.Model model;

        public ModelWrapper(TestData.Model model) {
            this.model = model;
        }

        TestData.Model getModel() {
            return model;
        }

        @Override
        public String toString() {
            return "ModelWrapper{" +
                    "model=" + model +
                    '}';
        }
    }

    /**
     * Views passed to {@link ListView} must either extend or
     * be wrapped by the {@link ListView.ViewHolder} abstract
     * class. This abstract class needs to know how to bind
     * and retrieve model values to and from the view, as well
     * as providing access to the view.
     */
    static class ViewWrapper
            extends ListView.ViewHolder<ModelWrapper> {

        TestView view;

        public ViewWrapper(ModelWrapper modelWrapper) {
            super(modelWrapper);
            view = new TestView();
        }

        @Override
        public Component getView() {
            return view.getView();
        }

        @Override
        public ModelWrapper getModel() {
            TestView.FormView formView = view.getFormView();
            TestView.ControlsView controlsView = view.getControlsView();

            model = new ModelWrapper(
                    new TestData.Model(
                            formView.firstNameField.getText(),
                            formView.lastNameField.getText(),
                            Integer.parseInt(formView.ageField.getText()),
                            controlsView.getIsMemberCheckBox().isSelected()
                    )
            );

            return model;
        }

        @Override
        public void bindModel(ModelWrapper wrapper) {
            TestData.Model model = wrapper.getModel();

            TestView.FormView formView = view.getFormView();
            formView.firstNameField.setText(model.firstName);
            formView.lastNameField.setText(model.lastName);
            formView.ageField.setText(String.valueOf(model.age));

            TestView.ControlsView controlsView = view.getControlsView();
            controlsView.getIsMemberCheckBox().setSelected(model.isMember);
        }
    }

    static class ParentView extends JFrame {
        ParentView() {
            ViewWrapper viewWrapper = new ViewWrapper(
                    new ModelWrapper(
                            new TestData.Model())
            );

            ListView<ViewWrapper, ModelWrapper> listView = new ListView<>(viewWrapper);

            getContentPane().add(listView.getView());

            setUndecorated(true);
            getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            pack();
            setVisible(true);

            List<ModelWrapper> wrappedModels = new ArrayList<>();
            TestData.models.forEach(model -> wrappedModels.add(new ModelWrapper(model)));

            listView.getAdapter().setModels(wrappedModels);
        }
    }
}
