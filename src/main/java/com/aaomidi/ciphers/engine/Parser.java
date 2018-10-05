package com.aaomidi.ciphers.engine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

public class Parser {
    public static Frequency read(InputStream is) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            Map<String, Integer> map = reader.lines()
                    .filter(s -> !s.isEmpty())

                    .map((s) -> s.split(" "))
                    .filter((a) -> a.length == 2)

                    .collect(Collectors.toMap(
                            (arr) -> arr[0],
                            (arr) -> Integer.valueOf(arr[1]),
                            Integer::sum
                    ));

            return new Frequency(map);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
