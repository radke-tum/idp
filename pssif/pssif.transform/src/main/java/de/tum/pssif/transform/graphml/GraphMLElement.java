package de.tum.pssif.transform.graphml;

import java.util.Map;


public interface GraphMLElement {
  public String getId();

  public String getType();

  public Map<String, String> getValues();
}
