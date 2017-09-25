//
//  ViewController.m
//  model
//
//  Created by apple on 2017/9/25.
//  Copyright © 2017年 🍃🌺🍃. All rights reserved.
//

#import "ViewController.h"
#import "HomeControl.h"
#import "ShopControl.h"
#import "findControl.h"
#import "MyControl.h"
@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
//    UITabBarController *tabControl = [[UITabBarController alloc]init];
    //首页tabBar
    HomeControl * homeCont = [[HomeControl alloc]init];
    homeCont.title = @"首页";
    homeCont.tabBarItem.image = [UIImage imageNamed:@""];
    UINavigationController * T1 = [[UINavigationController alloc]initWithRootViewController:homeCont];
    //门店tabBar
    ShopControl * shopCont = [[ShopControl alloc]init];
    shopCont.title =@"门店";
    shopCont.tabBarItem.image = [UIImage imageNamed:@""];
    UINavigationController * T2 =[[UINavigationController alloc]initWithRootViewController:shopCont];
    //发现tabBar
    findControl * findCont =[[findControl alloc]init];
    findCont.title = @"发现";
    findCont.tabBarItem.image = [UIImage imageNamed:@""];
    UINavigationController * T3 =[[UINavigationController alloc]initWithRootViewController:findCont];
    //我的tabBar
    MyControl * myCont =[[MyControl alloc]init];
    myCont.title = @"我的";
    myCont.tabBarItem.image = [UIImage imageNamed:@""];
    UINavigationController * T4 =[[UINavigationController alloc]initWithRootViewController:myCont];
    
    self.viewControllers = @[T1,T2,T3,T4];
    
    self.tabBar.tintColor = [UIColor orangeColor];


}

@end
