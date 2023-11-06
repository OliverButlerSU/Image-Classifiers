package COMP3204;

import java.io.IOException;

/**
 * A K-Nearest-Neighbour Classifier using the tiny image feature
 */
public class KNearestNeighbourClassifier extends ImageClassifier {

	public KNearestNeighbourClassifier(String pathToTestingImages, String pathToTrainingImages)
			throws IOException {
		super(pathToTestingImages, pathToTrainingImages);

	}

	/**
	 * Used to classify an image
	 */
	@Override
	protected void classifyImages() {

	}

}
