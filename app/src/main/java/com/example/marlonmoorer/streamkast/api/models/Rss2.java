package com.example.marlonmoorer.streamkast.api.models;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Root(name = "rss",strict = false)
public class Rss2 {

    @Element(name="channel", required = false)
    Channel channel;


    @Attribute(name="atom", required = false)
    String atom;


    @Attribute(name="cc", required = false)
    String cc;


    @Attribute(name="itunes", required = false)
    String itunes;


    @Attribute(name="media", required = false)
    String media;


    @Attribute(name="content", required = false)
    String content;


    @Attribute(name="rdf", required = false)
    String rdf;


    @Attribute(name="version", required = false)
    String version;



    public Channel getChannel() { return this.channel; }
    public void setChannel(Channel _value) { this.channel = _value; }


    public String getAtom() { return this.atom; }
    public void setAtom(String _value) { this.atom = _value; }


    public String getCc() { return this.cc; }
    public void setCc(String _value) { this.cc = _value; }


    public String getItunes() { return this.itunes; }
    public void setItunes(String _value) { this.itunes = _value; }


    public String getMedia() { return this.media; }
    public void setMedia(String _value) { this.media = _value; }


    public String getContent() { return this.content; }
    public void setContent(String _value) { this.content = _value; }


    public String getRdf() { return this.rdf; }
    public void setRdf(String _value) { this.rdf = _value; }


    public String getVersion() { return this.version; }
    public void setVersion(String _value) { this.version = _value; }


    @Root(strict = false)
    public static class Channel {

        @Element(name="link", required = false)
        String link;


        @Element(name="title", required = false)
        String title;


        @Element(name="pubDate", required = false)
        String pubDate;


        @Element(name="lastBuildDate", required = false)
        String lastBuildDate;


        @Element(name="generator", required = false)
        String generator;


        @Element(name="language", required = false)
        String language;


        @Element(name="copyright", required = false)
        String copyright;


        @Element(name="docs", required = false)
        String docs;


        @Element(name="summary", required = false)
        String summary;


        @Path("image")
        @Element(required = false)
        Image image;


        @Element(name="author", required = false)
        String author;


        @Element(name="keywords", required = false)
        String keywords;


        @ElementList(name = "category", inline = true, required = false)
        List<Category> category;


        @Element(name="explicit", required = false)
        String explicit;


        @Element(name="owner", required = false)
        Owner owner;


        @Element(name="description", required = false)
        String description;


        @Element(name="subtitle", required = false)
        String subtitle;


        @Element(name="type", required = false)
        String type;

//        @Path("item")
//        @ElementList( inline = true, required = false)
        List<Episode> episodes;

        @Path("item")
        @ElementList( inline = true, required = false,entry = "item")
        List<Item> item;



        public String getLink() { return this.link; }
        public void setLink(String _value) { this.link = _value; }


        public String getTitle() { return this.title; }
        public void setTitle(String _value) { this.title = _value; }


        public String getPubDate() { return this.pubDate; }
        public void setPubDate(String _value) { this.pubDate = _value; }


        public String getLastBuildDate() { return this.lastBuildDate; }
        public void setLastBuildDate(String _value) { this.lastBuildDate = _value; }


        public String getGenerator() { return this.generator; }
        public void setGenerator(String _value) { this.generator = _value; }


        public String getLanguage() { return this.language; }
        public void setLanguage(String _value) { this.language = _value; }


        public String getCopyright() { return this.copyright; }
        public void setCopyright(String _value) { this.copyright = _value; }


        public String getDocs() { return this.docs; }
        public void setDocs(String _value) { this.docs = _value; }


        public String getSummary() { return this.summary; }
        public void setSummary(String _value) { this.summary = _value; }


        public Image getImage() { return this.image; }
        public void setImage(Image _value) { this.image = _value; }


        public String getAuthor() { return this.author; }
        public void setAuthor(String _value) { this.author = _value; }


        public String getKeywords() { return this.keywords; }
        public void setKeywords(String _value) { this.keywords = _value; }


        public List<Category> getCategory() { return this.category; }
        public void setCategory(List<Category> _value) { this.category = _value; }


        public String getExplicit() { return this.explicit; }
        public void setExplicit(String _value) { this.explicit = _value; }


        public Owner getOwner() { return this.owner; }
        public void setOwner(Owner _value) { this.owner = _value; }


