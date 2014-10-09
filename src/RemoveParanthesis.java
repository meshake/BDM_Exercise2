import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

/**
 * Created by Tomas on 10/7/2014.
 */
public class RemoveParanthesis {
    public static void main(String[] args) {
        try {
            Scanner in = new Scanner(new FileReader("C:\\Users\\Tomas\\Desktop\\names2.txt"));
            while(in.hasNext()) {
                String name = in.nextLine();
                String[] splitName = name.split(" ");
                String firstNames = "";
                String surname = splitName[splitName.length-1];
                String surnameFirstLetter = surname.substring(0, 1).toLowerCase();

                for (int i = 0; i < splitName.length - 1; i++) {
                    if (firstNames.length() > 0) {
                        firstNames += "_" + splitName[i];
                    } else {
                        firstNames += splitName[i];
                    }
                }

                System.out.println(surnameFirstLetter + "/" +surname + ":" + firstNames);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
