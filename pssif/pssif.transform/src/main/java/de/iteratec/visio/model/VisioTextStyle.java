package de.iteratec.visio.model;

import java.awt.Color;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;


/**
 * Interface for objects representing the text style properties of a Visio text. 
 */
public interface VisioTextStyle {
  String getFontName();

  void setFontName(String fontName);

  Color getFontColor();

  void setFontColor(Color color);

  Set<CharStyle> getCharStyles();

  void setCharStyles(Collection<CharStyle> styles);

  /**
   * Adds a {@link CharStyle} to this text style, if it is not already set. Else does nothing.
   * @param style the style to add.
   */
  void addCharStyle(CharStyle style);

  /**
   * Removes a {@link CharStyle}
   * @param style
   */
  void removeCharStyle(CharStyle style);

  /**
   * Returns the font size of the text style.
   * @return Font size in inch
   */
  double getFontSize();

  /**
   * Sets the font size of the text style.
   * @param size Font size in inch
   */
  void setFontSize(double size);

  /**
   * Enumeration for character styles like BOLD, ITALICS, ...
   */
  public enum CharStyle {
    BOLD(1), ITALICS(2), UNDERLINED(4), SMALL_CAPS(8);

    private static final Map<Integer, CharStyle> INT_TO_STYLE = ImmutableMap.of(Integer.valueOf(1), BOLD, Integer.valueOf(2), ITALICS,
                                                                  Integer.valueOf(4), UNDERLINED, Integer.valueOf(8), SMALL_CAPS);
    private int                                  value;

    private CharStyle(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }

    public static CharStyle getStyleFor(int value) {
      return INT_TO_STYLE.get(Integer.valueOf(value));
    }
  }
}
