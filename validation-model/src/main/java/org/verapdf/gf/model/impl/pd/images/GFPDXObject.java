/**
 * This file is part of validation-model, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * validation-model is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with validation-model as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * validation-model as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.gf.model.impl.pd.images;

import org.verapdf.as.ASAtom;
import org.verapdf.cos.COSDictionary;
import org.verapdf.gf.model.impl.cos.GFCosDict;
import org.verapdf.gf.model.impl.pd.GFPDResource;
import org.verapdf.gf.model.impl.pd.util.PDResourcesHandler;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.pdlayer.PDSMaskImage;
import org.verapdf.model.pdlayer.PDXObject;
import org.verapdf.pd.images.PDXForm;
import org.verapdf.pd.images.PDXImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class GFPDXObject extends GFPDResource implements PDXObject {

	public static final String X_OBJECT_TYPE = "PDXObject";

	public static final String OPI = "OPI";
	public static final String S_MASK = "SMask";

	protected final PDResourcesHandler resourcesHandler;

	public GFPDXObject(
			org.verapdf.pd.images.PDXObject simplePDObject, PDResourcesHandler resourcesHandler) {
		this(simplePDObject, resourcesHandler, X_OBJECT_TYPE);
	}

	protected GFPDXObject(org.verapdf.pd.images.PDXObject simplePDObject, PDResourcesHandler resourcesHandler, final String type) {
		super(simplePDObject, type);
		this.resourcesHandler = resourcesHandler;
	}

	@Override
	public String getSubtype() {
		ASAtom subtype = ((org.verapdf.pd.images.PDXObject) simplePDObject).getSubtype();
		return subtype == null ? null : subtype.getValue();
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case S_MASK:
				return this.getSMask();
			case OPI:
				return this.getOPI();
			default:
				return super.getLinkedObjects(link);
		}
	}

	protected List<PDSMaskImage> getSMask() {
		PDXImage smask = ((org.verapdf.pd.images.PDXObject) simplePDObject).getSMask();
		if (smask != null) {
			List<PDSMaskImage> mask = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			mask.add(new GFPDSMaskImage(smask, this.resourcesHandler));
			return Collections.unmodifiableList(mask);
		}
		return Collections.emptyList();
	}

	protected List<CosDict> getOPI() {
		COSDictionary opi = ((org.verapdf.pd.images.PDXObject) simplePDObject).getOPI();
		if (opi != null) {
			List<CosDict> res = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			res.add(new GFCosDict(opi));
			return Collections.unmodifiableList(res);
		}
		return Collections.emptyList();
	}

	public static PDXObject getTypedPDXObject(
			org.verapdf.pd.images.PDXObject xObject,
			PDResourcesHandler resources) {
		ASAtom type = xObject.getType();
		if (ASAtom.FORM.equals(type)) {
			return new GFPDXForm((PDXForm) xObject, resources);
		} else if (ASAtom.IMAGE.equals(type)) {
			return new GFPDXImage((PDXImage) xObject, resources);
		} else if (ASAtom.PS.equals(type)) {
			return new GFPDXObject(xObject, resources);
		} else {
			return null;
		}
	}
}
