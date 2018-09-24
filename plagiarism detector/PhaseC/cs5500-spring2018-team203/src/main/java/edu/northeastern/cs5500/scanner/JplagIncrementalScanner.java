package edu.northeastern.cs5500.scanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;
import java.util.stream.Collectors;

import jplag.AllBasecodeMatches;
import jplag.AllMatches;
import jplag.AllMatches.AvgComparator;
import jplag.AllMatches.AvgReversedComparator;
import jplag.AllMatches.MaxComparator;
import jplag.AllMatches.MaxReversedComparator;
import jplag.AllMatches.MinReversedComparator;
import jplag.ExitException;
import jplag.GSTiling;
import jplag.HTMLFile;
import jplag.Report;
import jplag.SortedVector;
import jplag.Structure;
import jplag.Submission;
import jplag.Table;
import jplag.clustering.Cluster;
import jplag.clustering.Clusters;
import jplag.clustering.SimilarityMatrix;
import jplag.options.Options;
import jplag.options.util.Messages;
import jplagUtils.PropertiesLoader;

/**
 * JPlag scanner program based on https://github.com/jplag/jplag
 *
 * The program does the scanning and creates the scanning report.
 */
public class JplagIncrementalScanner extends jplag.Program {
    private static final Properties versionProps = PropertiesLoader.loadProps("jplag/version.properties");
    public static final String name;
    public static final String name_long;
    public DateFormat dateFormat;
    public DateFormat dateTimeFormat;
    public String currentSubmissionName = "<Unknown submission>";
    public Vector<String> errorVector = new Vector();
    private Submission basecodeSubmission = null;
    public Clusters clusters = null;
    private int errors = 0;
    private String invalidSubmissionNames = null;
    private HashSet<String> excluded = null;
    protected GSTiling gSTiling = new GSTiling(this);
    private Hashtable<String, AllBasecodeMatches> htBasecodeMatches = new Hashtable(30);
    private Vector<String> included = null;
    private Options options;
    public Report report;
    public Messages msg;
    private Runtime runtime = Runtime.getRuntime();
    private Vector<Submission> submissions;
    private FileWriter writer = null;
    private Map<String, String> subStudents;
    private List<edu.northeastern.cs5500.models.Submission> skipSubmissions;
    private Set<String> skipSubmissionIds;

    /**
     * Constructor for JPlag scanner
     * @param options
     * @throws ExitException
     */
    public JplagIncrementalScanner(Options options) throws ExitException {
        super(options);
        this.options = options;
        this.options.initializeSecondStep(this);
        if (this.options.language == null) {
            throw new ExitException("Language not initialized!", 401);
        } else {
            this.msg = new Messages(this.options.getCountryTag());
            if (this.options.getCountryTag().equals("de")) {
                this.dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                this.dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss 'GMT'");
            } else {
                this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                this.dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss 'GMT'");
            }

            this.dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            this.dateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            this.report = new JplagReport(this, get_language());
        }
    }


    /**
     * Set submissionid, studentName
     * @param subStudents
     */
    public void setSubStudents(Map<String, String> subStudents) {
        this.subStudents = subStudents;
        ((JplagReport) report).setSubStudents(subStudents);
    }

    /**
     * Scan report set
     * @param scanReport
     */
    public void setScanReport(edu.northeastern.cs5500.models.Report scanReport) {
        ((JplagReport) report).setScanReport(scanReport);
    }

    /**
     * Method to skip submissions
     * @param skipSubmissions list of submissions to skip
     */
    public void setSkipSubmissions(List<edu.northeastern.cs5500.models.Submission> skipSubmissions) {
        this.skipSubmissions = skipSubmissions;
        this.skipSubmissionIds = skipSubmissions.stream().map(l -> Integer.toString(l.getid())).
                collect(Collectors.toSet());
    }

   // Third party code
    protected int validSubmissions() {
        if (this.submissions == null) {
            return 0;
        } else {
            int size = 0;

            for(int i = this.submissions.size() - 1; i >= 0; --i) {
                if (!((Submission)this.submissions.elementAt(i)).errors) {
                    ++size;
                }
            }

            return size;
        }
    }

    // Third party code
    protected String allValidSubmissions(String separator) {
        String res = "";
        int size = this.submissions.size();
        boolean firsterr = true;

        for(int i = 0; i < size; ++i) {
            Submission subm = (Submission)this.submissions.elementAt(i);
            if (!subm.errors) {
                res = res + (!firsterr ? separator : "") + subm.name;
                firsterr = false;
            }
        }

        return res;
    }

    /**
     * Third party code
     */
    protected String allInvalidSubmissions() {
        return this.invalidSubmissionNames;
    }

    /**
     * Third party code
     */
    public void closeWriter() {
        try {
            if (this.writer != null) {
                this.writer.close();
            }
        } catch (IOException var2) {
            var2.printStackTrace();
        }

        this.writer = null;
    }

    /**
     * Third party code
     */
    public void addError(String errorMsg) {
        this.errorVector.add("[" + this.currentSubmissionName + "]\n" + errorMsg);
        this.print(errorMsg, (String)null);
    }


    /**
     * Third party code
     */
    public void print(String normal, String lng) {
        if (this.options.verbose_parser) {
            if (lng != null) {
                this.myWrite(lng);
            } else if (normal != null) {
                this.myWrite(normal);
            }
        }

        if (!this.options.verbose_quiet) {
            try {
                if (normal != null) {
                    System.out.print(normal);
                }

                if (lng != null && this.options.verbose_long) {
                    System.out.print(lng);
                }
            } catch (Throwable var4) {
                System.out.println(var4.getMessage());
            }

        }
    }

    // Third party code
    private void throwNotEnoughSubmissions() throws ExitException {
        StringBuilder errorStr = new StringBuilder();
        Iterator var2 = this.errorVector.iterator();

        while(var2.hasNext()) {
            String str = (String)var2.next();
            errorStr.append(str);
            errorStr.append('\n');
        }

        throw new ExitException("Not enough valid submissions! (only " + this.validSubmissions() + " " + (this.validSubmissions() != 1 ? "are" : "is") + " valid):\n" + errorStr.toString(), 402);
    }

