package COMP3204;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;

/**
 * An abstract class for creating an image classifier that loads a list of training and
 * testing images, of which are then classified and stored into a text file
 */
public abstract class ImageClassifier {

	/**
	 * List of training images
	 */
	protected final ArrayList<ArrayList<FImage>> trainingImages;

	/**
	 * List of testing images
	 */
	protected final ArrayList<FImage> testingImages;

	/**
	 * List of the classified test images
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
	protected ArrayList<ArrayList<FImage>> loadTrainingImages(String pathToTrainingImages)
			throws IOException {
		try {
			//Check if the directory to training images exists
			File trainingImagesDirectory = new File(
					Files.createTempDirectory(pathToTrainingImages).toString());

			if (!trainingImagesDirectory.isDirectory()) {
				throw new IOException("Path to training images does not exist");
			}

			trainingImagesDirectory = new File(pathToTrainingImages);

			//Get the list of folders in the training directory, and create a new arraylist to store the values
			File[] listOfClassifiers = trainingImagesDirectory.listFiles();
			ArrayList<ArrayList<FImage>> trainingImages = new ArrayList<>();

			//For each directory in training
			for (int i = 0; i < listOfClassifiers.length; i++) {

				//Get the list of files in each directory
				File[] listOfImages = listOfClassifiers[i].listFiles();
				ArrayList<FImage> images = new ArrayList<>();

				//For each file in each sub-directory
				for (int j = 0; j < listOfImages.length; i++) {

					//Read the image and add it to the list of training images
					images.add(ImageUtilities.readF(listOfImages[j]));
				}
				trainingImages.add(images);
			}

			return trainingImages;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Load a list of testing images from the testing folder
	 *
	 * @param pathToTestingImages Path to the testing images folder
	 * @return List of testing images
	 * @throws IOException
	 */
	protected ArrayList<FImage> loadTestImages(String pathToTestingImages) throws IOException {
		try {
			//Check if the directory to testing images exists
			File testingImagesDirectory = new File(
					Files.createTempDirectory(pathToTestingImages).toString());

			if (!testingImagesDirectory.isDirectory()) {
				throw new IOException("Path to testing images does not exist");
			}

			testingImagesDirectory = new File(pathToTestingImages);

			//Get the list of images in the testing directory, and create a new arraylist to store the values
			File[] listOfImages = testingImagesDirectory.listFiles();
			ArrayList<FImage> testingImages = new ArrayList<>();

			//For each image in testing
			for (int i = 0; i < listOfImages.length; i++) {

				//Read the image and add it to the list of testing images
				testingImages.add(ImageUtilities.readF(listOfImages[i]));
			}

			return testingImages;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
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
			//Create the run number fule
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
