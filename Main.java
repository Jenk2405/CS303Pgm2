/*
 * Name: Dorian Jenkins
 * Date: September 29, 2026
 * Assignment: CS303 Program 2 - Java Review: Maps
 */

import java.io.*;
import java.util.*;

public class Main {

    /*
     * PRE: Program is started with a readable tv.csv file in the working
     *      directory.
     * POST: Loads the TV-show map, repeatedly processes menu selections,
     *       writes requested output to report.txt, and closes all resources.
     */
    public static void main(String[] args) {

        System.out.println("Welcome to Program 2: Maps");

        /*
         * Source: W3Schools - Java HashMap
         * HashMap stores data using key/value pairs.
         */
        Map<Integer, ArrayList<String>> TVList =
            new HashMap<>();

        Scanner input = new Scanner(System.in);

        /*
         * Source: W3Schools - Java Exceptions (Try...Catch)
         * try/catch is used to handle runtime errors.
         */
        try (PrintWriter out = new PrintWriter("report.txt")) {

            // Load data into the map.
            Functions.loadData(TVList);

            String menuItem =
                Functions.getMenuItem(input);

            while (!menuItem.equals("Q")) {

                // Call the appropriate function for each menu option.
                switch (menuItem) {

                    case "A":
                        Functions.addShow(
                            TVList,
                            input,
                            out
                        );
                        break;

                    case "D":
                        Functions.deleteShow(
                            TVList,
                            input,
                            out
                        );
                        break;

                    case "K":
                        Functions.printAllKeys(
                            TVList,
                            out
                        );
                        break;

                    case "P":
                        Functions.printMap(
                            TVList,
                            out
                        );
                        break;

                    case "S":
                        Functions.printSpecificKey(
                            TVList,
                            input,
                            out
                        );
                        break;

                    default:
                        // getMenuItem() prevents an invalid choice.
                        break;
                }

                menuItem =
                    Functions.getMenuItem(input);
            }

            System.out.println(
                "Q: Program ended successfully."
            );
        }
        catch (Exception e) {

            System.out.println(
                "Error in input record"
            );
        }
        finally {
            input.close();
        }
    }
}