    // Third party code
    private void throwBadBasecodeSubmission() throws ExitException {
        StringBuilder errorStr = new StringBuilder();
        Iterator var2 = this.errorVector.iterator();

        while(var2.hasNext()) {
            String str = (String)var2.next();
            errorStr.append(str);
            errorStr.append('\n');
        }

        throw new ExitException("Bad basecode submission:\n" + errorStr.toString());
    }

    // Third party code
    private boolean shouldSkip(Submission submission) {
        if (submission != null && submission.name != null) {
            String[] sub= submission.name.split("_");
            return skipSubmissionIds.contains(sub[4]);
        }
        return false;
    }

    // Third party code
    private void compare() throws ExitException {
        int size = this.submissions.size();
        int[] dist = new int[10];
        SortedVector<AllMatches> avgmatches = new SortedVector(new AvgComparator());
        SortedVector<AllMatches> maxmatches = new SortedVector(new MaxComparator());
        this.options.setState(200);
        this.options.setProgress(0);
        long msec;
        Submission s1;
        int totalcomps;
        int i;
        if (this.options.useBasecode) {
            totalcomps = 0;
            msec = System.currentTimeMillis();

            for(i = 0; i < size; ++i) {
                s1 = (Submission)this.submissions.elementAt(i);
                AllBasecodeMatches bcmatch = this.gSTiling.compareWithBasecode(s1, this.basecodeSubmission);
                this.htBasecodeMatches.put(s1.name, bcmatch);
                this.gSTiling.resetBaseSubmission(this.basecodeSubmission);
                ++totalcomps;
                this.options.setProgress(totalcomps * 100 / size);
            }

            long timebc = System.currentTimeMillis() - msec;
            this.print("\n\n", "\nTime for comparing with Basecode: " + (timebc / 3600000L > 0L ? timebc / 3600000L + " h " : "") + (timebc / 60000L > 0L ? timebc / 60000L % 60000L + " min " : "") + timebc / 1000L % 60L + " sec\n" + "Time per basecode comparison: " + timebc / (long)size + " msec\n\n");
        }

        totalcomps = (size - 1) * size / 2;
        int anz = 0;
        int count = 0;
        this.options.setProgress(0);
        msec = System.currentTimeMillis();

        for(i = 0; i < size - 1; ++i) {
            s1 = (Submission)this.submissions.elementAt(i);
            if (s1.struct == null) {
                count += size - i - 1;
            } else {
                for(int j = i + 1; j < size; ++j) {
                    Submission s2 = (Submission)this.submissions.elementAt(j);

                    if(shouldSkip(s2)) {
                        continue;
                    }

                    if (s2.struct == null) {
                        ++count;
                    } else {
                        AllMatches match = this.gSTiling.compare(s1, s2);
                        ++anz;
                        System.out.println("Comparing " + s1.name + "-" + s2.name + ": " + match.percent());
                        if (this.options.useBasecode) {
                            match.bcmatchesA = (AllBasecodeMatches)this.htBasecodeMatches.get(match.subA.name);
                            match.bcmatchesB = (AllBasecodeMatches)this.htBasecodeMatches.get(match.subB.name);
                        }

                        this.registerMatch(match, dist, avgmatches, maxmatches, (SortedVector)null, i, j);
                        ++count;
                        this.options.setProgress(count * 100 / totalcomps);
                    }
                }
            }
        }

        this.options.setProgress(100);
        long time = System.currentTimeMillis() - msec;
        if (anz == 0) {
            return;
        }
        this.print("\n", "Total time for comparing submissions: " + (time / 3600000L > 0L ? time / 3600000L + " h " : "") + (time / 60000L > 0L ? time / 60000L % 60000L + " min " : "") + time / 1000L % 60L + " sec\n" + "Time per comparison: " + time / (long)anz + " msec\n");
        Cluster cluster = null;
        if (this.options.clustering) {
            cluster = this.clusters.calculateClustering(this.submissions);
        }

        this.writeResults(dist, avgmatches, maxmatches, (SortedVector)null, cluster);
    }

    // Third party code
    private void revisionCompare() throws ExitException {
        int size = this.submissions.size();
        int[] dist = new int[10];
        SortedVector<AllMatches> avgmatches = new SortedVector(new AvgReversedComparator());
        SortedVector<AllMatches> maxmatches = new SortedVector(new MaxReversedComparator());
        SortedVector<AllMatches> minmatches = new SortedVector(new MinReversedComparator());
        this.options.setState(200);
        this.options.setProgress(0);
        long msec;
        Submission s1;
        int totalcomps;
        if (this.options.useBasecode) {
            msec = System.currentTimeMillis();

            for(totalcomps = 0; totalcomps < size; ++totalcomps) {
                s1 = (Submission)this.submissions.elementAt(totalcomps);
                AllBasecodeMatches bcmatch = this.gSTiling.compareWithBasecode(s1, this.basecodeSubmission);
                this.htBasecodeMatches.put(s1.name, bcmatch);
                this.gSTiling.resetBaseSubmission(this.basecodeSubmission);
                this.options.setProgress((totalcomps + 1) * 100 / size);
            }

            long timebc = System.currentTimeMillis() - msec;
            this.print("\n\n", "\nTime for comparing with Basecode: " + (timebc / 3600000L > 0L ? timebc / 3600000L + " h " : "") + (timebc / 60000L > 0L ? timebc / 60000L % 60000L + " min " : "") + timebc / 1000L % 60L + " sec\n" + "Time per basecode comparison: " + timebc / (long)size + " msec\n\n");
        }

        totalcomps = size - 1;
        int anz = 0;
        int count = 0;
        this.options.setProgress(0);
        msec = System.currentTimeMillis();
        int i = 0;

        label57:
        while(i < size - 1) {
            s1 = (Submission)this.submissions.elementAt(i);
            if (s1.struct == null) {
                ++count;
            } else {
                int j = i;

                Submission s2;
                do {
                    ++j;
                    if (j >= size) {
                        break label57;
                    }

                    s2 = (Submission)this.submissions.elementAt(j);
                } while(s2.struct == null);

                AllMatches match = this.gSTiling.compare(s1, s2);
                ++anz;
                if (this.options.useBasecode) {
                    match.bcmatchesA = (AllBasecodeMatches)this.htBasecodeMatches.get(match.subA.name);
                    match.bcmatchesB = (AllBasecodeMatches)this.htBasecodeMatches.get(match.subB.name);
                }

                this.registerMatch(match, dist, avgmatches, maxmatches, minmatches, i, j);
                ++count;
                this.options.setProgress(count * 100 / totalcomps);
                i = j;
            }
        }

        this.options.setProgress(100);
        long time = System.currentTimeMillis() - msec;
        this.print("\n", "Total time for comparing submissions: " + (time / 3600000L > 0L ? time / 3600000L + " h " : "") + (time / 60000L > 0L ? time / 60000L % 60000L + " min " : "") + time / 1000L % 60L + " sec\n" + "Time per comparison: " + time / (long)anz + " msec\n");
        Cluster cluster = null;
        if (this.options.clustering) {
            cluster = this.clusters.calculateClustering(this.submissions);
        }

        this.writeResults(dist, avgmatches, maxmatches, minmatches, cluster);
    }

