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

import org.verapdf.model.pdlayer.PDLab;

/**
 * @author Maksim Bezrukov
 */
public class GFPDLab extends GFPDColorSpace implements PDLab {

    public static final String LAB_COLOR_SPACE_TYPE = "PDLab";

    public GFPDLab(
            org.verapdf.pd.colors.PDLab simplePDObject) {
        super(simplePDObject, LAB_COLOR_SPACE_TYPE);
    }
}
