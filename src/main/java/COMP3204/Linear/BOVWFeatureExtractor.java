package COMP3204.Linear;

import org.openimaj.feature.DoubleFV;
import org.openimaj.feature.FeatureExtractor;
import org.openimaj.image.FImage;

public class BOVWFeatureExtractor implements FeatureExtractor<DoubleFV, FImage> {

	@Override
	public DoubleFV extractFeature(FImage image) {
		//8x8 patches, sampled every 4 pixels in x and y direction. Cluster using K-Means to learn vocab (500 clusters to start)
		//Consider using Mean centered and normalising each patch before clustering/quantisation.

		return new DoubleFV(image.getDoublePixelVector());
	}
}
