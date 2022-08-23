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
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.InvalidQueryException;
import javax.jcr.query.QueryResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Reference;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@Model(adaptables = Resource.class)
public class SearchComponent {

    @ValueMapValue(name=PROPERTY_RESOURCE_TYPE, injectionStrategy=InjectionStrategy.OPTIONAL)
    @Default(values="No resourceType")
    protected String resourceType;

    @OSGiService
    private SlingSettingsService settings;
    @SlingObject
    private Resource currentResource;
    @SlingObject
    private ResourceResolver resourceResolver;
    
    @OSGiService
	private QueryBuilder builder;

    private String message;
    
    private ArrayList<String> resultPath;
    
    private ArrayList<String> resultPath2;
    

    @PostConstruct
    protected void init() throws ParserConfigurationException, RepositoryException {
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        String currentPagePath = Optional.ofNullable(pageManager)
                .map(pm -> pm.getContainingPage(currentResource))
                .map(Page::getPath).orElse("");
        Session session =resourceResolver.adaptTo(Session.class);
        queryBuilderAPI(session);
        xPathQueryAPI(session);
    }

	private void xPathQueryAPI(Session session) throws RepositoryException, InvalidQueryException {
		String xPathQuery = "/jcr:root/content/anf-code-challenge/us/en//element(*, cq:PageContent)[@anfCodeChallenge='true']";
        javax.jcr.query.QueryManager queryManager= session.getWorkspace().getQueryManager();
        javax.jcr.query.Query query = queryManager.createQuery(xPathQuery, javax.jcr.query.Query.XPATH);
        query.setLimit(10);
        QueryResult result = query.execute();
        resultPath2 = new ArrayList<>();
        javax.jcr.query.RowIterator rowIterator = result.getRows();
        while (rowIterator.hasNext()) {
        javax.jcr.query.Row row = rowIterator.nextRow();
        javax.jcr.Value[] values = row.getValues();
        resultPath2.add(row.getPath().replace("/jcr:content",""));
        }
	}

	private void queryBuilderAPI(Session session) throws ParserConfigurationException, RepositoryException {
		String fulltextSearchTerm = "jcr:content/anfCodeChallenge";

        // create query description as hash map (simplest way, same as form post)
        Map<String, String> map = new HashMap<String, String>();

    // create query description as hash map (simplest way, same as form post)
        map.put("path", "/content/anf-code-challenge/us/en");
        map.put("type", "cq:Page");
        map.put("property", fulltextSearchTerm);
        map.put("property.value", "true");

        // can be done in map or with Query methods
        map.put("p.offset", "0"); // same as query.setStart(0) below
        map.put("p.limit", "10"); // same as query.setHitsPerPage(20) below

        Query query = builder.createQuery(PredicateGroup.create(map), session);
        query.setStart(0);
        query.setHitsPerPage(10);

        SearchResult result = query.getResult();

        // paging metadata
        int hitsPerPage = result.getHits().size(); // 20 (set above) or lower
        long totalMatches = result.getTotalMatches();
        long offset = result.getStartIndex();
        long numberOfPages = totalMatches / 10;
        resultPath = new ArrayList<>();

        //Place the results in XML to return to client
        DocumentBuilderFactory factory =     DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        //Start building the XML to pass back to the AEM client
        Element root = doc.createElement( "results" );
        doc.appendChild( root );

        // iterating over the results
        for (Hit hit : result.getHits()) {
           String path = hit.getPath();
           resultPath.add(path);
          //Create a result element
          Element resultel = doc.createElement( "result" );
          root.appendChild( resultel );

          Element pathel = doc.createElement( "path" );
          pathel.appendChild( doc.createTextNode(path ) );
          resultel.appendChild( pathel );
        }
	}

    public String getMessage() {
        return message;
    }

	/**
	 * @return the resultPath
	 */
	public ArrayList<String> getResultPath() {
		return resultPath;
	}

	/**
	 * @return the resultPath2
	 */
	public ArrayList<String> getResultPath2() {
		return resultPath2;
	}

    
    

}
