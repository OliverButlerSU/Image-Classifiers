package COMP3204.KNN;

import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.ml.annotation.basic.KNNAnnotator;

/**
 * A K-Nearest-Neighbour Classifier using the tiny image feature
 */
public class KNearestNeighbourClassifier {

    /**
     * Annotator to classify images
     */
    private final KNNAnnotator annotator;

    /**
     * Used to train the classifier
     *
     * @param trainingImages Images to train classifier
     */
    public KNearestNeighbourClassifier(GroupedDataset trainingImages, int kValue) {
        //N = sqrt(number of data points in training data?) (should be odd)
        //15 classifiers x 100 data points = 1500  sqrt(1500) = 38.7 (we will round up)
        annotator = KNNAnnotator.create(new TinyImageFeatureExtractor(),
                DoubleFVComparison.EUCLIDEAN, kValue);

        annotator.train(trainingImages);
    }

    /**
     * Getter for annotator
     *
     * @return annotator
     */
    public KNNAnnotator getAnnotator() {
        return annotator;
    }
}
