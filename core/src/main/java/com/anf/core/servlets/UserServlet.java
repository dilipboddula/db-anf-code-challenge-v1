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
package com.anf.core.servlets;

import com.anf.core.services.ContentService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = { Servlet.class })
@SlingServletPaths(
        value = "/bin/saveUserDetails"
)
public class UserServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Reference
    private ContentService contentService;
    
    transient ResourceResolver resourceResolver;
    transient Resource pathResource;

    @Override
    protected void doGet(final SlingHttpServletRequest request,
            final SlingHttpServletResponse response) throws ServletException, IOException {
        // Make use of ContentService to write the business logic
    	
    	resourceResolver = request.getResourceResolver();
        pathResource = request.getResource();
        Resource resource = request.getResourceResolver().getResource("/etc/age");
        Node ageNode = resource.adaptTo(Node.class);
        try {
			int minAge= Integer.parseInt(ageNode.getProperty("minAge").getString());
			int maxAge= Integer.parseInt(ageNode.getProperty("maxAge").getString());
			int inputAge = Integer.parseInt(request.getParameter("age"));
			if (minAge<=inputAge && maxAge>=inputAge) {
				response.getWriter().write("success");
			} else {
				response.getWriter().write("fail");
			}
		
		} catch (ValueFormatException e) {
			response.getWriter().write("fail");
		} catch (PathNotFoundException e) {
			response.getWriter().write("fail");
		} catch (RepositoryException e) {
			response.getWriter().write("fail");
		} catch (Exception e) {
			response.getWriter().write("fail");
		}
        
        
    }
    
    protected void doPost(final SlingHttpServletRequest request,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        // Make use of ContentService to write the business logic
    	
    	doGet(request, resp);
    }
    
    
    
}
