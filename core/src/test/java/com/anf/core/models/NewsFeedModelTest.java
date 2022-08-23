/**
 * 
 */
package com.anf.core.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import javax.jcr.Node;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;


/**
 * @author Dilip
 *
 */
@ExtendWith({ AemContextExtension.class, MockitoExtension.class })
class NewsFeedModelTest {
	
	private ArrayList<FeedMessage> listItems;
	
	
	
	

	private ResourceResolver resourceResolver;

	/**
	 * @throws java.lang.Exception
	 */
	
	private final AemContext ctx = new AemContext();
	@BeforeEach
	void setUp() throws Exception {
		
		ctx.addModelsForClasses(NewsFeedModel.class);
	    ctx.load().json("/com/anf/core/models/newsfeed.json", "/var/commerce/products/anf-code-challenge/newsData");
	    resourceResolver = ctx.resourceResolver();
	}


	/**
	 * Test method for {@link com.anf.core.models.NewsFeedModel#getListItems()}.
	 */
	@Test
	final void testGetListItems() {
	
		
	    
	    Resource  r2 =ctx.currentResource("/var/commerce/products/anf-code-challenge/newsData");
	    NewsFeedModel newsFeedModel =  ctx.getService(ModelFactory.class).createModel(r2, NewsFeedModel.class);
	    //listItems= newsFeedModel.getListItems();
	   // assertEquals(description, newsFeedModel.getListItems().get(0).getDescription());
	    Resource  r =ctx.currentResource("/var/commerce/products/anf-code-challenge/newsData/news_0");
		 // FeedMessage feedMessage = ctx.request().adaptTo(FeedMessage.class);
		 FeedMessage feedMessage  = ctx.getService(ModelFactory.class).createModel(r, FeedMessage.class);
		 String description ="BBC Sport looks at the big talking points from UFC 273 as Alexander Volkanovski beats the 'Korean Zombie' Chan Sung Jung to retain his featherweight title.";
	     String author ="Caroline Fox";
	     String urlImage= "https://ichef.bbci.co.uk/live-experience/cps/624/cpsprodpb/B536/production/_124109364_gettyimages-1390585815.jpg";
	     String url = "https://www.bbc.co.uk/sport/mixed-martial-arts/61057214";
	     String title ="UFC 273: Five things we learned as Alexander Volkanovski dominates 'Korean Zombie'";
	     String content = "Volkanovski earned a performance-of-the-night bonus for his win over Chan Sung Jung";
	     
	     assertEquals(description, feedMessage.getDescription());
	     assertEquals(author, feedMessage.getAuthor());
	     assertEquals(urlImage, feedMessage.getUrlImage());
	     assertEquals(url, feedMessage.getUrl());
	     assertEquals(title, feedMessage.getTitle());
	    assertEquals(content, feedMessage.getContent());
	    
		
	}

}
