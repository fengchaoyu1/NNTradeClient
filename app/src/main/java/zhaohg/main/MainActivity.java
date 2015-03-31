package zhaohg.main;

import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import zhaohg.api.account.AccountLogout;
import zhaohg.api.account.AccountLogoutPostEvent;
import zhaohg.api.post.Post;
import zhaohg.post.PostsFragment;

public class MainActivity extends ActionBarActivity {

    private MainDrawer mainDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.color_primary_dark));
        }

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.mainDrawer = (MainDrawer) findViewById(R.id.drawer);
        this.mainDrawer.setOnSelectedItemChanged(new MainDrawer.OnSelectedItemChanged() {
            @Override
            public void onSelectedItemChanged(int id) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (id) {
                    case MainDrawer.ITEM_SELL:
                        fragmentTransaction.replace(R.id.frame_container, PostsFragment.newInstance(Post.POST_TYPE_SELL));
                        break;
                    case MainDrawer.ITEM_BUY:
                        fragmentTransaction.replace(R.id.frame_container, PostsFragment.newInstance(Post.POST_TYPE_BUY));
                        break;
                    case MainDrawer.ITEM_CHAT:
                        fragmentTransaction.replace(R.id.frame_container, PostsFragment.newInstance(Post.POST_TYPE_CHAT));
                        break;
                }
                fragmentTransaction.commit();
                drawerLayout.closeDrawers();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(mainDrawer);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, PostsFragment.newInstance(Post.POST_TYPE_SELL));
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mainDrawer.updateUserLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                AccountLogout logout = new AccountLogout(this);
                logout.setEvent(new AccountLogoutPostEvent() {
                    @Override
                    public void onSuccess() {
                        mainDrawer.updateUserLayout();
                    }
                });
                logout.request();
                break;
        }
        return true;
    }
}
