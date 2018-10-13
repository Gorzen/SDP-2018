package ch.epfl.sweng.studyup;

import org.junit.Test;

public class FirebaseUnitTest {
    @Test(expected = IllegalArgumentException.class)
    public void wrongSciper() {
        Firebase.get().getUser(0);
        Firebase.get().getUser(999999999);
        Firebase.get().getUser(-42);
    }
}
