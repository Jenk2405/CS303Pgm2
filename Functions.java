
import java.io.*;
import java.util.*;

public class Functions {

    /*
     * PRE: TVList must be a non-null map. The file tv.csv must be available
     *      in the program's working directory.
     * POST: All valid records from tv.csv are added to TVList. Invalid records
     *       are skipped and an error message is displayed.
     */
    public static void loadData(Map<Integer, ArrayList<String>> TVList) {

        String fileName = "tv.csv";

        try (Scanner inFile = new Scanner(new File(fileName))) {

            while (inFile.hasNextLine()) {

                String inputRecord =
                    inFile.nextLine().trim();

                if (inputRecord.isEmpty()) {
                    continue;
                }

                try {

                    // Find the first comma in the CSV record.
                    int comma =
                        inputRecord.indexOf(',');

                    if (comma == -1) {
                        throw new IllegalArgumentException();
                    }

                    // The first field is the number of seasons.
                    int duration =
                        Integer.parseInt(
                            inputRecord
                                .substring(0, comma)
                                .trim()
                        );

                    /*
                     * The second field is the show name.
                     * This method also handles show names that contain
                     * commas inside quotation marks.
                     */
                    String showName =
                        getSecondCSVField(
                            inputRecord.substring(comma + 1)
                        );

                    /*
                     * Source: W3Schools - Java HashMap
                     * A HashMap stores key/value pairs.
                     */
                    TVList.computeIfAbsent(
                        duration,
                        k -> new ArrayList<>()
                    ).add(showName);
                }
                catch (Exception e) {

                    System.out.println(
                        "Error in input record"
                    );
                }
            }
        }
        catch (Exception e) {

            System.out.println(
                "Error in input record"
            );
        }
    }


    /*
     * PRE: remainder must contain the second CSV field beginning immediately
     *      after the first comma in a record.
     * POST: Returns the second CSV field with surrounding quotes removed and
     *       doubled quotes converted to single quotes.
     */
    private static String getSecondCSVField(
            String remainder) {

        remainder =
            remainder.trim();

        if (remainder.startsWith("\"")) {

            StringBuilder result =
                new StringBuilder();

            for (int i = 1;
                 i < remainder.length();
                 i++) {

                char current =
                    remainder.charAt(i);

                if (current == '\"') {

                    if (i + 1 < remainder.length()
                            && remainder.charAt(i + 1) == '\"') {

                        result.append('\"');
                        i++;
                    }
                    else {

                        return result.toString();
                    }
                }
                else {

                    result.append(current);
                }
            }

            throw new IllegalArgumentException();
        }

        int comma =
            remainder.indexOf(',');

        return comma == -1
                ? remainder
                : remainder.substring(0, comma).trim();
    }


    /*
     * PRE: input must be a non-null Scanner connected to user input.
     * POST: Returns one of A, D, K, P, S, or Q. Invalid entries cause the
     *       user to be prompted again.
     */
    public static String getMenuItem(
            Scanner input) {

        String choice = " ";

        System.out.println(
            "\nACTIONS FOR TVSHOW MAP"
        );

        System.out.println(
            "A: Add a Show "
        );

        System.out.println(
            "D: Delete a Show "
        );

        System.out.println(
            "K: Print All Keys (Durations) to Report"
        );

        System.out.println(
            "P: Print Map Listing to Report "
        );

        System.out.println(
            "S: Print Specific Key (Duration) Listing to Report "
        );

        System.out.println(
            "Q: Quit "
        );

        System.out.print(
            "Please enter your choice: "
        );

        choice =
            input.nextLine()
                 .toUpperCase()
                 .trim();

        while (!(choice.equals("A")
                || choice.equals("D")
                || choice.equals("K")
                || choice.equals("P")
                || choice.equals("S")
                || choice.equals("Q"))) {

            System.out.print(
                "You entered an invalid value. "
                + "Please enter a valid choice: "
            );

            choice =
                input.nextLine()
                     .toUpperCase()
                     .trim();
        }

        System.out.println();

        return choice;
    }


