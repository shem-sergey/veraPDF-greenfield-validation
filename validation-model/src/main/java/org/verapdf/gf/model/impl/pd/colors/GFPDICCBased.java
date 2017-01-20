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
package org.verapdf.gf.model.impl.pd.colors;

import org.verapdf.external.ICCProfile;
import org.verapdf.gf.model.impl.external.GFICCInputProfile;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.external.ICCInputProfile;
import org.verapdf.model.pdlayer.PDICCBased;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class GFPDICCBased extends GFPDColorSpace implements PDICCBased {

    public static final String ICC_BASED_TYPE = "PDICCBased";

    public static final String ICC_PROFILE = "iccProfile";

    public GFPDICCBased(
            org.verapdf.pd.colors.PDICCBased simplePDObject) {
        super(simplePDObject, ICC_BASED_TYPE);
    }

    protected GFPDICCBased(
            org.verapdf.pd.colors.PDICCBased simplePDObject, String type) {
        super(simplePDObject, type);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (ICC_PROFILE.equals(link)) {
            return this.getICCProfile();
        }
        return super.getLinkedObjects(link);
    }

    private List<ICCInputProfile> getICCProfile() {
        ICCProfile iccProfile = ((org.verapdf.pd.colors.PDICCBased) simplePDObject).getICCProfile();
        if (iccProfile != null) {
            List<ICCInputProfile> profiles = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
            profiles.add(new GFICCInputProfile(iccProfile));
            return Collections.unmodifiableList(profiles);
        }
        return Collections.emptyList();
    }
}
