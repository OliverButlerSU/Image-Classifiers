package COMP3204.Linear;

import de.bwaldvogel.liblinear.SolverType;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.image.FImage;
import org.openimaj.ml.annotation.linear.LiblinearAnnotator;
import org.openimaj.ml.annotation.linear.LiblinearAnnotator.Mode;

/**
 * A Linear Classifier using a bag-of-visual-words feature based on fixed size densely-sampled pixel
 * patches
 */
public class LinearClassifier {

	private final LiblinearAnnotator annotator;

	public LinearClassifier(VFSGroupDataset trainingImages) {
		annotator = new LiblinearAnnotator<FImage, String>(new BOVWFeatureExtractor(),
				Mode.MULTICLASS, SolverType.L1R_L2LOSS_SVC, 1.0, 0.0001);
		annotator.train(trainingImages);
	}

	public LiblinearAnnotator getAnnotator(){
		return annotator;
	}
}
