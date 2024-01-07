package COMP3204.Linear;

import COMP3204.ZeroMeanUnitVarianceImage;

import java.util.ArrayList;
import java.util.List;

import org.openimaj.image.FImage;

/**
 * A Densely Sampled Pixel Patch Feature Extractor
 */
public class DSPPExtractor {

    /**
     * Size of patch
     */
    private static final int patchSize = 8;

    /**
     * Sample distance in x and y
     */
    private static final int sampleDistance = 4;

    /**
     * Extract a DSPP fron an image
     *
     * @param image Input image
     * @return List of bytes representing patches
     */
    public static List<byte[]> extractDSPP(FImage image) {
        List<byte[]> dspp = new ArrayList<>();

        // Iterate over the image every sample distance in both x and y
        for (int i = 0; i < image.height - patchSize; i += sampleDistance) {
            for (int j = 0; j < image.width - patchSize; j += sampleDistance) {
                //Extract the patch
                float[][] patch = extractPatch(image, i, j);

                //Apply zero mean and unit variance
                patch = ZeroMeanUnitVarianceImage.zeroMeanUnitVariance(patch);

                //Convert to byte image and add to list
                dspp.add(new FImage(patch).toByteImage());
            }
        }

        return dspp;
    }

    /**
     * Extract a patch
     *
     * @param image Input image
     * @param y     Top most Y position of patch
     * @param x     Left most X position of patch
     * @return Pixels at the location
     */
    private static float[][] extractPatch(FImage image, int y, int x) {
        float[][] pixels = new float[patchSize][patchSize];

        for (int i = 0; i < patchSize; i++) {
            for (int j = 0; j < patchSize; j++) {
                pixels[i][j] = image.getPixel(x + j, y + i);
            }
        }
        return pixels;
    }
}
