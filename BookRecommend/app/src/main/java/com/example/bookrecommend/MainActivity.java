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

        // ????????????
//        View view = this.getWindow().getDecorView().getRootView();

        setSingleUserInfoEntity(obj); // ???????????? UserInfoEntity ??????
//        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
//        if (obj.getRecordId() == 0 || obj.getIsUpdate() > 0) {
//            System.out.println("ToLogin: " + obj.toString());
//            setBottomNavigationVisibility(View.INVISIBLE);
////            navigationToLogin(navController, obj);
//        } else {
//            String maturityTime = obj.getMaturityTime();
//            Long maturityTimeStamp = TimeTools.parseLocalDateTimeToUnixTimeStamp(maturityTime);
//            Long currentTimeStamp = TimeTools.currentDateTimeUnixTimeStamp();
//
//            // ????????????????????????????????????????????????????????????????????????
//            if (currentTimeStamp > maturityTimeStamp) {
//                setBottomNavigationVisibility(View.INVISIBLE);
//                navigationToLogin(navController, obj);
//            }
//        }

        Tools.updateUserInfoEntitySingle(obj);
    }

    /**
     * ?????????????????????????????????
     */
    public static void setBottomNavigationVisibility(Integer visibility) {
        navView.setVisibility(visibility);
    }

    /**
     * ?????????????????????
     * @param navController ??? ??????????????????????????????
     * @param obj ??? ????????????????????????????????????????????????
     */
    private void navigationToLogin(@NonNull NavController navController, @NonNull UserInfoEntity obj) {
        Log.d("ToLogin", "HomeFragment Start to there");
        NavDirections navDirections =
                HomeFragmentDirections
                        .actionNavigationHomeToLoginFragment(obj);
        navController.navigate(navDirections);
    }

    /**
     * ???????????? UserInfo ?????????????????????
     * @param obj ??? ????????????????????? UserInfoEntity ??????
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