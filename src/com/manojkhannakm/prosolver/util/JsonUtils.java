package com.manojkhannakm.prosolver.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * @author Manoj Khanna
 */

public class JsonUtils {

    public static JsonElement read(Reader reader) throws IOException {
        JsonElement element = new JsonParser().parse(reader);

        reader.close();

        return element;
    }

    public static JsonWriter write(Writer writer) throws IOException {
        return new JsonWriter(writer);
    }

}
