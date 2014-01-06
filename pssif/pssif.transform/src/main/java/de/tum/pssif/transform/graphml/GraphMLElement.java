package de.tum.pssif.transform.graphml;

import java.util.Map;


public interface GraphMLElement {
  public String getId();

  public Map<String, String> getValues();
}
