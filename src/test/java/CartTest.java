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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class CartTest {

    Track track;
    CartTracker cartTracker;

    @Before
    public void setUp() throws IOException {
        //I don't need a mock track because I can read a good one in from file!
        List<String> lines = Files.readAllLines(Paths.get("src/test/ext/13.txt"));
        TrackBuilder trackBuilder = new TrackBuilder(lines, new CartTracker());
        track = trackBuilder.getTrack();
        cartTracker = new CartTracker();
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
                new Object[] {new Point(4,3), new Point(4,2), Direction.UP}, //Moves up
        };
    }
}