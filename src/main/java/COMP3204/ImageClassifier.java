package COMP3204;

import COMP3204.KNN.KNearestNeighbourClassifier;
import COMP3204.Linear.LinearClassifier;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.data.dataset.VFSListDataset;
import org.openimaj.experiment.dataset.split.GroupedRandomSplitter;
import org.openimaj.experiment.evaluation.classification.ClassificationEvaluator;
import org.openimaj.experiment.evaluation.classification.ClassificationResult;
import org.openimaj.experiment.evaluation.classification.analysers.confusionmatrix.CMAnalyser;
import org.openimaj.experiment.evaluation.classification.analysers.confusionmatrix.CMResult;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.ml.annotation.Annotator;
import org.openimaj.ml.annotation.ScoredAnnotation;

/**
 * A class that loads a list of training and testing images. The training data is used to train each
 * classifier, where we then classify the testing images, of which the output is saved to a txt file
 */
public class ImageClassifier {

    /**
     * Dataset of training image
     */
    private final VFSGroupDataset<FImage> trainingImages;

    /**
     * Dataset of testing images
     */
    private final VFSListDataset<FImage> testingImages;

    /**
     * Loads a list of training and testing images, classifies the images and outputs the result to
     * a txt file
     *
     * @param pathToTestingImages  Path to the training images folder
     * @param pathToTrainingImages Path to the testing images folder
     * @throws IOException
     */
    public ImageClassifier(String pathToTestingImages, String pathToTrainingImages)
            throws IOException {
        this.trainingImages = loadTrainingImages(pathToTrainingImages);
        this.testingImages = loadTestingImages(pathToTestingImages);
    }

    /**
     * Load the list of training images in the training folder. The order of which goes as follows:
     * bedroom - cost - forest - highway - industrial - insidecity - kitchen - living room -
     * mountain - office - opencountry - store - street - suburb - tallbuilding
     *
     * @param pathToTrainingImages Path to the training images folder
     * @return List of training images
     * @throws IOException
     */
    private VFSGroupDataset<FImage> loadTrainingImages(String pathToTrainingImages)
            throws IOException {
        return new VFSGroupDataset<>(pathToTrainingImages, ImageUtilities.FIMAGE_READER);
    }

    /**
     * Load a list of testing images from the testing folder
     *
     * @param pathToTestingImages Path to the testing images folder
     * @return List of testing images
     * @throws IOException
     */
    private VFSListDataset<FImage> loadTestingImages(String pathToTestingImages)
            throws IOException {
        return new VFSListDataset<>(pathToTestingImages, ImageUtilities.FIMAGE_READER);
    }

    /**
     * Write the list of predicted classified images to the output file
     *
     * @param runNumber               The current run
     * @param classifiedTestingImages Images to be classified
     * @throws IOException
     */
    private void writePredictedImages(String runNumber, String[][] classifiedTestingImages)
            throws IOException {
        try {
            //Create the run number file
            File file = new File(runNumber);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            //Write the list of classified test images with the name of the image, and it's predicted class
            for (int i = 0; i < classifiedTestingImages.length; i++) {
                String line = classifiedTestingImages[i][0] + " " + classifiedTestingImages[i][1];
                writer.write(line);
                writer.newLine();
            }

            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error in creating or reading the run file");
        }
    }

    /**
     * Used to create, train and run each classifier against the testing images
     *
     * @throws IOException
     */
    public void classifyImages() throws IOException {
        //Run KNearestNeighbour Classifier
      System.out.println("Running KNN classifier");
		KNearestNeighbourClassifier knn = new KNearestNeighbourClassifier(trainingImages, 39);
		String[][] classifications = annotateTestImages(testingImages, knn.getAnnotator());
		writePredictedImages("run1.txt", classifications);

        //Run Linear Classifier
        System.out.println("Running linear classifier");
        LinearClassifier lc = new LinearClassifier(trainingImages, 1500);
        String[][] classifications2 = annotateTestImages(testingImages, lc.getAnnotator());
        writePredictedImages("run2.txt", classifications2);
    }

    public void evaluateLinearClassifier() {
        GroupedRandomSplitter<String, FImage> splits =
                new GroupedRandomSplitter<>(trainingImages, 80, 0, 20);

        LinearClassifier lc = new LinearClassifier(splits.getTrainingDataset(), 1500);

        ClassificationEvaluator<CMResult<String>, String, FImage> eval =
                new ClassificationEvaluator<>(
                        lc.getAnnotator(), splits.getTestDataset(), new CMAnalyser<FImage, String>(CMAnalyser.Strategy.SINGLE));

        Map<FImage, ClassificationResult<String>> guesses = eval.evaluate();
        CMResult<String> result = eval.analyse(guesses);
        System.out.println(result.getDetailReport());
    }

