package com.enablix.core.security.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.beans.SpringBackedBeanRegistry;

@Component
public class GuestUserProviderFactoryImpl extends SpringBackedBeanRegistry<GuestUserProvider> implements GuestUserProviderFactory {

	private List<MatcherAndProvider> providers = new ArrayList<>(); 
	
	@Override
	public GuestUserProvider getProvider(HttpServletRequest request) {
		
		for (MatcherAndProvider provider : providers) {
			if (provider.pathMatcher.matches(request)) {
				return provider.provider;
			}
		}
		
		return null;
	}

	@Override
	protected Class<GuestUserProvider> lookupForType() {
		return GuestUserProvider.class;
	}

	@Override
	protected void registerBean(GuestUserProvider bean) {
		for (String urlPattern : bean.supportedRequestUrls()) {
			providers.add(new MatcherAndProvider(urlPattern, bean));
		}
	}
	
	private static class MatcherAndProvider {
		
		private AntPathRequestMatcher pathMatcher;
		private GuestUserProvider provider;
		
		private MatcherAndProvider(String matcherPattern, GuestUserProvider provider) {
			this.pathMatcher = new AntPathRequestMatcher(matcherPattern);
			this.provider = provider;
		}
	}

}
