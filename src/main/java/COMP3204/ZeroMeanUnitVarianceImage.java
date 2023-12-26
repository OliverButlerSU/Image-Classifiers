package COMP3204;

public class ZeroMeanUnitVarianceImage {


	public static float[][] zeroMean(float[][] pixels){
		float sum = 0;

		float[][] newPixels = new float[pixels.length][pixels[0].length];

		for(int i = 0; i < pixels.length; i++){
			for(int j = 0; j < pixels.length; j++){
				sum+=pixels[i][j];
			}
		}

		float mean = sum/(pixels.length* pixels[0].length);

		for(int i = 0; i < pixels.length; i++){
			for(int j = 0; j < pixels.length; j++){
				newPixels[i][j] = pixels[i][j] - mean;
			}
		}

		return newPixels;
	}

	public static float[][] unitVariance(float[][] pixels){
		int sum = 0;

		float[][] newPixels = new float[pixels.length][pixels[0].length];

		for(int i = 0; i < pixels.length; i++){
			for(int j = 0; j < pixels.length; j++){
				sum+=pixels[i][j];
			}
		}

		float mean = sum/(pixels.length* pixels[0].length);
		float sum2 = 0;

		for(int i = 0; i < pixels.length; i++){
			for(int j = 0; j < pixels.length; j++){
				sum2 += (pixels[i][j] - mean) * (pixels[i][j] - mean);
			}
		}

		float sd = (float) Math.sqrt(sum2/(pixels.length* pixels[0].length));

		for(int i = 0; i < pixels.length; i++){
			for(int j = 0; j < pixels.length; j++){
				newPixels[i][j] = pixels[i][j]/sd;
			}
		}

		return newPixels;
	}

	public static float[][] zeroMeanUnitVariance(float[][] pixels){
		return unitVariance(zeroMean(pixels));
	}

}
