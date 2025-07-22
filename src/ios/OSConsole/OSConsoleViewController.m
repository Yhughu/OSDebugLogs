#import "OSConsoleViewController.h"

@interface OSConsoleViewController ()
- (IBAction)onClearBtnTouchUp:(UIButton *)sender;
- (IBAction)onCloseBtnTouchUp:(UIButton *)sender;

@property (weak, nonatomic) IBOutlet UITextView *outputTextView;
@property (strong, nonatomic) NSMutableString* logString;
@property (weak, nonatomic) UIViewController* parentVC;

@end

@implementation OSConsoleViewController


-(instancetype)init {
    self = [super initWithNibName:@"OSConsole" bundle:nil];
    if (self) {
        [self setup];
    }
    return self;
}

-(instancetype) initWithParent:(UIViewController*)parent {
    self = [super initWithNibName:@"OSConsole" bundle:nil];
    if(self){
        self.parentVC = parent;
        [self setup];
    }
    return self;
}

-(void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:@"CDVLoggerNotification"
                                                  object:nil];
    [self.logString setString:@""];
    self.logString = nil;
    
}



-(void) setup {
    self.logString = [[NSMutableString alloc ]init];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(receivedRemoteLog:)
                                                 name:@"CDVLoggerNotification"
                                               object:nil];
}

-(void) receivedRemoteLog: (NSNotification*) notification {
    if([notification.name isEqualToString:@"CDVLoggerNotification"]){
        NSDictionary* userInfo = notification.userInfo;
        NSString *output = (NSString*) userInfo[@"message"];
        [self log:output];
    }
}

-(void)log:(NSString*)output {
    [self.logString appendString:[NSString stringWithFormat: @"\n%@",output]];

    if([[_parentVC.view subviews]containsObject: self.view]) {
        [self.outputTextView setText: [NSString stringWithString:self.logString]];
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)onClearBtnTouchUp:(UIButton *)sender {
    [self.outputTextView setText:@""];
    [self.logString setString:@""];
}

- (IBAction)onCloseBtnTouchUp:(UIButton *)sender {
    //[OSConsoleViewController removeControllerIn:self.parentViewController animated:YES];
    [self hideConsoleAnimated:YES];
}

-(void)openConsoleAnimated:(BOOL) animated {
    if(_parentVC) {
        BOOL exists = NO;
        for(UIViewController* vc in _parentVC.childViewControllers) {
            if([vc isKindOfClass:[OSConsoleViewController class]]) {
                exists = YES;
                break;
            }
        }
        
        self.view.backgroundColor = [UIColor whiteColor];
        self.view.frame = UIEdgeInsetsInsetRect(_parentVC.view.bounds, UIEdgeInsetsZero);
        
        if(!exists){
            [_parentVC addChildViewController:self];
            [_parentVC willMoveToParentViewController:self];
        }
        
        
        if( ![[_parentVC.view subviews]containsObject: self.view]) {
            if (animated) {
                // animation with a simple fade
                self.view.alpha = 0.0f;

                [_parentVC.view addSubview:self.view];
                [UIView animateWithDuration:0.3 animations:^{
                    _parentVC.view.userInteractionEnabled = NO;
                    self.view.alpha = 1.0f;
                    
                }                completion:^(BOOL finished) {
                    _parentVC.view.userInteractionEnabled = YES;
                    [self onShow];
                }];
            } else {
                // no animation so we just add the view as subview
                [_parentVC.view addSubview:self.view];
                [self onShow];
            }
        }
        
        if(!exists){
            [_parentVC didMoveToParentViewController:self];
        }
    } else {
        NSLog(@"Controller can't be nil");
    }
}

-(void)hideConsoleAnimated:(BOOL) animated {
    if (_parentVC) {
        for (UIViewController *vc in _parentVC.childViewControllers) {
            if ([vc isKindOfClass:[OSConsoleViewController class]]) {
                
                if (animated) {
                    // animation with a simple fade
                    [UIView animateWithDuration:0.3 animations:^{
                        _parentVC.view.userInteractionEnabled = NO;
                        vc.view.alpha = 0.0f;
                        
                    } completion:^(BOOL finished) {
                        _parentVC.view.userInteractionEnabled = YES;
                        [vc.view removeFromSuperview];
                        [self onHide];
                    }];
                } else {
                    // no animation so we just add the view as subview
                    [vc.view removeFromSuperview];
                }
                //[vc removeFromParentViewController];
            }
        }
    } else {
        NSLog(@"Controller can't be nil");
    }
}


-(void)onHide {
    
}

-(void)onWillAppear {
    
}

-(void)onShow {
    [self.outputTextView setText: [NSString stringWithString:self.logString]];
}

@end
