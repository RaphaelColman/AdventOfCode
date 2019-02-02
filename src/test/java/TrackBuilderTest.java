import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.core.Is.is;

@RunWith(JUnitParamsRunner.class)
public class TrackBuilderTest {

    public static final String TEXT_TRACK_FILE_PATH = "src/test/ext/13.txt";
    private TrackBuilder trackBuilder;

    @Before
    public void setUp() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(TEXT_TRACK_FILE_PATH));
        trackBuilder = new TrackBuilder(lines);
    }

    @Test
    public void initializesHashMapFromFile() {
        HashMap<Point, Character> hmPointToChar = trackBuilder.getHmPointToChar();

        assertThat(hmPointToChar, hasEntry(new Point(0,0), '/'));
        assertThat(hmPointToChar, hasEntry(new Point(0,1), '|'));
        assertThat(hmPointToChar, hasEntry(new Point(2,0), '>'));
        assertThat(hmPointToChar, hasEntry(new Point(3,2), '-'));
    }

    @Test
    @Parameters(method="trackPieces")
    public void pieceShouldBeCorrectlyLabelledBasedOnContext(Point point, TrackPiece expectedTrackPiece) throws TrackPieceNotRecognisedException {
        trackBuilder.getTrackPieceForPoint(point).ifPresent(trackPiece -> {
            Set<Border> exits = trackPiece.getExits();
            assertThat(exits, is(expectedTrackPiece.getExits()));
        });

    }

    @Test
    public void builder_recognises_curved_track_pieces() {
        assertThat(TrackBuilder.isCurvedPiece('\\'), is(true));
        assertThat(TrackBuilder.isCurvedPiece('/'), is(true));
        assertThat(TrackBuilder.isCurvedPiece('+'), is(false));
    }

    @Test
    @Parameters(method="trackPieces")
    public void fullyInitialisedTrackShouldHaveCorrectTrackPieces(Point point, TrackPiece trackPiece) {
        Track track = trackBuilder.getTrack();
        HashMap<Point, TrackPiece> hmPointToTrackPiece = track.getHmPointToTrackPiece();

        assertThat(hmPointToTrackPiece, hasEntry(point, trackPiece));
    }

    @SuppressWarnings("unused")
    private Object[] trackPieces() {
        return new Object[] {
                new Object[] {new Point(0,1), new TrackPiece(Border.TOP, Border.BOTTOM)},
                new Object[] {new Point(1,0), new TrackPiece(Border.RIGHT, Border.LEFT)},
                new Object[] {new Point(4,2), new TrackPiece(Border.values())},

                //Curved pieces
                new Object[] {new Point(0,0), new TrackPiece(Border.BOTTOM, Border.RIGHT)}, // '/'
                new Object[] {new Point(4,4), new TrackPiece(Border.TOP, Border.LEFT)}, // '/'
                new Object[] {new Point(4,0), new TrackPiece(Border.LEFT, Border.BOTTOM)}, // '\'
                new Object[] {new Point(2,5), new TrackPiece(Border.TOP, Border.RIGHT)}, // '\'

                //Carts
                new Object[] {new Point(2,0), new TrackPiece(Border.LEFT, Border.RIGHT)}, // >
                new Object[] {new Point(9,3), new TrackPiece(Border.TOP, Border.BOTTOM)} // v
        };
    }

}