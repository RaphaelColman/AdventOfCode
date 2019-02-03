import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class CartTest {

    Track track;

    @Before
    public void setUp() throws IOException {
        //I don't need a mock track because I can read a good one in from file!
        List<String> lines = Files.readAllLines(Paths.get("src/test/ext/13.txt"));
        TrackBuilder trackBuilder = new TrackBuilder(lines, new CartTracker());
        track = trackBuilder.getTrack();
    }

    @Test
    @Parameters(method = "straightMovements")
    public void cart_moves_right_when_on_straight_track(Point start, Point finish, Direction facing) {
        Cart cart = new Cart(facing, start, track);
        cart.move();

        assertThat(cart.getLocation(), is(finish));
    }

    @SuppressWarnings("unused")
    private Object[] straightMovements() {
        return new Object[] {
                new Object[] {new Point(1,0), new Point(2,0), Direction.RIGHT}, //Moves right
                new Object[] {new Point(0,1), new Point(0,2), Direction.DOWN}, //Moves down
                new Object[] {new Point(2,0), new Point(1,0), Direction.LEFT}, //Moves left
                new Object[] {new Point(4,3), new Point(4,2), Direction.UP} //Moves up
        };
    }

    @Test
    @Parameters(method = "curvedMovements")
    public void cart_turns_when_on_curved_piece(Point start, Point finish, Direction wasfacing, Direction nowFacing) {
        Cart cart = new Cart(wasfacing, start, track);
        cart.move();

        assertThat(cart.getLocation(), is(finish));
        assertThat(cart.getFacing(), is(nowFacing));
    }

    @SuppressWarnings("unused")
    private Object[] curvedMovements() {
        return new Object[] {
                //  /-
                //  |
                new Object[] {new Point(0,0), new Point(1,0), Direction.UP, Direction.RIGHT},
                new Object[] {new Point(0,0), new Point(0,1), Direction.LEFT, Direction.DOWN},

                //   |
                //  -/
                new Object[] {new Point(4,4), new Point(3,4), Direction.DOWN, Direction.LEFT},
                new Object[] {new Point(4,4), new Point(4,3), Direction.RIGHT, Direction.UP},

                //  |
                //  \-
                new Object[] {new Point(0,4), new Point(0,3), Direction.LEFT, Direction.UP},
                new Object[] {new Point(0,4), new Point(1,4), Direction.DOWN, Direction.RIGHT},

        };
    }

    @Test
    public void cart_turns_correctly_on_junctions() {

        TrackPiece junctionTrackPiece = new TrackPiece(Direction.values());

        Track mockTrack = Mockito.mock(Track.class);
        when(mockTrack.getTrackPieceForLocation(any(Point.class))).thenReturn(junctionTrackPiece);

        Cart cart = new Cart(Direction.UP, new Point(10, 10), mockTrack);

        cart.move();
        assertThat(cart.getFacing(), is(Direction.LEFT));
        assertThat(cart.getLocation(), is(new Point(9, 10)));

        cart.move();
        assertThat(cart.getFacing(), is(Direction.LEFT));
        assertThat(cart.getLocation(), is(new Point(8, 10)));

        cart.move();
        assertThat(cart.getFacing(), is(Direction.UP));
        assertThat(cart.getLocation(), is(new Point(8, 9)));

        cart.move();
        assertThat(cart.getFacing(), is(Direction.LEFT));
        assertThat(cart.getLocation(), is(new Point(7, 9)));
    }

}