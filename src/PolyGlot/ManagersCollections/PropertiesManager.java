/*
 * Copyright (c) 2014-2015, Draque Thompson, draquemail@gmail.com
 * All rights reserved.
 *
 * Licensed under: Creative Commons Attribution-NonCommercial 4.0 International Public License
 * See LICENSE.TXT included with this code to read the full license agreement.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package PolyGlot.ManagersCollections;

import PolyGlot.CustomControls.PAlphaMap;
import PolyGlot.IOHandler;
import PolyGlot.PGTUtil;
import java.awt.Font;
import java.util.Arrays;
import javax.swing.JTextField;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author draque
 */
public class PropertiesManager {
    private String overrideProgramPath = "";
    private Font font = null;
    private Integer fontStyle = 0;
    private Integer fontSize = 12;
    private final PAlphaMap alphaOrder;
    private String alphaPlainText = "";
    private String langName = "";
    private String localLangName = "";
    private String copyrightAuthorInfo = "";
    private boolean typesMandatory = false;
    private boolean localMandatory = false;
    private boolean wordUniqueness = false;
    private boolean localUniqueness = false;
    private boolean ignoreCase = false;
    private boolean disableProcRegex = false;
    private boolean enforceRTL = false;
    private byte[] cachedFont = null;
    private final Font charisUnicode;

    public PropertiesManager() {
        alphaOrder = new PAlphaMap();

        // set default font to Charis, as it's unicode compatible
        charisUnicode = IOHandler.getCharisUnicodeFontInitial();
        setFontCon(charisUnicode);
    }

    /**
     * Gets unicode charis font. Defaults/hard coded to size 12
     *
     * @return
     */
    public Font getCharisUnicodeFont() {
        return charisUnicode.deriveFont(0, 12);
    }

    /**
     * Gets the java FX version of an AWT font
     *
     * @return javafx font
     */
    public javafx.scene.text.Font getFXFont() {
        javafx.scene.text.Font ret;

        if (font == null) {
            ret = (new javafx.scene.control.TextField()).getFont();
        } else {
            java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            ret = javafx.scene.text.Font.font(font.getFamily(), fontSize);
        }

        return ret;
    }

    /**
     * Sets value of cached font file as byte array
     *
     * @param _cachedFont value of cached font
     */
    public void setCachedFont(byte[] _cachedFont) {
        cachedFont = _cachedFont;
    }

    /**
     * Gets cached font file if one exists, null otherwise
     *
     * @return byte array of cached font file
     */
    public byte[] getCachedFont() {
        return cachedFont;
    }

    public void setOverrideProgramPath(String override) {
        if (override.equals(PGTUtil.emptyFile)) {
            overrideProgramPath = "";
        } else {
            overrideProgramPath = override;
        }
    }

    public String getOverrideProgramPath() {
        return overrideProgramPath;
    }

    public void setDisableProcRegex(boolean _disableProcRegex) {
        disableProcRegex = _disableProcRegex;
    }

    public boolean isDisableProcRegex() {
        return disableProcRegex;
    }

    public void setEnforceRTL(boolean _enforceRTL) {
        enforceRTL = _enforceRTL;
    }

    public boolean isEnforceRTL() {
        return enforceRTL;
    }

    /**
     * Sets ignore case value for dictionary
     *
     * @param _ignoreCase new value
     */
    public void setIgnoreCase(boolean _ignoreCase) {
        ignoreCase = _ignoreCase;
    }

    /**
     * Retrieves ignore case
     *
     * @return ignore case status of dictionary
     */
    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    /**
     * Sets font.
     *
     * @param _fontCon The font being set
     * @param _fontStyle The style of the font (bold, underlined, etc.)
     * @param _fontSize Size of font
     */
    public void setFontCon(Font _fontCon, Integer _fontStyle, Integer _fontSize) {
        setFontCon(_fontCon);
        setFontSize(_fontSize);
        setFontStyle(_fontStyle);
    }

    /**
     * Gets language's font
     *
     * @return the fontCon
     */
    public Font getFontCon() {
        // under certain circumstances, this can default to 0...
        if (fontSize == 0) {
            fontSize = 12;
        }

        return font == null ? new JTextField().getFont() : font.deriveFont(fontStyle, fontSize);
    }

