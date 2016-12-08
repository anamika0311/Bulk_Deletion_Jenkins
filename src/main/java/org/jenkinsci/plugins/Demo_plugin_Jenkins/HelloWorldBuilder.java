package org.jenkinsci.plugins.Demo_plugin_Jenkins;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.util.FormValidation;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.BuildListener;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import jenkins.model.Jenkins;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;

import org.codehaus.groovy.control.CompilationFailedException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Sample {@link Builder}.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link HelloWorldBuilder} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #application_Name})
 * to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform} method will be invoked. 
 *
 * @author Kohsuke Kawaguchi
 */
public class HelloWorldBuilder extends Builder implements SimpleBuildStep {

    private final String application_Name;
    
    private final String regex_string;

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public HelloWorldBuilder(String application_Name,String regex_string) {
        this.application_Name = application_Name;
        this.regex_string=regex_string;
    }
    

    /**
     * We'll use this from the {@code config.jelly}.
     */
    public String getApplication_Name() {
        return application_Name;
    }
    public String getRegex_string() {
        return regex_string;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException {
        // This is where you 'build' the project.
        // Since this is a dummy, we just say 'hello world' and call that a build.

        // This also shows how you can consult the global configuration of the builder
        /*if (getDescriptor().getUseFrench())
            listener.getLogger().println("Bonjour, "+name+"!");
        else
            listener.getLogger().println("Hello, "+name+"!");*/
    	String workspace=application_Name;
    	String regExp=regex_string;

         try {
         	
				setProperty(workspace,listener,build,regExp);
			} catch (CompilationFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         
      
         listener.getLogger().println("Application Name is:  "+workspace);
         listener.getLogger().println("Regular Expression is:  "+regExp);
         return true;
    	
    	
    }
    
public boolean setProperty(String prop, BuildListener listener,AbstractBuild build, String regex_string) throws CompilationFailedException, IOException, InstantiationException, IllegalAccessException,NullPointerException{
    	
    	Jenkins jenkins=Jenkins.getInstance();
 
   
   // File file=new File (jenkins.getRootDir()+"/plugins/Demo_plugin_Jenkins/WEB-INF/");

    try{
   // listener.getLogger().println("Path:"+file.getCanonicalPath());
    String fileName="json.groovy";
  //  String[] roots=new String[]{file.getCanonicalPath()};
    listener.getLogger().println("Path:"+jenkins.getRootDir());
    listener.getLogger().println("Path of Groovy:"+jenkins.getRootDir()+"/plugins/Demo_plugin_Jenkins/WEB-INF/json.groovy");
    
  

    GroovyScriptEngine gse=new GroovyScriptEngine("D:/TestPlugin/Demo_plugin_Jenkins/src/main/resources/json.groovy");
    

    
    
  
    listener.getLogger().println("Groovy Script Path:"+jenkins.getRootDir()+"/plugins/Demo_plugin_Jenkins/WEB-INF/json.groovy");
    Binding binding = new Binding();
    binding.setVariable("appName", prop);
    binding.setVariable("regex_string", regex_string);
    binding.setProperty("build", build);
    binding.setProperty("listener", listener);
    listener.getLogger().println("Initializing Groovy Script:"+fileName);
    listener.getLogger().println("Before script run Object"); 
    try {
		gse.run(fileName, binding);
	} catch (ResourceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ScriptException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	
}	catch(NullPointerException e){
	e.printStackTrace();
}
    	System.out.println("Success");
    	return true;
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    /**
     * Descriptor for {@link HelloWorldBuilder}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See {@code src/main/resources/hudson/plugins/hello_world/HelloWorldBuilder/*.jelly}
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        /**
         * To persist global configuration information,
         * simply store it in a field and call save().
         *
         * <p>
         * If you don't want fields to be persisted, use {@code transient}.
         */
        //private boolean useFrench;

        /**
         * In order to load the persisted global configuration, you have to 
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        /**
         * Performs on-the-fly validation of the form field 'name'.
         *
         * @param value
         *      This parameter receives the value that the user has typed.
         * @return
         *      Indicates the outcome of the validation. This is sent to the browser.
         *      <p>
         *      Note that returning {@link FormValidation#error(String)} does not
         *      prevent the form from being saved. It just means that a message
         *      will be displayed to the user. 
         */
        public FormValidation doCheckName(@QueryParameter String value)
                throws IOException, ServletException {
        	if (value.length() == 0)
                return FormValidation.error("Please set an Application Name");
            else if(!Pattern.matches("[a-zA-Z _]+", value))
            	return FormValidation.error("Application Name can contain Alphabets,underscores and spaces");
            return FormValidation.ok();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types 
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Jobs Cleanup";
        }

       // @Override
        /*public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            // To persist global configuration information,
            // set that to properties and call save().
            useFrench = formData.getBoolean("useFrench");
            // ^Can also use req.bindJSON(this, formData);
            //  (easier when there are many fields; need set* methods for this, like setUseFrench)
            save();
            return super.configure(req,formData);
        }*/

        /**
         * This method returns true if the global configuration says we should speak French.
         *
         * The method name is bit awkward because global.jelly calls this method to determine
         * the initial state of the checkbox by the naming convention.
         */
       /* public boolean getUseFrench() {
            return useFrench;
        }*/
    }

	@Override
	public void perform(Run<?, ?> arg0, FilePath arg1, Launcher arg2,
			TaskListener arg3) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		
	}
}

