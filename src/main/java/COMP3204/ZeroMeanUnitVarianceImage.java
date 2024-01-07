package COMP3204;

/**
 * A class for applying zero mean and unit variance to a 2d array of pixels
 */
public class ZeroMeanUnitVarianceImage {

    /**
     * Apply zero mean to image
     *
     * @param pixels Image
     * @return Image with zero mean applied
     */
    public static float[][] zeroMean(float[][] pixels) {
        float sum = 0;

        float[][] newPixels = new float[pixels.length][pixels[0].length];

        //Find the sum of the array
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels.length; j++) {
                sum += pixels[i][j];
            }
        }

        //Find the mean
        float mean = sum / (pixels.length * pixels[0].length);

        //Remove the mean from all pixels
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels.length; j++) {
                newPixels[i][j] = pixels[i][j] - mean;
            }
        }

        return newPixels;
    }

    /**
     * Apply unit variance to image
     *
     * @param pixels Image
     * @return Image with unit variance applied
     */
    public static float[][] unitVariance(float[][] pixels) {
        int sum = 0;

        float[][] newPixels = new float[pixels.length][pixels[0].length];

        //Find the sum of the array
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels.length; j++) {
                sum += pixels[i][j];
            }
        }

        //Find the mean
        float mean = sum / (pixels.length * pixels[0].length);
        float sum2 = 0;

        //Calculate the variance
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels.length; j++) {
                sum2 += (pixels[i][j] - mean) * (pixels[i][j] - mean);
            }
        }

        //Calculate the standard deviation
        float sd = (float) Math.sqrt(sum2 / (pixels.length * pixels[0].length));

        //Divide by standard deviation
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels.length; j++) {
                newPixels[i][j] = pixels[i][j] / sd;
            }
        }

        return newPixels;
    }

    /**
     * Apply both zero mean and unit variance to an image
     *
     * @param pixels Image
     * @return Image with zero mean and unit variance applied
     */
    public static float[][] zeroMeanUnitVariance(float[][] pixels) {
        return unitVariance(zeroMean(pixels));
    }

}
