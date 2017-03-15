package moviereview.dao.impl;

import moviereview.dao.MovieDao;
import moviereview.model.Movie;
import moviereview.model.Review;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kray on 2017/3/7.
 */

@Repository
public class MovieDaoImpl implements MovieDao {

    //constant
    private static final String SEPARATOR = "--------------------";
    private static final int INFO_IN_ONE_FILE = 1000;

    //local
    private static final String FILE_LOCATION = "/Users/Kray/Desktop/MovieSmallCache";
    private static final String PYTHON_FILE_LOCATION = "/Users/Kray/Desktop/MovieWordCounter";
    //    server
//    private static final String FILE_LOCATION = "/mydata/moviereview/MovieSmallCache";
//    private static final String PYTHON_FILE_LOCATION = "/mydata/moviereview/MovieWordCounter";
    //file
    private File movieIndexFile;
    private File userIndexFile;
    private File movieIndexWithNameFile;
    private File tempResultFile;

    /**
     * writer
     */
    //BufferedWriter
    private BufferedWriter resultBufferedWriter;
    private BufferedWriter movieIndexBufferedWriter;
    private BufferedWriter userIndexBufferedWriter;
    private BufferedWriter movieIndexWithNameBufferedWriter;
    private BufferedWriter tempResultBufferedWriter;

    /**
     * reader
     */
    //BufferedReader
    private BufferedReader sourceFileBufferedReader;


    /**
     * logger
     */
    private Logger logger;
    private FileHandler fileHandler;

