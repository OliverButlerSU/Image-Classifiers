package COMP3204;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.data.dataset.VFSListDataset;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;

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

		classifyImages();
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
	 * @throws IOException
	 */
	private void writePredictedImages(String runNumber, String[] classifiedTestingImages)
			throws IOException {
		try {
			//Create the run number file
			File file = new File(runNumber);
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));

			//Write the list of classified test images with the name of the image, and it's predicted class
			for (int i = 0; i < classifiedTestingImages.length; i++) {
				String line = i + ".jpg " + classifiedTestingImages[i];
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
	 * Used to classify the test images
	 */
	private void classifyImages() {
		//Run KNearestNeighbour Classifier
		//Run Linear Classifier
		//Run 3rd Classifier

		//writePredictedImages("run1.txt", KNearestNeighbour Data);
		//writePredictedImages("run2.txt", Linear Data);
		//writePredictedImages("run3.txt", 3rd Data);
	}

}