    // Third party code
    private void createSubmissions() throws ExitException {
        this.submissions = new Vector();
        File f = new File(this.options.root_dir);
        if (f != null && f.isDirectory()) {
            String[] list = null;

            try {
                list = f.list();
            } catch (SecurityException var8) {
                throw new ExitException("Unable to retrieve directory: " + this.options.root_dir + " Cause : " + var8.toString());
            }

            Arrays.sort(list);

            for(int i = 0; i < list.length; ++i) {
                File subm_dir = new File(f, list[i]);
                if (subm_dir.isDirectory()) {
                    if (this.options.exp && this.excludeFile(subm_dir.toString())) {
                        System.err.println("excluded: " + subm_dir);
                    } else {
                        File file_dir = this.options.sub_dir == null ? subm_dir : new File(subm_dir, this.options.sub_dir);
                        if (!file_dir.isDirectory()) {
                            throw new ExitException("Cannot find directory: " + file_dir.toString());
                        }

                        if (this.options.basecode.equals(subm_dir.getName())) {
                            this.basecodeSubmission = new Submission(subm_dir.getName(), file_dir, this.options.read_subdirs, this, this.get_language());
                        } else {
                            this.submissions.addElement(new Submission(subm_dir.getName(), file_dir, this.options.read_subdirs, this, this.get_language()));
                        }
                    }
                } else if (this.options.sub_dir == null) {
                    boolean ok = false;
                    String name = subm_dir.getName();

                    for(int j = 0; j < this.options.suffixes.length; ++j) {
                        if (name.endsWith(this.options.suffixes[j])) {
                            ok = true;
                            break;
                        }
                    }

                    if (ok) {
                        this.submissions.addElement(new Submission(name, f, this, this.get_language()));
                    }
                }
            }

        } else {
            throw new ExitException("\"" + this.options.root_dir + "\" is not a directory!");
        }
    }

    // Third party code
    private void createSubmissionsExp() throws ExitException {
        this.readIncludeFile();
        this.submissions = new Vector();
        File f = new File(this.options.root_dir);
        if (f != null && f.isDirectory()) {
            String[] list = new String[this.included.size()];
            this.included.copyInto(list);

            for(int i = 0; i < list.length; ++i) {
                File subm_dir = new File(f, list[i]);
                if (subm_dir != null && subm_dir.isDirectory()) {
                    if (this.options.exp && this.excludeFile(subm_dir.toString())) {
                        System.err.println("excluded: " + subm_dir);
                    } else {
                        File file_dir = this.options.sub_dir == null ? subm_dir : new File(subm_dir, this.options.sub_dir);
                        if (file_dir != null && file_dir.isDirectory()) {
                            this.submissions.addElement(new Submission(subm_dir.getName(), file_dir, this.options.read_subdirs, this, this.get_language()));
                        } else if (this.options.sub_dir == null) {
                            throw new ExitException(this.options.root_dir + " is not a directory!");
                        }
                    }
                }
            }

        } else {
            throw new ExitException(this.options.root_dir + " is not a directory!");
        }
    }

    // Third party code
    protected boolean excludeFile(String file) {
        if (this.excluded == null) {
            return false;
        } else {
            Iterator iter = this.excluded.iterator();

            do {
                if (!iter.hasNext()) {
                    return false;
                }
            } while(!file.endsWith((String)iter.next()));

            return true;
        }
    }

    // Third party code
    private void expCompare() throws ExitException {
        int size = this.validSubmissions();
        int[] similarity = new int[(size * size - size) / 2];
        int anzSub = this.submissions.size();
        int count = 0;
        long msec = System.currentTimeMillis();

        int i;
        for(i = 0; i < anzSub - 1; ++i) {
            Submission s1 = (Submission)this.submissions.elementAt(i);
            if (s1.struct != null) {
                for(int j = i + 1; j < anzSub; ++j) {
                    Submission s2 = (Submission)this.submissions.elementAt(j);
                    if (s2.struct != null) {
                        AllMatches match = this.gSTiling.compare(s1, s2);
                        similarity[count++] = (int)match.percent();
                    }
                }
            }
        }

        long time = System.currentTimeMillis() - msec;
        System.out.print(this.options.root_dir + " ");
        System.out.print(this.options.min_token_match + " ");
        System.out.print(this.options.filtername + " ");
        System.out.print(time + " ");

        for(i = 0; i < similarity.length; ++i) {
            System.out.print(similarity[i] + " ");
        }

        System.out.println();
    }

