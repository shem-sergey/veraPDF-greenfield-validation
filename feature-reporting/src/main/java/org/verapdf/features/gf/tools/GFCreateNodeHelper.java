/**
 * This file is part of feature-reporting, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * feature-reporting is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with feature-reporting as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * feature-reporting as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.features.gf.tools;

import org.verapdf.as.ASAtom;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.cos.COSObjType;
import org.verapdf.cos.COSObject;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.pd.PDMetadata;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helps in creating similar nodes in different features objects
 *
 * @author Maksim Bezrukov
 */
public final class GFCreateNodeHelper {

	private static final Logger LOGGER = Logger.getLogger(GFCreateNodeHelper.class.getCanonicalName());

	private static final String LLX = "llx";
	private static final String LLY = "lly";
	private static final String URX = "urx";
	private static final String URY = "ury";

	private GFCreateNodeHelper() {
	}

	private static String getXMLFormat(Calendar calendar) throws DatatypeConfigurationException {
		GregorianCalendar greg = new GregorianCalendar(Locale.US);
		greg.setTime(calendar.getTime());
		greg.setTimeZone(calendar.getTimeZone());
		XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(greg);
		return xmlCalendar.toXMLFormat();
	}

	/**
	 * Creates node with date value formatted in XML format from the given
	 * Calendar
	 *
	 * @param nodeName
	 *            name of the created node
	 * @param parent
	 *            parent element for created node
	 * @param date
	 *            the given date as Calendar class
	 * @param collection
	 *            collection for which this node creates
	 * @return created node
	 * @throws FeatureParsingException
	 */
	public static FeatureTreeNode createDateNode(String nodeName, FeatureTreeNode parent, Calendar date,
												 FeatureExtractionResult collection) throws FeatureParsingException {
		FeatureTreeNode modificationDate = null;

		if (date != null) {
			modificationDate = parent.addChild(nodeName);
			try {
				modificationDate.setValue(getXMLFormat(date));
			} catch (DatatypeConfigurationException e) {
				LOGGER.log(Level.FINE, "DatatypeFactory implementation not available or can't be instantiated", e);
				ErrorsHelper.addErrorIntoCollection(collection, modificationDate, e.getMessage());
			}
		}

		return modificationDate;
	}

	/**
	 * Creates feature node for boxes
	 *
	 * @param name
	 *            name of the node
	 * @param box
	 *            PDRectangle object represents the box
	 * @param parent
	 *            parent element for the created node
	 * @return created node
	 * @throws FeatureParsingException
	 */
	public static FeatureTreeNode addBoxFeature(String name, double[] box, FeatureTreeNode parent)
			throws FeatureParsingException {
		FeatureTreeNode boxNode = null;

		if (box != null && box.length >= 4) {
			boxNode = parent.addChild(name);
			boxNode.setAttribute(LLX, String.valueOf(box[0]));
			boxNode.setAttribute(LLY, String.valueOf(box[1]));
			boxNode.setAttribute(URX, String.valueOf(box[2]));
			boxNode.setAttribute(URY, String.valueOf(box[3]));
		}

		return boxNode;
	}

	/**
	 * Creates new node with given name and value if both of this parametrs are
	 * not null
	 *
	 * @param name
	 *            name of the node
	 * @param value
	 *            value of the node
	 * @param parent
	 *            parent of the node
	 * @return generated node
	 * @throws FeatureParsingException
	 */
	public static FeatureTreeNode addNotEmptyNode(String name, String value, FeatureTreeNode parent)
			throws FeatureParsingException {
		if (name != null && value != null) {
			FeatureTreeNode node = parent.addChild(name);
			node.setValue(value);
			return node;
		}
		return null;
	}

	/**
	 * Creates new node with given name and value if both of this parametrs are
	 * not null
	 *
	 * @param name
	 *            name of the node
	 * @param value
	 *            value of the node
	 * @param parent
	 *            parent of the node
	 * @return generated node
	 * @throws FeatureParsingException
	 */
	public static FeatureTreeNode addNotEmptyNode(String name, ASAtom value, FeatureTreeNode parent)
			throws FeatureParsingException {
		if (name != null && value != null) {
			FeatureTreeNode node = parent.addChild(name);
			node.setValue(value.getValue());
			return node;
		}
		return null;
	}

	/**
	 * Creates new node with given name and value if both of this parametrs are
	 * not null and only in case when value is a COSString or COSName
	 *
	 * @param name
	 *            name of the node
	 * @param value
	 *            value of the node
	 * @param parent
	 *            parent of the node
	 * @return generated node
	 * @throws FeatureParsingException
	 */
	public static FeatureTreeNode addNotEmptyNode(String name, COSObject value, FeatureTreeNode parent)
			throws FeatureParsingException {
		if (name != null && value != null && !value.empty()
				&& (value.getType() == COSObjType.COS_NAME || value.getType() == COSObjType.COS_STRING)) {
			FeatureTreeNode node = parent.addChild(name);
			node.setValue(value.getString());
			return node;
		}
		return null;
	}

