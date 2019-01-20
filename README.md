# Offline Reverse Geocoding Java library

An Offline Reverse Geocoding Java library supporting multiple placename files.

Uses KD-Trees for extremely fast placename lookups

Licensed under The MIT License

A C# port by Necrolis is available at https://github.com/Necrolis/GeoSharp

Usage:

First download a placenames file from http://download.geonames.org/export/dump/

Allcountries.zip from that site is comprehensive however if you're on mobile try the cities1000.zip file. It's 1/80th of the size.

Then simply

```java 
ReverseGeoCode coder = new ReverseGeoCode();
// multiple geoname zips
for (String geonameZip : geonameZips) {
	FileInputStream targetStream = null;
	File zipFile = new File(geonameZip);
	targetStream = new FileInputStream(zipFile);
	ZipInputStream zippedPlacednames = new ZipInputStream(targetStream);
	coder.addZipInputStream(zippedPlacednames, false);
}
coder.build();
System.out.println("Nearest to -23.456, 123.456 is " + coder.nearestPlace(-23.456, 123.456));
```