    // Third party code
    private void externalCompare() throws ExitException {
        int size = this.submissions.size();
        SortedVector<AllMatches> avgmatches = new SortedVector(new AvgComparator());
        SortedVector<AllMatches> maxmatches = new SortedVector(new MaxComparator());
        int[] dist = new int[10];
        this.print("Comparing: " + size + " submissions\n", (String)null);
        this.options.setState(200);
        this.options.setProgress(0);
        long totalComparisons = (long)(size * (size - 1) / 2);
        long count = 0L;
        long comparisons = 0L;
        this.print("Checking memory size...\n", (String)null);
        int index = this.fillMemory(0, size);
        long totalTime = 0L;
        int startA = 0;
        int endA = index / 2;
        int startB = endA + 1;
        int endB = index;

        while(true) {
            long startTime = System.currentTimeMillis();
            this.print("Comparing block A (" + startA + "-" + endA + ") to block A\n", (String)null);

            AllMatches match;
            Submission s1;
            Submission s2;
            int i;
            int j;
            for(i = startA; i <= endA; ++i) {
                this.options.setProgress((int)(count * 100L / totalComparisons));
                s1 = (Submission)this.submissions.elementAt(i);
                if (s1.struct == null) {
                    count += (long)(endA - i);
                } else {
                    for(j = i + 1; j <= endA; ++j) {
                        s2 = (Submission)this.submissions.elementAt(j);
                        if (s2.struct == null) {
                            ++count;
                        } else {
                            match = this.gSTiling.compare(s1, s2);
                            this.registerMatch(match, dist, avgmatches, maxmatches, (SortedVector)null, i, j);
                            ++comparisons;
                            ++count;
                        }
                    }
                }
            }

            this.options.setProgress((int)(count * 100L / totalComparisons));
            this.print("\n", (String)null);
            totalTime += System.currentTimeMillis() - startTime;
            String totalTimeStr;
            if (startA == startB) {
                totalTime += System.currentTimeMillis() - startTime;
                totalTimeStr = "" + (totalTime / 3600000L > 0L ? totalTime / 3600000L + " h " : "") + (totalTime / 60000L > 0L ? totalTime / 60000L % 60000L + " min " : "") + totalTime / 1000L % 60L + " sec";
                this.print("Total comparison time: " + totalTimeStr + "\nComparisons: " + count + "/" + comparisons + "/" + totalComparisons + "\n", (String)null);

                for(i = startA; i <= endA; ++i) {
                    ((Submission)this.submissions.elementAt(i)).struct = null;
                }

                this.runtime.runFinalization();
                this.runtime.gc();
                Thread.yield();
                Cluster cluster = null;
                if (this.options.clustering) {
                    cluster = this.clusters.calculateClustering(this.submissions);
                }

                this.writeResults(dist, avgmatches, maxmatches, (SortedVector)null, cluster);
                return;
            }

            while(true) {
                totalTimeStr = "" + (totalTime / 3600000L > 0L ? totalTime / 3600000L + " h " : "") + (totalTime / 60000L > 0L ? totalTime / 60000L % 60L + " min " : "") + totalTime / 1000L % 60L + " sec";
                long remain;
                if (comparisons != 0L) {
                    remain = totalTime * (totalComparisons - count) / comparisons;
                } else {
                    remain = 0L;
                }

                String remainTime = "" + (remain / 3600000L > 0L ? remain / 3600000L + " h " : "") + (remain / 60000L > 0L ? remain / 60000L % 60L + " min " : "") + remain / 1000L % 60L + " sec";
                this.print("Progress: " + 100L * count / totalComparisons + "%\nTime used for comparisons: " + totalTimeStr + "\nRemaining time (estimate): " + remainTime + "\n", (String)null);
                startTime = System.currentTimeMillis();
                this.print("Comparing block A (" + startA + "-" + endA + ") to block B (" + startB + "-" + endB + ")\n", (String)null);

                for(i = startB; i <= endB; ++i) {
                    this.options.setProgress((int)(count * 100L / totalComparisons));
                    s1 = (Submission)this.submissions.elementAt(i);
                    if (s1.struct == null) {
                        count += (long)(endA - startA + 1);
                    } else {
                        for(j = startA; j <= endA; ++j) {
                            s2 = (Submission)this.submissions.elementAt(j);
                            if (s2.struct == null) {
                                ++count;
                            } else {
                                match = this.gSTiling.compare(s1, s2);
                                this.registerMatch(match, dist, avgmatches, maxmatches, (SortedVector)null, i, j);
                                ++comparisons;
                                ++count;
                            }
                        }

                        s1.struct = null;
                    }
                }

                this.options.setProgress((int)(count * 100L / totalComparisons));
                this.print("\n", (String)null);
                totalTime += System.currentTimeMillis() - startTime;
                if (endB == size - 1) {
                    totalTimeStr = "" + (totalTime / 3600000L > 0L ? totalTime / 3600000L + " h " : "") + (totalTime / 60000L > 0L ? totalTime / 60000L % 60L + " min " : "") + totalTime / 1000L % 60L + " sec";
                    remain = totalTime * (totalComparisons - count) / comparisons;
                    remainTime = "" + (remain / 3600000L > 0L ? remain / 3600000L + " h " : "") + (remain / 60000L > 0L ? remain / 60000L % 60L + " min " : "") + remain / 1000L % 60L + " sec";
                    this.print("Progress: " + 100L * count / totalComparisons + "%\nTime used for comparisons: " + totalTimeStr + "\nRemaining time (estimate): " + remainTime + "\n", (String)null);

                    for(i = startA; i <= endA; ++i) {
                        ((Submission)this.submissions.elementAt(i)).struct = null;
                    }

                    this.runtime.runFinalization();
                    this.runtime.gc();
                    Thread.yield();
                    this.print("Find next A.\n", (String)null);
                    index = this.fillMemory(endA + 1, size);
                    if (index != size - 1) {
                        startA = endA + 1;
                        endA = startA + (index - startA + 1) / 2;
                        startB = endA + 1;
                        endB = index;
                    } else {
                        startA = startB;
                        endB = index;
                        endA = index;
                    }
                    break;
                }

                this.runtime.runFinalization();
                this.runtime.gc();
                Thread.yield();
                this.print("Finding next B\n", (String)null);
                index = this.fillMemory(endB + 1, size);
                startB = endB + 1;
                endB = index;
            }
        }
    }

