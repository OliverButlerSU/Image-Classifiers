package COMP3204.Linear;

import org.openimaj.feature.DoubleFV;
import org.openimaj.feature.FeatureExtractor;
import org.openimaj.image.FImage;
import org.openimaj.image.feature.local.aggregate.BagOfVisualWords;
import org.openimaj.ml.clustering.assignment.HardAssigner;
import org.openimaj.util.pair.IntFloatPair;

/**
 * A Bag of Visual Words Feature Extractor
 */
public class BOVWFeatureExtractor implements FeatureExtractor<DoubleFV, FImage> {

    public BagOfVisualWords bovw;

    /**
     * Initializer for BOVW class
     *
     * @param assigner KMeans hard assigner
     */
    public BOVWFeatureExtractor(HardAssigner<byte[], float[], IntFloatPair> assigner) {
        this.bovw = new BagOfVisualWords<>(assigner);
    }

    /**
     * Extract the features of an image using BOVW and DSPP
     *
     * @param image Input image
     * @return Double Float Vector representing the image
     */
    @Override
    public DoubleFV extractFeature(FImage image) {
        return bovw.aggregateVectorsRaw(DSPPExtractor.extractDSPP(image)).asDoubleFV();
    }
}
