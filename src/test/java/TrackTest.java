import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.*;
import java.util.HashMap;

@RunWith(MockitoJUnitRunner.class)
public class TrackTest {

    @Mock
    private HashMap<Point, TrackPiece> hmPointToTrackpiece;

    @Test
    public void trackGetsCorrectTrackPieces() {
        Track track = new Track(hmPointToTrackpiece);
        Point location = new Point(1, 1);
        track.getTrackPieceForLocation(location);

        Mockito.verify(hmPointToTrackpiece).get(location);
    }
}