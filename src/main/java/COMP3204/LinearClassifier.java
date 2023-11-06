package COMP3204;

import java.io.IOException;

/**
 * A Linear Classifier using a bag-of-visual-words feature based on fixed size densely-sampled pixel
 * patches
 */
public class LinearClassifier extends ImageClassifier {

	public LinearClassifier(String pathToTestingImages, String pathToTrainingImages)
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
