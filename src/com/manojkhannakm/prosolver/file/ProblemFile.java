package com.manojkhannakm.prosolver.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.manojkhannakm.prosolver.model.Problem;
import com.manojkhannakm.prosolver.model.Test;
import com.manojkhannakm.prosolver.util.JsonUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Manoj Khanna
 */

public class ProblemFile extends JsonFile<Problem> {

    private static final String URL_NAME = "url";
    private static final String CODE_NAME = "code";
    private static final String NAME_NAME = "name";
    private static final String PACKAGE_NAME_NAME = "package_name";
    private static final String CLASS_NAME_NAME = "class_name";
    private static final String CLASS_PATH_NAME = "class_path";
    private static final String SIZE_LIMIT_NAME = "size_limit";
    private static final String TIME_LIMIT_NAME = "time_limit";
    private static final String MEMORY_LIMIT_NAME = "memory_limit";
    private static final String TESTS_NAME = "tests";

    private static final String TEST_ENABLED_NAME = "enabled";
    private static final String TEST_INPUT_NAME = "input";
    private static final String TEST_OUTPUT_NAME = "output";

    public ProblemFile(File file) {
        super(file);
    }

    @Override
    public boolean read() {
        if (!file.exists()) {
            return false;
        }

        try {
            JsonElement element = JsonUtils.read(new FileReader(file));
            if (!element.isJsonObject()) {
                return false;
            }

            JsonObject object = element.getAsJsonObject();
            if (!object.has(URL_NAME) || !object.has(CODE_NAME) || !object.has(NAME_NAME)
                    || !object.has(PACKAGE_NAME_NAME) || !object.has(CLASS_NAME_NAME)
                    || !object.has(SIZE_LIMIT_NAME) || !object.has(TIME_LIMIT_NAME) || !object.has(MEMORY_LIMIT_NAME)
                    || !object.has(TESTS_NAME) || !object.get(TESTS_NAME).isJsonArray()) {
                return false;
            }

            Problem problem = new Problem();
            problem.setUrl(object.get(URL_NAME).getAsString());
            problem.setCode(object.get(CODE_NAME).getAsString());
            problem.setName(object.get(NAME_NAME).getAsString());
            problem.setPackageName(object.get(PACKAGE_NAME_NAME).getAsString());
            problem.setClassName(object.get(CLASS_NAME_NAME).getAsString());
            problem.setClassPath(object.get(CLASS_PATH_NAME).getAsString());
            problem.setSizeLimit(object.get(SIZE_LIMIT_NAME).getAsInt());
            problem.setTimeLimit(object.get(TIME_LIMIT_NAME).getAsInt());
            problem.setMemoryLimit(object.get(MEMORY_LIMIT_NAME).getAsInt());

            ArrayList<Test> testList = new ArrayList<>();
            for (JsonElement testElement : object.get(TESTS_NAME).getAsJsonArray()) {
                if (!testElement.isJsonObject()) {
                    return false;
                }

                JsonObject testObject = testElement.getAsJsonObject();
                if (!testObject.has(TEST_ENABLED_NAME)
                        || !testObject.has(TEST_INPUT_NAME)
                        || !testObject.has(TEST_OUTPUT_NAME)) {
                    return false;
                }

                Test test = new Test();
                test.setEnabled(testObject.get(TEST_ENABLED_NAME).getAsBoolean());
                test.setInput(testObject.get(TEST_INPUT_NAME).getAsString());
                test.setOutput(testObject.get(TEST_OUTPUT_NAME).getAsString());
                testList.add(test);
            }

            problem.setTestList(testList);

            set(problem);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean write() {
        //noinspection ResultOfMethodCallIgnored
        file.getParentFile().mkdirs();

        Problem problem = get();

        try {
            JsonWriter writer = JsonUtils.write(new FileWriter(file))
                    .beginObject()
                    .name(URL_NAME).value(problem.getUrl())
                    .name(CODE_NAME).value(problem.getCode())
                    .name(NAME_NAME).value(problem.getName())
                    .name(PACKAGE_NAME_NAME).value(problem.getPackageName())
                    .name(CLASS_NAME_NAME).value(problem.getClassName())
                    .name(CLASS_PATH_NAME).value(problem.getClassPath())
                    .name(SIZE_LIMIT_NAME).value(problem.getSizeLimit())
                    .name(TIME_LIMIT_NAME).value(problem.getTimeLimit())
                    .name(MEMORY_LIMIT_NAME).value(problem.getMemoryLimit())
                    .name(TESTS_NAME)
                    .beginArray();
            for (Test test : problem.getTestList()) {
                writer.beginObject()
                        .name(TEST_ENABLED_NAME).value(test.isEnabled())
                        .name(TEST_INPUT_NAME).value(test.getInput())
                        .name(TEST_OUTPUT_NAME).value(test.getOutput())
                        .endObject();
            }

            writer.endArray()
                    .endObject()
                    .close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void clear() {
        set(null);
    }

}
