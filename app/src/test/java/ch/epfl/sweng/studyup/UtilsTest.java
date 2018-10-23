package ch.epfl.sweng.studyup;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static ch.epfl.sweng.studyup.utils.Utils.waitAndTag;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class UtilsTest {
    @Test
    public void waitInterrupted() {
        PowerMockito.mockStatic(Log.class);

        PowerMockito.when(Log.w("FAIL","Test was interrupted: ")).thenThrow(InterruptedException.class);

        waitAndTag(500, "FAIL");

        // ?
    }
}
