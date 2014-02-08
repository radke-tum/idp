package de.tum.pssif.vsdx;

import java.io.OutputStream;
import java.util.List;


public interface VsdxDocument {

  List<VsdxPage> getPages();

  List<VsdxMaster> getMasters();

  VsdxPage getPage(int index);

  VsdxMaster getMaster(int id);

  VsdxMaster getMaster(String name);

  void wite(OutputStream outputStream);

}
