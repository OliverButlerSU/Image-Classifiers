package COMP3204;


public class ImageClassifierTester {

	public static void main(String[] args){
		try{
			String test = "C:\\Users\\olive\\OneDrive\\Documents\\Uni work\\comp3204-cw2\\testing";
			String train = "C:\\Users\\olive\\OneDrive\\Documents\\Uni work\\comp3204-cw2\\training";

			ImageClassifier ic = new ImageClassifier(test , train);
			ic.evaluateLinearClassifierWithDifferentValues();
//			ic.evaluateLinearClassifier();
//			ic.classifyImages();

		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
