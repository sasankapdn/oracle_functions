package com.example.fn;

import java.io.File;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

/*
* Class StateTaxCalc
* Purpose: Input a state name and price. Search on State name for tax rate. Return total cost of item.
 */


public class StateTaxCalc {
    //variable to hold state name and rate
    public List<stateRates> rateCard = new ArrayList<>();

    public static class Input {
        public String state;
        public double price;
    }

    public static class Result {
        public String state;
        public double price;
        public double tax_rate;
        public double tax;
        public double total_cost;
    }


    /*
    Method to read .csv for state name and rate rows, send row to getRecordFromLine() method.
     */
    private void loadStateTaxRates() {
        try {
                     InputStream inputCSV = getClass().getClassLoader().getResourceAsStream("stateRates.csv");
                     Scanner scanner = new Scanner(inputCSV);
                while (scanner.hasNextLine()) {
                    getRecordFromLine(scanner.nextLine());
                }

        }
        catch (Exception e) {
           System.out.println("Error loading State Rates: "+e.toString());
        }
    }

    /*
    * Filter through to get the state rate from the List.
*/
    public double getRate(String st) {
        for (stateRates a : rateCard) {
            if (a.getState().equalsIgnoreCase(st)) {
                return a.getRate();
            }
        }
        return -1;
    }

    /*
    Method to process state name and rate, colums into single List
     */
    private void getRecordFromLine(String line) {
        try (Scanner rowScanner = new Scanner(line)) {
            stateRates holder = new stateRates();
            rowScanner.useDelimiter(",");
            int i = 1;
            while (rowScanner.hasNext()) {
                switch (i) {
                    case 1:
                        holder.setState(rowScanner.next());
                        i++;
                        break;
                    case 2:
                        holder.setRate(rowScanner.next());
                        i=0;
                        break;
                    default:
                        //do nothing
                }
               }
            rateCard.add(holder);
        }
        catch (Exception e) {
            System.out.println("Error parsing rows into array in getRecordFromLine: "+e.toString());
        }

    }

    /*
    Fn Result method gathers and returns json result
     */
    public Result calcTotalCost(Input input) {
        try {
            DecimalFormat twoDForm = new DecimalFormat("#.00");
            twoDForm.setRoundingMode(RoundingMode.UP);
            this.loadStateTaxRates();
            if (Double.compare(this.getRate(input.state),-1) == 0) {
                throw new IllegalArgumentException("Value entered for State Rate Lookup could not be found. Size of RateCard is: "+rateCard.size());
            }

            Result result = new Result();
            result.state = input.state;
            result.price = input.price;
            result.tax_rate = getRate(input.state);
            result.tax = Double.parseDouble(twoDForm.format(input.price * result.tax_rate));
            result.total_cost = Double.parseDouble(twoDForm.format(input.price * (1.0 + getRate(input.state))));

            return result;
        }
        catch (IllegalArgumentException e) {
            Result result = new Result();
            result.state = e.toString();
           return result;
        }
    }

    /*
    Main method is purely to test

    public static void main(String[] args)
    {
        System.out.println("Testing the core .csv read and processing");
        StateTaxCalc a = new StateTaxCalc();
        a.loadStateTaxRates();
        System.out.println("Total size of Rate Card: "+ a.rateCard.size());
        System.out.println(a.getRate("Virginia"));
      //  System.out.println(getClass().getResource("/stateRates.csv"));
     }
*/
}