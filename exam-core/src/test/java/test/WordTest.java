//package test;
//
//import cn.gov.baiyin.court.core.util.CosineSimilarAlgorithm;
//import org.apdplat.word.analysis.*;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.*;
//
///**
// * Created by WK on 2017/3/30.
// */
//public class WordTest {
//
//    @Before
//    public void b(){
////        TextSimilarity similarity = new ManhattanDistanceTextSimilarity();
////        System.out.println(similarity);
//    }
//
//    @Test
//    public void t3(){
//
//        String s1 = getFileContent("D:\\work2017\\exam\\exam-core\\src\\test\\java\\test\\11.txt");
//        String s2 = getFileContent("D:\\work2017\\exam\\exam-core\\src\\test\\java\\test\\22.txt");
//        String s3 = getFileContent("D:\\work2017\\exam\\exam-core\\src\\test\\java\\test\\33.txt");
//
//        System.out.println(CosineSimilarAlgorithm.levenshtein(s1, s2));
//        System.out.println(CosineSimilarAlgorithm.levenshtein(s1, s3));
//    }
//
//    @Test
//    public void t2(){
//
//        String s1 = getFileContent("D:\\work2017\\exam\\exam-core\\src\\test\\java\\test\\11.txt");
//        String s2 = getFileContent("D:\\work2017\\exam\\exam-core\\src\\test\\java\\test\\22.txt");
//        String s3 = getFileContent("D:\\work2017\\exam\\exam-core\\src\\test\\java\\test\\33.txt");
//
//        System.out.println(CosineSimilarAlgorithm.getSimilarity(s1, s2));
//        System.out.println(CosineSimilarAlgorithm.getSimilarity(s1, s3));
//    }
//
//    @Test
//    public void t() {
//
//        String s1 = getFileContent("D:\\work2017\\exam\\exam-core\\src\\test\\java\\test\\11.txt");
//        String s2 = getFileContent("D:\\work2017\\exam\\exam-core\\src\\test\\java\\test\\22.txt");
//
//        TextSimilarity similarity1 = new CosineTextSimilarity();
//        TextSimilarity similarity2 = new SimpleTextSimilarity();
//        TextSimilarity similarity3 = new EditDistanceTextSimilarity();
//        TextSimilarity similarity4 = new SimHashPlusHammingDistanceTextSimilarity();
//        TextSimilarity similarity5 = new JaccardTextSimilarity();
//        TextSimilarity similarity6 = new EuclideanDistanceTextSimilarity();
//        TextSimilarity similarity7 = new ManhattanDistanceTextSimilarity();
//        TextSimilarity similarity8 = new JaroDistanceTextSimilarity();
//        TextSimilarity similarity9 = new JaroWinklerDistanceTextSimilarity();
//        TextSimilarity similarity10 = new SørensenDiceCoefficientTextSimilarity();
//
//        TextSimilarity[] similarities = {similarity1, similarity2, similarity3, similarity4, similarity5, similarity6, similarity7, similarity8,
//                similarity9, similarity10};
//
//        try {
//            System.setErr(new PrintStream(new File("D:\\work2017\\exam\\exam-core\\src\\test\\java\\test\\log.txt")));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        for (TextSimilarity similarity : similarities) {
//            long t1 = System.currentTimeMillis();
//            System.err.println(similarity.similarScore(s1, s2) + "::::耗时：：：" + (System.currentTimeMillis() - t1));
//        }
//
//
//    }
//
//    private String getFileContent(String s) {
//
//        try (DataInputStream dis = new DataInputStream(new FileInputStream(s))) {
//
//            byte[] buff = new byte[dis.available()];
//            dis.read(buff);
//            return new String(buff);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//}