    /*
     * PRE: TVList, input, and out must be non-null.
     * POST: Prompts for a valid integer duration and a show name, adds the
     *       show to the appropriate duration list, and reports the action.
     */
    public static void addShow(
            Map<Integer, ArrayList<String>> TVList,
            Scanner input,
            PrintWriter out) {

        int duration =
            getDuration(input);

        System.out.print(
            "Enter the name of the show: "
        );

        String showName =
            input.nextLine();

        /*
         * Source: W3Schools - Java ArrayList
         * ArrayList allows elements to be added and removed dynamically.
         */
        TVList.computeIfAbsent(
            duration,
            k -> new ArrayList<>()
        ).add(showName);

        String message =
            "A: This new show was added to the map: "
            + showName
            + " with the duration of "
            + duration
            + " years.";

        System.out.println(message);

        out.println(message);
        out.flush();
    }


    /*
     * PRE: TVList, input, and out must be non-null.
     * POST: Prompts for a show name. If the show is present, it is removed
     *       from its duration list. Otherwise, an error is reported.
     */
    public static void deleteShow(
            Map<Integer, ArrayList<String>> TVList,
            Scanner input,
            PrintWriter out) {

        System.out.print(
            "Enter the name of the show: "
        );

        String showName =
            input.nextLine();

        Integer durationFound = null;

        for (Map.Entry<Integer, ArrayList<String>> entry
                : TVList.entrySet()) {

            if (entry.getValue().contains(showName)) {

                durationFound =
                    entry.getKey();

                entry.getValue().remove(showName);

                break;
            }
        }

        String message;

        if (durationFound == null) {

            message =
                "R: Unable to delete "
                + showName
                + ". Item is not in the map";
        }
        else {

            if (TVList.get(durationFound).isEmpty()) {
                TVList.remove(durationFound);
            }

            message =
                "R: Deleted item "
                + showName
                + " from the map";
        }

        System.out.println(message);

        out.println(message);
        out.flush();
    }


    /*
     * PRE: TVList and out must be non-null.
     * POST: Writes every duration key in ascending order to the report file
     *       and displays a confirmation message.
     */
    public static void printAllKeys(
            Map<Integer, ArrayList<String>> TVList,
            PrintWriter out) {

        /*
         * Source: W3Schools - Java TreeMap
         * TreeMap stores key/value pairs in sorted order by key.
         */
        Map<Integer, ArrayList<String>> sortedTVList =
            new TreeMap<>(TVList);

        for (Integer duration :
                sortedTVList.keySet()) {

            out.println(duration);
        }

        String message =
            "K: All keys (durations) were printed to the report file.";

        System.out.println(message);

        out.println(message);
        out.flush();
    }


    /*
     * PRE: TVList and out must be non-null.
     * POST: Writes every key and its associated show list in ascending key
     *       order to the report file.
     */
    public static void printMap(
            Map<Integer, ArrayList<String>> TVList,
            PrintWriter out) {

        /*
         * Source: W3Schools - Java TreeMap
         * TreeMap is used here so the durations appear in sorted order.
         */
        Map<Integer, ArrayList<String>> sortedTVList =
            new TreeMap<>(TVList);

        for (Map.Entry<Integer, ArrayList<String>> entry
                : sortedTVList.entrySet()) {

            out.println(
                entry.getKey()
                + ": "
                + entry.getValue()
            );
        }

        String message =
            "P: Map listing was printed to the report file.";

        System.out.println(message);

        out.println(message);
        out.flush();
    }


    /*
     * PRE: TVList, input, and out must be non-null.
     * POST: Prompts for a duration and writes all shows associated with that
     *       duration to the report file.
     */
    public static void printSpecificKey(
            Map<Integer, ArrayList<String>> TVList,
            Scanner input,
            PrintWriter out) {

        int duration =
            getDuration(input);

        ArrayList<String> shows =
            TVList.get(duration);

        if (shows == null) {

            String message =
                "S: No shows with the duration of "
                + duration
                + " were found in the map.";

            System.out.println(message);

            out.println(message);
        }
        else {

            for (String show : shows) {
                out.println(show);
            }

            String message =
                "S: Shows with the duration of "
                + duration
                + " were written to the report file.";

            System.out.println(message);

            out.println(message);
        }

        out.flush();
    }


    /*
     * PRE: input must be a non-null Scanner.
     * POST: Returns a valid integer entered by the user. Non-integer input
     *       causes an error message and another prompt.
     */
    private static int getDuration(
            Scanner input) {

        while (true) {

            System.out.print(
                "Enter the duration: "
            );

            String value =
                input.nextLine().trim();

            try {

                return Integer.parseInt(value);
            }
            catch (NumberFormatException e) {

                System.out.println(
                    "Invalid duration. "
                    + "Please enter a valid integer."
                );
            }
        }
    }
}