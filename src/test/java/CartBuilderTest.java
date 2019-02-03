import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;

@RunWith(JUnitParamsRunner.class)
public class CartBuilderTest {

    CartTracker cartTracker;

    @Before
    public void setUp() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(InputFiles.TEST_TRACK.getPath()));
        TrackBuilder trackBuilder = new TrackBuilder(lines, new CartTracker());
        cartTracker = trackBuilder.getCartTracker();
    }

    @Test
    @Parameters(method = "carts")
    public void cartsShouldBeInitialisedInTheRightPlace(Cart expectedCart) {
        Set<Point> locations = cartTracker.getCarts().stream().map(Cart::getLocation).collect(Collectors.toSet());
        assertThat(locations, hasItem(expectedCart.getLocation()));
    }

    @Test
    @Parameters(method = "carts")
    public void cartsShouldBeFacingTheRightWay(Cart expectedCart) {
        Cart cartForLocation = cartTracker.getCarts().stream().filter(cart ->
                cart.getLocation().equals(expectedCart.getLocation())).collect(Collectors.toSet()).iterator().next();

        assertThat(cartForLocation.getFacing(), is(expectedCart.getFacing()));
    }

    @SuppressWarnings("unused")
    private Object[] carts() {
        Track mockTrack = new Track(new HashMap<Point, TrackPiece>());
        return new Object[]{
                new Cart(Direction.RIGHT, new Point(2, 0), mockTrack),
                new Cart(Direction.DOWN, new Point(9, 3), mockTrack)
        };
    }

}