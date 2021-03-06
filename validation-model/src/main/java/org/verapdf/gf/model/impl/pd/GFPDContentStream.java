package org.verapdf.gf.model.impl.pd;


import org.verapdf.as.io.ASInputStream;
import org.verapdf.cos.COSObjType;
import org.verapdf.cos.COSObject;
import org.verapdf.cos.COSStream;
import org.verapdf.gf.model.factory.operators.OperatorFactory;
import org.verapdf.gf.model.impl.pd.util.PDResourcesHandler;
import org.verapdf.model.operator.Operator;
import org.verapdf.model.pdlayer.PDContentStream;
import org.verapdf.parser.PDFStreamParser;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Timur Kamalov
 */
public class GFPDContentStream extends GFPDObject implements PDContentStream {

	private static final Logger LOGGER = Logger.getLogger(GFPDContentStream.class.getCanonicalName());

	public static final String CONTENT_STREAM_TYPE = "PDContentStream";

	public static final String OPERATORS = "operators";

	private PDResourcesHandler resourcesHandler;

	private List<Operator> operators = null;
	private boolean containsTransparency = false;

	public GFPDContentStream(org.verapdf.pd.PDContentStream contentStream, PDResourcesHandler resourcesHandler) {
		super(contentStream, CONTENT_STREAM_TYPE);
		this.resourcesHandler = resourcesHandler;
	}

	@Override
	public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
		if (OPERATORS.equals(link)) {
			return this.getOperators();
		}
		return super.getLinkedObjects(link);
	}

	private List<Operator> getOperators() {
		if (this.operators == null) {
			parseOperators();
		}
		return this.operators;
	}

	private void parseOperators() {
		if (this.contentStream == null) {
			this.operators = Collections.emptyList();
		} else {
			try {
				COSObject contentStream = this.contentStream.getContents();
				if (!contentStream.empty() && contentStream.getType() == COSObjType.COS_STREAM) {
					ASInputStream opStream = contentStream.getDirectBase().getData(COSStream.FilterFlags.DECODE);
					PDFStreamParser streamParser = new PDFStreamParser(opStream);
					opStream.close();
					try {
						streamParser.parseTokens();
						OperatorFactory operatorFactory = new OperatorFactory();
						List<Operator> result = operatorFactory.operatorsFromTokens(streamParser.getTokens(), resourcesHandler);
						this.containsTransparency = operatorFactory.isLastParsedContainsTransparency();
						this.operators = Collections.unmodifiableList(result);
					} finally {
						streamParser.closeInputStream();
					}
				} else {
					this.operators = Collections.emptyList();
				}
			} catch (IOException e) {
				LOGGER.log(Level.FINE, "Error while parsing content stream. " + e.getMessage(), e);
				this.operators = Collections.emptyList();
			}
		}
	}

	public boolean isContainsTransparency() {
		if (this.operators == null) {
			parseOperators();
		}
		return containsTransparency;
	}
}
