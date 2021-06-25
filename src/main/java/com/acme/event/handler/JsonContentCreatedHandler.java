package com.acme.event.handler;

import org.alfresco.core.handler.RenditionsApi;
import org.alfresco.core.model.RenditionBodyCreate;
import org.alfresco.event.sdk.handling.filter.EventFilter;
import org.alfresco.event.sdk.handling.filter.MimeTypeFilter;
import org.alfresco.event.sdk.handling.handler.OnNodeCreatedEventHandler;
import org.alfresco.event.sdk.model.v1.model.DataAttributes;
import org.alfresco.event.sdk.model.v1.model.NodeResource;
import org.alfresco.event.sdk.model.v1.model.RepoEvent;
import org.alfresco.event.sdk.model.v1.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Create a custom json2pdf rendition when a json content is created.
 *
 */
@Component
public class JsonContentCreatedHandler implements OnNodeCreatedEventHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonContentCreatedHandler.class);

	public static final String RENDITION_JSON2PDF = "json2pdf";

	@Autowired
	private RenditionsApi renditionsApi;

	@Override
	public void handleEvent(final RepoEvent<DataAttributes<Resource>> repoEvent) {
		LOGGER.info("A JSON content named {} has been created!",
				((NodeResource) repoEvent.getData().getResource()).getName());
		final NodeResource nodeResource = (NodeResource) repoEvent.getData().getResource();
		String id = nodeResource.getId();
		RenditionBodyCreate body = new RenditionBodyCreate();
		body.setId(RENDITION_JSON2PDF);
		ResponseEntity response = renditionsApi.createRendition(id, body);
		LOGGER.info("Status={}", response.getStatusCodeValue());
	}

	@Override
	public EventFilter getEventFilter() {
		return MimeTypeFilter.of("application/json");
	}
}
