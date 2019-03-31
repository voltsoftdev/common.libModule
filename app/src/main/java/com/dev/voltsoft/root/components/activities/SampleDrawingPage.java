package com.dev.voltsoft.root.components.activities;

import android.os.Bundle;
import com.dev.voltsoft.lib.component.CommonActivity;
import com.dev.voltsoft.lib.view.sketchbook.SketchBookView;
import com.dev.voltsoft.root.R;
public class SampleDrawingPage extends CommonActivity
{

    @Override
    protected void init(Bundle savedInstanceState) throws Exception
    {
        setContentView(R.layout.page_drawing);

        SketchBookView sketchBookView = find(R.id.sketchBook);
        sketchBookView.loadBackGroundImage("https://i.pinimg.com/originals/b3/ef/44/b3ef441aa93e0026a0dd0e7f795b6130.jpg");
    }
}
