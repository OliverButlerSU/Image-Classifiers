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
 * An abstract class for creating an image classifier that loads a list of training and testing
 * images, of which are then classified and stored into a text file
 */
public abstract class ImageClassifier {

	/**
	 * Dataset of training images
	 */
	protected final VFSGroupDataset<FImage> trainingImages;

	/**
	 * Dataset of testing images
	 */
	protected final VFSListDataset<FImage> testingImages;

	/**
	 * List of the classified testing images
	 */
	protected final String[] classifiedTestingImages;

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
		this.trainingImages = loadTrainingImages(pathToTestingImages);
		this.testingImages = loadTestImages(pathToTrainingImages);
		this.classifiedTestingImages = new String[testingImages.size()];

		classifyImages();
		writePredictedImages();
	}

	/**
	 * Load the list of training images in the training folder. The order of which goes as follows:
	 *     bedroom - cost - forest - highway - industrial - insidecity - kitchen - living room -
	 *     mountain - office - opencountry - store - street - suburb - tallbuilding
	 *
	 * @param pathToTrainingImages Path to the training images folder
	 * @return List of training images
	 * @throws IOException
	 */
	protected VFSGroupDataset<FImage> loadTrainingImages(String pathToTrainingImages)
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
	protected VFSListDataset<FImage> loadTestImages(String pathToTestingImages) throws IOException {
		return new VFSListDataset<>(pathToTestingImages, ImageUtilities.FIMAGE_READER);
	}

	/**
	 * Write the list of predicted classified images to the output file
	 *
	 * @throws IOException
	 */
	protected void writePredictedImages() throws IOException {
		String runNumber;

		//Get the run number for each class (it's a dumb way to do it but make's my life simpler)
		if (this instanceof KNearestNeighbourClassifier) {
			runNumber = "run1.txt";
		} else if (this instanceof LinearClassifier) {
			runNumber = "run2.txt";
		} else {
			runNumber = "run3.txt";
		}

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
	 * Used to classify an image
	 */
	protected abstract void classifyImages();

}
