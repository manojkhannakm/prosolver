package com.manojkhannakm.prosolver.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.manojkhannakm.prosolver.model.Contest;
import com.manojkhannakm.prosolver.util.JsonUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Manoj Khanna
 */

public class ContestFile extends JsonFile<Contest> {

    private static final String URL_NAME = "url";
    private static final String CODE_NAME = "code";
    private static final String NAME_NAME = "name";
    private static final String PROBLEM_FILES_NAME = "problem_files";

    private final ArrayList<ProblemFile> problemFileList = new ArrayList<>();

    public ContestFile(File file) {
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
                    || !object.has(PROBLEM_FILES_NAME) || !object.get(PROBLEM_FILES_NAME).isJsonArray()) {
                return false;
            }

            Contest contest = new Contest();
            contest.setUrl(object.get(URL_NAME).getAsString());
            contest.setCode(object.get(CODE_NAME).getAsString());
            contest.setName(object.get(NAME_NAME).getAsString());

            set(contest);

            for (JsonElement problemFileElement : object.get(PROBLEM_FILES_NAME).getAsJsonArray()) {
                problemFileList.add(getProblemFile(problemFileElement.getAsString()));
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

        Contest contest = get();

        try {
            JsonWriter writer = JsonUtils.write(new FileWriter(file))
                    .beginObject()
                    .name(URL_NAME).value(contest.getUrl())
                    .name(CODE_NAME).value(contest.getCode())
                    .name(NAME_NAME).value(contest.getName())
                    .name(PROBLEM_FILES_NAME)
                    .beginArray();
            for (ProblemFile problemFile : problemFileList) {
                String problemFileName = problemFile.getFile().getName();
                writer.value(problemFileName.substring(0, problemFileName.lastIndexOf('.')));
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
    }

    public ProblemFile getProblemFile(String problemFileName) {
        return new ProblemFile(new File(file.getParentFile(), problemFileName + ".json"));
    }

    public ArrayList<ProblemFile> getProblemFileList() {
        return problemFileList;
    }

}
