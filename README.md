# FontAwesome

Awesome Android library to use the [Font Awesome](https://fontawesome.com/icons) Icon collection in your android apps. This library contains the latest font awesome icon collection (**v5.14.0 Pro**).


How to Use
-------------
Include the `fontawesome` dependency in app's **build.gradle** and you are good to go.
```gradle
dependencies {
    implementation 'com.apachat:fontawesome-android:5.14.0'
}
```


Referring Icon:
-----
Font Awesome provides three set of icons **Regular**, **Solid** and **Brand**. All the icons can be referred from `Strings` resource file. For example,

`@string/map` - Regular map icon

`@string/heart_solid` - Solid heart icon

`@string/facebook` - Facebook brand icon.


Displaying Text Icon: FontTextView
----
To display an icon in xml layout, use the **FontTextView** widget. This class is extended from **AppCompatTextView**, so all the TextView related properties will apply.
```java
<com.apachat.fontawesome.core.FontTextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/calendar_check_solid"
            android:textColor="@color/icon_color"
            android:textSize="@dimen/icon_size"
            app:solid_icon="true" />
```

`solid_icon`: Use this attribute to display a solid icon (true / false).
`brand_icon`: Use this attribute to display a brand icon (true / false).


Displaying drawable Icon: FontDrawable
----
If you want to set an icon to a widget (buttons, menus, bottom sheet, navigation drawer), use the **FontDrawable** class to create font awesome drawable.

Here [Paper Plane](https://fontawesome.com/icons/paper-plane?style=solid) icon is set to **Floating Action Button**
```java
FloatingActionButton fab = findViewById(R.id.fab);

// using paper plane icon for FAB
FontDrawable drawable = new FontDrawable(this, R.string.paper_plane_solid, true, false);

// white color to icon
drawable.setTextColor(ContextCompat.getColor(this, android.R.color.white));
fab.setImageDrawable(drawable);
```


Displaying Icons in Menus (Bottom Navigation, Navigation Drawer, Toolbar etc.,)
----
You can also display Font Awesome icons in UI elements those use menu file to render items. In the below example, font awesome icons are set to Navigation Drawer items.
```java
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intDrawerLayout();
    }

    /**
     * Changing navigation drawer icons
     * This involves looping through menu items and applying icons
     */
    private void intDrawerLayout() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageView iconHeader = navigationView.getHeaderView(0).findViewById(R.id.nav_header_icon);
        FontDrawable drawable = new FontDrawable(this, R.string.font_awesome, false, true);
        drawable.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        drawable.setTextSize(50);
        iconHeader.setImageDrawable(drawable);

        int[] icons = {
                R.string.home_solid, R.string.calendar_alt_solid, R.string.user_solid,
                R.string.heart_solid, R.string.comment_solid, R.string.dollar_sign_solid, R.string.gift_solid
        };
        renderMenuIcons(navigationView.getMenu(), icons, true, false);

        int[] iconsSubmenu = {R.string.cog_solid, R.string.sign_out_alt_solid};

        renderMenuIcons(navigationView.getMenu().getItem(7).getSubMenu(), iconsSubmenu, true, false);
    }

    /**
     * Looping through menu icons are applying font drawable
     */
    private void renderMenuIcons(Menu menu, int[] icons, boolean isSolid, boolean isBrand) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (!menuItem.hasSubMenu()) {
                FontDrawable drawable = new FontDrawable(this, icons[i], isSolid, isBrand);
                drawable.setTextColor(ContextCompat.getColor(this, R.color.icon_nav_drawer));
                drawable.setTextSize(22);
                menu.getItem(i).setIcon(drawable);
            }
        }
    }
}
```


Note:
-----
This library includes the **Premium icons** font awesome.
