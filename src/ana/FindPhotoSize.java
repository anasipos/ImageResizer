package ana;

import java.util.ArrayList;
import java.util.List;

public class FindPhotoSize {

    private static final int MAX_WIDTH = 670;
    private static final int MIN_WIDTH = 630;
    private static final double OPTIMAL_THRESHOLD = 0.1;

    private int photoMaxWidth = MAX_WIDTH;
    private int photoMinWidth = MIN_WIDTH;

    private double threshold = OPTIMAL_THRESHOLD;

    public int getPhotoMaxWidth() {
        return photoMaxWidth;
    }

    public void setPhotoMaxWidth(int photoMaxWidth) {
        this.photoMaxWidth = photoMaxWidth;
    }

    public int getPhotoMinWidth() {
        return photoMinWidth;
    }

    public void setPhotoMinWidth(int photoMinWidth) {
        this.photoMinWidth = photoMinWidth;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }

    List<Size> calculateOptimalSizes(Size originalSize) throws Exception {
        if (originalSize == null) {
            throw new Exception("Undefined size");
        }

        List<Size> optimalSizes = new ArrayList<Size>();
        double ratio = (double) originalSize.getHeight() / originalSize.getWidth();

        for (int width = photoMinWidth; width <= photoMaxWidth; width++) {
            double height = width * ratio;
            final double calculatedThreshold = height - Math.floor(height);
            if (calculatedThreshold <= threshold) {
                optimalSizes.add(new Size(width, (int) Math.round(height), calculatedThreshold));
            }
        }

        return optimalSizes;
    }
}

