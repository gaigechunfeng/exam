//package cn.gov.baiyin.court.core.shiro;
//
//import cn.gov.baiyin.court.core.entity.User;
//import cn.gov.baiyin.court.core.service.IUserService;
//import org.apache.shiro.authc.*;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// * Created by WK on 2017/3/25.
// */
//public class MyAccountRealm extends AuthorizingRealm {
//
//    private IUserService userService;
//
//    @Autowired
//    public void setUserService(IUserService userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        return null;
//    }
//
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//
//        String username = (String) authenticationToken.getPrincipal();
//
//        User user = userService.findByUserName(username);
//        if (user == null) {
//            throw new UnknownAccountException();
//        }
//
//        return new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), user.getName());
//    }
//}
