import hudson.model.*
import hudson.AbortException
import hudson.console.HyperlinkNote
import java.util.concurrent.CancellationException
import java.util.regex.*
 // get current thread / Executor
def thr = Thread.currentThread()
// get current build
def build = thr.executable
jenkins = Hudson.instance
//def jobView = build.getEnvironment(listener).get('Application_Name')
//def regex_pattern="16.12.01" 
//build.getEnvironment(listener).get('Regex_Pattern')

// get job
def jobs = Hudson.instance.items.findAll()

def count=0

jenkins.instance.getView(appName).items.each { item ->
  def jobName = item.name
  def anotherBuild
 if(jobName.startsWith(regex_string))
  {
    
    println "Job to be deleted: "+jobName
    job = Hudson.instance.getJob(jobName)
    count++
   item.delete()
   
  }
  
}

println "Total delete count: "+count