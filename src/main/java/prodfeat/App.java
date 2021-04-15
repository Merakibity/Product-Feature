package prodfeat;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        ProdFeature pf = new ProdFeature();
        ProdFeature.createAndStartService();
        pf.createDriver();
        int l = pf.ketData();
        for (int i = 1; i <= l; i++) {
            pf.getValues(i);
            try {
                pf.remProdFeat();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
