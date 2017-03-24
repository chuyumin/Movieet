package po;

import javafx.scene.image.Image;
import util.FieldCount;

import java.time.LocalDate;
import java.util.EnumSet;

/**
 * Created by SilverNarcissus on 2017/3/3.
 * id name imageURL genre(标签/类型) duration releaseDate country language plot(简介/故事结构)
 * imdbId (director, writers, actors)
 */
public class MoviePO {
    /**
     * 电影海报
     */
    private Image poster;
    /**
     * 电影序列号
     */
    private String id;
    /**
     * 电影名称
     */
    private String name;
    /**
     * 电影图片URL
     */
    private String imageURL;
    /**
     * 片长(分)
     */
    private int duration;
    /**
     * 电影类型
     */
    private String genre;
    /**
     * 发布日期
     */
    private LocalDate releaseDate;
    /**
     * 电影国家
     */
    private String country;
    /**
     * 电影语言
     */
    private String language;
    /**
     * 电影剧情简介
     */
    private String plot;
    /**
     * 电影imdbId
     */
    private String imdbId;
    /**
     * 电影导演
     */
    private String director;
    /**
     * 电影创作者
     */

    private String writers;
    /**
     * 主要演员
     */
    private String actors;

    public MoviePO() {

    }
    public MoviePO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * 提供一个编辑PO的编辑器
     *
     * @return 编辑器
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    public Image getPoster() {
        return poster;
    }

    public void setPoster(Image poster) {
        this.poster = poster;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriters() {
        return writers;
    }

    public void setWriters(String writers) {
        this.writers = writers;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Builder {
        //要build的产品
        private MoviePO product;
        //用于查看域是否填充完全
        private EnumSet<FieldCount> fieldCount;

        //id name imageURL genre(标签/类型) duration releaseDate country language plot(简介/故事结构)
        // imdbId (director, writers, actors)
        private Builder() {
            product = new MoviePO();
            fieldCount = EnumSet.noneOf(FieldCount.class);
        }

        public Builder setActors(String actors) {
            product.actors = actors;
            fieldCount.add(FieldCount.attribute13);
            return this;
        }

        public Builder setWriters(String writers) {
            product.writers = writers;
            fieldCount.add(FieldCount.attribute12);
            return this;
        }

        public Builder setDirector(String director) {
            product.director = director;
            fieldCount.add(FieldCount.attribute11);
            return this;
        }

        public Builder setImdbId(String imdbId) {
            product.imdbId = imdbId;
            fieldCount.add(FieldCount.attribute10);
            return this;
        }

        public Builder setPlot(String plot) {
            product.plot = plot;
            fieldCount.add(FieldCount.attribute9);
            return this;
        }

        public Builder setLanguage(String language) {
            product.language = language;
            fieldCount.add(FieldCount.attribute8);
            return this;
        }

        public Builder setCountry(String country) {
            product.country = country;
            fieldCount.add(FieldCount.attribute7);
            return this;
        }

        public Builder setReleaseDate(LocalDate releaseDate) {
            product.releaseDate = releaseDate;
            fieldCount.add(FieldCount.attribute6);
            return this;
        }

        public Builder setDuration(int duration) {
            product.duration = duration;
            fieldCount.add(FieldCount.attribute5);
            return this;
        }

        public Builder setImageURL(String imageURL) {
            product.imageURL = imageURL;
            fieldCount.add(FieldCount.attribute3);
            return this;
        }

        public Builder setGenre(String genre) {
            product.genre = genre;
            fieldCount.add(FieldCount.attribute4);
            return this;
        }

        public Builder setId(String id) {
            product.id = id;
            fieldCount.add(FieldCount.attribute1);
            return this;
        }


        public Builder setName(String name) {
            product.name = name;
            fieldCount.add(FieldCount.attribute2);
            return this;
        }

        public Builder setPoster(Image poster){
            product.poster=poster;
            fieldCount.add(FieldCount.attribute14);
            return this;
        }

        public MoviePO build() {
            if (!valid()) {
                throw new IllegalStateException("MoviePO's fields aren't complete!");
            }
            return product;
        }

        //检查产品的重要属性是否均设置完全
        private boolean valid() {
            return fieldCount.size() == 14;
        }
    }
}
