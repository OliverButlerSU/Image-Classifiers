package COMP3204.Linear;

import de.bwaldvogel.liblinear.SolverType;
import java.util.ArrayList;
import java.util.List;
import org.openimaj.data.dataset.Dataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.experiment.dataset.sampling.StratifiedGroupedUniformRandomisedSampler;
import org.openimaj.image.FImage;
import org.openimaj.ml.annotation.linear.LiblinearAnnotator;
import org.openimaj.ml.annotation.linear.LiblinearAnnotator.Mode;
import org.openimaj.ml.clustering.ByteCentroidsResult;
import org.openimaj.ml.clustering.assignment.HardAssigner;
import org.openimaj.ml.clustering.kmeans.ByteKMeans;
import org.openimaj.util.pair.IntFloatPair;

/**
 * A Linear Classifier using a bag-of-visual-words feature based on fixed size densely-sampled pixel
 * patches
 */
public class LinearClassifier {

	private final LiblinearAnnotator annotator;
	private final int clusterSize = 500;

	public LinearClassifier(VFSGroupDataset trainingImages) {
		System.out.println("Training Quantiser");
		HardAssigner<byte[], float[], IntFloatPair> assigner = trainQuantiser(
				new StratifiedGroupedUniformRandomisedSampler(0.2).sample(trainingImages));

		System.out.println("Creating Liblinear annotator");
		annotator = new LiblinearAnnotator<FImage, String>(new BOVWFeatureExtractor(assigner),
				Mode.MULTICLASS, SolverType.L2R_L2LOSS_SVC, 1.0, 0.0001);

		System.out.println("Training annotator");
		annotator.train(trainingImages);
	}

	private HardAssigner<byte[],float[], IntFloatPair> trainQuantiser(Dataset<FImage> sample) {
		List<byte[]> allkeys = new ArrayList<>();

		int i = 0;
		for (FImage rec : sample) {
			System.out.println(i++ + "/" + sample.numInstances() + " extracted DSPP");
			allkeys.addAll(DSPPExtractor.extractDSPP(rec));
		}

		System.out.println("Creating KD Tree Ensemble");
		ByteKMeans km = ByteKMeans.createKDTreeEnsemble(clusterSize);
		byte[][] datasource = allkeys.toArray(new byte[0][]);
		System.out.println("Clustering data");
		ByteCentroidsResult result = km.cluster(datasource);
		System.out.println("Returning hard assigner");
		return result.defaultHardAssigner();

	}

	public LiblinearAnnotator getAnnotator(){
		return annotator;
	}
}
