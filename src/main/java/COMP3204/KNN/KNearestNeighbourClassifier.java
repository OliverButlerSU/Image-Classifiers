package COMP3204.KNN;

import java.util.List;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.data.dataset.VFSListDataset;
import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.FImage;
import org.openimaj.ml.annotation.ScoredAnnotation;
import org.openimaj.ml.annotation.basic.KNNAnnotator;

/**
 * A K-Nearest-Neighbour Classifier using the tiny image feature
 */
public class KNearestNeighbourClassifier{

	private final KNNAnnotator annotator;

	public KNearestNeighbourClassifier(VFSGroupDataset trainingImages) {

		//Test to maybe use either EUCLIEAN, MANHATTEN? Not sure which to use but EUCLIDEAN SEEMS BEST?
		//N = sqrt(number of data points in training data?) (should be odd)
		//15 classifiers x 100 data points = 1500  sqrt(1500) = 38.7 (we will round up)
		annotator = KNNAnnotator.create(new TinyImageFeatureExtractor(),
				DoubleFVComparison.EUCLIDEAN, 39);
		annotator.train(trainingImages);
	}

	public KNNAnnotator getAnnotator(){
		return annotator;
	}
}
