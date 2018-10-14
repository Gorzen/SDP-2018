package ch.epfl.sweng.studyup;

import org.junit.Test;

public class FirebaseUnitTest {
    @Test(expected = IllegalArgumentException.class)
    public void sciperTooHigh() {
        Firebase.get().getAndSetUserData(0, "testFirstName", "testLastName");

    }

    @Test(expected = IllegalArgumentException.class)
    public void sciperTooLow() {
        Firebase.get().getAndSetUserData(999999999, "testFirstName", "testLastName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void sciperNegative() {
        Firebase.get().getAndSetUserData(-42, "testFirstName", "testLastName");
    }
}