	/**
	 * Creates new node for device color space
	 *
	 * @param name
	 *            name for the created node
	 * @param color
	 *            PDColor class represents device color space for creating node
	 * @param parent
	 *            parent node for the creating node
	 * @param collection
	 *            features collection in which parent situated
	 * @return created node
	 * @throws FeatureParsingException
	 */
	public static FeatureTreeNode addDeviceColorSpaceNode(String name, double[] color, FeatureTreeNode parent,
														  FeatureExtractionResult collection) throws FeatureParsingException {
		if (name == null || color == null) {
			return null;
		}
		FeatureTreeNode colorNode = parent.addChild(name);
		boolean typeDefined = false;

		for (ColorComponent component : ColorComponent.values()) {
			if (component.getSize() == color.length) {
				typeDefined = true;
				colorNode.setAttributes(component.createAttributesMap(color));
			}
		}

		if (!typeDefined) {
			ErrorsHelper.addErrorIntoCollection(collection, colorNode, "Can not define color type");
		}

		return colorNode;
	}

	/**
	 * Creates elements with name {@code elementName} and attribute id with
	 * values from {@code set} and attach them to the {@code root} element in
	 * case, when {@code setName} is null and to the element with {@code root}
	 * parent and name {@code elementName} in other case
	 *
	 * @param set
	 *            set of elements id
	 * @param elementName
	 *            element names
	 * @param setName
	 *            name of the parent element for created elements. If null, all
	 *            created elements will be attached to the {@code root}
	 * @param root
	 *            root element for the generated parent element for generated
	 *            elements or direct paren for generated elements in case of
	 *            {@code setName} equals to null
	 * @throws FeatureParsingException
	 */
	public static void parseIDSet(Set<String> set, String elementName, String setName, FeatureTreeNode root)
			throws FeatureParsingException {
		if (set != null && !set.isEmpty()) {
			FeatureTreeNode setNode;
			if (setName == null) {
				setNode = root;
			} else {
				setNode = root.addChild(setName);
			}
			for (String entry : set) {
				if (entry != null) {
					FeatureTreeNode entryNode = setNode.addChild(elementName);
					entryNode.setAttribute("id", entry);
				}
			}
		}
	}

	/**
	 * Generates byte array with contents of a stream
	 *
	 * @param is
	 *            input stream for converting
	 * @return byte array with contents of a stream
	 * @throws IOException
	 *             If the first byte cannot be read for any reason other than
	 *             end of file, or if the input stream has been closed, or if
	 *             some other I/O error occurs.
	 */
	public static byte[] inputStreamToByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] bytes = new byte[1024];
		int length;
		while ((length = is.read(bytes)) != -1) {
			baos.write(bytes, 0, length);
		}
		return baos.toByteArray();
	}

	/**
	 * Creates FeatureTreeNode with name {@code nodeName}, parent
	 * {@code parent}, and content which is a stream r epresentation of the
	 * {@code metadata} content. If there is an exception during getting
	 * metadata, then it will create node with errorID and error for this
	 * situation.
	 *
	 * @param metadata
	 *            PDMetadata class from which metadata will be taken
	 * @param nodeName
	 *            name for the created node
	 * @param collection
	 *            collection for the created node
	 * @return created node
	 * @throws FeatureParsingException
	 *             occurs when wrong features tree node constructs
	 */
	public static FeatureTreeNode parseMetadata(PDMetadata metadata, String nodeName, FeatureTreeNode parent,
												FeatureExtractionResult collection) throws FeatureParsingException {
		if (metadata == null || metadata.empty()) {
			return null;
		}
		FeatureTreeNode node;
		node = parent.addMetadataChild(nodeName);
		try (InputStream stream = metadata.getStream()) {
			if (stream != null) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				while (true) {
					int r = stream.read(buffer);
					if (r == -1) break;
					out.write(buffer, 0, r);
				}
				byte[] bStream = out.toByteArray();
				String hexString = DatatypeConverter.printHexBinary(bStream);
				node.setValue(hexString);
			}
		} catch (IOException e) {
			LOGGER.log(Level.FINE, "Error while obtaining unfiltered metadata stream", e);
			ErrorsHelper.addErrorIntoCollection(collection, node, e.getMessage());
		}

		return node;
	}

	public static void parseMatrix(double[] array, FeatureTreeNode parent) throws FeatureParsingException {
		if (array != null) {
			for (int i = 0; i < array.length; ++i) {
				FeatureTreeNode element = parent.addChild("element");
				element.setAttribute("index", String.valueOf(i + 1));
				element.setAttribute("value", String.valueOf(array[i]));
			}
		}
	}

	public static String getStringFromASAtom(ASAtom asAtom) {
		return asAtom == null ? null : asAtom.getValue();
	}
}
