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
package org.verapdf.gf.model.impl.operator.markedcontent;

import org.verapdf.as.ASAtom;
import org.verapdf.cos.*;
import org.verapdf.gf.model.impl.cos.GFCosDict;
import org.verapdf.gf.model.impl.cos.GFCosLang;
import org.verapdf.gf.model.impl.cos.GFCosName;
import org.verapdf.gf.model.impl.operator.base.GFOperator;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosLang;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.operator.OpMarkedContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class GFOpMarkedContent extends GFOperator implements OpMarkedContent {

	/** Name of link to the tag name */
    public static final String TAG = "tag";
	/** Name of link to the properties dictionary */
    public static final String PROPERTIES = "properties";
	/** Name of link to Lang value from the properties dictionary */
	public static final String LANG = "Lang";

    public GFOpMarkedContent(List<COSBase> arguments, final String opType) {
        super(arguments, opType);
    }

    protected List<CosName> getTag() {
        if (this.arguments.size() > 1) {
			COSBase name = this.arguments.get(this.arguments.size() - 2);
			if (name.getType() == COSObjType.COS_NAME) {
				List<CosName> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new GFCosName((COSName) name));
				return Collections.unmodifiableList(list);
			}
        }
        return Collections.emptyList();
    }

    protected List<CosDict> getPropertiesDict() {
        if (!this.arguments.isEmpty()) {
			COSBase dict = this.arguments.get(this.arguments.size() - 1);
			if (dict.getType() == COSObjType.COS_DICT) {
				List<CosDict> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new GFCosDict((COSDictionary) dict));
				return Collections.unmodifiableList(list);
			}
        }
        return Collections.emptyList();
    }

	protected List<CosLang> getLang() {
		if (!this.arguments.isEmpty()) {
			COSBase dict = this.arguments.get(this.arguments.size() - 1);
			if (dict.getType() == COSObjType.COS_DICT) {
				COSObject baseLang = dict.getKey(ASAtom.LANG);
				if (baseLang != null && !baseLang.empty() && baseLang.getType() == COSObjType.COS_STRING) {
					List<CosLang> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
					list.add(new GFCosLang((COSString) baseLang.getDirectBase()));
					return Collections.unmodifiableList(list);
				}
			}
		}
		return Collections.emptyList();
	}

}
