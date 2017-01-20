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
package org.verapdf.features.gf.objects;

import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.gf.tools.GFCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.pd.PDAnnotation;

import java.util.Set;

/**
 * Feature object for annotation
 *
 * @author Maksim Bezrukov
 */
public class GFAnnotationFeaturesObject implements IFeaturesObject {

	private static final String ID = "id";

	private PDAnnotation annot;
	private String id;
	private String popupId;
	private Set<String> formXObjects;


	/**
	 * Constructs new Annotation Feature Object
	 *
	 * @param annot        class represents annotation object
	 * @param id           annotation id
	 * @param popupId      id of the popup annotation
	 * @param formXObjects set of id of the form XObjects which used in appearance stream of this annotation
	 */
	public GFAnnotationFeaturesObject(PDAnnotation annot, String id,
									  String popupId, Set<String> formXObjects) {
		this.annot = annot;
		this.id = id;
		this.popupId = popupId;
		this.formXObjects = formXObjects;
	}

	/**
	 * @return ANNOTATION instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.ANNOTATION;
	}

	/**
	 * Reports featurereport into collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeatureExtractionResult collection) throws FeatureParsingException {
		if (annot != null && !annot.empty()) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("annotation");
			if (id != null) {
				root.setAttribute(ID, id);
			}

			GFCreateNodeHelper.addNotEmptyNode("subType", annot.getSubtype(), root);
			GFCreateNodeHelper.addBoxFeature("rectangle", annot.getRect(), root);
			GFCreateNodeHelper.addNotEmptyNode("contents", annot.getContents(), root);
			GFCreateNodeHelper.addNotEmptyNode("annotationName", annot.getAnnotationName(), root);
			GFCreateNodeHelper.addNotEmptyNode("modifiedDate", annot.getModDate(), root);

			if (formXObjects != null && !formXObjects.isEmpty()) {
				FeatureTreeNode resources = root.addChild("resources");
				for (String xObjID : formXObjects) {
					if (xObjID != null) {
						FeatureTreeNode xObjNode = resources.addChild("xobject");
						xObjNode.setAttribute(ID, xObjID);
					}
				}
			}

			if (popupId != null) {
				FeatureTreeNode popup = root.addChild("popup");
				popup.setAttribute(ID, popupId);
			}

			GFCreateNodeHelper.addDeviceColorSpaceNode("color", annot.getColor(), root, collection);

			GFCreateNodeHelper.addNotEmptyNode("invisible", String.valueOf(annot.isInvisible()), root);
			GFCreateNodeHelper.addNotEmptyNode("hidden", String.valueOf(annot.isHidden()), root);
			GFCreateNodeHelper.addNotEmptyNode("print", String.valueOf(annot.isPrinted()), root);
			GFCreateNodeHelper.addNotEmptyNode("noZoom", String.valueOf(annot.isNoZoom()), root);
			GFCreateNodeHelper.addNotEmptyNode("noRotate", String.valueOf(annot.isNoRotate()), root);
			GFCreateNodeHelper.addNotEmptyNode("noView", String.valueOf(annot.isNoView()), root);
			GFCreateNodeHelper.addNotEmptyNode("readOnly", String.valueOf(annot.isReadOnly()), root);
			GFCreateNodeHelper.addNotEmptyNode("locked", String.valueOf(annot.isLocked()), root);
			GFCreateNodeHelper.addNotEmptyNode("toggleNoView", String.valueOf(annot.isToggleNoView()), root);
			GFCreateNodeHelper.addNotEmptyNode("toggleNoView", String.valueOf(annot.isLockedContents()), root);

			collection.addNewFeatureTree(FeatureObjectType.ANNOTATION, root);
			return root;
		}
		return null;
	}

	/**
	 * @return null
	 */
	@Override
	public FeaturesData getData() {
		return null;
	}
}