        public String getDescription() { return this.description; }
        public void setDescription(String _value) { this.description = _value; }


        public String getSubtitle() { return this.subtitle; }
        public void setSubtitle(String _value) { this.subtitle = _value; }


        public String getType() { return this.type; }
        public void setType(String _value) { this.type = _value; }


        public List<Episode> getEpisodes() { return this.episodes; }
        public void setEpisodes(List<Episode> _value) { this.episodes = _value; }

        public List<Item> getItem() { return this.item; }
        public void setItem(List<Item> _value) { this.item = _value; }


    }

    public static class Link {

        @Attribute(name="href", required = false)
        String href;


        @Attribute(name="rel", required = false)
        String rel;


        @Attribute(name="type", required = false)
        String type;



        public String getHref() { return this.href; }
        public void setHref(String _value) { this.href = _value; }


        public String getRel() { return this.rel; }
        public void setRel(String _value) { this.rel = _value; }


        public String getType() { return this.type; }
        public void setType(String _value) { this.type = _value; }


    }

    public static class Image {

        @Element(name="url", required = false)
        String url;


        @Element(name="title", required = false)
        String title;


        @Element(name="link", required = false)
        String link;


        @Attribute(name="href", required = false)
        String href;



        public String getUrl() { return this.url; }
        public void setUrl(String _value) { this.url = _value; }


        public String getTitle() { return this.title; }
        public void setTitle(String _value) { this.title = _value; }


        public String getLink() { return this.link; }
        public void setLink(String _value) { this.link = _value; }


        public String getHref() { return this.href; }
        public void setHref(String _value) { this.href = _value; }


    }

    public static class Category {

        @Attribute(name="text", required = false)
        String text;


        @Element(name="category", required = false)
        Category category;



        public String getText() { return this.text; }
        public void setText(String _value) { this.text = _value; }


        public Category getCategory() { return this.category; }
        public void setCategory(Category _value) { this.category = _value; }


    }

    public static class Owner {

        @Element(name="name", required = false)
        String name;


        @Element(name="email", required = false)
        String email;



        public String getName() { return this.name; }
        public void setName(String _value) { this.name = _value; }


        public String getEmail() { return this.email; }
        public void setEmail(String _value) { this.email = _value; }


    }
    public static class Item{

        @Element(name="title", required = false)
        String title;


        @Element(name="pubDate", required = false)
        String pubDate;


        @Element(name="guid", required = false)
        Guid guid;


        @Element(name="link", required = false)
        String link;


        @Element(name="image", required = false)
        Image image;


        @Element(name="description", required = false)
        String description;


        @Element(name="encoded", required = false)
        String encoded;


        @Element(name="enclosure", required = false)
        Enclosure enclosure;


        @Element(name="duration", required = false)
        String duration;


        @Element(name="explicit", required = false)
        String explicit;


        @Element(name="keywords", required = false)
        String keywords;


        @Element(name="subtitle", required = false)
        String subtitle;


        @Element(name="summary", required = false)
        String summary;


        @Element(name="season", required = false)
        String season;


        @Element(name="episode", required = false)
        String episode;


        @Element(name="episodeType", required = false)
        String episodeType;


        @Element(name="author", required = false)
        String author;



        public String getTitle() { return this.title; }
        public void setTitle(String _value) { this.title = _value; }


        public String getPubDate() { return this.pubDate; }
        public void setPubDate(String _value) { this.pubDate = _value; }


        public Guid getGuid() { return this.guid; }
        public void setGuid(Guid _value) { this.guid = _value; }


        public String getLink() { return this.link; }
        public void setLink(String _value) { this.link = _value; }


        public Image getImage() { return this.image; }
        public void setImage(Image _value) { this.image = _value; }


        public String getDescription() { return this.description; }
        public void setDescription(String _value) { this.description = _value; }


        public String getEncoded() { return this.encoded; }
        public void setEncoded(String _value) { this.encoded = _value; }


        public Enclosure getEnclosure() { return this.enclosure; }
        public void setEnclosure(Enclosure _value) { this.enclosure = _value; }


        public String getDuration() { return this.duration; }
        public void setDuration(String _value) { this.duration = _value; }


        public String getExplicit() { return this.explicit; }
        public void setExplicit(String _value) { this.explicit = _value; }


