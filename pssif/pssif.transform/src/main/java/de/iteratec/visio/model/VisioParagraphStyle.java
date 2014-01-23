package de.iteratec.visio.model;

import java.util.Map;

import com.google.common.collect.ImmutableMap;


/**
 * Interface for objects representing the paragraph properties of a Visio shape text. 
 */
public interface VisioParagraphStyle {

  /**
   * Returns the left indentation of this paragraph style.
   * @return Left indentation in inches
   */
  double getIndentLeft();

  /**
   * Sets the left indentation of this paragraph style.
   * @param indent Left indentation in inches
   */
  void setIndentLeft(double indent);

  /**
   * Returns the right indentation of this paragraph style.
   * @return Right indentation in inches
   */
  double getIndentRight();

  /**
   * Sets the right indentation of this paragraph style.
   * @param indent Right indentation in inches
   */
  void setIndentRight(double indent);

  /**
   * Returns the left indentation for the first line of this paragraph style.
   * @return Indentation in inches
   */
  double getIndentFirst();

  /**
   * Specifies the distance the first line of each paragraph with this style
   * in the shape's text block is indented from the left indent of the paragraph.
   * @param indent Indentation in inches
   */
  void setIndentFirst(double indent);

  /**
   * Returns the {@link HorizAlignment} of the text in this paragraph style.
   * @return The horizontal alignment
   */
  HorizAlignment getHorizontalAlignment();

  /**
   * Sets the {@link HorizAlignment} of the text in this paragraph style.
   * @param alignment The horizontal alignment
   */
  void setHorizontalAlignment(HorizAlignment alignment);

  /**
   * Enumeration for possible horizontal alignment options of a Visio shape text.
   */
  public enum HorizAlignment {
    LEFT(0), CENTER(1), RIGHT(2), JUSTIFY(3), FORCE_JUSTIFY(4);

    private static final Map<Integer, HorizAlignment> INT_TO_ALIGN = ImmutableMap.of(Integer.valueOf(0), LEFT, Integer.valueOf(1), CENTER,
                                                                       Integer.valueOf(2), RIGHT, Integer.valueOf(3), JUSTIFY, Integer.valueOf(4),
                                                                       FORCE_JUSTIFY);
    private int                                       value;

    private HorizAlignment(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }

    public static HorizAlignment getAlignFor(int value) {
      return INT_TO_ALIGN.get(Integer.valueOf(value));
    }
  }
}