    /**
     * 分割源文件并索引
     */
    public MovieDaoImpl() {
        //初始化file
//        File sourceFile = new File(filePath);
        File resultFile = new File(FILE_LOCATION + "/result0.txt");
        movieIndexFile = new File(FILE_LOCATION + "/movieIndex.txt");
        userIndexFile = new File(FILE_LOCATION + "/userIndex.txt");
        movieIndexWithNameFile = new File(FILE_LOCATION + "/movieIndexWithName.txt");
        tempResultFile = new File(PYTHON_FILE_LOCATION + "/tempResult.txt");
        //初始化一级I/O
        try {
//            FileReader sourceFileReader = new FileReader(sourceFile);
            FileWriter resultWriter = new FileWriter(resultFile, true);
            FileWriter movieIndexWriter = new FileWriter(movieIndexFile, true);
            FileWriter userIndexWriter = new FileWriter(userIndexFile, true);
            FileWriter movieIndexWithNameWriter = new FileWriter(movieIndexWithNameFile, true);
            FileWriter tempResultWriter = new FileWriter(movieIndexWithNameFile, false);

            movieIndexBufferedWriter = new BufferedWriter(movieIndexWriter);
            resultBufferedWriter = new BufferedWriter(resultWriter);
//            sourceFileBufferedReader = new BufferedReader(sourceFileReader);
            userIndexBufferedWriter = new BufferedWriter(userIndexWriter);
            movieIndexWithNameBufferedWriter = new BufferedWriter(movieIndexWithNameWriter);
            tempResultBufferedWriter = new BufferedWriter(tempResultWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //初始化缓存I/O

        //初始化logger
        initLogger();
    }


    /**
     * flush buffer
     */
    private void flushFiles() {
        try {
            movieIndexBufferedWriter.flush();
            resultBufferedWriter.flush();
            userIndexBufferedWriter.flush();
            movieIndexWithNameBufferedWriter.flush();
            tempResultBufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * change a file to record new data
     *
     * @param i data NO.
     * @throws IOException handle by up level
     */
    private BufferedWriter changeFileToWrite(BufferedWriter resultBufferedWriter, int i) throws IOException {
        if (resultBufferedWriter != null) {
            resultBufferedWriter.close();
        }

        File fileToWrite = new File(FILE_LOCATION + "/result" + getFileIndex(i) + ".txt");
        FileWriter resultWriter = new FileWriter(fileToWrite, true);
        return new BufferedWriter(resultWriter);
    }

    /**
     * close file stream
     */
    private void closeFiles() {
        try {
            movieIndexBufferedWriter.close();
            sourceFileBufferedReader.close();
            resultBufferedWriter.close();
            userIndexBufferedWriter.close();
            movieIndexWithNameBufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get File index by data number
     *
     * @param number data number
     * @return file index
     */
    private int getFileIndex(int number) {
        return (number - 1) / INFO_IN_ONE_FILE;
    }

    /**
     * get BufferedReader connecting to certain file
     *
     * @param fileToRead certain file
     * @return BufferedReader connecting to certain file
     */
    private BufferedReader getBufferedReader(File fileToRead) {
        FileReader reader = null;
        try {
            reader = new FileReader(fileToRead);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert reader != null : "can't connect to file " + fileToRead.getName();

        return new BufferedReader(reader);
    }

    /**
     * get BufferedWriter connecting to certain file
     *
     * @param fileToWrite certain file
     * @return BufferedWriter connecting to certain file
     */
    private BufferedWriter getBufferedWriter(File fileToWrite, boolean append) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileToWrite, append);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert writer != null : "can't connect to file " + movieIndexFile.getName();

        return new BufferedWriter(writer);
    }

    /**
     * handle the page fault
     */
    private BufferedReader changeFileToRead(BufferedReader beginBufferedReader, int dataNumber) throws IOException {
        if (beginBufferedReader != null) {
            beginBufferedReader.close();
        }
        File fileToRead = new File(FILE_LOCATION + "/result" + getFileIndex(dataNumber) + ".txt");
        FileReader beginFileReader = new FileReader(fileToRead);
        return new BufferedReader(beginFileReader);
    }

    private Review parseDataToReviewPO(BufferedReader reader) {
        String[] props = new String[8];
        try {
            for (int i = 0; i < 8; i++) {
                String[] temp = reader.readLine().split(": ");
                if (temp.length == 1) {
                    props[i] = "-1";
                } else {
                    props[i] = temp[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Review(props[0]
                , props[1]
                , props[2]
                , props[3]
                , Integer.parseInt(props[4].split("\\.")[0])
                , Long.parseLong(props[5])
                , props[6]
                , props[7]
        );
    }

    private void initLogger() {
        logger = Logger.getLogger("DataLogger");

        //设置handler
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);
        fileHandler = null;
        try {
            fileHandler = new FileHandler("../log.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        fileHandler.setLevel(Level.CONFIG);
        logger.addHandler(fileHandler);

        //设置formatter
        fileHandler.setFormatter(new DataLogFormatter());
    }

    /**
     * close all files
     * warning! This method should only be invoked when exit the system
     */
    public void close() {
        closeFiles();
        fileHandler.close();
    }

    /**
     * 通过用户ID寻找该用户的所有评论
     *
     * @param userId 用户ID
     * @return 所有评论集合的迭代器
     */
    public List<Review> findReviewsByUserId(String userId) {
        //用来读取索引列表
        BufferedReader indexBufferedReader = getBufferedReader(userIndexFile);
        //用来读取信息；
        BufferedReader dataBufferedReader = null;
        //用来保存搜索到的信息编号
        int index = 0;
        //用来暂存上一个信息文件编号
        int previousNumber = -1;
        //缓存一行信息
        String temp;
        List<Review> reviews = new ArrayList<Review>();
        try {

            Set<Review> reviewSet = new HashSet<Review>();
            while ((temp = indexBufferedReader.readLine()) != null) {
                //增加索引号
                index++;
                //略过不是该用户ID的索引
                if (!temp.split(":")[0].equals(" " + userId)) {
                    continue;
                }
                //下面就已经找到了需要的索引
                //查看是否需要更换文件
                if (getFileIndex(index) != previousNumber) {
                    dataBufferedReader = changeFileToRead(dataBufferedReader, index);
                }
                //
                String tag;
                while (true) {
                    //找到序号标签
                    while (!(tag = dataBufferedReader.readLine()).startsWith(SEPARATOR)) ;
                    //找到了合适的标签
                    if (Integer.parseInt(tag.split(SEPARATOR)[1]) == index) {
                        reviewSet.add(parseDataToReviewPO(dataBufferedReader));
//                        reviews.add(parseDataToReviewPO(dataBufferedReader));
                        break;
                    }
                }
            }

            reviews.addAll(reviewSet);

            return reviews;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (indexBufferedReader != null) {
                    indexBufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert false : "we should't get here!";
        return null;
    }

    /**
     * 通过电影ID寻找该电影的所有评论
     *
     * @param productId 电影ID
     * @return 所有评论集合的迭代器
     */
    public List<Review> findReviewsByMovieId(String productId) {

        BufferedReader indexBufferedReader = getBufferedReader(movieIndexFile);
        //在索引中寻找
        String temp;
        //查询时必要的组件和缓存
        BufferedReader beginBufferedReader = null;
        //保存结果的list
        List<Review> reviews = new ArrayList<Review>();
        try {
            indexBufferedReader.readLine();
            while ((temp = indexBufferedReader.readLine()) != null && !temp.split(":")[0].equals(" " + productId)) ;
            if (temp == null) {
                return Collections.emptyList();
            }
            //确定具体文件索引
            int length = temp.split(":")[1].split("/").length;
            int from = Integer.parseInt(temp.split(":")[1].split("/")[0]);
            int to = Integer.parseInt(temp.split(":")[1].split("/")[length - 1]);

            int beginIndex = getFileIndex(from);
            //开始寻找具体文件

            //开始进行查询
            //初始化管道
            beginBufferedReader = getBufferedReader(new File(FILE_LOCATION + "/result" + beginIndex + ".txt"));

            String tag;
            while (true) {
                //找到序号标签
                while (!(tag = beginBufferedReader.readLine()).startsWith(SEPARATOR)) ;
                //找到了合适的标签
                if (Integer.parseInt(tag.split(SEPARATOR)[1]) == from) {
                    for (int k = from; k <= to; k++) {
                        //如果必要，更换文件
                        if ((k - 1) % INFO_IN_ONE_FILE == 0) {
                            beginBufferedReader = changeFileToRead(beginBufferedReader, k);
                            //略过第一个标签
                            beginBufferedReader.readLine();
                        }
                        System.out.println("file" + k);
                        reviews.add(parseDataToReviewPO(beginBufferedReader));
                        beginBufferedReader.readLine();
                    }
                    break;
                }
            }

            assert reviews.size() == to - from + 1 : "Error in find movies";

            return reviews;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (beginBufferedReader != null) {
                    beginBufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 通过电影ID寻找指定的电影
     *
     * @param productId 电影ID
     * @return 指定的电影
     */
    public Movie findMovieByMovieId(String productId) {
        BufferedReader indexBufferedReader = getBufferedReader(movieIndexWithNameFile);
        //在索引中寻找
        String temp = null;
        //查询时必要的组件和缓存
        BufferedReader beginBufferedReader = null;
        //保存结果
        Movie movie = new Movie();
        try {
            while (true) {
                temp = indexBufferedReader.readLine();

                if (temp == null) {
                    break;
                }

                //找 ID
                String[] splitResult = temp.split(",");
                //如果 ID 匹配,找到,设定名字、ID
                if (splitResult[0].equals(productId)) {
                    movie.setId(splitResult[0]);

                    String movieName = "";
                    for (int i = 1; i < splitResult.length; i++) {
                        movieName += splitResult[i];
                    }
                    movie.setName(movieName);

                    return movie;
                }
            }
            //找不到电影
            return new Movie("-1", "Not Found");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (beginBufferedReader != null) {
                    beginBufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Movie("-1", "Not Found");
    }


    /**
     * 通过电影 ID 寻找该电影的词频统计
     *
     * @param productId 电影ID
     * @return 词频统计的迭代器
     */
    public Map<String, Integer> findWordCountByMovieId(String productId) {
        try {
            ArrayList<Review> reviews = (ArrayList<Review>) findReviewsByMovieId(productId);
            /*
                读取评论写到文件里
             */
            BufferedWriter output = tempResultBufferedWriter;
            try {
                File file = tempResultFile;
                output = getBufferedWriter(file, false);
                output = new BufferedWriter(new FileWriter(file));

                //都放到 Set 里
                Set<Review> reviewSet = new HashSet<Review>();
                for (Review review : reviews) {
                    reviewSet.add(review);
                }
                for (Review review : reviewSet) {
                    output.write(review.getText());
                    output.write("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (output != null) output.close();
            }

            String path = tempResultFile.getPath();
            System.out.println(path);

            /*
                分词
             */
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            CommandLine commandline = new CommandLine(new File(PYTHON_FILE_LOCATION + "/WordCounter.sh"));
            DefaultExecutor exec = new DefaultExecutor();
            exec.setExitValues(null);
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
            exec.setStreamHandler(streamHandler);
            exec.execute(commandline);
            String out = outputStream.toString("utf8");

            Map<String, Integer> result = new HashMap<String, Integer>();

            String[] pairs = out.split("\n");

            for (int i = 0; i < pairs.length; i++) {
                String pair = pairs[i];
                String[] pairSplit = pair.trim().split(" ");
                if (pairSplit.length == 2) {
                    result.put(pairSplit[1], Integer.parseInt(pairSplit[0]));
                }
            }

            Set<Map.Entry<String,Integer>> mapEntries = result.entrySet();
            List<Map.Entry<String,Integer>> aList = new LinkedList<Map.Entry<String,Integer>>(mapEntries);
            Collections.sort(aList, new Comparator<Map.Entry<String,Integer>>() {
                public int compare(Map.Entry<String, Integer> ele1, Map.Entry<String, Integer> ele2) {
                    return ele2.getValue().compareTo(ele1.getValue());
                }
            });
            Map<String,Integer> aMap2 = new LinkedHashMap<String, Integer>();
            for(Map.Entry<String,Integer> entry: aList) {
                aMap2.put(entry.getKey(), entry.getValue());
            }

            return aMap2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, Integer>();
    }

}
