package de.iteratec.visio.model;

/**
 * Interface for objects representing and accessing the text of a Visio shape.
 * This interface is deliberately kept simple, allowing only one {@link VisioParagraphStyle} and one {@link VisioTextStyle}
 * for the whole {@link Shape}.
 */
public interface ShapeText {

  /**
   * Returns the current {@link VisioTextStyle} of this shape text.
   * @return the current text style
   */
  VisioTextStyle getTextStyle();

  /**
   * Sets the {@link VisioTextStyle} of this shape text.
   * Note: Changes to the TextStyle object after the call of this method won't be applied.
   * You need to either use {@link #setTextStyle(VisioTextStyle)} again or get the actual
   * TextStyle object via {@link #getTextStyle()} and change that one.
   * @param style the text style to apply
   */
  void setTextStyle(VisioTextStyle style);

  /**
   * Returns the current {@link VisioParagraphStyle} of this shape text.
   * @return the current paragraph style
   */
  VisioParagraphStyle getParagraphStyle();

  /**
   * Sets the {@link VisioParagraphStyle} of this shape text.
   * Note: Changes to the ParagraphStyle object after the call of this method won't be applied.
   * You need to either use {@link #setParagraphStyle(VisioParagraphStyle)} again or get the actual
   * ParagraphStyle object via {@link #getParagraphStyle()} and change that one.
   * @param paragraphStyle the paragraph style to apply
   */
  void setParagraphStyle(VisioParagraphStyle paragraphStyle);

  /**
   * Returns the text content of this shape text.
   * @return A String containing the text to display
   */
  String getText();

  /**
   * Sets the text content of this shape text.
   * @param text A String containing the text to display
   */
  void setText(String text);

}
