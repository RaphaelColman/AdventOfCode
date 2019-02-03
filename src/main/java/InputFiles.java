import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum InputFiles {
    TEST_TRACK("src/test/ext/13.txt"), TEST_TRACK_2("src/test/ext/13_2.txt"), ACTUAL_TRACK("ext/13.txt");

    @NonNull private String path;

    public String getPath() {
        return path;
    }
}
