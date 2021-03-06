package org.verapdf.gf.model.impl.operator.pathpaint;

import org.verapdf.cos.COSBase;
import org.verapdf.gf.model.factory.operators.GraphicState;
import org.verapdf.gf.model.impl.pd.util.PDResourcesHandler;
import org.verapdf.model.baselayer.Object;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class GFOpFillPaint extends GFOpPathPaint {

    protected GFOpFillPaint(List<COSBase> arguments, final GraphicState state,
                            final PDResourcesHandler resources, final String opType) {
        super(arguments, state, resources, opType);
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        if (FILL_CS.equals(link)) {
            return this.getFillCS();
        }
        return super.getLinkedObjects(link);
    }

}
