package org.verapdf.gf.model.impl.operator.textshow;

import org.verapdf.gf.model.tools.GFIDGenerator;
import org.verapdf.model.GenericModelObject;
import org.verapdf.model.operator.Glyph;
import org.verapdf.pd.font.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents glyph used in text.
 *
 * @author Sergey Shemyakov
 */
public class GFGlyph extends GenericModelObject implements Glyph {

    private static final Logger LOGGER = Logger.getLogger(GFGlyph.class.getCanonicalName());

    public final static String GLYPH_TYPE = "Glyph";
    private final String id;

    private Boolean glyphPresent;
    private Boolean widthsConsistent;
    private String name;
    private String toUnicode;
    private Long renderingMode;

    public GFGlyph(Boolean glyphPresent, Boolean widthsConsistent, PDFont font, int glyphCode, int renderingMode) {
        this(glyphPresent, widthsConsistent, font, glyphCode, GLYPH_TYPE, renderingMode);
    }

    public GFGlyph(Boolean glyphPresent, Boolean widthsConsistent, PDFont font, int glyphCode, String type, int renderingMode) {
        super(type);
        this.glyphPresent = glyphPresent;
        this.widthsConsistent = widthsConsistent;
        this.renderingMode = Long.valueOf(renderingMode);

        if (font instanceof PDSimpleFont) {
            Encoding encoding = font.getEncodingMapping();
            this.name = encoding == null ? null : encoding.getName(glyphCode);
        } else if (font instanceof PDType0Font) {
            try {
                FontProgram pr = font.getFontProgram();
                if (pr == null) {
                    this.name = null;
                } else {
                    pr.parseFont();
                    if (glyphCode == 0 || !pr.containsCode(glyphCode)) {
                        this.name = ".notdef";
                    } else {
                        this.name = null;
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.FINE, "Can't convert code to glyph", e);
                this.name = null;
            }
        }
        this.toUnicode = font.toUnicode(glyphCode);
        this.id = GFIDGenerator.generateID(font.getDictionary().hashCode(),
                font.getName(), glyphCode, renderingMode);
    }

    @Override
    public String getname() {
        return this.name;
    }

    @Override
    public Boolean getisWidthConsistent() {
        return this.widthsConsistent;
    }

    @Override
    public Boolean getisGlyphPresent() {
        return this.glyphPresent;
    }

    @Override
    public String gettoUnicode() {
        return this.toUnicode;
    }

    @Override
    public Long getrenderingMode() {
        return this.renderingMode;
    }

    @Override
    public String getID() {
        return this.id;
    }
}
