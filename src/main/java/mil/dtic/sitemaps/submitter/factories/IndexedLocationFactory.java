package mil.dtic.sitemaps.submitter.factories;

import org.springframework.stereotype.Component;

import mil.dtic.sitemaps.submitter.resources.IndexedLocation;

@Component
public class IndexedLocationFactory {

	public IndexedLocation createIndexedLocation() {
		return new IndexedLocation();
	}
	
	public IndexedLocation createIndexedLocation(String location) {
		IndexedLocation returnLocation = new IndexedLocation();
		returnLocation.setLocation(location);
		
		return returnLocation;
	}
}
