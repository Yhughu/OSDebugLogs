#import "OSDebugLogs.h"
#import "OSConsoleViewController.h"

@interface OSDebugLogs()
 
 @property (strong, nonatomic) OSConsoleViewController* osConsoleVC;

@end

@implementation OSDebugLogs


-(void)pluginInitialize{
    
    _osConsoleVC = [[OSConsoleViewController alloc] initWithParent:self.viewController];

}


- (void)openConsole:(CDVInvokedUrlCommand*)command
{
	CDVPluginResult* pluginResult = nil;
	if(_osConsoleVC){

		[_osConsoleVC openConsoleAnimated:YES];

		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
	}
	else{
		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];	
	}

    

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)closeConsole:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
	if(_osConsoleVC){

		[_osConsoleVC hideConsoleAnimated:YES];

		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
	}
	else{
		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];	
	}

    
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
