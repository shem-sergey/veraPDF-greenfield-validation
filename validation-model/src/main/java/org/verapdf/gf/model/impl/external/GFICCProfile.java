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
package org.verapdf.gf.model.impl.external;

import org.verapdf.external.ICCProfile;

/**
 * @author Maksim Bezrukov
 */
public class GFICCProfile extends GFExternal implements org.verapdf.model.external.ICCProfile {

	private final ICCProfile iccProfile;

	protected GFICCProfile(ICCProfile iccProfile, String type) {
		super(type);
		this.iccProfile = iccProfile;
	}

	/**
	 * @return number of colorants for ICC profile, described in profile
	 *         dictionary
	 */
	@Override
	public Long getN() {
		return this.iccProfile.getNumberOfColorants();
	}

	/**
	 * @return string representation of device class or null, if profile length
	 *         is too small
	 */
	@Override
	public String getdeviceClass() {
		return this.iccProfile.getDeviceClass();
	}

	/**
	 * @return string representation of color space or null, if profile length
	 *         is too small
	 */
	@Override
	public String getcolorSpace() {
		return this.iccProfile.getColorSpace();
	}

	/**
	 * @return version of ICC profile or null, if profile length is too small
	 */
	@Override
	public Double getversion() {
		return this.iccProfile.getVersion();
	}

	/**
	 * Indicate validity of icc profile.
	 *
	 * @return true if profile is valid, false if ICC header is less then 128
	 * bytes or stream cannot be read. Other checks should be implemented by
	 * customer.
	 */
	@Override
	public Boolean getisValid() {
		return Boolean.valueOf(this.iccProfile.isLooksValid());
	}
}
