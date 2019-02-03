import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.mockito.Mockito.when;

public class CartTrackerTest {

    CartTracker cartTracker;

    @Before
    public void setUp() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(InputFiles.TEST_TRACK_2.getPath()));
        TrackBuilder trackBuilder = new TrackBuilder(lines, new CartTracker());
        cartTracker = trackBuilder.getCartTracker();
    }

    @Test
    public void cartCollisionsAreDetected() {
        CartTracker cartTracker = new CartTracker();

        //Add two carts at the same location
        addCartsToTrackAtSameLocation(cartTracker, 2, new Point(2,2));
        addCartsToTrackAtSameLocation(cartTracker, 2, new Point(5,5));

        Set<Point> collisions = cartTracker.getCollisions();

        assertThat(collisions, allOf(hasItems(new Point(2,2), new Point(5,5)), iterableWithSize(2)));
    }

    private void addCartsToTrackAtSameLocation(CartTracker cartTrackerWithCollidedCarts, int numberOfCartsToAdd, Point location) {
        IntStream.range(0, numberOfCartsToAdd).forEach(value -> {
            Cart cart = Mockito.mock(Cart.class);
            when(cart.getLocation()).thenReturn(new Point(location));
            cartTrackerWithCollidedCarts.addCart(cart);
        });
    }

    @Test
    public void cartsAreRemovedAfterColliding() {
        cartTracker.moveCarts(1);

        assertThat(cartTracker.getCarts(), iterableWithSize(3));
    }

    @Test
    public void finalCartLocationShouldBeCorrect() {
        Point finalCartLocation = cartTracker.getFinalCartLocation();
        assertThat(finalCartLocation, is(new Point(6,4)));
    }

}