    public void evaluateKNNClassifier() {
        GroupedRandomSplitter<String, FImage> splits =
                new GroupedRandomSplitter<>(trainingImages, 80, 0, 20);

        KNearestNeighbourClassifier knn = new KNearestNeighbourClassifier(splits.getTrainingDataset(), 39);

        ClassificationEvaluator<CMResult<String>, String, FImage> eval =
                new ClassificationEvaluator<>(
                        knn.getAnnotator(), splits.getTestDataset(), new CMAnalyser<FImage, String>(CMAnalyser.Strategy.SINGLE));

        Map<FImage, ClassificationResult<String>> guesses = eval.evaluate();
        CMResult<String> result = eval.analyse(guesses);
        System.out.println(result.getDetailReport());
    }

    public void evaluateKNNClassifierWithDifferentValues() throws IOException {
        GroupedRandomSplitter<String, FImage> splits =
                new GroupedRandomSplitter<>(trainingImages, 80, 0, 20);

        List<Float> accVals = new ArrayList<>();

        for(int i = 1; i < 51; i++){
            Float accuracy = 0.0f;
            for(int j = 0; j < 50; j++){
                KNearestNeighbourClassifier knn = new KNearestNeighbourClassifier(splits.getTrainingDataset(),  i);

                ClassificationEvaluator<CMResult<String>, String, FImage> eval =
                        new ClassificationEvaluator<>(
                                knn.getAnnotator(), splits.getTestDataset(), new CMAnalyser<FImage, String>(CMAnalyser.Strategy.SINGLE));

                Map<FImage, ClassificationResult<String>> guesses = eval.evaluate();
                CMResult<String> result = eval.analyse(guesses);
                accuracy+= Float.parseFloat(result.getSummaryReport().replaceAll("\n", " ").split(" ")[3]);
            }
            accuracy/=50;
            System.out.println(accuracy + " " + i);
            accVals.add(accuracy);
        }

        FileWriter fileWriter = new FileWriter("run1Acc.txt");
        for (Float str : accVals) {
            fileWriter.write(str.toString() + System.lineSeparator());
        }
        fileWriter.close();
    }

    public void evaluateLinearClassifierWithDifferentValues() throws IOException {
        GroupedRandomSplitter<String, FImage> splits =
                new GroupedRandomSplitter<>(trainingImages, 80, 0, 20);

        List<Float> accVals = new ArrayList<>();

        for(int i = 100; i < 2001; i+=100){
            Float accuracy = 0.0f;
            for(int j = 0; j < 2; j++){
                LinearClassifier lc = new LinearClassifier(splits.getTrainingDataset(),  i);

                ClassificationEvaluator<CMResult<String>, String, FImage> eval =
                        new ClassificationEvaluator<>(
                                lc.getAnnotator(), splits.getTestDataset(), new CMAnalyser<FImage, String>(CMAnalyser.Strategy.SINGLE));

                Map<FImage, ClassificationResult<String>> guesses = eval.evaluate();
                CMResult<String> result = eval.analyse(guesses);
                accuracy+= Float.parseFloat(result.getSummaryReport().replaceAll("\n", " ").split(" ")[3]);
            }
            accuracy/=2;
            System.out.println(accuracy + " " + i);
            accVals.add(accuracy);

            FileWriter fileWriter = new FileWriter("run2Acc.txt");
            for (Float str : accVals) {
                fileWriter.write(str.toString() + System.lineSeparator());
            }
            fileWriter.close();
        }
    }

    /**
     * Classify a list of test images using a classifier
     *
     * @param testImages Dataset of test images to be classified
     * @param classifier Classifier to classify images
     * @return A List of images coupled with their name and class
     */
    private String[][] annotateTestImages(VFSListDataset<FImage> testImages, Annotator classifier) {
        String[][] classifications = new String[testImages.size()][2];

        int i = 0;
        for (FImage image : testImages) {
            if (i % 100 == 0) {
                System.out.println("Completed " + i + "/" + testImages.size() + " classifications");
            }

            //Annotate the image
            List<ScoredAnnotation> scores = classifier.annotate(image);
            String annotation = "";
            double prediction = 0;

            //Get the best score from the annotations
            for (ScoredAnnotation score : scores) {
                if (score.confidence > prediction) {
                    prediction = score.confidence;
                    annotation = score.annotation.toString();
                }
            }

            //Add the image name and classification to the list of values
            String name = testImages.getFileObject(i).getName().getBaseName();
            String[] values = new String[]{name, annotation};

            classifications[i] = values;
            i++;
        }

        return classifications;
    }
}
