package ch.epfl.sweng.studyup;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class FirebaseUnitTest {
    @Test(expected = IllegalArgumentException.class)
    public void wrongSciper() {
        Map<String, Object> thing;
        thing = Firebase.get().getUserData(0);
        thing = Firebase.get().getUserData(999999999);
        thing = Firebase.get().getUserData(-42);
    }

    @Test
    public void correctData() {
        Map<String, Object> thing = Firebase.get().getUserData(0);

        Map<String, Object> expected = new HashMap<>();

        Map<String, String> name = new HashMap<>();
        name.put("firstname", "Jean-Louis");
        name.put("lastname", "Reymond");

        expected.put("name", name);
        expected.put("section", "IN");
        expected.put("year", "BA1");

        assertThat(thing, is(expected));

        assertEquals(thing.get(name), expected.get(name));
        assertEquals(thing.get("section"), expected.get("section"));
        assertEquals(thing.get("year"), expected.get("year"));
    }
}
