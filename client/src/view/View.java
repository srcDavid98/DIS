package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Base class with utility methods for views.
 */
class View {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Clears the console through a workaround.
     */
    void clearConsole() {
        System.out.println(new String(new char[5]).replace("\0", "\r\n")); //work around to clear console
    }

    private final String posIntErrorMsg = "You must enter a positive integer value, please try again. ";

    /**
     * Gets the next positive integer value from the console. Will try again until a positive value is entered.
     * @return A positive integer value.
     */
    int getNextPosIntValue()
    {
        Integer value = null;

        while (value == null)
        {
            try
            {
                value = Integer.parseInt(br.readLine());
                if (value < 0)
                {
                    System.out.println(posIntErrorMsg);
                    value = null;
                }
                if (value != null)
                    return value;
            }
            catch (Exception e)
            {
                System.out.println(posIntErrorMsg);
            }
        }
        return -1;
    }

    /**
     * Gets the next string value from console. Will try again if there is an error.
     * @return String value.
     */
    String getNextStringValue()
    {
        while (true)
            try {
                return br.readLine();
            } catch (IOException e) {
                System.out.println("Error while reading from the console. Please try again. ");
            }
    }
}
