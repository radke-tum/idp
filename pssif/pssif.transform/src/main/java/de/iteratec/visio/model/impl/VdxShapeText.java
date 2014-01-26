package de.iteratec.visio.model.impl;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.iteratec.visio.model.ShapeText;
import de.iteratec.visio.model.VisioParagraphStyle;
import de.iteratec.visio.model.VisioTextStyle;


public class VdxShapeText implements ShapeText {

  private VdxTextStyle      textStyle;
  private VdxParagraphStyle paraStyle;
  private String            text;

  public VdxShapeText() {
    textStyle = new VdxTextStyle();
    paraStyle = new VdxParagraphStyle();
    text = "";
  }

  public VdxShapeText(ShapeText shapeText) {
    textStyle = new VdxTextStyle(shapeText.getTextStyle());
    paraStyle = new VdxParagraphStyle(shapeText.getParagraphStyle());
    text = shapeText.getText();
  }

  @Override
  public VisioTextStyle getTextStyle() {
    return textStyle;
  }

  @Override
  public void setTextStyle(VisioTextStyle style) {
    textStyle = new VdxTextStyle(style);
  }

  @Override
  public VisioParagraphStyle getParagraphStyle() {
    return paraStyle;
  }

  @Override
  public void setParagraphStyle(VisioParagraphStyle paragraphStyle) {
    paraStyle = new VdxParagraphStyle(paragraphStyle);
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public void setText(String text) {
    this.text = (text == null ? "" : text);
  }

  /**
   * Writes a {@link ShapeText} into a vdx shape 
   */
  public static class Writer {

    /**
     * Writes this shape text to the given DOM element, replacing text and paragraph styles of ix=0 and the text content.
     * @param textToWrite the {@link ShapeText} object to write
     * @param shape the DOM element representing the shape this text should be set to
     */
    public static void writeToShapeElement(ShapeText textToWrite, Element shape) {
      VdxTextStyle.Writer.writeToShapeElement(textToWrite.getTextStyle(), shape, 0);
      VdxParagraphStyle.Writer.writeToShapeElement(textToWrite.getParagraphStyle(), shape, 0);

      Element textElement = VisioDOMUtils.getOrCreateFirstChildWithName(shape, "Text");
      VisioDOMUtils.clearNode(textElement);
      textElement.setTextContent(textToWrite.getText());
    }
  }

  /**
   * Writes a {@link ShapeText} from a vdx shape 
   */
  public static class Reader {

    /**
     * Reads text and style information from the given DOM element into this {@link VdxShapeText}.
     * Reads only the first character style and paragraph style and uses them for the whole text content.
     * @param shape the DOM element representing the shape this text should be set to
     * @return the {@link ShapeText} object read
     */
    public static ShapeText readFromShapeElement(Element shape) {
      assert (shape.getNodeName().equals("Shape"));

      ShapeText shapeText = new VdxShapeText();

      shapeText.setTextStyle(VdxTextStyle.Reader.readFromShapeElement(shape, 0));
      shapeText.setParagraphStyle(VdxParagraphStyle.Reader.readFromShapeElement(shape, 0));

      Element textElement = VisioDOMUtils.getFirstChildWithName(shape, "Text");
      if (textElement != null) {
        NodeList childNodes = textElement.getChildNodes();
        Node lastChild = textElement.getLastChild();

        StringBuilder textBuilder = new StringBuilder();
        for (int i = 0; i < childNodes.getLength(); i++) {
          Node childNode = childNodes.item(i);
          if (childNode.getNodeType() == Node.TEXT_NODE) {
            String nodeValue = childNode.getNodeValue();

            if (childNode.equals(lastChild)) {
              if (nodeValue.trim().isEmpty()) {
                continue;
                // if the last node only contains whitespace, don't add it
              }
              // remove the last line break if there is one, Visio seems to insert one per default
              if (nodeValue.endsWith("\n")) {
                nodeValue = nodeValue.substring(0, nodeValue.length() - 1);
              }
            }

            textBuilder.append(nodeValue);
          }
        }

        shapeText.setText(textBuilder.toString());
      }
      return shapeText;
    }
  }

}
