package org.verapdf.gf.model.impl.operator.inlineimage;

import org.verapdf.cos.COSBase;
import org.verapdf.cos.COSObject;
import org.verapdf.gf.model.impl.pd.images.GFPDInlineImage;
import org.verapdf.gf.model.impl.pd.util.PDResourcesHandler;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.operator.Op_EI;
import org.verapdf.model.pdlayer.PDInlineImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public class GFOp_EI extends GFOpInlineImage implements Op_EI {

	public static final String OP_EI_TYPE = "Op_EI";

	public static final String INLINE_IMAGE = "inlineImage";

	private PDResourcesHandler resourcesHandler;

	public GFOp_EI(List<COSBase> arguments, PDResourcesHandler resourcesHandler) {
		super(arguments, OP_EI_TYPE);
		this.resourcesHandler = resourcesHandler;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (INLINE_IMAGE.equals(link)) {
			return this.getInlineImage();
		}
		return super.getLinkedObjects(link);
	}

	private List<PDInlineImage> getInlineImage() {
		COSBase parameters = this.arguments.get(0);
		org.verapdf.pd.images.PDInlineImage inlineImage =
				new org.verapdf.pd.images.PDInlineImage(new COSObject(parameters),
						resourcesHandler.getObjectResources(), resourcesHandler.getPageResources());
		List<PDInlineImage> inlineImages = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		inlineImages.add(new GFPDInlineImage(inlineImage));
		return Collections.unmodifiableList(inlineImages);
	}

}
