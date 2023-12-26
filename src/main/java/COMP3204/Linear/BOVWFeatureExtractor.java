package COMP3204.Linear;

import org.openimaj.feature.DoubleFV;
import org.openimaj.feature.FeatureExtractor;
import org.openimaj.image.FImage;
import org.openimaj.image.feature.local.aggregate.BagOfVisualWords;
import org.openimaj.ml.clustering.assignment.HardAssigner;
import org.openimaj.util.pair.IntFloatPair;

public class BOVWFeatureExtractor implements FeatureExtractor<DoubleFV, FImage> {

	HardAssigner<byte[], float[], IntFloatPair> assigner;

	public BOVWFeatureExtractor(HardAssigner<byte[], float[], IntFloatPair> assigner){
		this.assigner = assigner;
	}

	@Override
	public DoubleFV extractFeature(FImage image) {
		return new BagOfVisualWords<>(assigner).aggregateVectorsRaw(DSPPExtractor.extractDSPP(image)).asDoubleFV();
	}
}
