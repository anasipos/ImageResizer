package ana;

import java.util.ArrayList;
import java.util.List;

import static ana.Utils.getDouble;
import static ana.Utils.getInt;

class FindPhotoSize {

    private static final int MAX_WIDTH = 670;
    private static final int MIN_WIDTH = 630;
    private static final double OPTIMAL_THRESHOLD = 0.1;

    List<Size> calculateOptimalSizes(Size originalSize, String minWidthTxt, String maxWidthTxt, String thresholdTxt) throws Exception {
        int minWidth = getInt(minWidthTxt, MIN_WIDTH);
        int maxWidth = getInt(maxWidthTxt, MAX_WIDTH);
        double threshold = getDouble(thresholdTxt, OPTIMAL_THRESHOLD);
        return calculateOptimalSizes(originalSize, minWidth, maxWidth, threshold);
    }

    private List<Size> calculateOptimalSizes(Size originalSize, int minWidth, int maxWidth, double threshold) throws Exception {
        if (originalSize == null) {
            throw new Exception("Undefined size");
        }

        List<Size> optimalSizes = new ArrayList<Size>();
        double ratio = (double) originalSize.getHeight() / originalSize.getWidth();

        for (int width = minWidth; width <= maxWidth; width++) {
            double height = width * ratio;
            final double calculatedThreshold = height - Math.floor(height);
            if (calculatedThreshold <= threshold) {
                optimalSizes.add(new Size(width, (int) Math.round(height), calculatedThreshold));
            }
        }

        return optimalSizes;
    }
}