        public String getKeywords() { return this.keywords; }
        public void setKeywords(String _value) { this.keywords = _value; }


        public String getSubtitle() { return this.subtitle; }
        public void setSubtitle(String _value) { this.subtitle = _value; }


        public String getSummary() { return this.summary; }
        public void setSummary(String _value) { this.summary = _value; }


        public String getSeason() { return this.season; }
        public void setSeason(String _value) { this.season = _value; }


        public String getEpisode() { return this.episode; }
        public void setEpisode(String _value) { this.episode = _value; }


        public String getEpisodeType() { return this.episodeType; }
        public void setEpisodeType(String _value) { this.episodeType = _value; }


        public String getAuthor() { return this.author; }
        public void setAuthor(String _value) { this.author = _value; }


    }

    public static class Episode implements Serializable{

        @Element(name="title", required = false)
        String title;


        @Element(name="pubDate", required = false)
        String pubDate;


        @Element(name="guid", required = false)
        Guid guid;


        @Element(name="link", required = false)
        String link;


        @Element(name="image", required = false)
        Image image;


        @Element(name="description", required = false)
        String description;


        @Element(name="encoded", required = false)
        String encoded;


        @Element(name="enclosure", required = false)
        Enclosure enclosure;


        @Element(name="duration", required = false)
        String duration;


        @Element(name="explicit", required = false)
        String explicit;


        @Element(name="keywords", required = false)
        String keywords;


        @Element(name="subtitle", required = false)
        String subtitle;


        @Element(name="summary", required = false)
        String summary;


        @Element(name="season", required = false)
        String season;


        @Element(name="episode", required = false)
        String episode;


        @Element(name="episodeType", required = false)
        String episodeType;


        @Element(name="author", required = false)
        String author;



        public String getTitle() { return this.title; }
        public void setTitle(String _value) { this.title = _value; }


        public String getPubDate() { return this.pubDate; }
        public void setPubDate(String _value) { this.pubDate = _value; }


        public Guid getGuid() { return this.guid; }
        public void setGuid(Guid _value) { this.guid = _value; }


        public String getLink() { return this.link; }
        public void setLink(String _value) { this.link = _value; }


        public Image getImage() { return this.image; }
        public void setImage(Image _value) { this.image = _value; }


        public String getDescription() { return this.description; }
        public void setDescription(String _value) { this.description = _value; }


        public String getEncoded() { return this.encoded; }
        public void setEncoded(String _value) { this.encoded = _value; }


        public Enclosure getEnclosure() { return this.enclosure; }
        public void setEnclosure(Enclosure _value) { this.enclosure = _value; }


        public String getDuration() { return this.duration; }
        public void setDuration(String _value) { this.duration = _value; }


        public String getExplicit() { return this.explicit; }
        public void setExplicit(String _value) { this.explicit = _value; }


        public String getKeywords() { return this.keywords; }
        public void setKeywords(String _value) { this.keywords = _value; }


        public String getSubtitle() { return this.subtitle; }
        public void setSubtitle(String _value) { this.subtitle = _value; }


        public String getSummary() { return this.summary; }
        public void setSummary(String _value) { this.summary = _value; }


        public String getSeason() { return this.season; }
        public void setSeason(String _value) { this.season = _value; }


        public String getEpisode() { return this.episode; }
        public void setEpisode(String _value) { this.episode = _value; }


        public String getEpisodeType() { return this.episodeType; }
        public void setEpisodeType(String _value) { this.episodeType = _value; }


        public String getAuthor() { return this.author; }
        public void setAuthor(String _value) { this.author = _value; }


    }

    public static class Guid {

        @Attribute(name="isPermaLink", required = false)
        Boolean isPermaLink;



        public Boolean getIsPermaLink() { return this.isPermaLink; }
        public void setIsPermaLink(Boolean _value) { this.isPermaLink = _value; }


    }

    public static class Enclosure {

        @Attribute(name="length", required = false)
        String length;


        @Attribute(name="type", required = false)
        String type;


        @Attribute(name="url", required = false)
        String url;



        public String getLength() { return this.length; }
        public void setLength(String _value) { this.length = _value; }


        public String getType() { return this.type; }
        public void setType(String _value) { this.type = _value; }


        public String getUrl() { return this.url; }
        public void setUrl(String _value) { this.url = _value; }


    }
}