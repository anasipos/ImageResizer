package ana;

public class Size {

    private int width;
    private int height;
    private double threshold;

    Size(int width, int height, double threshold) {
        this.width = width;
        this.height = height;
        this.threshold = threshold;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return
                "width=\"" + width + "\" " +
                        "height=\"" + height + "\"";
    }

    String displaySize() {
        return String.format("width=\"%d\" height=\"%d\" --> %.4f", width, height, threshold);
    }
}
