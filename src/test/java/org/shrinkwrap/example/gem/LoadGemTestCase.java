package org.shrinkwrap.example.gem;

import java.io.File;
import java.util.zip.GZIPInputStream;

import org.jboss.shrinkwrap.api.ArchiveFormat;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.classloader.ShrinkWrapClassLoader;
import org.jboss.shrinkwrap.api.importer.TarImporter;
import org.jboss.shrinkwrap.impl.base.io.IOUtil;
import org.junit.Test;

public class LoadGemTestCase {

	@Test
	public void loadFileFromGem() throws Exception {
		GenericArchive gem = ShrinkWrap.create(TarImporter.class)
				.importFrom(new File("src/test/resources/asciidoctor-1.5.2.gem"))
				.as(GenericArchive.class);
		
		GenericArchive archive = gem.getAsType(GenericArchive.class, "data.tar.gz", ArchiveFormat.TAR_GZ);

		ClassLoader cl = new ShrinkWrapClassLoader(archive);
		IOUtil.copy(cl.getResourceAsStream("lib/asciidoctor.rb"), System.out);
		
		
		Node metadata = gem.get("metadata.gz");
		IOUtil.copy(new GZIPInputStream(metadata.getAsset().openStream()), System.out);
	}
}
