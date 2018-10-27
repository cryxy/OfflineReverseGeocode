/*
The MIT License (MIT)
[OSI Approved License]
The MIT License (MIT)

Copyright (c) 2014 Daniel Glasson

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package geocode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import geocode.kdtree.KDTree;

/**
 *
 * Created by Daniel Glasson on 18/05/2014. Uses KD-trees to quickly find the
 * nearest point
 * 
 * Adapted by cryxy on 27.10.2018.
 * 
 * <pre>
 * ReverseGeoCode reverseGeoCode = new ReverseGeoCode();
 * reverseGeoCode.add(new FileInputStream("c:\\AU.txt"), true);
 * reverseGeoCode.build();
 * System.out.println("Nearest to -23.456, 123.456 is "
 * geocode.nearestPlace(-23.456, 123.456));
 * </pre>
 */
public class ReverseGeoCode {
	ArrayList<GeoName> arPlaceNames;

	KDTree<GeoName> kdTree;

	public ReverseGeoCode() {
		arPlaceNames = new ArrayList<>();
	}

	// Get placenames from http://download.geonames.org/export/dump/
	/**
	 * Parse the zipped geonames file.
	 * 
	 * @param zippedPlacednames
	 *            a {@link ZipInputStream} zip file downloaded from
	 *            http://download.geonames.org/export/dump/; can not be null.
	 * @param majorOnly
	 *            only include major cities in KD-tree.
	 * 
	 * @throws IOException
	 *             if there is a problem reading the {@link ZipInputStream}.
	 * @throws NullPointerException
	 *             if zippedPlacenames is {@code null}.
	 */
	public void addZipInputStream(ZipInputStream zippedPlacednames, boolean majorOnly) throws IOException {
		// depending on which zip file is given,
		// country specific zip files have read me files
		// that we should ignore
		ZipEntry entry;
		do {
			entry = zippedPlacednames.getNextEntry();
		} while (entry.getName().equals("readme.txt"));

		addInputStream(zippedPlacednames, majorOnly);

	}

	public void addInputStream(InputStream placenames, boolean majorOnly) throws IOException {
		// Read the geonames file in the directory
		BufferedReader in = new BufferedReader(new InputStreamReader(placenames));
		String str;
		try {
			while ((str = in.readLine()) != null) {
				GeoName newPlace = new GeoName(str);
				if (!majorOnly || newPlace.majorPlace) {
					arPlaceNames.add(newPlace);
				}
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			in.close();
		}
	}

	public void build() {
		kdTree = new KDTree<GeoName>(arPlaceNames);
		arPlaceNames.clear();
	}

	public GeoName nearestPlace(double latitude, double longitude) {
		return kdTree.findNearest(new GeoName(latitude, longitude));
	}
}
