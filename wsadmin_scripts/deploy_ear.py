import time
import string

EAR_NAME =  'was8.ear'
APP_NAME = 'was8-ear'
CONTEXT_ROOT = '/was8'
SERVER_NAME = 'server1'

# @see https://stackoverflow.com/questions/8185866/how-do-i-determine-if-an-application-is-running-using-wsadmin-jython-script

def getAppStatus(appName):
    # If objectName is blank, then the application is not running.
    objectName = AdminControl.completeObjectName('type=Application,name='+ appName+',*')
    if objectName == "":
        appStatus = 'Stopped'
    else:
        appStatus = 'Running'
    return appStatus

def appStatusInfo():
    Apps=AdminApp.list().split(java.lang.System.getProperty("line.separator"))

    print '============================'
    print ' Status |    Application   '
    print '============================'

    # Print apps and their status
    for x in Apps:
        print getAppStatus(x) + ' | ' + x

    print '============================'

print ("Uninstalling application : [" + APP_NAME + "]")
result = AdminControl.completeObjectName('type=Application,name=' + APP_NAME + ',*')
print ("AdminControl.completeObjectName result ["  + result + "]")
if (result):
   AdminApp.uninstall(APP_NAME)
   AdminConfig.save()

print ("Uninstalltion complete")
print ("")

print "ReInstalling Application [" + APP_NAME + "] found in file [" + EAR_NAME +"] on server " + SERVER_NAME
AdminApp.install('/tmp/'+ EAR_NAME,'[-node dockerboyNode01 -server ' + SERVER_NAME + ' -defaultbinding.virtual.host default_host -usedefaultbindings]')

AdminConfig.save()
print "ReInstallation Complete " + APP_NAME
print ("")

print ("Get Deployments : ")
deployments = AdminConfig.getid('/Deployment:' + APP_NAME + '/')
print deployments
print ("")

print ("Deployment Object")
deploymentObject = AdminConfig.showAttribute(deployments, 'deployedObject')
print deploymentObject

myModules = AdminConfig.showAttribute(deploymentObject, 'modules')
myModules = myModules[1:len(myModules)-1].split(" ")
print myModules
for module in myModules:
     if (module.find('WebModuleDeployment')!= -1):
        AdminConfig.modify(module, [['classloaderMode', 'PARENT_LAST']])

AdminConfig.save()
print ("Set module to PARENT_LAST")

result = AdminApp.isAppReady(APP_NAME)
while (result == "false"):
   ### Wait 5 seconds before checking again
   time.sleep(5)
   result = AdminApp.isAppReady(APP_NAME)
print (APP_NAME + " is ready to start")
print ("")

print ("Retreiving appManager")
appManager = AdminControl.queryNames('node=dockerboyNode01,type=ApplicationManager,process=server1,*')
print appManager
print ("")

print ("Starting " + APP_NAME)
AdminControl.invoke(appManager, 'startApplication', APP_NAME)
print ("")

appStatusInfo()
