import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import org.json.simple.JSONObject;

import static java.util.Map.entry;

public class Main {
    public static char REST_NOTE = '.';
    public static Map<Character, Character[]> neighbouringNotes = Map.ofEntries(
            entry('1', new Character[]{'2'}),
            entry('2', new Character[]{'1', '3'}),
            entry('3', new Character[]{'2', '4'}),
            entry('4', new Character[]{'3', '5'}),
            entry('5', new Character[]{'4', '6'}),
            entry('6', new Character[]{'5', '7'}),
            entry('7', new Character[]{'6'})
    );

    public static void main(String args[]) throws Exception {
        Scanner input = new Scanner(System.in);
        String balungan;
        String irama;

        do {
            System.out.println("Enter balungan");
            balungan = input.nextLine();
            balungan = balungan.replaceAll("\\s", "");

            // if the first note is a rest note, we replace it with the next note that is not a rest note
            // not sure if this is technically correct
            if (balungan.charAt(0) == REST_NOTE) {
                for (int i = 0; i < balungan.length(); i++) {
                    if (balungan.charAt(i) != REST_NOTE) {
                        StringBuilder temp = new StringBuilder(balungan);
                        temp.setCharAt(0, balungan.charAt(i));
                        balungan = temp.toString();
                        break;
                    }
                }
            }
        } while (!isMultipleOf4(balungan));

        do {
            System.out.println("Enter irama");
            irama = input.nextLine();
        } while (!isValidIrama(irama));

        System.out.println("Balungan is: " + balungan);
        System.out.println("Irama is: " + irama);

        System.out.println("Bonang(Mipil) pattern is: " + getBonang(balungan, irama));
        System.out.println("Peking pattern is : " + getPeking(balungan, irama));

        alignNotes(balungan, getBonang(balungan, irama), getPeking(balungan, irama), irama);
    }

    public static boolean isMultipleOf4(String balungan) {
        return balungan.length() >= 4 && balungan.length() % 4 == 0;
    }

    public static boolean isValidIrama(String irama) {
        return irama.equals("1") || irama.equals("2");
    }

    public static String getBonang(String balungan, String irama) {
        StringBuilder result = new StringBuilder();
        for (int i = 2; i <= balungan.length(); i += 2) {
            char a = balungan.charAt(i - 2);
            char b = balungan.charAt(i - 1);

            if (a == REST_NOTE && i >= 3) {
                a = balungan.charAt(i - 3);
            }

            if (b == REST_NOTE) {
                b = a;
            }

            String pattern = "" + a + b + a + REST_NOTE;

            if (irama.equals("2")) {
                pattern = pattern + REST_NOTE + b + a + REST_NOTE;
            }

            result.append(pattern);
        }
        return result.toString();
    }

    public static char getNeighbour(char note) {
        Character[] neighbours = neighbouringNotes.get(note);
        int rnd = new Random().nextInt(neighbours.length);
        return neighbours[rnd];
    }

    public static String handleRepetition(char a, String irama) {
        char neighbour = getNeighbour(a);
        String pattern = "" + neighbour + neighbour + a + a;

        if (irama.equals("1")) {
            return pattern;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(pattern).append(pattern);
            return sb.toString();
        }
    }

    public static String getPeking(String balungan, String irama) {
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i <= balungan.length(); i += 2) {
            char a = balungan.charAt(i - 2);
            char b = balungan.charAt(i - 1);

            if (a == REST_NOTE && i >= 3) {
                a = balungan.charAt(i - 3);
            }

            if (b == REST_NOTE) {
                b = a;
            }

            if (a == b) {
                sb.append(handleRepetition(a, irama));
            } else {
                sb.append(a).append(a).append(b).append(b);
                if (irama.equals("2")) {
                    sb.append(a).append(a).append(b).append(b);
                }
            }
        }
        return sb.toString();
    }

    public static void alignNotes(String balungan, String bonang, String peking, String irama) {
        try {
            File file = new File("alignment.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file);

            // separation for the balungan notes so that it aligns with peking and bonang
            String spacing = irama.equals("1") ? "" + REST_NOTE : "" + REST_NOTE + REST_NOTE + REST_NOTE;
            String newBalungan = "";
            for (int i = 0; i < balungan.length(); i++) {
                newBalungan = newBalungan + spacing + balungan.charAt(i);
            }
            newBalungan = newBalungan + REST_NOTE;

            fw.write(newBalungan);
            fw.write(System.lineSeparator());
            fw.write(REST_NOTE + peking);
            fw.write(System.lineSeparator());
            fw.write(bonang + REST_NOTE);

            fw.close();
        } catch (IOException e) {
            System.out.println("An error occurred when creating the file.");
            e.printStackTrace();
        }
    }
}
