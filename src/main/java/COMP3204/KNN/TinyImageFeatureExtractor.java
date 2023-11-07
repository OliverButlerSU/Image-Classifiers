package COMP3204.KNN;

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
		scaledImage.pixels = zeroMean(scaledImage.pixels); //Does this even make it better?
		scaledImage.pixels = unitVariance(scaledImage.pixels);

		//Flatten image into a Double Pixel Vector
		return new DoubleFV(scaledImage.getDoublePixelVector());
	}

	public float[][] zeroMean(float[][] pixels){
		float sum = 0;

		float[][] newPixels = new float[pixels.length][pixels[0].length];

		for(int i = 0; i < pixels.length; i++){
			for(int j = 0; j < pixels.length; j++){
				sum+=pixels[i][j];
			}
		}

		float mean = sum/(pixels.length* pixels[0].length);

		for(int i = 0; i < pixels.length; i++){
			for(int j = 0; j < pixels.length; j++){
				newPixels[i][j] = pixels[i][j] - mean;
			}
		}

		return newPixels;
	}

	public float[][] unitVariance(float[][] pixels){
		int sum = 0;

		float[][] newPixels = new float[pixels.length][pixels[0].length];

		for(int i = 0; i < pixels.length; i++){
			for(int j = 0; j < pixels.length; j++){
				sum+=pixels[i][j];
			}
		}

		float mean = sum/(pixels.length* pixels[0].length);
		float sum2 = 0;

		for(int i = 0; i < pixels.length; i++){
			for(int j = 0; j < pixels.length; j++){
				sum2 += (pixels[i][j] - mean) * (pixels[i][j] - mean);
			}
		}

		float sd = (float) Math.sqrt(sum2/(pixels.length* pixels[0].length));

		for(int i = 0; i < pixels.length; i++){
			for(int j = 0; j < pixels.length; j++){
				newPixels[i][j] = pixels[i][j]/sd;
			}
		}

		return newPixels;
	}
}
