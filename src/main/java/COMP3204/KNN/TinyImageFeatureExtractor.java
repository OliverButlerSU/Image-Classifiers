package COMP3204.KNN;

import COMP3204.ZeroMeanUnitVarianceImage;
import org.openimaj.feature.DoubleFV;
import org.openimaj.feature.FeatureExtractor;
import org.openimaj.image.FImage;
import org.openimaj.image.processing.resize.ResizeProcessor;

/**
 * A Feature Extractor class based off of a "Tiny Image". We make the image square by the centre. We then scale the image to a
 * smaller resolution. The image then has zero mean and unit variance applied to it. This is then packed into a Double Float Vector.
 */
public class TinyImageFeatureExtractor implements FeatureExtractor<DoubleFV, FImage> {

	/**
	 * Used to change the resolution of the image
	 */
	static final int IMAGE_RESOLUTION = 16;

	@Override
	public DoubleFV extractFeature(FImage image) {
		//Crop image to be square
		int getLowestSize = Math.min(image.height, image.width);
		FImage croppedImage = image.extractCenter(getLowestSize, getLowestSize);

		//Scale image(16x16)
		FImage scaledImage = croppedImage.processInplace(new ResizeProcessor(IMAGE_RESOLUTION, IMAGE_RESOLUTION));

		//Apply zero mean and unit length
		scaledImage.pixels = ZeroMeanUnitVarianceImage.zeroMean(scaledImage.pixels); //Does this even make it better?
		scaledImage.pixels = ZeroMeanUnitVarianceImage.unitVariance(scaledImage.pixels); //Does this even make it better?

		//Flatten image into a Double Pixel Vector
		return new DoubleFV(scaledImage.getDoublePixelVector());
	}
}
