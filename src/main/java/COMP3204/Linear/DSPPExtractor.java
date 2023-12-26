package COMP3204.Linear;

import COMP3204.ZeroMeanUnitVarianceImage;
import java.util.ArrayList;
import java.util.List;
import org.openimaj.image.FImage;

public class DSPPExtractor {

	private static final int patchSize = 8;
	private static final int sampleSize = 4;

	public static List<byte[]> extractDSPP(FImage image){
		List<byte[]> dspp = new ArrayList<>();

		for(int i = 0; i < image.height - patchSize; i+=sampleSize){
			for(int j = 0; j < image.width - patchSize; j+=sampleSize){
				float[][] patch = extractPatch(image, i, j);
				patch = ZeroMeanUnitVarianceImage.zeroMeanUnitVariance(patch);
				dspp.add(new FImage(patch).toByteImage());
			}
		}

		return dspp;
	}

	private static float[][] extractPatch(FImage image, int y, int x){
		float[][] pixels = new float[patchSize][patchSize];

		for(int i = 0; i < patchSize; i++){
			for (int j = 0; j < patchSize; j++){
				pixels[i][j] = image.getPixel(x+j, y+i);
			}
		}
		return pixels;
	}
}
