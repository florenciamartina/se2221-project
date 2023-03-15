import java.util.*;

public class Main {
    public static void main(String args[]) throws Exception {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter balungan");
        String balungan = input.nextLine();

        System.out.println("Enter irama");
        String irama = input.nextLine();

        System.out.println("Balungan is: " + balungan);
        System.out.println("Irama is: " + irama);

        System.out.println("Bonang(Mipil) pattern is: " + getBonang(balungan, irama));
    }

    public static String getBonang(String balungan, String irama) {
        String result = "";

        for (int i = 2; i <= balungan.length(); i += 2) {
            char a = balungan.charAt(i - 2);
            char b = balungan.charAt(i - 1);

            String pattern = "" + a + b + a + "-";

            if (irama.equals("2")) {
                pattern = pattern + "-" + b + a + "-";
            }

            result = result + pattern;
        }

        return result;
    }
}
