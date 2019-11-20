package afc.org.maven.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import afc.org.maven.plugin.util.GsonMerger;

@Mojo(
    name = "json-merge",
    defaultPhase = LifecyclePhase.GENERATE_SOURCES
)
public class JsonMergeMojo extends AbstractMojo {

    @Parameter( property = "json-merge.sources" )
    private String[] sources;
    
    @Parameter( property = "json-merge.overwrite", defaultValue = "true")
    private Boolean overwrite = true;

    @Parameter( property = "json-merge.output" )
    private String output;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
    	GsonMerger.merge(output, sources);
    }
}