    /**
     * Sets conlang font and nulls cached font value
     *
     * @param fontCon the fontCon to set
     */
    public final void setFontCon(Font fontCon) {
        // null cached font if being set to new font
        if (font != null && !font.getFamily().equals(fontCon.getFamily())) {
            cachedFont = null;
        }

        font = fontCon;
    }

    /**
     * @return the fontStyle
     */
    public Integer getFontStyle() {
        return fontStyle;
    }

    /**
     * @param _fontStyle the fontStyle to set
     */
    public void setFontStyle(Integer _fontStyle) {
        fontStyle = _fontStyle;
    }

    /**
     * @return the fontSize
     */
    public Integer getFontSize() {
        return fontSize;
    }

    /**
     * Cannot be set to 0 or lower. Will default to 12 if set to 0 or lower.
     *
     * @param _fontSize the fontSize to set
     */
    public void setFontSize(Integer _fontSize) {
        fontSize = _fontSize < 0 ? 12 : _fontSize;
    }

    /**
     * @return the alphaOrder
     */
    public PAlphaMap getAlphaOrder() {
        return alphaOrder;
    }

    /**
     * @param order alphabetical order
     */
    public void setAlphaOrder(String order) {
        alphaPlainText = order;

        alphaOrder.clear();

        // if comma delimited, alphabet may contain multiple character values
        if (order.contains(",")) {
            String[] orderVals = order.split(",");
            
            for (int i = 0; i < orderVals.length; i++) {
                String curVal = orderVals[i].trim();
                if (curVal.isEmpty()) {
                    continue;
                }
                
                alphaOrder.put(curVal, i);
            }
        } else {
            for (int i = 0; i < order.length(); i++) {
                alphaOrder.put(order.substring(i, i+1), i);
            }
        }
    }

    /**
     * @return the alphaPlainText
     */
    public String getAlphaPlainText() {
        return alphaPlainText;
    }

    /**
     * @return the langName
     */
    public String getLangName() {
        return langName;
    }

    /**
     * @param langName the langName to set
     */
    public void setLangName(String langName) {
        this.langName = langName;
    }

    /**
     * @return the typesMandatory
     */
    public boolean isTypesMandatory() {
        return typesMandatory;
    }

    /**
     * @param typesMandatory the typesMandatory to set
     */
    public void setTypesMandatory(boolean typesMandatory) {
        this.typesMandatory = typesMandatory;
    }

    /**
     * @return the localMandatory
     */
    public boolean isLocalMandatory() {
        return localMandatory;
    }

    /**
     * @param localMandatory the localMandatory to set
     */
    public void setLocalMandatory(boolean localMandatory) {
        this.localMandatory = localMandatory;
    }

    /**
     * @return the wordUniqueness
     */
    public boolean isWordUniqueness() {
        return wordUniqueness;
    }

    /**
     * @param wordUniqueness the wordUniqueness to set
     */
    public void setWordUniqueness(boolean wordUniqueness) {
        this.wordUniqueness = wordUniqueness;
    }

    /**
     * @return the localUniqueness
     */
    public boolean isLocalUniqueness() {
        return localUniqueness;
    }

    /**
     * @param localUniqueness the localUniqueness to set
     */
    public void setLocalUniqueness(boolean localUniqueness) {
        this.localUniqueness = localUniqueness;
    }

    public String buildPropertiesReport() {
        String ret = "";

        ret += ConWordCollection.formatPlain("Language Name: " + langName + "<br><br>");

        return ret;
    }

    /**
     * Tests whether system has given font installed
     *
     * @param testFont Font to test system for
     * @return true if system has font, false otherwise
     */
    public static boolean testSystemHasFont(Font testFont) {
        boolean ret = false;
        String[] fontNames = java.awt.GraphicsEnvironment
                .getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        if (Arrays.asList(fontNames).contains(testFont.getName())) {
            ret = true;
        }

        return ret;
    }