    // Third party code
    private int fillMemory(int from, int size) {
        Submission sub = null;
        int index = from;
        this.runtime.runFinalization();
        this.runtime.gc();
        Thread.yield();
        long freeBefore = this.runtime.freeMemory();

        try {
            for(; index < size; ++index) {
                sub = (Submission)this.submissions.elementAt(index);
                sub.struct = new Structure();
                if (!sub.struct.load(new File("temp", sub.dir.getName() + sub.name))) {
                    sub.struct = null;
                }
            }
        } catch (OutOfMemoryError var9) {
            sub.struct = null;
            this.print("Memory overflow after loading " + (from - from + 1) + " submissions.\n", (String)null);
        }

        if (index >= size) {
            index = size - 1;
        }

        if (freeBefore / this.runtime.freeMemory() <= 2L) {
            return index;
        } else {
            for(int i = (index - from) / 2; i > 0; --i) {
                ((Submission)this.submissions.elementAt(index--)).struct = null;
            }

            this.runtime.runFinalization();
            this.runtime.gc();
            Thread.yield();

            long free;
            while(freeBefore / (free = this.runtime.freeMemory()) > 2L) {
                ((Submission)this.submissions.elementAt(index--)).struct = null;
                this.runtime.runFinalization();
                this.runtime.gc();
                Thread.yield();
            }

            this.print(free / 1024L / 1024L + "MByte freed. Current index: " + index + "\n", (String)null);
            return index;
        }
    }

    /**
     * Third party code
     */
    public String get_basecode() {
        return this.options.basecode;
    }
    /**
     * Third party code
     */
    public int get_clusterType() {
        return this.options.clusterType;
    }
    /**
     * Third party code
     */
    public String get_commandLine() {
        return this.options.commandLine;
    }
    /**
     * Third party code
     */
    public String getCountryTag() {
        return this.options.getCountryTag();
    }
    /**
     * Third party code
     */
    public int get_distri() {
        return 0;
    }
    /**
     * Third party code
     */
    public File get_jplagResult() {
        return new File(this.options.result_dir);
    }

