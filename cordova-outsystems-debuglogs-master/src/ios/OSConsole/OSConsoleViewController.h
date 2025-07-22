#import <UIKit/UIKit.h>

@interface OSConsoleViewController : UIViewController

-(void)log:(NSString*)output;
-(void)hideConsoleAnimated:(BOOL) animated;
-(void)openConsoleAnimated:(BOOL) animated;
-(instancetype) initWithParent:(UIViewController*)parent;
@end
