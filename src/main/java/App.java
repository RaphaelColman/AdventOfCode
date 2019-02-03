import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class App {

    public static void main(String[] args) {
        //Do nothing. I haven't finished the tests yet!
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(InputFiles.ACTUAL_TRACK.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        TrackBuilder trackBuilder = new TrackBuilder(lines, new CartTracker());
        Track track = trackBuilder.getTrack();
        CartTracker cartTracker = trackBuilder.getCartTracker();

        int index = 0;
        while (cartTracker.getCollisions().size() == 0) {
            cartTracker.moveCarts();
            index++;

            if (index > 1000) {
                return;
            }
        }
        System.out.println("Carts collided at: " + cartTracker.getCollisions());
    }
}
