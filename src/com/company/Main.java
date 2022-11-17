package com.company;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        HashMap<Integer, String> currencyCodes = new HashMap<Integer, String>();

        // Add currency codes

        currencyCodes.put(1, "USD");
        currencyCodes.put(2, "CAD");
        currencyCodes.put(3, "EUR");
        currencyCodes.put(4, "INR");

        String fromCode, toCode;
        double amount;

        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to Currency Converter");

        System.out.println("Currency converting FROM ");
        System.out.println("1:USD (US Dollar)\t2:CAD (Canadian Dollar)\t3:EUR (Euro)\t4:INR(Indian Rupees)");
        fromCode = currencyCodes.get(sc.nextInt());

        System.out.println("Currency converting TO ");
        System.out.println("1:USD (US Dollar) \t2:CAD (Canadian Dollar) \t 3:EUR (Euro)\t 4:INR(Indian Rupees)");
        toCode = currencyCodes.get(sc.nextInt());

        System.out.println("Amount you wish to convert?");
        amount = sc.nextFloat();

        sendHttpGETRequest(fromCode, toCode, amount);

        System.out.println("Thank you for using the currency converter!");

    }
    private static void sendHttpGETRequest(String fromCode, String toCode, double amount) throws IOException {

        DecimalFormat f = new DecimalFormat("00.00");
        String GET_URL = "https://api.exchangerate.host/convert?from=" + fromCode + "&to=" + toCode;

        URL url = new URL(GET_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setRequestMethod("GET");


        int responseCode = httpURLConnection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) { // success

            BufferedReader in = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
//
            JSONObject obj = new JSONObject(response.toString());
//            Double exchangeRate = obj.getJSONObject("info").getDouble(formCode);
//            System.out.println(obj.getJSONObject("query"));
//            System.out.println(exchangeRate); // keep dubbing
//            System.out.println();

            BigDecimal result = (BigDecimal) obj.get("result");
            Double exchangeRate = result.doubleValue();
            System.out.println(toCode + exchangeRate); // keep dubbing
            System.out.println();
            System.out.println(f.format(amount*exchangeRate) +" "+ toCode);
//            Logic for conversion from base currency to converted currency
            System.out.println( "1 " +fromCode + " = " + f.format(amount/exchangeRate) +" "+ toCode);
            System.out.println(f.format(amount) +" "+ fromCode + " = " + amount/exchangeRate +" "+ toCode);
            System.out.println();


        }

        else{

            System.out.println("GET request failed!");
        }
    }
}
