#import <Cordova/CDVPlugin.h>

@interface OSDebugLogs : CDVPlugin
  
-(void)openConsole:(CDVInvokedUrlCommand*)command;

-(void)closeConsole:(CDVInvokedUrlCommand*)command;

@end
