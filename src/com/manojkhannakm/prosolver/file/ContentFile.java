package com.manojkhannakm.prosolver.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.manojkhannakm.prosolver.model.Content;
import com.manojkhannakm.prosolver.util.JsonUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Manoj Khanna
 */

public class ContentFile extends JsonFile<Content> {

    private static final String PROBLEM_FILES_NAME = "problem_files";
    private static final String PLATFORM_FILES_NAME = "platform_files";

    private final ArrayList<ProblemFile> problemFileList = new ArrayList<>();
    private final ArrayList<PlatformFile> platformFileList = new ArrayList<>();

    public ContentFile(File file) {
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
            if (!object.has(PROBLEM_FILES_NAME) || !object.get(PROBLEM_FILES_NAME).isJsonArray()
                    || !object.has(PLATFORM_FILES_NAME) || !object.get(PLATFORM_FILES_NAME).isJsonArray()) {
                return false;
            }

            for (JsonElement problemFileElement : object.get(PROBLEM_FILES_NAME).getAsJsonArray()) {
                problemFileList.add(getProblemFile(problemFileElement.getAsString()));
            }

            for (JsonElement platformFileElement : object.get(PLATFORM_FILES_NAME).getAsJsonArray()) {
                platformFileList.add(getPlatformFile(platformFileElement.getAsString()));
            }

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

        try {
            JsonWriter writer = JsonUtils.write(new FileWriter(file))
                    .beginObject()
                    .name(PROBLEM_FILES_NAME)
                    .beginArray();
            for (ProblemFile problemFile : problemFileList) {
                String problemFileName = problemFile.getFile().getName();
                writer.value(problemFileName.substring(0, problemFileName.lastIndexOf('.')));
            }

            writer.endArray()
                    .name(PLATFORM_FILES_NAME)
                    .beginArray();
            for (PlatformFile platformFile : platformFileList) {
                String platformFileName = platformFile.getFile().getName();
                writer.value(platformFileName.substring(0, platformFileName.lastIndexOf('.')));
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

        problemFileList.clear();
        platformFileList.clear();
    }

    public ProblemFile getProblemFile(String problemFileName) {
        return new ProblemFile(new File(file.getParentFile(), problemFileName + ".json"));
    }

    public PlatformFile getPlatformFile(String platformFileName) {
        return new PlatformFile(new File(file.getParentFile(), platformFileName + "/" + platformFileName + ".json"));
    }

    public ArrayList<ProblemFile> getProblemFileList() {
        return problemFileList;
    }

    public ArrayList<PlatformFile> getPlatformFileList() {
        return platformFileList;
    }

}
