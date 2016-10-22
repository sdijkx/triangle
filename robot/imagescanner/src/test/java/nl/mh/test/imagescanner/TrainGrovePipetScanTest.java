package nl.mh.test.imagescanner;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.testng.Assert.assertEquals;

/**
 * Created by Marc on 21-11-2015.
 */
public class TrainGrovePipetScanTest {

    @Test
    public void testje(){


        double a = 0.0393873854405537;
        String b = String.valueOf(a).substring(2,4);
      //String c =  b.substring(2,4);
        assertEquals("03", b);
    }


    @Test
    public void testMap(){
        List<Integer> costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        costBeforeTax.stream().map((cost) -> "--" + cost).forEach(System.out::println);

        IntStream.range(0, 20).boxed().map((cost) -> "--" + cost).forEach(System.out::println);

        List<String> cards = IntStream.rangeClosed(1, 4)
                .boxed()
                .flatMap(value ->
                        IntStream.rangeClosed(1, 13)
                                .mapToObj(suit -> value + "_" + suit)
                )
                .collect(Collectors.toList());


//        Object subimages = IntStream.rangeClosed(0, 200).boxed()
//                .forEach(i -> IntStream.rangeClosed(0, 300).boxed()
//                        //.filter(j -> i * step + bloksize <= image.getHeight() && j * step + bloksize <= image.getWidth())
//                        .map(j -> i + "_ " + j)
//                        .collect(Collectors.toList()));

        List<String> list = IntStream.rangeClosed(0, 200).boxed()
                .flatMap(x -> IntStream.rangeClosed(0, 300).boxed()
                        .map(y -> x + "_" +  y)
                )
                .collect(Collectors.toList());
        String kip = null;

    }
}