    /*
    use parent
    public Language get_language() {
        return this.options.language;
    }*/
    /**
     * Third party code
     */
    public int get_min_token_match() {
        return this.options.min_token_match;
    }
    /**
     * Third party code
     */
    public String get_title() {
        return this.options.title;
    }
    /**
     * Third party code
     */
    public String get_original_dir() {
        return this.options.original_dir;
    }
    /**
     * Third party code
     */
    public String get_result_dir() {
        return this.options.result_dir;
    }
    /**
     * Third party code
     */
    public String get_root_dir() {
        return this.options.root_dir;
    }
    /**
     * Third party code
     */
    public SimilarityMatrix get_similarity() {
        return this.options.similarity;
    }
    /**
     * Third party code
     */
    public String get_sub_dir() {
        return this.options.sub_dir;
    }
    /**
     * Third party code
     */
    public String[] get_suffixes() {
        return this.options.suffixes;
    }
    /**
     * Third party code
     */
    public int[] get_themewords() {
        return this.options.themewords;
    }
    /**
     * Third party code
     */
    public float[] get_threshold() {
        return this.options.threshold;
    }
    /**
     * Third party code
     */
    public int getErrors() {
        return this.errors;
    }
    /**
     * Third party code
     */
    public void hash_distribution() {
        int[] dist = new int[20];
        int count = 0;
        Enumeration enum1 = this.submissions.elements();

        while(enum1.hasMoreElements()) {
            Structure struct = ((Submission)enum1.nextElement()).struct;
            if (struct != null) {
                Field field = null;
                try {
                    field = Structure.class.getDeclaredField("table");
                    field.setAccessible(true);
                    Object table = field.get(struct);
                    ((Table)(table)).count_dist(dist);
                    ++count;
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }

        System.out.println("Count: " + count);

        for(int i = 0; i < dist.length; ++i) {
            System.out.println(i + "\t" + dist[i]);
        }

    }
    // Third party code
    private void makeTempDir() throws ExitException {
        this.print((String)null, "Creating temporary dir.\n");
        File f = new File("temp");
        if (!f.exists() && !f.mkdirs()) {
            throw new ExitException("Cannot create temporary directory!");
        } else if (!f.isDirectory()) {
            throw new ExitException("'temp' is not a directory!");
        } else if (!f.canWrite()) {
            throw new ExitException("Cannot write directory: 'temp'");
        }
    }
    // Third party code
    private void myWrite(String str) {
        if (this.writer != null) {
            try {
                this.writer.write(str);
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        } else {
            System.out.print(str);
        }

    }
    // Third party code
    private void parseAll() throws ExitException {
        if (this.submissions == null) {
            System.out.println("  Nothing to parse!");
        } else {
            int count = 0;
            int totalcount = this.submissions.size();
            this.options.setState(100);
            this.options.setProgress(0);
            long msec = System.currentTimeMillis();
            Iterator<Submission> iter = this.submissions.iterator();
            if (this.options.externalSearch) {
                this.makeTempDir();
            }

            int invalid = 0;

            while(true) {
                while(iter.hasNext()) {
                    boolean ok = true;
                    boolean removed = false;
                    Submission subm = (Submission)iter.next();
                    this.print((String)null, "------ Parsing submission: " + subm.name + "\n");
                    this.currentSubmissionName = subm.name;
                    this.options.setProgress(count * 100 / totalcount);
                    if (!(ok = subm.parse())) {
                        ++this.errors;
                    }

                    if (this.options.exp && this.options.filter != null) {
                        subm.struct = this.options.filter.filter(subm.struct);
                    }

                    ++count;
                    if (subm.struct != null && subm.size() < this.options.min_token_match) {
                        this.print((String)null, "Submission contains fewer tokens than minimum match length allows!\n");
                        subm.struct = null;
                        ++invalid;
                        removed = true;
                    }

                    if (this.options.externalSearch && subm.struct != null) {
                        this.gSTiling.create_hashes(subm.struct, this.options.min_token_match, false);
                        subm.struct.save(new File("temp", subm.dir.getName() + subm.name));
                        subm.struct = null;
                    }

                    if (!this.options.externalSearch && subm.struct == null) {
                        this.invalidSubmissionNames = this.invalidSubmissionNames == null ? subm.name : this.invalidSubmissionNames + " - " + subm.name;
                        iter.remove();
                    }

                    if (ok && !removed) {
                        this.print((String)null, "OK\n");
                    } else {
                        this.print((String)null, "ERROR -> Submission removed\n");
                    }
                }

                this.options.setProgress(100);
                this.print("\n" + (count - this.errors - invalid) + " submissions parsed successfully!\n" + this.errors + " parser error" + (this.errors != 1 ? "s!\n" : "!\n"), (String)null);
                if (invalid != 0) {
                    this.print((String)null, invalid + (invalid == 1 ? " submission is not valid because it contains" : " submissions are not valid because they contain") + " fewer tokens\nthan minimum match length allows.\n");
                }

                long time = System.currentTimeMillis() - msec;
                this.print("\n\n", "\nTotal time for parsing: " + (time / 3600000L > 0L ? time / 3600000L + " h " : "") + (time / 60000L > 0L ? time / 60000L % 60000L + " min " : "") + time / 1000L % 60L + " sec\n" + "Time per parsed submission: " + (count > 0 ? time / (long)count : "n/a") + " msec\n\n");
                return;
            }
        }
    }
    // Third party code
    private void parseBasecodeSubmission() throws ExitException {
        Submission subm = this.basecodeSubmission;
        if (subm == null) {
            this.options.useBasecode = false;
        } else {
            long msec = System.currentTimeMillis();
            this.print("----- Parsing basecode submission: " + subm.name + "\n", (String)null);
            if (this.options.externalSearch) {
                this.makeTempDir();
            }

            if (!subm.parse()) {
                this.throwBadBasecodeSubmission();
            }

            if (this.options.exp && this.options.filter != null) {
                subm.struct = this.options.filter.filter(subm.struct);
            }

            if (subm.struct != null && subm.size() < this.options.min_token_match) {
                throw new ExitException("Basecode submission contains fewer tokens than minimum match length allows!\n");
            } else {
                if (this.options.useBasecode) {
                    this.gSTiling.create_hashes(subm.struct, this.options.min_token_match, true);
                }

                if (this.options.externalSearch && subm.struct != null) {
                    this.gSTiling.create_hashes(subm.struct, this.options.min_token_match, false);
                    subm.struct.save(new File("temp", subm.dir.getName() + subm.name));
                    subm.struct = null;
                }

                this.print("\nBasecode submission parsed!\n", (String)null);
                long time = System.currentTimeMillis() - msec;
                this.print("\n", "\nTime for parsing Basecode: " + (time / 3600000L > 0L ? time / 3600000L + " h " : "") + (time / 60000L > 0L ? time / 60000L % 60000L + " min " : "") + time / 1000L % 60L + " sec\n");
            }
        }
    }
    // Third party code
    private void readExclusionFile() {
        if (this.options.exclude_file != null) {
            this.excluded = new HashSet();

            try {
                BufferedReader in = new BufferedReader(new FileReader(this.options.exclude_file));

                String line;
                while((line = in.readLine()) != null) {
                    this.excluded.add(line.trim());
                }

                in.close();
            } catch (FileNotFoundException var3) {
                System.out.println("Exclusion file not found: " + this.options.exclude_file);
            } catch (IOException var4) {
                ;
            }

            this.print((String)null, "Excluded files:\n");
            if (this.options.verbose_long) {
                Iterator iter = this.excluded.iterator();

                while(iter.hasNext()) {
                    this.print((String)null, "  " + (String)iter.next() + "\n");
                }
            }

        }
    }
    // Third party code
    private void readIncludeFile() {
        if (this.options.include_file != null) {
            this.included = new Vector();

            try {
                BufferedReader in = new BufferedReader(new FileReader(this.options.include_file));

                String line;
                while((line = in.readLine()) != null) {
                    this.included.addElement(line.trim());
                }

                in.close();
            } catch (FileNotFoundException var3) {
                System.out.println("Include file not found: " + this.options.include_file);
            } catch (IOException var4) {
                ;
            }

            this.print((String)null, "Included dirs:\n");
            if (this.options.verbose_long) {
                Enumeration enum1 = this.included.elements();

                while(enum1.hasMoreElements()) {
                    this.print((String)null, "  " + (String)enum1.nextElement() + "\n");
                }
            }

        }
    }
    // Third party code
    private void registerMatch(AllMatches match, int[] dist, SortedVector<AllMatches> avgmatches, SortedVector<AllMatches> maxmatches, SortedVector<AllMatches> minmatches, int a, int b) {
        float avgpercent = match.percent();
        float maxpercent = match.percentMaxAB();
        float minpercent = match.percentMinAB();
        ++dist[(int)avgpercent / 10 == 10 ? 9 : (int)avgpercent / 10];
        if (!this.options.store_percent) {
            if ((avgmatches.size() < this.options.store_matches || avgpercent > ((AllMatches)avgmatches.lastElement()).percent()) && avgpercent > 0.0F) {
                avgmatches.insert(match);
                if (avgmatches.size() > this.options.store_matches) {
                    avgmatches.removeElementAt(this.options.store_matches);
                }
            }

            if (maxmatches != null && (maxmatches.size() < this.options.store_matches || maxpercent > ((AllMatches)maxmatches.lastElement()).percent()) && maxpercent > 0.0F) {
                maxmatches.insert(match);
                if (maxmatches.size() > this.options.store_matches) {
                    maxmatches.removeElementAt(this.options.store_matches);
                }
            }

            if (minmatches != null && (minmatches.size() < this.options.store_matches || minpercent > ((AllMatches)minmatches.lastElement()).percent()) && minpercent > 0.0F) {
                minmatches.insert(match);
                if (minmatches.size() > this.options.store_matches) {
                    minmatches.removeElementAt(this.options.store_matches);
                }
            }
        } else {
            if (avgpercent > (float)this.options.store_matches) {
                avgmatches.insert(match);
                if (avgmatches.size() > 1000) {
                    avgmatches.removeElementAt(1000);
                }
            }

            if (maxmatches != null && maxpercent > (float)this.options.store_matches) {
                maxmatches.insert(match);
                if (maxmatches.size() > 1000) {
                    maxmatches.removeElementAt(1000);
                }
            }

            if (minmatches != null && minpercent > (float)this.options.store_matches) {
                minmatches.insert(match);
                if (minmatches.size() > 1000) {
                    minmatches.removeElementAt(1000);
                }
            }
        }

        if (this.options.clustering) {
            this.options.similarity.setSimilarity(a, b, avgpercent);
        }

    }
    // Third party code
    private String toUTF8(String str) {
        byte[] utf8 = null;

        try {
            utf8 = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException var4) {
            ;
        }

        return new String(utf8);
    }
    // Third party code
    public void run() throws ExitException {
        if (this.options.output_file != null) {
            try {
                this.writer = new FileWriter(new File(this.options.output_file));
                this.writer.write(name_long + "\n");
                this.writer.write(this.dateTimeFormat.format(new Date()) + "\n\n");
            } catch (IOException var10) {
                System.out.println("Unable to open or write to log file: " + this.options.output_file);
                throw new ExitException("Unable to create log file!");
            }
        } else {
            this.print((String)null, name_long + "\n\n");
        }

        this.print((String)null, "Language: " + this.options.language.name() + "\n\n");
        if (this.options.original_dir == null) {
            this.print((String)null, "Root-dir: " + this.options.root_dir + "\n");
        }

        this.readExclusionFile();
        if (this.options.include_file == null) {
            this.createSubmissions();
            System.out.println(this.submissions.size() + " submissions");
        } else {
            this.createSubmissionsExp();
        }

        if (!this.options.skipParse) {
            try {
                this.parseAll();
                System.gc();
                this.parseBasecodeSubmission();
            } catch (OutOfMemoryError var7) {
                this.submissions = null;
                System.gc();
                System.out.println("[" + new Date() + "] OutOfMemoryError " + "during parsing of submission \"" + this.currentSubmissionName + "\"");
                throw new ExitException("Out of memory during parsing of submission \"" + this.currentSubmissionName + "\"");
            } catch (ExitException var8) {
                throw var8;
            } catch (Throwable var9) {
                System.out.println("[" + new Date() + "] Unknown exception " + "during parsing of submission \"" + this.currentSubmissionName + "\"");
                var9.printStackTrace();
                throw new ExitException("Unknown exception during parsing of submission \"" + this.currentSubmissionName + "\"");
            }
        } else {
            this.print("Skipping parsing...\n", (String)null);
        }

        if (this.validSubmissions() < 2) {
            this.throwNotEnoughSubmissions();
        }

        this.errorVector = null;
        if (this.options.clustering) {
            this.clusters = new Clusters(this);
            this.options.similarity = new SimilarityMatrix(this.submissions.size());
        }

        System.gc();
        if (this.options.exp) {
            this.expCompare();
        } else if (this.options.externalSearch) {
            try {
                this.externalCompare();
            } catch (OutOfMemoryError var6) {
                var6.printStackTrace();
            }
        } else if (this.options.compare > 0) {
            this.specialCompare();
        } else {
            switch(this.options.comparisonMode) {
                case 0:
                    this.compare();
                    break;
                case 1:
                    this.revisionCompare();
                    break;
                default:
                    throw new ExitException("Illegal comparison mode: \"" + this.options.comparisonMode + "\"");
            }
        }

        this.closeWriter();
        String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
        str = str + "<jplag_infos>\n";
        str = str + "<infos \n";
        String sp = "\"";
        str = str + " title = " + sp + this.toUTF8(this.options.getTitle()) + sp;
        str = str + " source = " + sp + (this.get_original_dir() != null ? this.toUTF8(this.get_original_dir()) : "") + sp;
        str = str + " n_of_programs = " + sp + this.submissions.size() + sp;
        str = str + " errors = " + sp + this.get_language().errorsCount() + sp;
        str = str + " path_to_files = " + sp + this.toUTF8(this.options.sub_dir != null ? this.options.sub_dir : "") + sp;
        str = str + " basecode_dir = " + sp + this.toUTF8(this.options.basecode != null ? this.options.basecode : "") + sp;
        str = str + " read_subdirs = " + sp + this.options.read_subdirs + sp;
        str = str + " clustertype = " + sp + this.options.getClusterTyp() + sp;
        str = str + " store_matches = " + sp + this.options.store_matches + (this.options.store_percent ? "%" : "") + sp;
        String suf = "";

        for(int s = 0; s < this.options.suffixes.length; ++s) {
            suf = suf + "," + this.options.suffixes[s];
        }

        str = str + " suffixes = " + sp + suf.substring(1) + sp;
        str = str + " language_name = " + sp + this.options.languageName + sp;
        str = str + " comparison_mode = " + sp + this.options.comparisonMode + sp;
        str = str + " country_tag = " + sp + this.options.getCountryTag() + sp;
        str = str + " min_token = " + sp + this.options.min_token_match + sp;
        str = str + " date = " + sp + System.currentTimeMillis() + sp;
        str = str + "/>\n";
        str = str + "</jplag_infos>";

        try {
            FileWriter fw = new FileWriter(new File(this.options.result_dir + File.separator + "result.xml"));
            fw.write(str);
            fw.close();
        } catch (IOException var5) {
            System.out.println("Unable to create result.xml");
        }

    }

    /**
     * Third party code
     */
    public void set_result_dir(String result_dir) {
        this.options.result_dir = result_dir;
    }
    // Third party code
    private void specialCompare() throws ExitException {
        File root = new File(this.options.result_dir);
        HTMLFile f = this.report.openHTMLFile(root, "index.html");
        this.report.copyFixedFiles(root);
        this.report.writeIndexBegin(f, "Special Search Results");
        f.println("<P><A NAME=\"matches\"><H4>Matches:</H4><P>");
        int size = this.submissions.size();
        int matchIndex = 0;
        this.print("Comparing: ", this.validSubmissions() + " submissions");
        this.print("\n(Writing results at the same time.)\n", (String)null);
        this.options.setState(200);
        this.options.setProgress(0);
        int totalcomps = size * size;
        int anz = 0;
        int count = 0;
        long msec = System.currentTimeMillis();

        for(int i = 0; i < size - 1; ++i) {
            SortedVector<AllMatches> matches = new SortedVector(new AvgComparator());
            Submission s1 = (Submission)this.submissions.elementAt(i);
            if (s1.struct == null) {
                count += size - 1;
            } else {
                AllMatches match;
                for(int j = 0; j < size; ++j) {
                    Submission s2 = (Submission)this.submissions.elementAt(j);
                    if (i != j && s2.struct != null) {
                        match = this.gSTiling.compare(s1, s2);
                        ++anz;
                        float percent = match.percent();
                        if ((matches.size() < this.options.compare || matches.size() == 0 || match.moreThan(((AllMatches)matches.lastElement()).percent())) && match.moreThan(0.0F)) {
                            matches.insert(match);
                            if (matches.size() > this.options.compare) {
                                matches.removeElementAt(this.options.compare);
                            }
                        }

                        if (this.options.clustering) {
                            this.options.similarity.setSimilarity(i, j, percent);
                        }

                        ++count;
                        this.options.setProgress(count * 100 / totalcomps);
                    } else {
                        ++count;
                    }
                }

                f.println("<TABLE CELLPADDING=3 CELLSPACING=2>");
                boolean once = true;
                Iterator iter = matches.iterator();

                while(iter.hasNext()) {
                    match = (AllMatches)iter.next();
                    if (once) {
                        f.println("<TR><TD BGCOLOR=" + this.report.color(match.percent(), 128, 192, 128, 192, 255, 255) + ">" + s1.name + "<TD WIDTH=\"10\">-&gt;");
                        once = false;
                    }

                    int other = match.subName(0).equals(s1.name) ? 1 : 0;
                    f.println(" <TD BGCOLOR=" + this.report.color(match.percent(), 128, 192, 128, 192, 255, 255) + " ALIGN=center><A HREF=\"match" + matchIndex + ".html\">" + match.subName(other) + "</A><BR><FONT COLOR=\"" + this.report.color(match.percent(), 0, 255, 0, 0, 0, 0) + "\"><B>(" + match.roundedPercent() + "%)</B></FONT>");
                    this.report.writeMatch(root, matchIndex++, match);
                }

                f.println("</TR>");
            }
        }

        f.println("</TABLE><P>\n");
        f.println("<!---->");
        this.report.writeIndexEnd(f);
        f.close();
        this.options.setProgress(100);
        long time = System.currentTimeMillis() - msec;
        this.print("\n", "Total time: " + (time / 3600000L > 0L ? time / 3600000L + " h " : "") + (time / 60000L > 0L ? time / 60000L % 60000L + " min " : "") + time / 1000L % 60L + " sec\n" + "Time per comparison: " + time / (long)anz + " msec\n");
    }

    /**
     * Third party code
     */
    public void token_distribution() {
        int[] count = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Enumeration enum1 = this.submissions.elements();

        while(true) {
            Structure struct;
            int i;
            do {
                if (!enum1.hasMoreElements()) {
                    int tot = 0;

                    for(i = 0; i < 0; ++i) {
                        int c = count[i];
                        tot += c;
                    }

                    System.out.println((tot > 999 ? "" : (tot > 99 ? " " : (tot > 9 ? "  " : "   "))) + tot);
                    return;
                }

                struct = ((Submission)enum1.nextElement()).struct;
            } while(struct == null);

            for(i = struct.size() - 1; i >= 0; --i) {
                ++count[struct.tokens[i].type];
            }
        }
    }

    /**
     * Third party code
     */
    public boolean use_clustering() {
        return this.options.clustering;
    }

    /**
     * Third party code
     */
    public boolean use_debugParser() {
        return this.options.debugParser;
    }

    /**
     * Third party code
     */
    public boolean use_diff_report() {
        return this.options.diff_report;
    }

    /**
     * Third party code
     */
    public boolean use_externalSearch() {
        return this.options.externalSearch;
    }

    /**
     * Third party code
     */
    public boolean use_verbose_details() {
        return this.options.verbose_details;
    }

    /**
     * Third party code
     */
    public boolean use_verbose_long() {
        return this.options.verbose_long;
    }

    /**
     * Third party code
     */
    public boolean use_verbose_parser() {
        return this.options.verbose_parser;
    }

    /**
     * Third party code
     */
    public boolean use_verbose_quiet() {
        return this.options.verbose_quiet;
    }

    /**
     * Third party code
     */
    public boolean useBasecode() {
        return this.options.useBasecode;
    }

    /**
     * Third party code
     */
    private void writeResults(int[] dist, SortedVector<AllMatches> avgmatches, SortedVector<AllMatches> maxmatches, SortedVector<AllMatches> minmatches, Cluster clustering) throws ExitException {
        this.options.setState(250);
        this.options.setProgress(0);
        if (this.options.original_dir == null) {
            this.print("Writing results to: " + this.options.result_dir + "\n", (String)null);
        }

        File f = new File(this.options.result_dir);
        if (!f.exists() && !f.mkdirs()) {
            throw new ExitException("Cannot create directory!");
        } else if (!f.isDirectory()) {
            throw new ExitException(this.options.result_dir + " is not a directory!");
        } else if (!f.canWrite()) {
            throw new ExitException("Cannot write directory: " + this.options.result_dir);
        } else {
            this.report.write(f, dist, avgmatches, maxmatches, minmatches, clustering, this.options);
            if (this.options.externalSearch) {
                this.writeTextResult(f, avgmatches);
            }

        }
    }

    /**
     * Third party code
     */
    private void writeTextResult(File dir, SortedVector<AllMatches> matches) {
        Iterator<AllMatches> iter = matches.iterator();
        this.print("Writing special 'matches.txt' file\n", (String)null);

        try {
            File f = new File(dir, "matches.txt");

            PrintWriter writer;
            AllMatches match;
            String file1;
            String file2;
            for(writer = new PrintWriter(new FileWriter(f)); iter.hasNext(); writer.println(file1 + "\t" + file2 + "\t" + match.percent())) {
                match = (AllMatches)iter.next();
                file1 = match.subA.name;
                file2 = match.subB.name;
                if (file1.compareTo(file2) > 0) {
                    String tmp = file2;
                    file2 = file1;
                    file1 = tmp;
                }
            }

            writer.close();
        } catch (IOException var10) {
            this.print("IOException while writing file\n", (String)null);
        }

    }

    static {
        name = "JPlag" + versionProps.getProperty("version", "devel");
        name_long = "JPlag (Version " + versionProps.getProperty("version", "devel") + ")";
    }
}
