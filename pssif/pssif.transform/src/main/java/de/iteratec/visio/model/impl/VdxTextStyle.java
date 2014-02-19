package de.iteratec.visio.model.impl;

import java.awt.Color;
import java.util.Collection;
import java.util.Set;

import org.w3c.dom.Element;

import com.google.common.collect.Sets;

import de.iteratec.visio.model.VisioTextStyle;


public class VdxTextStyle implements VisioTextStyle {

  private String         fontName;
  private Color          fontColor;
  private Set<CharStyle> characterStyles;
  private double         fontSize;

  public VdxTextStyle() {
    this.fontName = "Arial";
    this.fontColor = Color.BLACK;
    this.characterStyles = Sets.newHashSet();
    this.fontSize = 11 / 72d;
  }

  public VdxTextStyle(VisioTextStyle style) {
    this.fontName = style.getFontName();
    this.fontColor = style.getFontColor();
    this.characterStyles = style.getCharStyles();
    this.fontSize = style.getFontSize();
  }

  @Override
  public String getFontName() {
    return fontName;
  }

  @Override
  public void setFontName(String fontName) {
    this.fontName = fontName;
  }

  @Override
  public Color getFontColor() {
    return fontColor;
  }

  @Override
  public void setFontColor(Color color) {
    this.fontColor = color;
  }

  @Override
  public Set<CharStyle> getCharStyles() {
    return characterStyles;
  }

  @Override
  public void setCharStyles(Collection<CharStyle> styles) {
    this.characterStyles = Sets.newHashSet(styles);
  }

  @Override
  public void addCharStyle(CharStyle style) {
    characterStyles.add(style);
  }

  @Override
  public void removeCharStyle(CharStyle style) {
    characterStyles.remove(style);
  }

  @Override
  public double getFontSize() {
    return fontSize;
  }

  @Override
  public void setFontSize(double size) {
    this.fontSize = size;
  }

  /**
   * Writes a {@link VisioTextStyle} into a vdx shape 
   */
  public static class Writer {

    /**
    * Writes this text style to the given DOM element with the given ix ID,
    * replacing an already existing element of that ix-ID.
    * @param textStyle the {@link VisioTextStyle} to write
    * @param shape the DOM element representing the shape this text should be set to
    * @param ix the ix-ID of the style element in the DOM representation (0 based)
    */
    public static void writeToShapeElement(VisioTextStyle textStyle, Element shape, int ix) {
      Element charElement = VisioDOMUtils.getFirstChildWithNameAndAttribute(shape, VisioDOMUtils.CHAR, "IX", String.valueOf(ix));
      if (charElement == null) {
        charElement = VisioDOMUtils.createChildWithName(shape, VisioDOMUtils.CHAR);
        charElement.setAttribute("IX", String.valueOf(ix));
      }

      // Order here is important in case the elements are not yet there.
      // If they are created in the incorrect order, Visio will reject the file
      Element fontElement = VisioDOMUtils.getOrCreateFirstChildWithName(charElement, "Font");
      VisioDOMUtils.setValue(fontElement, VisioDOMUtils.getFontId(charElement.getOwnerDocument(), textStyle.getFontName()));

      Element colorElement = VisioDOMUtils.getOrCreateFirstChildWithName(charElement, "Color");
      VisioDOMUtils.setColorValue(colorElement, textStyle.getFontColor());

      Element styleElement = VisioDOMUtils.getOrCreateFirstChildWithName(charElement, "Style");
      VisioDOMUtils.setValue(styleElement, getVisioStyleValue(textStyle.getCharStyles()));

      Element sizeElement = VisioDOMUtils.getOrCreateFirstChildWithName(charElement, "Size");
      VisioDOMUtils.setValue(sizeElement, textStyle.getFontSize());

      Element transElement = VisioDOMUtils.getOrCreateFirstChildWithName(charElement, "ColorTrans");
      double transparency = (255 - textStyle.getFontColor().getAlpha()) / 255d;
      transparency = Math.max(0.0, Math.min(transparency, 1.0)); // make sure transparency is between 0 and 1
      VisioDOMUtils.setValue(transElement, transparency);
    }

    private static int getVisioStyleValue(Set<CharStyle> characterStyles) {
      int result = 0;
      for (CharStyle charStyle : characterStyles) {
        result += charStyle.getValue();
      }
      return result;
    }
  }

  /**
   * Reads a {@link VisioTextStyle} from a vdx shape 
   */
  public static class Reader {

    /**
    * Reads the text style information with the given ix ID from the given DOM element into
    * this {@link VdxTextStyle}.
    * @param shape the DOM element representing the shape this text should be set to
    * @param ix the ix-ID of the style element in the DOM representation (0 based)
    * @return the {@link VdxTextStyle} read from the shape
    */
    public static VisioTextStyle readFromShapeElement(Element shape, int ix) {
      VisioTextStyle textStyle = new VdxTextStyle();

      Element charElement = VisioDOMUtils.getFirstChildWithNameAndAttribute(shape, VisioDOMUtils.CHAR, "IX", String.valueOf(ix));
      if (charElement != null) {
        Element colorElement = VisioDOMUtils.getFirstChildWithName(charElement, "Color");
        if (colorElement != null) {
          textStyle.setFontColor(Color.decode(colorElement.getTextContent()));
        }

        Element transElement = VisioDOMUtils.getFirstChildWithName(charElement, "ColorTrans");
        if (transElement != null) {
          Color oldColor = textStyle.getFontColor();
          double transparency = Double.parseDouble(transElement.getTextContent());
          int alpha = (int) Math.round((1 - transparency) * 255);
          textStyle.setFontColor(new Color(oldColor.getRed(), oldColor.getGreen(), oldColor.getBlue(), alpha));
        }

        Element sizeElement = VisioDOMUtils.getFirstChildWithName(charElement, "Size");
        if (sizeElement != null) {
          textStyle.setFontSize(Double.parseDouble(sizeElement.getTextContent()));
        }

        Element fontElement = VisioDOMUtils.getFirstChildWithName(charElement, "Font");
        if (fontElement != null) {
          textStyle.setFontName(VisioDOMUtils.getFontName(shape.getOwnerDocument(), fontElement.getTextContent()));
        }

        Element styleElement = VisioDOMUtils.getFirstChildWithName(charElement, "Style");
        if (styleElement != null) {
          setCharStyleSet(Integer.parseInt(styleElement.getTextContent()), textStyle);
        }
      }
      return textStyle;
    }

    private static void setCharStyleSet(int stylesValue, VisioTextStyle textStyle) {
      textStyle.getCharStyles().clear();

      for (int i = 0; i <= 3; i++) {
        int baseNumberToCheck = 1 << i; // 1, 2, 4, 8
        // bitwise check of stylesValue
        if ((stylesValue & baseNumberToCheck) != 0) {
          textStyle.addCharStyle(CharStyle.getStyleFor(baseNumberToCheck));
        }
      }
    }
  }

}
