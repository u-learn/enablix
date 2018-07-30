package com.enablix.wordpress.integration.impl;

import static java.net.URLDecoder.decode;
import static java.util.Optional.ofNullable;

import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.afrozaar.wordpress.wpapi.v2.Client;
import com.afrozaar.wordpress.wpapi.v2.Strings;
import com.afrozaar.wordpress.wpapi.v2.model.Link;

public class WPClient extends Client {

	public WPClient(String baseUrl, String username, String password, boolean usePermalinkEndpoint, boolean debug) {
       this(null, baseUrl, username, password, usePermalinkEndpoint, debug, null);
    }

    public WPClient(String baseUrl, String username, String password, boolean usePermalinkEndpoint, boolean debug, ClientHttpRequestFactory requestFactory) {
        this(null, baseUrl, username, password, usePermalinkEndpoint, debug, requestFactory);
    }

    public WPClient(String context, String baseUrl, String username, String password, boolean usePermalinkEndpoint, boolean debug) {
       this(context, baseUrl, username, password, usePermalinkEndpoint, debug, null);
    }

    public WPClient(String context, String baseUrl, String username, String password, boolean usePermalinkEndpoint, boolean debug,
                  ClientHttpRequestFactory requestFactory) {
    	super(context, baseUrl, username, password, usePermalinkEndpoint, debug, requestFactory);
    }
    
    public List<Link> parseLinks(HttpHeaders headers) {
        //Link -> [<http://johan-wp/wp-json/wp/v2/posts?page=2>; rel="next"]

        Optional<List<String>> linkHeader = ofNullable(headers.get(Strings.HEADER_LINK));

        return linkHeader
                .map(List::stream)
                .map(Stream::findFirst)
                .map(rawResponse -> {
                    final String[] links = rawResponse.get().split(", ");

                    return Arrays.stream(links).map(link -> { // <http://johan-wp/wp-json/wp/v2/posts?page=2>; rel="next"
                        String[] linkData = link.split("; ");
                        final String href = linkData[0].replace("<", "").replace(">", "");
                        final String rel = linkData[1].substring(4).replace("\"", "");
                        return Link.of(fixQuery(href), rel);
                    }).collect(Collectors.toList());
                })
                .orElse(Collections.emptyList());
    }

    @SuppressWarnings("deprecation")
	private String fixQuery(String href) {

        final UriComponents build = UriComponentsBuilder.fromHttpUrl(href).build();

        final MultiValueMap<String, String> queryParams = build.getQueryParams();
        final MultiValueMap<String, String> queryParamsFixed = new LinkedMultiValueMap<>();

        queryParams.forEach((key, values) -> queryParamsFixed.put(decode(key), 
        		values.stream().map((s) -> { return s == null ? null : URLDecoder.decode(s); }).collect(Collectors.toList())));

        return UriComponentsBuilder.fromPath(build.getPath())
                .scheme(build.getScheme())
                .queryParams(queryParamsFixed)
                .fragment(build.getFragment())
                .port(build.getPort())
                .host(build.getHost()).build().toUriString();

    }
    
    
}
