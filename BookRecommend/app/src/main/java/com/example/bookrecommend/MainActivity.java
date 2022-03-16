package com.example.bookrecommend;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bookrecommend.constant.Tools;
import com.example.bookrecommend.db.DBUtil;
import com.example.bookrecommend.db.UserInfoEntity;
import com.example.bookrecommend.sington.SingleObject;
import com.example.bookrecommend.ui.home.HomeFragmentDirections;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import lombok.SneakyThrows;

public class MainActivity extends AppCompatActivity {
    private static BottomNavigationView navView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SneakyThrows
    @SuppressLint("QueryPermissionsNeeded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_setting)
                .build();

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController,
                appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        UserInfoEntity obj = DBUtil.getRecentUserInfoRecord(getApplicationContext());

        Log.d("UserInfoEntity", obj.toString());

        // 当前视图
//        View view = this.getWindow().getDecorView().getRootView();

        setSingleUserInfoEntity(obj); // 设置单例 UserInfoEntity 对象
//        // 如果当前没有相关的用户登录信息或用户记录状态已经被更新，则需要重新登录
//        if (obj.getRecordId() == 0 || obj.getIsUpdate() > 0) {
//            System.out.println("ToLogin: " + obj.toString());
//            setBottomNavigationVisibility(View.INVISIBLE);
////            navigationToLogin(navController, obj);
//        } else {
//            String maturityTime = obj.getMaturityTime();
//            Long maturityTimeStamp = TimeTools.parseLocalDateTimeToUnixTimeStamp(maturityTime);
//            Long currentTimeStamp = TimeTools.currentDateTimeUnixTimeStamp();
//
//            // 如果当前的时间大与预设的过期时间，则需要重新登录
//            if (currentTimeStamp > maturityTimeStamp) {
//                setBottomNavigationVisibility(View.INVISIBLE);
//                navigationToLogin(navController, obj);
//            }
//        }

        Tools.updateUserInfoEntitySingle(obj);
    }

    /**
     * 设置底部导航条的可见性
     */
    public static void setBottomNavigationVisibility(Integer visibility) {
        navView.setVisibility(visibility);
    }

    /**
     * 导航的登录界面
     * @param navController ： 当前的访问导航控制器
     * @param obj ： 传入到登录界面的用户信息对象参数
     */
    private void navigationToLogin(@NonNull NavController navController, @NonNull UserInfoEntity obj) {
        Log.d("ToLogin", "HomeFragment Start to there");
        NavDirections navDirections =
                HomeFragmentDirections
                        .actionNavigationHomeToLoginFragment(obj);
        navController.navigate(navDirections);
    }

    /**
     * 设置单例 UserInfo 对象的相关属性
     * @param obj ： 带有相关属性的 UserInfoEntity 对象
     */
    private void setSingleUserInfoEntity(@NonNull UserInfoEntity obj) {
        UserInfoEntity entity = SingleObject.getUserInfoEntity();
        entity.setRecordId(obj.getRecordId());
        entity.setUserId(obj.getUserId());
        entity.setUserPhone(obj.getUserPhone());
        entity.setUserEmail(obj.getUserEmail());
        entity.setUserName(obj.getUserName());
        entity.setUserSex(obj.getUserSex());
        entity.setHeadImage(obj.getHeadImage());
        entity.setUserPassword(obj.getUserPassword());
        entity.setLastLoginTime(obj.getLastLoginTime());
        entity.setMaturityTime(obj.getMaturityTime());
        entity.setIsUpdate(obj.getIsUpdate());
        entity.setFeatures(obj.getFeatures());
    }
}