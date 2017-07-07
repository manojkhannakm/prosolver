package com.manojkhannakm.prosolver.file;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.manojkhannakm.prosolver.model.Setting;
import com.manojkhannakm.prosolver.util.JsonUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Manoj Khanna
 */

public class SettingFile extends JsonFile<Setting> {

    private static final String SOURCE_DIRECTORY_NAME = "source_directory";
    private static final String TEMPLATE_CLASS_NAME = "template_class";
    private static final String PACKAGE_PATTERN_NAME = "package_pattern";
    private static final String CLASS_PATTERN_NAME = "class_pattern";
    private static final String CLASS_ARGUMENTS_NAME = "class_arguments";

    public SettingFile(File file) {
        super(file);
    }

    @Override
    public boolean read() {
        if (!file.exists()) {
            return true;
        }

        try {
            JsonElement element = JsonUtils.read(new FileReader(file));
            if (!element.isJsonObject()) {
                return false;
            }

            JsonObject object = element.getAsJsonObject();
            if (!object.has(SOURCE_DIRECTORY_NAME) || !object.has(TEMPLATE_CLASS_NAME)
                    || !object.has(PACKAGE_PATTERN_NAME) || !object.has(CLASS_PATTERN_NAME)
                    || !object.has(CLASS_ARGUMENTS_NAME) || !object.get(CLASS_ARGUMENTS_NAME).isJsonArray()) {
                return false;
            }

            Setting setting = get();

            setting.setSourceDirectory(object.get(SOURCE_DIRECTORY_NAME).getAsString());
            setting.setTemplateClass(object.get(TEMPLATE_CLASS_NAME).getAsString());
            setting.setPackagePattern(object.get(PACKAGE_PATTERN_NAME).getAsString());
            setting.setClassPattern(object.get(CLASS_PATTERN_NAME).getAsString());

            JsonArray classArgumentsArray = object.get(CLASS_ARGUMENTS_NAME).getAsJsonArray();
            String[] classArguments = new String[classArgumentsArray.size()];
            for (int i = 0; i < classArgumentsArray.size(); i++) {
                classArguments[i] = classArgumentsArray.get(i).getAsString();
            }

            setting.setClassArguments(classArguments);

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

        Setting setting = get();

        try {
            JsonWriter writer = JsonUtils.write(new FileWriter(file))
                    .beginObject()
                    .name(SOURCE_DIRECTORY_NAME).value(setting.getSourceDirectory())
                    .name(TEMPLATE_CLASS_NAME).value(setting.getTemplateClass())
                    .name(PACKAGE_PATTERN_NAME).value(setting.getPackagePattern())
                    .name(CLASS_PATTERN_NAME).value(setting.getClassPattern())
                    .name(CLASS_ARGUMENTS_NAME)
                    .beginArray();
            for (String classArgument : setting.getClassArguments()) {
                writer.value(classArgument);
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
