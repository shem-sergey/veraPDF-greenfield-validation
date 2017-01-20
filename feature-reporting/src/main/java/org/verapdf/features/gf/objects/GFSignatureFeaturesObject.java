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
import org.verapdf.cos.COSString;
import org.verapdf.features.*;
import org.verapdf.features.gf.tools.GFCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.pd.PDSignature;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Features object for digital signature.
 *
 * @author Sergey Shemyakov
 */
public class GFSignatureFeaturesObject implements IFeaturesObject {

    private PDSignature signature;

    public GFSignatureFeaturesObject(PDSignature signature) {
        this.signature = signature;
    }

    @Override
    public FeatureObjectType getType() {
        return FeatureObjectType.SIGNATURE;
    }

    @Override
    public FeatureTreeNode reportFeatures(FeatureExtractionResult featureExtractionResult) throws FeatureParsingException {
        if (signature != null && !signature.empty()) {
            FeatureTreeNode root = FeatureTreeNode.createRootNode("signature");

            GFCreateNodeHelper.addNotEmptyNode("filter", signature.getFilter(), root);
            GFCreateNodeHelper.addNotEmptyNode("subFilter", signature.getSubfilter(), root);
            COSString contents = signature.getContents();
            if (contents != null) {
                GFCreateNodeHelper.addNotEmptyNode("contents", contents.getHexString(), root);
            }
            GFCreateNodeHelper.addNotEmptyNode("name", signature.getName(), root);
            GFCreateNodeHelper.createDateNode("signDate", root, signature.getSignDate(), featureExtractionResult);
            GFCreateNodeHelper.addNotEmptyNode("location", signature.getLocation(), root);
            GFCreateNodeHelper.addNotEmptyNode("reason", signature.getReason(), root);
            GFCreateNodeHelper.addNotEmptyNode("contactInfo", signature.getContactInfo(), root);

            featureExtractionResult.addNewFeatureTree(FeatureObjectType.SIGNATURE, root);

            return root;
        }
        return null;
    }

    @Override
    public FeaturesData getData() {
        COSString contents = signature.getContents();
        InputStream stream = contents == null ? null :
                new ByteArrayInputStream(contents.get());
        return SignatureFeaturesData.newInstance(
                stream, GFCreateNodeHelper.getStringFromASAtom(signature.getFilter()),
                GFCreateNodeHelper.getStringFromASAtom(signature.getSubfilter()), signature.getName(),
                signature.getSignDate(), signature.getLocation(),
                signature.getReason(), signature.getContactInfo());
    }


}
