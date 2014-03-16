package de.tum.pssif.sysml4mechatronics;

import java.io.InputStream;
import java.io.OutputStream;

import de.tum.pssif.sysml4mechatronics.sfb768.SFB768Model;


public interface SFB768Mapper {

  SFB768Model read(InputStream in);

  void write(SFB768Model model, OutputStream out);

}
