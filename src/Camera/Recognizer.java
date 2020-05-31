package Camera;

import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import static org.bytedeco.javacpp.opencv_face.createLBPHFaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;

public class Recognizer {
    
	public int returnPredict(File Camimage ,File root)
	{
        Mat testImage = imread(Camimage.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
        
        FilenameFilter imgFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".pgm") || name.endsWith(".png");
            }
        };

        File[] imageFiles = root.listFiles(imgFilter);       
        MatVector images = new MatVector(imageFiles.length);
        Mat labels = new Mat(imageFiles.length, 1, CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();
        int counter = 0;
        for (File image : imageFiles) {
            Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
            int label = Integer.parseInt(image.getName().split("\\-")[0]);
            images.put(counter, img);
            labelsBuf.put(counter, label);
            counter++;
        }
        //FaceRecognizer faceRecognizer = createFisherFaceRecognizer();
        //FaceRecognizer faceRecognizer = createEigenFaceRecognizer();
        FaceRecognizer faceRecognizer = createLBPHFaceRecognizer();
        faceRecognizer.setThreshold(50);
        faceRecognizer.train(images, labels);
        IntPointer label = new IntPointer(1);
        DoublePointer confidence = new DoublePointer(0);
        faceRecognizer.predict(testImage, label, confidence);      
        int predictedLabel = label.get(0);
        return predictedLabel;
        
	}

}