    /**
     * Writes all dictionary properties to XML document
     *
     * @param doc Document to write dictionary properties to
     * @param rootElement root element of document
     */
    public void writeXML(Document doc, Element rootElement) {
        Element wordValue;

        // store font for Conlang words
        wordValue = doc.createElement(PGTUtil.fontConXID);
        Font curFont = getFontCon();
        wordValue.appendChild(doc.createTextNode(curFont == null ? "" : curFont.getName()));
        rootElement.appendChild(wordValue);

        // store font style
        wordValue = doc.createElement(PGTUtil.langPropFontStyleXID);
        wordValue.appendChild(doc.createTextNode(getFontStyle().toString()));
        rootElement.appendChild(wordValue);

        // store font for Local words
        wordValue = doc.createElement(PGTUtil.langPropFontSizeXID);
        wordValue.appendChild(doc.createTextNode(getFontSize().toString()));
        rootElement.appendChild(wordValue);

        // store name for conlang
        wordValue = doc.createElement(PGTUtil.langPropLangNameXID);
        wordValue.appendChild(doc.createTextNode(getLangName()));
        rootElement.appendChild(wordValue);

        // store alpha order for conlang
        wordValue = doc.createElement(PGTUtil.langPropAlphaOrderXID);
        wordValue.appendChild(doc.createTextNode(getAlphaPlainText()));
        rootElement.appendChild(wordValue);

        // store option for mandatory Types
        wordValue = doc.createElement(PGTUtil.langPropTypeMandatoryXID);
        wordValue.appendChild(doc.createTextNode(isTypesMandatory() ? PGTUtil.True : PGTUtil.False));
        rootElement.appendChild(wordValue);

        // store option for mandatory Local word
        wordValue = doc.createElement(PGTUtil.langPropLocalMandatoryXID);
        wordValue.appendChild(doc.createTextNode(isLocalMandatory() ? PGTUtil.True : PGTUtil.False));
        rootElement.appendChild(wordValue);

        // store option for unique local word
        wordValue = doc.createElement(PGTUtil.langPropLocalUniquenessXID);
        wordValue.appendChild(doc.createTextNode(isLocalUniqueness() ? PGTUtil.True : PGTUtil.False));
        rootElement.appendChild(wordValue);

        // store option for unique conwords
        wordValue = doc.createElement(PGTUtil.langPropWordUniquenessXID);
        wordValue.appendChild(doc.createTextNode(isWordUniqueness() ? PGTUtil.True : PGTUtil.False));
        rootElement.appendChild(wordValue);

        // store option for ignoring case
        wordValue = doc.createElement(PGTUtil.langPropIgnoreCaseXID);
        wordValue.appendChild(doc.createTextNode(isIgnoreCase() ? PGTUtil.True : PGTUtil.False));
        rootElement.appendChild(wordValue);

        // store option for disabling regex or pronunciations
        wordValue = doc.createElement(PGTUtil.langPropDisableProcRegexXID);
        wordValue.appendChild(doc.createTextNode(isDisableProcRegex() ? PGTUtil.True : PGTUtil.False));
        rootElement.appendChild(wordValue);

        // store option for enforcing RTL in conlang
        wordValue = doc.createElement(PGTUtil.langPropEnforceRTLXID);
        wordValue.appendChild(doc.createTextNode(isEnforceRTL() ? PGTUtil.True : PGTUtil.False));
        rootElement.appendChild(wordValue);

        // store option for Author and copyright info
        wordValue = doc.createElement(PGTUtil.langPropAuthCopyrightXID);
        wordValue.appendChild(doc.createTextNode(copyrightAuthorInfo));
        rootElement.appendChild(wordValue);

        // store option local language name
        wordValue = doc.createElement(PGTUtil.langPropLocalLangNameXID);
        wordValue.appendChild(doc.createTextNode(localLangName));
        rootElement.appendChild(wordValue);
    }

    /**
     * @return the localLangName
     */
    public String getLocalLangName() {
        return localLangName;
    }

    /**
     * @param localLangName the localLangName to set
     */
    public void setLocalLangName(String localLangName) {
        this.localLangName = localLangName;
    }

    /**
     * @return the copyrightAuthorInfo
     */
    public String getCopyrightAuthorInfo() {
        return copyrightAuthorInfo;
    }

    /**
     * @param copyrightAuthorInfo the copyrightAuthorInfo to set
     */
    public void setCopyrightAuthorInfo(String copyrightAuthorInfo) {
        this.copyrightAuthorInfo = copyrightAuthorInfo;
    }
}
