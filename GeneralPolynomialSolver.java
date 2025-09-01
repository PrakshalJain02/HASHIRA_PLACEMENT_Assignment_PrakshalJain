import java.math.BigInteger;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class GeneralPolynomialSolver {

    // Convert string in given base to BigInteger
    public static BigInteger convert(String value, int base) {
        return new BigInteger(value, base);
    }

    // Multiply polynomial by (x - r)
    public static List<BigInteger> multiplyPoly(List<BigInteger> coeff, BigInteger r) {
        List<BigInteger> newCoeff = new ArrayList<>(Collections.nCopies(coeff.size() + 1, BigInteger.ZERO));

        for (int i = 0; i < coeff.size(); i++) {
            newCoeff.set(i, newCoeff.get(i).add(coeff.get(i).multiply(r.negate())));
            newCoeff.set(i + 1, newCoeff.get(i + 1).add(coeff.get(i)));
        }
        return newCoeff;
    }

    // Build polynomial from roots
    public static List<BigInteger> buildPolynomial(List<BigInteger> roots) {
        List<BigInteger> coeff = new ArrayList<>();
        coeff.add(BigInteger.ONE); // start with P(x)=1
        for (BigInteger r : roots) {
            coeff = multiplyPoly(coeff, r);
        }
        return coeff;
    }

    public static void main(String[] args) throws Exception {
        JSONParser parser = new JSONParser();

        // ---------------- SAMPLE TEST CASE (GENERAL POLYNOMIAL) ----------------
        String sampleJson = "{ \"keys\": {\"n\": 4, \"k\": 3}, " +
                "\"1\": {\"base\": \"10\", \"value\": \"4\"}, " +
                "\"2\": {\"base\": \"2\", \"value\": \"111\"}, " +
                "\"3\": {\"base\": \"10\", \"value\": \"12\"}, " +
                "\"6\": {\"base\": \"4\", \"value\": \"213\"} }";

        JSONObject sampleObj = (JSONObject) parser.parse(sampleJson);
        JSONObject keys = (JSONObject) sampleObj.get("keys");
        int n = Integer.parseInt(keys.get("n").toString());
        int k = Integer.parseInt(keys.get("k").toString());

        List<BigInteger> roots = new ArrayList<>();
        for (int i = 1; i <= k; i++) {
            JSONObject rootObj = (JSONObject) sampleObj.get(String.valueOf(i));
            String value = (String) rootObj.get("value");
            int base = Integer.parseInt((String) rootObj.get("base"));
            roots.add(convert(value, base));
        }

        List<BigInteger> coeff = buildPolynomial(roots);
        System.out.println("Sample Testcase Polynomial Coefficients:");
        for (int i = coeff.size() - 1; i >= 0; i--) {
            System.out.print(coeff.get(i) + " ");
        }
        System.out.println("\n");

        // ---------------- QUADRATIC CASE (CONSTANT c) ----------------
        String quadraticRootsJson = "{ \"1\": {\"base\": \"10\", \"value\": \"4\"}, " +
                "\"2\": {\"base\": \"2\", \"value\": \"111\"} }";
        String yJson = "{ \"y\": {\"base\": \"16\", \"value\": \"1a\"} }";

        JSONObject qRootObj = (JSONObject) parser.parse(quadraticRootsJson);
        JSONObject yObj = (JSONObject) parser.parse(yJson);

        JSONObject r1 = (JSONObject) qRootObj.get("1");
        JSONObject r2 = (JSONObject) qRootObj.get("2");
        BigInteger x1 = convert((String) r1.get("value"), Integer.parseInt((String) r1.get("base")));
        BigInteger x2 = convert((String) r2.get("value"), Integer.parseInt((String) r2.get("base")));

        JSONObject yEnc = (JSONObject) yObj.get("y");
        BigInteger yVal = convert((String) yEnc.get("value"), Integer.parseInt((String) yEnc.get("base")));

        // Quadratic coefficients
        BigInteger a = BigInteger.ONE;
        BigInteger b = x1.add(x2).negate();
        BigInteger c = x1.multiply(x2);

        System.out.println("Quadratic Equation: f(x) = " + a + "x^2 + " + b + "x + " + c);
        System.out.println("Constant term c = " + c);

        BigInteger discriminant = b.multiply(b).subtract(a.multiply(BigInteger.valueOf(4)).multiply(c));
        System.out.println("Discriminant D = " + discriminant);

        BigInteger fx = a.multiply(yVal.pow(2)).add(b.multiply(yVal)).add(c);
        System.out.println("f(" + yVal + ") = " + fx);
    }
}