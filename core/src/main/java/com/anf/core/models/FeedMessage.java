package com.anf.core.models;

import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;

/*
 * Represents one RSS message
 */
@Model(adaptables = Resource.class)
public class FeedMessage {
	@Inject
	@Named("title")
    String title;
	@Inject
	@Named("description")
    String description;
	@Inject
	@Named("author")
    String author;
	@Inject
	@Named("content")
    String content;
	@Inject
	@Named("url")
    String url;
	@Inject
	@Named("urlImage")
    String urlImage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the urlImage
	 */
	public String getUrlImage() {
		return urlImage;
	}

	/**
	 * @param urlImage the urlImage to set
	 */
	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	@Override
	public String toString() {
		return "FeedMessage [title=" + title + ", description=" + description + ", author=" + author + ", content="
				+ content + ", url=" + url + ", urlImage=" + urlImage + "]";
	}
    
    

}