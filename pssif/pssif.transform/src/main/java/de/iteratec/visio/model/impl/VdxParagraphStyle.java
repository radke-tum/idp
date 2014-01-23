package de.iteratec.visio.model.impl;

import org.w3c.dom.Element;

import de.iteratec.visio.model.VisioParagraphStyle;


public class VdxParagraphStyle implements VisioParagraphStyle {

  private double         indentFirst;
  private double         indentLeft;
  private double         indentRight;
  private HorizAlignment horizontalAlignment;

  public VdxParagraphStyle() {
    this.horizontalAlignment = HorizAlignment.CENTER;
  }

  public VdxParagraphStyle(VisioParagraphStyle paraStyle) {
    this.indentFirst = paraStyle.getIndentFirst();
    this.indentLeft = paraStyle.getIndentLeft();
    this.indentRight = paraStyle.getIndentRight();
    this.horizontalAlignment = paraStyle.getHorizontalAlignment();
  }

  @Override
  public double getIndentLeft() {
    return indentLeft;
  }

  @Override
  public void setIndentLeft(double indent) {
    this.indentLeft = indent;
  }

  @Override
  public double getIndentRight() {
    return indentRight;
  }

  @Override
  public void setIndentRight(double indent) {
    this.indentRight = indent;
  }

  @Override
  public double getIndentFirst() {
    return indentFirst;
  }

  @Override
  public void setIndentFirst(double indent) {
    this.indentFirst = indent;
  }

  @Override
  public HorizAlignment getHorizontalAlignment() {
    return horizontalAlignment;
  }

  @Override
  public void setHorizontalAlignment(HorizAlignment alignment) {
    this.horizontalAlignment = alignment;
  }

  /**
   * Writes a {@link VisioParagraphStyle} into a vdx shape 
   */
  public static class Writer {
    /**
     * Writes this paragraph style to the given DOM element with the given ix-ID,
     * replacing an already existing element of that ix-ID.
     * @param paraStyle the {@link VisioParagraphStyle} to write
     * @param shape the DOM element representing the shape this text should be set to
     * @param ix the ix-ID of the style element in the DOM representation (0 based)
     */
    public static void writeToShapeElement(VisioParagraphStyle paraStyle, Element shape, int ix) {
      Element paraElement = VisioDOMUtils.getFirstChildWithNameAndAttribute(shape, VisioDOMUtils.PARA, "IX", String.valueOf(ix));
      if (paraElement == null) {
        paraElement = VisioDOMUtils.createChildWithName(shape, VisioDOMUtils.PARA);
        paraElement.setAttribute("IX", String.valueOf(ix));
      }

      Element indFirstElement = VisioDOMUtils.getOrCreateFirstChildWithName(paraElement, "IndFirst");
      VisioDOMUtils.setValue(indFirstElement, paraStyle.getIndentFirst());

      Element indLeftElement = VisioDOMUtils.getOrCreateFirstChildWithName(paraElement, "IndLeft");
      VisioDOMUtils.setValue(indLeftElement, paraStyle.getIndentLeft());

      Element indRightElement = VisioDOMUtils.getOrCreateFirstChildWithName(paraElement, "IndRight");
      VisioDOMUtils.setValue(indRightElement, paraStyle.getIndentRight());

      Element alignElement = VisioDOMUtils.getOrCreateFirstChildWithName(paraElement, "HorzAlign");
      VisioDOMUtils.setValue(alignElement, paraStyle.getHorizontalAlignment().getValue());
    }
  }

  /**
   * Reads a {@link VisioParagraphStyle} from a vdx shape 
   */
  public static class Reader {

    /**
     * Reads the first paragraph style information with the given ix ID in the given DOM element into
     * this {@link VdxParagraphStyle}.
     * @param shape the DOM element representing the shape this text should be set to
     * @param ix the ix-ID of the style element in the DOM representation (0 based)
     * @return the {@link VdxParagraphStyle} read
     */
    public static VisioParagraphStyle readFromShapeElement(Element shape, int ix) {
      VisioParagraphStyle paraStyle = new VdxParagraphStyle();

      Element paraElement = VisioDOMUtils.getFirstChildWithNameAndAttribute(shape, VisioDOMUtils.PARA, "IX", String.valueOf(ix));
      if (paraElement != null) {
        Element indFirstElement = VisioDOMUtils.getFirstChildWithName(paraElement, "IndFirst");
        if (indFirstElement != null) {
          paraStyle.setIndentFirst(Double.parseDouble(indFirstElement.getTextContent()));
        }

        Element indLeftElement = VisioDOMUtils.getFirstChildWithName(paraElement, "IndLeft");
        if (indLeftElement != null) {
          paraStyle.setIndentLeft(Double.parseDouble(indLeftElement.getTextContent()));
        }

        Element indRightElement = VisioDOMUtils.getFirstChildWithName(paraElement, "IndRight");
        if (indRightElement != null) {
          paraStyle.setIndentRight(Double.parseDouble(indRightElement.getTextContent()));
        }

        Element alignElement = VisioDOMUtils.getFirstChildWithName(paraElement, "HorzAlign");
        if (alignElement != null) {
          int alignValue = Integer.parseInt(alignElement.getTextContent());
          HorizAlignment determinedAlign = HorizAlignment.getAlignFor(alignValue);
          if (determinedAlign != null) {
            paraStyle.setHorizontalAlignment(determinedAlign);
          }
        }
      }
      return paraStyle;
    }
  }

}
