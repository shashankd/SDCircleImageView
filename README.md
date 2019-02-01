## SDCircleImageView Android Library
    SDCircleImageView is a easy to use and memory efficient ImageView with customisable background and border color

![SDCircleImageView](https://github.com/shashankd/SDCircleImageView/blob/master/Screenshot.png)

We can customise background color and other dimension properties.
Pass the list of drawables (ImageDrawables or ColorDrawables)

# SDCircleImageView

### Gradle
```
dependencies {
    ...
    implementation 'com.sdcircleimageview:sdcircleimageview:1.0.1'
}
```

##### Using SDCircleImageView in XML
```xml
    <com.sdcircleimageview.SDCircleImageView
        android:id="@+id/sdCircleView"
        android:layout_width="250dp"
        android:layout_height="250dp"
        android:layout_below="@id/sdColorChooser"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="20dp"
        custom:borderColor="@color/colorAccent"
        custom:borderWidth="@dimen/ring_width"
        custom:showBorder="true" />
```

## Setting Image bitmap to SDCircleImageView in Java
```
    sdCircleView.setInnerBitmapImage(bitmap);
```

## Setting Color as a background to SDCircleImageView in Java
```
    int color = Color.RED;
    sdCirclceView.setInnerColor(color);
```