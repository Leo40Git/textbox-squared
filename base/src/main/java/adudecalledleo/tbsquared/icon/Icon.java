package adudecalledleo.tbsquared.icon;

import java.awt.image.*;

import adudecalledleo.tbsquared.definition.Definition;

public record Icon(Definition sourceDefinition, String name, BufferedImage image) { }
