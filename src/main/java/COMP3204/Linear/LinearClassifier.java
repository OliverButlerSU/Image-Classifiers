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
 * A 15v1 Multi Class Linear Classifier using bag-of-visual-words clustered with K-Means using a feature extractor based on fixed size densely-sampled pixel patches
 */
public class LinearClassifier {

    /**
     * Annotator to classify images
     */
    private final LiblinearAnnotator annotator;

    /**
     * Size of KMeans cluster
     */
    private final int clusterSize = 1500;

    public LinearClassifier(VFSGroupDataset trainingImages) {

        System.out.println("Training KMeans Quantiser");
        HardAssigner<byte[], float[], IntFloatPair> assigner = trainQuantiser(
                new StratifiedGroupedUniformRandomisedSampler(0.2).sample(trainingImages));
        
        annotator = new LiblinearAnnotator<FImage, String>(new BOVWFeatureExtractor(assigner),
                Mode.MULTICLASS, SolverType.L2R_L2LOSS_SVC, 1.0, 0.0001);

        System.out.println("Training annotator");
        annotator.train(trainingImages);
    }

    /**
     * Train a Quantiser using KMeans
     *
     * @param sample Sample of data to train the Quantiser
     * @return HardAssigner
     */
    private HardAssigner<byte[], float[], IntFloatPair> trainQuantiser(Dataset<FImage> sample) {
        List<byte[]> allkeys = new ArrayList<>();

        for (FImage rec : sample) {
            allkeys.addAll(DSPPExtractor.extractDSPP(rec));
        }

        System.out.println("Creating KD Tree Ensemble");
        ByteKMeans km = ByteKMeans.createKDTreeEnsemble(clusterSize);

        System.out.println("Clustering data");
        byte[][] datasource = allkeys.toArray(new byte[0][]);
        ByteCentroidsResult result = km.cluster(datasource);
        return result.defaultHardAssigner();

    }

    /**
     * Getter for annotator
     *
     * @return annotator
     */
    public LiblinearAnnotator getAnnotator() {
        return annotator;
    }
}
