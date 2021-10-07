package domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.beans.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Use case item is a simple domain object ({@link data.MyModel}) wrapped in a
 * bean. This enables the domain object to employ property change, vetoable
 * change and change support, facilitating a natural way to talk to Swing.
 */
@SuppressWarnings("HardCodedStringLiteral")
public class UseCaseItemTest {

    private static final String TAG = "UseCaseItemTest" + ": ";

    private final String newLine = "\n";

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private ChangeEventListener changeListener;
    private PreChangeVetoListener vetoableChangeListener;
    private PostChangeListener propertyChangeListener;
    // endregion helper fields ---------------------------------------------------------------------

    private UseCaseItem SUT;

    @BeforeEach
    public void setup() {
        SUT = givenUseCase();
    }

    private UseCaseItem givenUseCase() {
        return new UseCaseItem();
    }

    @Test
    @DisplayName("Testing for bean info")
    public void testForBeanInfo() {
        // Arrange
        BeanInfo info = null;
        try {
            info = Introspector.getBeanInfo(UseCaseItem.class);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        // Act
        if (info == null) System.out.println(TAG + "there is no bean info");
        else System.out.println(TAG + "Bean info is as follows: " +
                "\n Descriptor= " + info.getBeanDescriptor() +
                "\n PropertyDescriptors=" + getPropertyDescriptorsAsString(info.getPropertyDescriptors())
        );
        // Assert
    }

    @DisplayName("PropertyChangeListener: PropertyChange: first name")
    @Test
    public void postChangeFirstName()
            throws PropertyVetoException {
        // Arrange
        String firstName = "Peter";
        propertyChangeListener = new PostChangeListener();
        SUT.addPropertyChangeListener(propertyChangeListener);

        // Act
        SUT.setFirstName(firstName);

        // Assert listener registered and fired
        PropertyChangeEvent event = propertyChangeListener.postChangeEvent;

        assertEquals(
                UseCaseItem.FIRST_NAME,
                event.getPropertyName()
        );

        assertEquals(
                "",
                event.getOldValue()
        );

        assertEquals(
                firstName,
                event.getNewValue()
        );
    }

    @DisplayName("PropertyChangeListener: PropertyChange: last name")
    @Test
    public void postChangeLastName()
            throws PropertyVetoException {
        // Arrange
        String value = "Wanden";
        propertyChangeListener = new PostChangeListener();
        SUT.addPropertyChangeListener(propertyChangeListener);

        // Act
        SUT.setLastName(value);

        // Assert listener registered and fired
        PropertyChangeEvent event = propertyChangeListener.postChangeEvent;

        assertEquals(
                UseCaseItem.LAST_NAME,
                event.getPropertyName()
        );

        assertEquals(
                "",
                event.getOldValue()
        );

        assertEquals(
                value,
                event.getNewValue()
        );
    }

    @DisplayName("PropertyChangeListener: PropertyChange: age")
    @Test
    public void postChangeAge()
            throws PropertyVetoException {

        // Arrange
        int value = 50;
        propertyChangeListener = new PostChangeListener();
        SUT.addPropertyChangeListener(propertyChangeListener);

        // Act
        SUT.setAge(value);

        // Assert listener registered and fired
        PropertyChangeEvent event = propertyChangeListener.postChangeEvent;

        assertEquals(
                UseCaseItem.AGE,
                event.getPropertyName()
        );

        assertEquals(
                0,
                event.getOldValue()
        );

        assertEquals(
                value,
                event.getNewValue()
        );
    }

    @DisplayName("PropertyChangeListener: PropertyChange: isMember")
    @Test
    public void postChangeIsMember()
            throws PropertyVetoException {

        // Arrange
        propertyChangeListener = new PostChangeListener();
        SUT.addPropertyChangeListener(propertyChangeListener);

        // Act
        SUT.setMember(true);

        // Assert listener registered and fired
        PropertyChangeEvent event = propertyChangeListener.postChangeEvent;

        assertEquals(
                UseCaseItem.IS_MEMBER,
                event.getPropertyName()
        );

        assertFalse(
                (Boolean) event.getOldValue()
        );
        assertTrue(
                (Boolean) event.getNewValue()
        );
    }

    @DisplayName("VetoableChangeListener: PropertyChange: firstName, shouldVeto=false")
    @Test
    public void preChangeFirstNameVetoFalse()
            throws PropertyVetoException {

        // Arrange
        String value = "Peter";
        vetoableChangeListener = new PreChangeVetoListener();
        vetoableChangeListener.shouldVeto = false;
        SUT.addVetoableChangeListener(vetoableChangeListener);

        // Act
        SUT.setFirstName(value);

        // Assert
        PropertyChangeEvent event = vetoableChangeListener.preChangeEvent;

        assertEquals(
                UseCaseItem.FIRST_NAME,
                event.getPropertyName()
        );

        assertEquals(
                "",
                event.getOldValue()
        );

        assertEquals(
                value,
                event.getNewValue()
        );
    }

    @DisplayName("VetoableChangeListener: PropertyChange: firstName, shouldVeto=true")
    @Test
    public void preChangeFirstNameVetoTrue() {

        // Arrange
        String value = "Peter";
        vetoableChangeListener = new PreChangeVetoListener();
        vetoableChangeListener.shouldVeto = true;
        SUT.addVetoableChangeListener(vetoableChangeListener);
        PropertyVetoException vetoException = null;

        // Act
        try {
            SUT.setFirstName(value);
        } catch (PropertyVetoException e) {
            vetoException = e;
        } finally {

            PropertyChangeEvent event = vetoableChangeListener.preChangeEvent;

            // Assert
            assertEquals(
                    UseCaseItem.FIRST_NAME,
                    event.getPropertyName()
            );

            assertEquals(
                    "",
                    event.getOldValue()
            );

            assertEquals(
                    value,
                    event.getNewValue()
            );

            assertNotNull(vetoException);

            System.out.println(TAG + vetoException);
        }
    }

    // Todo: Test ChangeListeners
    // Todo: Test getters
    // Todo: Test removing listeners

    // region helper methods -----------------------------------------------------------------------
    private String getPropertyDescriptorsAsString(PropertyDescriptor[] descriptors) {

        StringBuilder sb = new StringBuilder();
        for (PropertyDescriptor pd : descriptors) {
            sb
                    .append("name=").append(pd.getName()).append(newLine)
                    .append("propertyType=").append(pd.getPropertyType()).append(newLine)
                    .append("shortDescription=").append(pd.getShortDescription()).append(newLine)
                    .append("propertyEditorClass=").append(pd.getPropertyEditorClass()).append(newLine)
                    .append("displayName=").append(pd.getDisplayName()).append(newLine);
        }
        return sb.toString();
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class PreChangeVetoListener
            implements VetoableChangeListener {

        private static final String TAG = "PreChangeVetoListener" + ": ";

        private PropertyChangeEvent preChangeEvent;
        private boolean shouldVeto;

        @Override
        public void vetoableChange(PropertyChangeEvent evt)
                throws PropertyVetoException {

            preChangeEvent = evt;

            if (shouldVeto) throw new PropertyVetoException(
                    "eventVetoed. Property change event=", preChangeEvent
            );
        }
    }

    private static class PostChangeListener
            implements PropertyChangeListener {

        private PropertyChangeEvent postChangeEvent;

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            postChangeEvent = evt;
        }
    }

    private class ChangeEventListener
            implements ChangeListener {

        private ChangeEvent changeEvent;

        @Override
        public void stateChanged(ChangeEvent e) {
            changeEvent = e;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}