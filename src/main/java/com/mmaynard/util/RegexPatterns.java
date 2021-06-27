package com.mmaynard.util;

import java.util.regex.Pattern;

public class RegexPatterns
{
    public static final Pattern A_HREF = Pattern.compile("<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1");
}
