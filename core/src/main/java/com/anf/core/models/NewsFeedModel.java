/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.anf.core.models;

import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;

import javax.annotation.PostConstruct;
import javax.jcr.Node;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

@Model(adaptables = Resource.class)
public class NewsFeedModel {

    @ValueMapValue(name=PROPERTY_RESOURCE_TYPE, injectionStrategy=InjectionStrategy.OPTIONAL)
    @Default(values="No resourceType")
    protected String resourceType;

    @OSGiService
    private SlingSettingsService settings;
    @SlingObject
    private Resource currentResource;
    @SlingObject
    private ResourceResolver resourceResolver;
    
    private ArrayList<FeedMessage> listItems;

    @PostConstruct
    protected void init() {
        
    	Resource newsListResource = resourceResolver.getResource("/var/commerce/products/anf-code-challenge/newsData");
        Iterator < Resource > children = newsListResource.listChildren();
        
        listItems = new ArrayList <FeedMessage > ();
        while (children.hasNext()) {
          final Resource child = children.next();
          FeedMessage feedMessage= child.adaptTo(FeedMessage.class);
          listItems.add(feedMessage);
          
        }
        
    }

	/**
	 * @return the listItems
	 */
	public ArrayList<FeedMessage> getListItems() {
		
		return listItems;
	}
    
    

}
