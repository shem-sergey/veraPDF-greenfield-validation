package org.verapdf.gf.model.impl.pd.colors;

import org.verapdf.model.pdlayer.PDDeviceGray;

/**
 * @author Maksim Bezrukov
 */
public class GFPDDeviceGray extends GFPDColorSpace implements PDDeviceGray {

    private static final PDDeviceGray INSTANCE = new GFPDDeviceGray(
            org.verapdf.pd.colors.PDDeviceGray.INSTANCE);
    private static final PDDeviceGray INHERITED_INSTANCE = new GFPDDeviceGray(
            org.verapdf.pd.colors.PDDeviceGray.INHERITED_INSTANCE);

    public static final String DEVICE_GRAY_TYPE = "PDDeviceGray";

    private GFPDDeviceGray(
            org.verapdf.pd.colors.PDDeviceGray simplePDObject) {
        super(simplePDObject, DEVICE_GRAY_TYPE);
    }

    public static PDDeviceGray getInstance() {
        return INSTANCE;
    }

    public static PDDeviceGray getInheritedInstance() {
        return INHERITED_INSTANCE;
    }
}
