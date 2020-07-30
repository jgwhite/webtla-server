package app.webtla.Server;

import java.util.Set;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tla2sany.drivers.FrontEndException;
import tla2sany.drivers.SANY;
import tla2sany.modanalyzer.SpecObj;
import tla2sany.semantic.*;

class SpecInspector {
  SpecObj specObj;

  public SpecInspector(SpecObj specObj) {
    this.specObj = specObj;
  }

  public String getName() {
    return specObj.getName();
  }

  public String getFileName() {
    return specObj.getFileName();
  }

  public Set<String> getModuleNames() {
    return specObj.getModuleNames();
  }

  public ModuleNodeInspector getRootModule() {
    return new ModuleNodeInspector(specObj.getRootModule());
  }

  public int getErrorLevel() {
    return specObj.getErrorLevel();
  }
}

class ModuleNodeInspector {
  ModuleNode moduleNode;

  public ModuleNodeInspector(ModuleNode moduleNode) {
    this.moduleNode = moduleNode;
  }

  public SemanticNodeInspector[] getChildren() {
    return Stream.of(moduleNode.getChildren())
      .map(SemanticNodeInspector::new)
      .toArray(SemanticNodeInspector[]::new);
  }
}

class SemanticNodeInspector {
  SemanticNode semanticNode;

  public SemanticNodeInspector(SemanticNode semanticNode) {
    this.semanticNode = semanticNode;
  }

  public String getLocation() {
    return semanticNode.toString();
  }

  public String getSource() {
    return semanticNode.getHumanReadableImage();
  }
}

@RestController
@SpringBootApplication
public class Main {

  Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @GetMapping("/")
  public SpecInspector index() {
    String filename = "/app/MySpec.tla";
    SpecObj specObj = new SpecObj(filename, null);

    try {
      SANY.frontEndMain(specObj, filename, System.err);
    } catch(FrontEndException err) {
      logger.error(err.toString());
    }

    return new SpecInspector(specObj);
  }

}
