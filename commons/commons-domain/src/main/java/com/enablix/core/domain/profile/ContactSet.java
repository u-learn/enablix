package com.enablix.core.domain.profile;

import java.util.HashSet;
import java.util.Set;

import com.enablix.core.domain.BaseWrappedEntity;

public class ContactSet<C extends Contact> extends BaseWrappedEntity {

	private Set<C> contacts = new HashSet<C>(0);
	
	public void add(C contact) {
		
		if (contact != null) {
		
			if (contacts.isEmpty()) {
				contact.setPrimary(true);
			}
			
			contacts.add(contact);
			if (contact.isPrimary()) {
				setPrimary(contact);
			}
		}
	}
	
	public void setPrimary(C contact) {
		
		if (contact != null) {
		
			if (!contacts.contains(contact)) {
				add(contact);
			}
			
			for (C otherContact : contacts) {
				if (!otherContact.equals(contact)) {
					otherContact.setPrimary(false);
				}
			}
		}
	}
	
}
