package com.manojkhannakm.prosolver.platform.codechef;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.manojkhannakm.prosolver.model.*;
import com.manojkhannakm.prosolver.parser.Parser;
import com.manojkhannakm.prosolver.parser.ParserListener;
import com.manojkhannakm.prosolver.util.JsonUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Manoj Khanna
 */

public class CodeChefParser extends Parser {

    private static final String PROBLEM_URL = "https://www.codechef.com/problems/%s";
    private static final String CONTEST_URL = "https://www.codechef.com/%s";
    private static final String CONTEST_PROBLEM_URL = "https://www.codechef.com/%s/problems/%s";
    private static final String PROBLEM_API_URL = "https://www.codechef.com/api/contests/PRACTICE/problems/%s";
    private static final String CONTEST_API_URL = "https://www.codechef.com/api/contests/%s";
    private static final String CONTEST_PROBLEM_API_URL = "https://www.codechef.com/api/contests/%s/problems/%s";

    @Override
    public boolean parseProblem(Problem problem) {
        try {
            Matcher matcher = Pattern.compile(PROBLEM_URL.replace("%s", "(.+)")).matcher(problem.getUrl());
            String problemApiUrl;
            if (matcher.find()) {
                problemApiUrl = String.format(PROBLEM_API_URL, matcher.group(1));
            } else {
                matcher = Pattern.compile(CONTEST_PROBLEM_URL.replace("%s", "(.+)")).matcher(problem.getUrl());
                if (matcher.find()) {
                    problemApiUrl = String.format(CONTEST_PROBLEM_API_URL, matcher.group(1), matcher.group(2));
                } else {
                    return false;
                }
            }

            JsonElement element = JsonUtils.read(new InputStreamReader(new URL(problemApiUrl).openStream()));
            if (!element.isJsonObject()) {
                return false;
            }

            JsonObject problemObject = element.getAsJsonObject();
            if (!problemObject.has("status") || !problemObject.get("status").getAsString().equals("success")
                    || !problemObject.has("problem_code") || !problemObject.has("problem_name")
                    || !problemObject.has("source_sizelimit") || !problemObject.has("max_timelimit")
                    || !problemObject.has("body")) {
                return false;
            }

            String problemCode = problemObject.get("problem_code").getAsString(),
                    problemName = problemObject.get("problem_name").getAsString();
            int sizeLimit = (int) Math.ceil(problemObject.get("source_sizelimit").getAsInt() / 1024.0f),
                    timeLimit = (int) (problemObject.get("max_timelimit").getAsFloat() * 1000.0f),
                    memoryLimit = 1536;

            matcher = Pattern.compile("Example((.+(?=Explanation))|(.+(?=Test))|(.+))", Pattern.DOTALL)
                    .matcher(Jsoup.parse(problemObject.get("body").getAsString()).text());
            if (!matcher.find()) {
                return false;
            }

            matcher = Pattern.compile("[Ii]nput(?: \\d+)?:?(.+?)[Oo]utput(?: \\d+)?:?((.+(?=[Ii]nput))|(.+))", Pattern.DOTALL)
                    .matcher(matcher.group(1));
            ArrayList<Test> testList = new ArrayList<>();
            while (matcher.find()) {
                testList.add(new Test(true, matcher.group(1).trim(), matcher.group(2).trim()));
            }

            if (testList.isEmpty()) {
                return false;
            }

            problem.setCode(problemCode);
            problem.setName(problemName);
            problem.setSizeLimit(sizeLimit);
            problem.setTimeLimit(timeLimit);
            problem.setMemoryLimit(memoryLimit);
            problem.setTestList(testList);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean parseContest(Contest contest) {
        try {
            Matcher matcher = Pattern.compile(CONTEST_URL.replace("%s", "(.+)")).matcher(contest.getUrl());
            if (!matcher.find()) {
                return false;
            }

            JsonElement element = JsonUtils.read(new InputStreamReader(
                    new URL(String.format(CONTEST_API_URL, matcher.group(1))).openStream()));
            if (!element.isJsonObject()) {
                return false;
            }

            JsonObject contestObject = element.getAsJsonObject();
            if (!contestObject.has("status") || !contestObject.get("status").getAsString().equals("success")
                    || !contestObject.has("code") || !contestObject.has("name") || !contestObject.has("problems")) {
                return false;
            }

            String contestCode = contestObject.get("code").getAsString(),
                    contestName = contestObject.get("name").getAsString();

            ArrayList<Problem> problemList = new ArrayList<>();
            for (Map.Entry<String, JsonElement> entry : contestObject.get("problems").getAsJsonObject().entrySet()) {
                JsonObject problemObject = entry.getValue().getAsJsonObject();
                if (!problemObject.has("code") || !problemObject.has("name")) {
                    continue;
                }

                String problemCode = problemObject.get("code").getAsString(),
                        problemName = problemObject.get("name").getAsString(),
                        problemUrl = String.format(CONTEST_PROBLEM_URL, contestCode, problemCode);

                problemList.add(new Problem(problemUrl, problemCode, problemName));
            }

            if (problemList.isEmpty()) {
                return false;
            }

            contest.setCode(contestCode);
            contest.setName(contestName);
            contest.setProblemList(problemList);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean parseProblems(Platform platform, Category category, ParserListener listener) {
        try {
            Elements rowElements = Jsoup.connect(category.getUrl()).get().getElementsByClass("problemrow");
            if (rowElements.isEmpty()) {
                return false;
            }

            ArrayList<Problem> problemList = new ArrayList<>();
            for (Element rowElement : rowElements) {
                if (rowElement.children().size() != 4) {
                    continue;
                }

                String problemCode = rowElement.child(1).child(0).html(),
                        problemName = rowElement.child(0).child(0).child(0).child(0).html(),
                        problemUrl = String.format(PROBLEM_URL, problemCode);

                Problem problem = new Problem(problemUrl, problemCode, problemName);
                problemList.add(problem);

                listener.problemParsed(problem);
            }

            if (problemList.isEmpty()) {
                return false;
            }

            platform.setProblemList(problemList);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean parseContests(Platform platform, Category category, ParserListener listener) {
        try {
            Elements tableElements = Jsoup.connect(category.getUrl()).get().getElementsByClass("dataTable");
            if (tableElements.isEmpty()) {
                return false;
            }

            String categoryCode = category.getCode();
            if (categoryCode.equals("present")) {
                if (tableElements.size() == 1) {
                    return false;
                }
            } else if (categoryCode.equals("past")) {
                for (int i = 0; i < tableElements.size() - 1; i++) {
                    tableElements.remove(i);
                }
            }

            ArrayList<Contest> contestList = new ArrayList<>();
            for (Element bodyElement : tableElements.get(0).getElementsByTag("tbody")) {
                for (Element rowElement : bodyElement.getElementsByTag("tr")) {
                    if (rowElement.children().size() != 4) {
                        continue;
                    }

                    String contestCode = rowElement.child(0).html(),
                            contestName = rowElement.child(1).child(0).html(),
                            contestUrl = String.format(CONTEST_URL, contestCode);

                    Contest contest = new Contest(contestUrl, contestCode, contestName);
                    contestList.add(contest);

                    listener.contestParsed(contest);
                }
            }

            if (contestList.isEmpty()) {
                return false;
            }

            platform.setContestList(contestList);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

}
