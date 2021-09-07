package com.github.rdx7777.stripeinvoice.util;

import java.util.regex.Pattern;

public class RegexPatterns {

    private static final Pattern emailPattern =
        Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");

    static boolean emailPatternCheck(String email) {
        return emailPattern.matcher(email).matches();
    }
}
